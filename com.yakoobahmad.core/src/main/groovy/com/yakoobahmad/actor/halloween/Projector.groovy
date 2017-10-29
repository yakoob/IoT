package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import akka.actor.Cancellable
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.actor.HomeManager
import com.yakoobahmad.command.Command
import com.yakoobahmad.command.CommandableMedia
import com.yakoobahmad.command.video.Pause
import com.yakoobahmad.command.video.Play
import com.yakoobahmad.command.video.Resume
import com.yakoobahmad.event.MediaPlaybackComplete
import com.yakoobahmad.event.MediaPlaybackStarted
import com.yakoobahmad.event.MotionDetected
import com.yakoobahmad.fsm.FSM
import com.yakoobahmad.fsm.Guard
import com.yakoobahmad.fsm.state.Any
import com.yakoobahmad.fsm.state.Off
import com.yakoobahmad.fsm.state.video.Paused
import com.yakoobahmad.fsm.state.video.Playing
import com.yakoobahmad.media.HalloweenVideo
import grails.util.Holders
import groovy.util.logging.Slf4j
import scala.concurrent.duration.Duration

import java.util.concurrent.TimeUnit

@Slf4j
class Projector extends BaseActor implements FSM {

    def jsonService = Holders.applicationContext.getBean("jsonService")
    def mqttClientService = Holders.applicationContext.getBean("mqttClientService")
    def twitterService = Holders.applicationContext.getBean("twitterService")

    HalloweenVideo currentVideo
    HalloweenVideo previousVideo

    HalloweenVideo woods

    Cancellable randomVideoTimer
    Cancellable twitterMentionsTimer

    Projector(){

        println "You moms, Projector initialized with : "+self?.path()?.name()

        HalloweenVideo.withNewSession {
            def w = HalloweenVideo.findByName(HalloweenVideo.Name.WOODS)
            currentVideo = w
            woods = w
        }

        startStateMachine(Off)

        configureFsmDsl()

        startTwitterMentionsTimer()

        startRandomVideoTimer()

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof String && message == "SHOW_CURRENT_STATE"){

            fsm.showState()
            log.info "currentVideo:${currentVideo?.name} | previousVideo: ${previousVideo?.name}"

        } else if (message instanceof Command) {

            if (message.media?.type == HalloweenVideo.Type.HOLOGRAM){

                self.tell(new Play(media: woods), ActorRef.noSender())
                sleep(1000)
                HomeManager.halloweenProjectorSam.tell(message, ActorRef.noSender())

            } else {

                fsm.fire(message)

            }


        } else if (message instanceof MediaPlaybackComplete) {

            if (message.next){
                playRandomVideo()
            } else {
                self.tell(new Play(media: woods), ActorRef.noSender())
            }


        } else if (message instanceof MediaPlaybackStarted) {
            if (message.media instanceof HalloweenVideo) {
                println "Projector1 MediaPlaybackStarted: " + message.media?.dump()
                this.currentVideo = message.media

                HalloweenVideo.withNewSession {
                    if (currentVideo.name == HalloweenVideo.Name.WOODS){
                        HomeManager.halloweenProjectorSam.tell("PLAY_RANDOM_VIDEO", ActorRef.noSender())
                    }

                }

            }
        } else if (message instanceof MotionDetected){
            if (currentVideoIsWoods()){
                randomVideoTimer?.cancel()
                startRandomVideoTimer()
            }
        }

    }


    @Override
    void configureFsmDsl() {

        fsm.record().onCommands([Play]).fromState(Any).goToState(Playing).transition = { Command command ->
            remoteDispatch(command)
        }

        fsm.record().onCommands([Off, Pause]).fromState(Any).goToState(Paused).transition = { Command command ->
            remoteDispatch(command)
        }

        fsm.record().onCommands([Resume]).fromState(Paused).goToState(Playing).transition = { Command command ->

            if (!currentVideo)
                return new Guard(reason: "ineligible video for command:Resume")

            remoteDispatch(command)

        }

    }

    private void remoteDispatch(Command command){

        if (command instanceof CommandableMedia) {

            if ( !currentVideoIsWoods() )
                this.previousVideo = this.currentVideo

            this.currentVideo = command?.media

            if (currentVideo?.jsonTemplatePath) {
                // send to mqtt so android can process
                mqttClientService.publish(
                        "halloween/video",
                        jsonService.toJsonFromDomainTemplate(currentVideo)
                )
            }
        }

    }

    private void startRandomVideoTimer(){

        randomVideoTimer = context.system().scheduler().schedule(Duration.Zero(), Duration.create(30, TimeUnit.MINUTES),
                new Runnable() {
                    @Override
                    public void run() {

                        try{

                            log.debug "playing random video"

                            HalloweenVideo.withNewSession {

                                def videos = HalloweenVideo.findAllByType(HalloweenVideo.Type.PUMPKINS)

                                if(videos?.size()){

                                    videos.removeAll([woods,previousVideo])

                                    Collections.shuffle(videos)

                                    HalloweenVideo selectedVideo = videos?.first()

                                    log.debug "selectedVideo is ${selectedVideo.name}"

                                    if ( currentVideoIsWoods() ){
                                        self.tell(new Play(media: selectedVideo), ActorRef.noSender())
                                    } else {
                                        log.warn "can not play random video because currentVideo is not WOODS"
                                    }

                                } else {
                                    log.warn "no videos found!!!"
                                }
                            }
                        } catch(e){
                            e.printStackTrace()
                        }


                    }
                }, context.system().dispatcher())

    }


    private playRandomVideo(){

        HalloweenVideo.withNewSession {

            def videos = HalloweenVideo.findAllByTypeAndNameNotEqual(HalloweenVideo.Type.PUMPKINS, HalloweenVideo.Name.WOODS)
            // def videos = HalloweenVideo.findAllByName(HalloweenVideo.Name.SAM_SCARE4)
            if(videos?.size()){

                videos.removeAll([previousVideo])

                Collections.shuffle(videos)

                HalloweenVideo selectedVideo = videos?.first()

                log.debug "selectedVideo is ${selectedVideo.name}"

                Play newPlay = new Play()
                newPlay.media=selectedVideo


                remoteDispatch(newPlay)


            } else {
                log.warn "no videos found!!!"
            }
        }
    }


    private void startTwitterMentionsTimer(){

        twitterMentionsTimer = context.system().scheduler().schedule(Duration.Zero(), Duration.create(5, TimeUnit.SECONDS),
            new Runnable() {
                @Override
                public void run() {

                    if (!twitterService.enabled)
                        return

                    log.debug "tell twitter actor to try next"

                    log.debug "current media: ${currentVideo.name}"

                    if (currentVideoIsWoods()){

                        akkaService.twitter.tell("NEXT", self)

                    } else {
                        log.debug "media in progress try again"
                    }


                }
            }, context.system().dispatcher())


    }

    boolean currentVideoIsWoods(){
        return currentVideo?.name == HalloweenVideo.Name.WOODS
    }
}

