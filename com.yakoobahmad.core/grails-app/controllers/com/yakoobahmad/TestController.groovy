package com.yakoobahmad

import akka.actor.ActorRef
import com.yakoobahmad.command.halloween.BlowSmoke
import com.yakoobahmad.command.video.Play
import com.yakoobahmad.media.HalloweenVideo
import groovy.util.logging.Log

@Log
class TestController {

    def akkaService
    def mqttClientService
    def httpClientService
    def twitterService

    def play(){
        String name = params.video
        akkaService.halloweenManager.tell(new Play(media: HalloweenVideo.findByName(HalloweenVideo.Name.valueOf(name.toUpperCase()))), ActorRef.noSender())
        akkaService.halloweenManager.tell(new Play(media: HalloweenVideo.findByName(HalloweenVideo.Name.valueOf(name.toUpperCase()))), ActorRef.noSender())
        redirect(action: "index")
        return
    }

    def index() {

        render view: '/test', model: [videos:HalloweenVideo.findAllByType(HalloweenVideo.Type.PUMPKINS)]
        return

        /*
        akkaService.halloweenManager.tell(new BlowSmoke(), ActorRef.noSender())
        sleep(5000)
        akkaService.halloweenManager.tell(new StopSmoke(), ActorRef.noSender())
        akkaService.halloweenManager.tell("SHOW_CURRENT_STATE", ActorRef.noSender())
        akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.OOGIE_BOOGIE_PUMPKINS)), ActorRef.noSender())
        */





        akkaService.halloweenManager.tell("SHOW_CURRENT_STATE", ActorRef.noSender())

        /*
        akkaService.halloweenManager.tell(new SoundDetectionCalculationComplete(avg: 1, sum: 4), ActorRef.noSender())
        akkaService.halloweenManager.tell(new SoundDetectionCalculationComplete(avg: 100, sum: 100), ActorRef.noSender())
        akkaService.halloweenManager.tell(new SoundDetectionCalculationComplete(avg: 50, sum: 50), ActorRef.noSender())
        akkaService.halloweenManager.tell(new SoundDetectionCalculationComplete(avg: 25, sum: 25), ActorRef.noSender())
        */

        render "foo"
    }

    def smoke(){
        akkaService.halloweenManager.tell(new BlowSmoke(), ActorRef.noSender())
        render "smoke"
    }

}

