package com.yakoobahmad.actor.halloween

import grails.util.Holders
import akka.actor.Cancellable
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.command.Command
import com.yakoobahmad.command.CommandableMedia
import com.yakoobahmad.command.video.Pause
import com.yakoobahmad.command.video.Play
import com.yakoobahmad.command.video.Resume
import com.yakoobahmad.event.MediaPlaybackComplete
import com.yakoobahmad.fsm.FSM
import com.yakoobahmad.fsm.state.Any
import com.yakoobahmad.fsm.state.Off
import com.yakoobahmad.fsm.state.video.Paused
import com.yakoobahmad.fsm.state.video.Playing
import com.yakoobahmad.media.HalloweenVideo
import scala.concurrent.duration.Duration

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import groovy.util.logging.Slf4j

@Slf4j
class Projector2 extends BaseActor implements FSM {

    def jsonService = Holders.applicationContext.getBean("jsonService")
    def mqttClientService = Holders.applicationContext.getBean("mqttClientService")

    HalloweenVideo currentVideo
    HalloweenVideo previousVideo

    Cancellable randomVideoTimer

    Boolean canPlay = true

    CopyOnWriteArrayList<HalloweenVideo> playlistQueue = new CopyOnWriteArrayList<HalloweenVideo>()

    Projector2(){
        startStateMachine(Off)
        configureFsmDsl()
    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof String && message == "PLAY_RANDOM_VIDEO") {

            if (canPlay)
                playRandomVideo()


        } else if (message instanceof Play){

            HalloweenVideo media = message.media
            if (media.type == HalloweenVideo.Type.HOLOGRAM){
                playMedia(message)
            }

        } else if (message instanceof MediaPlaybackComplete) {

            MediaPlaybackComplete mediaPlaybackCompleteEvent = message.clone()
            mediaPlaybackCompleteEvent.topic = "ActorSystem/Halloween/Projector"
            akkaService.homeManager?.tell(mediaPlaybackCompleteEvent, akkaService.actorNoSender())

        }

    }

    private playMedia(Command cmd){

        HalloweenVideo.withNewSession {
            remoteDispatch(cmd)
        }
    }


    private playRandomVideo(){

        HalloweenVideo.withNewSession {

            if (playlistQueue.size() >= HalloweenVideo.countByType(HalloweenVideo.Type.HOLOGRAM)){
                playlistQueue.clear()
                if (previousVideo)
                    playlistQueue.addIfAbsent(previousVideo)
            }

            def videos = HalloweenVideo.findAllByType(HalloweenVideo.Type.HOLOGRAM)

            videos.removeAll(playlistQueue)

            if(videos?.size()){

                Collections.shuffle(videos)

                HalloweenVideo selectedVideo = videos?.first()
                playlistQueue.addIfAbsent(selectedVideo)

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

            this.previousVideo = this.currentVideo

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

