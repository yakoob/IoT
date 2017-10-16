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
import com.yakoobahmad.event.Event
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
class Projector2 extends BaseActor implements FSM {

    def jsonService = Holders.applicationContext.getBean("jsonService")
    def mqttClientService = Holders.applicationContext.getBean("mqttClientService")

    HalloweenVideo currentVideo
    HalloweenVideo previousVideo

    Cancellable randomVideoTimer

    Boolean canPlay = true

    Projector2(){

        startStateMachine(Off)
        configureFsmDsl()

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof String && message == "PLAY_RANDOM_VIDEO") {

            if (canPlay)
                playRandomVideo()


        } else if (message instanceof MediaPlaybackComplete) {

            def foo = message.clone()
            foo.topic = "ActorSystem/Halloween/Projector"

            akkaService.homeManager?.tell(foo, akkaService.actorNoSender())

        }

    }


    private playRandomVideo(){

        HalloweenVideo.withNewSession {

            // def videos = HalloweenVideo.findAllByType(HalloweenVideo.Type.HOLOGRAM)
            def videos = HalloweenVideo.findAllByName(HalloweenVideo.Name.SAM_SCARE4)

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

    @Override
    void configureFsmDsl() {

        fsm.record().onCommands([Play]).fromState(Any).goToState(Playing).transition = { Command command ->
            remoteDispatch(command)
        }

        fsm.record().onCommands([Off, Pause]).fromState(Any).goToState(Paused).transition = { Command command ->
            remoteDispatch(command)
        }

        fsm.record().onCommands([Resume]).fromState(Paused).goToState(Playing).transition = { Command command ->

            remoteDispatch(command)

        }

    }

    private void remoteDispatch(Command command){

        if (command instanceof CommandableMedia){

            this.currentVideo = command?.media

            if (currentVideo?.jsonTemplatePath) {
                // send to mqtt so android can process
                mqttClientService.publish(
                    "halloween/video2",
                    jsonService.toJsonFromDomainTemplate(currentVideo)
                )

                startCanPlayTimer()
            }
        }

    }


    def startCanPlayTimer(){

        canPlay = false

        randomVideoTimer = context.system().scheduler().schedule(Duration.Zero(), Duration.create(5, TimeUnit.MINUTES),
            new Runnable() {
                @Override
                public void run() {
                    canPlay = true
                }
            }, context.system().dispatcher())
    }
}

