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

    def index() {

        akkaService.halloweenManager.tell(new BlowSmoke(), ActorRef.noSender())

        sleep(5000)
        akkaService.halloweenManager.tell(new StopSmoke(), ActorRef.noSender())

        sleep(500)

        akkaService.halloweenManager.tell(new Play(media: Video.findByName(Video.Name.THIS_IS_HALLOWEEN)), ActorRef.noSender())

        sleep(5000)

        akkaService.halloweenManager.tell(new Pause(media: Video.findByName(Video.Name.NONE)), ActorRef.noSender())

        sleep(5000)

        akkaService.halloweenManager.tell(new Resume(media: Video.findByName(Video.Name.NONE)), ActorRef.noSender())

        sleep(1000)

        akkaService.halloweenManager.tell("SHOW_CURRENT_STATE", ActorRef.noSender())

        render "foo"
    }

    def woods(){
        mqttClientService.publish("halloween/video", "{'foo':'bar'}")
        render "foo"
    }

}
