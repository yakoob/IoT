package com.yakoobahmad.actor.christmas

import akka.actor.ActorRef
import akka.actor.Cancellable
import com.yakoobahmad.actor.BaseActor
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
import com.yakoobahmad.media.ChristmasVideo
import grails.util.Holders
import groovy.util.logging.Slf4j
import scala.concurrent.duration.Duration

import java.util.concurrent.TimeUnit

@Slf4j
class Projector extends BaseActor implements FSM {

    def jsonService = Holders.applicationContext.getBean("jsonService")
    def mqttClientService = Holders.applicationContext.getBean("mqttClientService")

    ChristmasVideo currentVideo
    ChristmasVideo previousVideo
    ChristmasVideo idleVideo

    Cancellable randomVideoTimer


    Projector(){

        startStateMachine(Off)

        configureFsmDsl()

        startRandomVideoTimer()

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof String && message == "SHOW_CURRENT_STATE"){

            fsm.showState()
            log.info "currentVideo:${currentVideo?.name} | previousVideo: ${previousVideo?.name}"

        } else if (message instanceof Command) {

            fsm.fire(message)

        } else if (message instanceof MediaPlaybackComplete) {

            self.tell(new Play(media: idleVideo), ActorRef.noSender())

        } else if (message instanceof MediaPlaybackStarted) {
            if (message.media instanceof ChristmasVideo) {
                this.currentVideo = message.media
            }
        } else if (message instanceof MotionDetected){

            randomVideoTimer?.cancel()
            startRandomVideoTimer()

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

            this.previousVideo = this.currentVideo
            this.currentVideo = command?.media

            if (currentVideo?.jsonTemplatePath) {
                // send to mqtt so android can process
                mqttClientService.publish(
                        "christmas/video",
                        jsonService.toJsonFromDomainTemplate(currentVideo)
                )
            }
        }

    }

    private void startRandomVideoTimer(){

        randomVideoTimer = context.system().scheduler().schedule(Duration.Zero(), Duration.create(3, TimeUnit.MINUTES),
                new Runnable() {
                    @Override
                    public void run() {

                        try{

                            log.debug "playing random video"

                            ChristmasVideo.withNewSession {

                                def videos = ChristmasVideo.findAll()

                                // log.info "christmas videos: " + videos.toListString()

                                if(videos?.size()){

                                    videos.removeAll([previousVideo])

                                    Collections.shuffle(videos)

                                    ChristmasVideo selectedVideo = videos?.first()

                                    log.debug "TELL PROJECTOR TO PLAY NEW VIDEO: ${selectedVideo.name}"

                                    self.tell(new Play(media: selectedVideo), ActorRef.noSender())

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

}

