package com.yakoobahmad

import akka.actor.Actor
import akka.actor.ActorRef
import com.yakoobahmad.command.halloween.*
import com.yakoobahmad.command.video.Pause
import com.yakoobahmad.command.video.Play
import com.yakoobahmad.command.video.Resume
import com.yakoobahmad.device.Light
import com.yakoobahmad.event.SoundDetectionCalculationComplete
import com.yakoobahmad.halloween.Video
import groovy.transform.TupleConstructor
import groovy.util.logging.Log

@Log
class TestController {

    def akkaService
    def mqttClientService
    def httpClientService
    def twitterService

    def index() {

        /*
        akkaService.halloweenManager.tell(new BlowSmoke(), ActorRef.noSender())
        sleep(5000)
        akkaService.halloweenManager.tell(new StopSmoke(), ActorRef.noSender())
        akkaService.halloweenManager.tell("SHOW_CURRENT_STATE", ActorRef.noSender())
        akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.OOGIE_BOOGIE_PUMPKINS)), ActorRef.noSender())
        */

        String text = params.t

        if (text?.toUpperCase() == "GGG") {
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.GRIM_GRINNING_GHOST)), ActorRef.noSender())
        } else if (text == "WT"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.WHATS_THIS)), ActorRef.noSender())
        } else if (text == "OBP"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.OOGIE_BOOGIE_PUMPKINS)), ActorRef.noSender())
        } else if (text == "KSC"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.KIDNAP_SANDY_CLAWS)), ActorRef.noSender())
        } else if (text == "TIH"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.THIS_IS_HALLOWEEN)), ActorRef.noSender())
        } else if (text == "MM"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.MONSTER_MASH)), ActorRef.noSender())
        } else if (text == "WOODS"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.WOODS)), ActorRef.noSender())
        }

        akkaService.halloweenManager.tell("SHOW_CURRENT_STATE", ActorRef.noSender())

        /*
        akkaService.halloweenManager.tell(new SoundDetectionCalculationComplete(avg: 1, sum: 4), ActorRef.noSender())
        akkaService.halloweenManager.tell(new SoundDetectionCalculationComplete(avg: 100, sum: 100), ActorRef.noSender())
        akkaService.halloweenManager.tell(new SoundDetectionCalculationComplete(avg: 50, sum: 50), ActorRef.noSender())
        akkaService.halloweenManager.tell(new SoundDetectionCalculationComplete(avg: 25, sum: 25), ActorRef.noSender())
        */

        render "foo"
    }

}

@TupleConstructor
class HueEffect {

    def on = true
    def bri = 255
    def sat = 255
    def hue = 30000

}