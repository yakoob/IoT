package com.yakoobahmad

import akka.actor.ActorRef
import com.yakoobahmad.command.halloween.*
import com.yakoobahmad.command.video.Pause
import com.yakoobahmad.command.video.Play
import com.yakoobahmad.command.video.Resume
import com.yakoobahmad.device.Light
import com.yakoobahmad.halloween.Video

import groovy.util.logging.Log

@Log
class TestController {

    def akkaService
    def mqttClientService
    def twitterService

    def index() {

        def text = params.t

        if (text == "GGG"){
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
        }



        /*
        akkaService.halloweenManager.tell(new BlowSmoke(), ActorRef.noSender())

        sleep(5000)
        akkaService.halloweenManager.tell(new StopSmoke(), ActorRef.noSender())


        akkaService.halloweenManager.tell("SHOW_CURRENT_STATE", ActorRef.noSender())


        akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.OOGIE_BOOGIE_PUMPKINS)), ActorRef.noSender())
        */

        akkaService.halloweenManager.tell("SHOW_CURRENT_STATE", ActorRef.noSender())

        render "foo"
    }

}
