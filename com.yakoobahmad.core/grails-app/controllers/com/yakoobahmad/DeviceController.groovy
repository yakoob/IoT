package com.yakoobahmad

import akka.actor.ActorRef
import com.yakoobahmad.command.halloween.BlowSmoke

class DeviceController {

    def akkaService

    def index() {
        render "ok"
    }

    def smoke(){
        akkaService.halloweenManager.tell(new BlowSmoke(), ActorRef.noSender())
        render "ok"
    }

}
