package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import akka.actor.Props
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.event.SoundDetectionCalculationComplete
import com.yakoobahmad.device.light.Hue
import groovy.util.logging.Log

@Log
class Lighting extends BaseActor {

    ActorRef pumpkinLeft
    ActorRef pumpkinRight
    ActorRef pumpkinCenter

    Lighting(){

        Hue.withNewSession {
            def hueLeft = Hue.findByDescription("HalloweenLeft")
            def hueRight = Hue.findByDescription("HalloweenRight")
            def hueCenter = Hue.findByDescription("HalloweenCenter")
            pumpkinLeft = context.system().actorOf(Props.create(Light.class, hueLeft), hueLeft.description)
            pumpkinRight = context.system().actorOf(Props.create(Light.class, hueRight), hueRight.description)
            pumpkinCenter = context.system().actorOf(Props.create(Light.class, hueCenter), hueCenter.description)
        }

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof SoundDetectionCalculationComplete){

            pumpkinLeft.tell(message, self)
            pumpkinRight.tell(message, self)
            pumpkinCenter.tell(message, self)

        }

    }

}
