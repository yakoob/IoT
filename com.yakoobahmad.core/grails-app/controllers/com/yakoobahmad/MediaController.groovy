package com.yakoobahmad

import akka.actor.ActorRef
import com.yakoobahmad.command.video.Play
import com.yakoobahmad.media.HalloweenVideo
import com.yakoobahmad.media.ChristmasVideo

class MediaController {

    def akkaService

    def video() {

        String t = params.media
        t = t?.toUpperCase()

        /*
        if (t == "GGG") {
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.GRIM_GRINNING_GHOST)), ActorRef.noSender())
        } else if (t == "WT"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.WHATS_THIS)), ActorRef.noSender())
        } else if (t == "OBP"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.OOGIE_BOOGIE_PUMPKINS)), ActorRef.noSender())
        } else if (t == "KSC"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.KIDNAP_SANDY_CLAWS)), ActorRef.noSender())
        } else if (t == "TIH"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.THIS_IS_HALLOWEEN)), ActorRef.noSender())
        } else if (t == "MM"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.MONSTER_MASH)), ActorRef.noSender())
        } else if (t == "WOODS"){
            akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.WOODS)), ActorRef.noSender())
        }
        */
        render t?:"media not found"
        return
    }

}
