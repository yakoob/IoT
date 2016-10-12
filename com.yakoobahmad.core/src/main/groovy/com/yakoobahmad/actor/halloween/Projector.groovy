package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import akka.actor.Cancellable
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.command.Command
import com.yakoobahmad.command.CommandableMedia
import com.yakoobahmad.command.video.Pause
import com.yakoobahmad.command.video.Play
import com.yakoobahmad.command.video.Resume
import com.yakoobahmad.event.MediaPlaybackComplete
import com.yakoobahmad.event.SoundDetected
import com.yakoobahmad.fsm.FSM
import com.yakoobahmad.fsm.Guard
import com.yakoobahmad.fsm.state.Any
import com.yakoobahmad.fsm.state.Off
import com.yakoobahmad.fsm.state.video.Paused
import com.yakoobahmad.fsm.state.video.Playing
import com.yakoobahmad.halloween.Video
import grails.util.Holders
import groovy.util.logging.Log
import groovy.util.logging.Slf4j
import scala.concurrent.duration.Duration

import java.util.concurrent.TimeUnit

@Slf4j
class Projector extends BaseActor implements FSM {

    def jsonService = Holders.applicationContext.getBean("jsonService")
    def mqttClientService = Holders.applicationContext.getBean("mqttClientService")
    def twitterService = Holders.applicationContext.getBean("twitterService")

    Video currentVideo

    Video woods

    Cancellable randomVideoTimer
    Cancellable twitterMentionsTimer

    Projector(){

        Video.withNewSession {
            currentVideo = Video.findByName(Video.Name.WOODS)
            woods = Video.findByName(Video.Name.WOODS)
        }

        startStateMachine(Off)

        configureFsmDsl()

        startTwitterMentionsTimer()

        startRandomVideoTimer()

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof String && message == "SHOW_CURRENT_STATE")
            fsm.showState()

        else if (message instanceof Command)
            fsm.fire(message)

        else if (message instanceof MediaPlaybackComplete)
            self.tell(new Play(media: woods), ActorRef.noSender())

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

            if (!currentVideo || currentVideo == Video.Name.NONE)
                return new Guard(reason: "ineligible video for command:Resume")

            remoteDispatch(command)

        }

    }

    private void remoteDispatch(Command command){

        if (command instanceof CommandableMedia) {

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

        randomVideoTimer = context.system().scheduler().schedule(Duration.Zero(), Duration.create(8, TimeUnit.MINUTES),
                new Runnable() {
                    @Override
                    public void run() {

                        log.debug "playing random video"

                        if (currentVideo?.id == woods?.id){
                            Video.withNewSession {
                                def videos = Video.findAll()
                                Collections.shuffle(videos)
                                self.tell(new Play(media: videos.first()), ActorRef.noSender())
                            }
                        } else {
                            log.debug "could not play random video"
                        }


                    }
                }, context.system().dispatcher())

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

                        if (currentVideo?.id == woods?.id){

                            akkaService.twitter.tell("NEXT", self)

                        } else {
                            log.debug "media in progress try again"
                        }


                    }
                }, context.system().dispatcher())


    }

}

