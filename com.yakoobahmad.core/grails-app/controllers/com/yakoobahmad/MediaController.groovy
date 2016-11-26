package com.yakoobahmad

import akka.actor.ActorRef
import com.yakoobahmad.command.video.Play
import com.yakoobahmad.media.HalloweenVideo
import com.yakoobahmad.media.ChristmasVideo
import com.yakoobahmad.media.Video

class MediaController {

    def akkaService

    def video() {

        String t = params.media
        t = t?.toUpperCase()
        ChristmasVideo cv
        try {
            def cvName = ChristmasVideo.Name.valueOf(t)
            if (cvName){
                cv = ChristmasVideo.findByName(cvName)
                if (cv)
                    akkaService.christmasManger.tell(new Play(media: cv), ActorRef.noSender())

            }
        } catch (e) {
            log.error("an error ${e.message}")
        }

        render cv?:"media not found"

        return
    }

}
