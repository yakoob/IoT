package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import akka.actor.Props
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.event.SoundDetectionCalculationComplete
import com.yakoobahmad.halloween.light.Hue
import groovy.util.logging.Log

@Log
class Lighting extends BaseActor {

    ActorRef lightPumpkinLeft
    ActorRef lightPumpkinRight
    ActorRef lightPumpkinSinging

    Lighting(){

        Hue.withNewSession {

            lightPumpkinLeft = context.system().actorOf(Props.create(Light.class, Hue.findByNode("5")), "lightPumpkinLeft")
            lightPumpkinRight = context.system().actorOf(Props.create(Light.class, Hue.findByNode("6")), "lightPumpkinRight")
            lightPumpkinSinging = context.system().actorOf(Props.create(Light.class, Hue.findByNode("7")), "lightPumpkinSinging")
        }

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof SoundDetectionCalculationComplete){

            /*
            lightRearLeft.tell(message, self)
            lightRearCenter.tell(message, self)
            lightRearRight.tell(message, self)
            lightDining.tell(message, self)
            */

            lightPumpkinLeft.tell(message, self)
            lightPumpkinRight.tell(message, self)
            lightPumpkinSinging.tell(message, self)

        }

    }

}
