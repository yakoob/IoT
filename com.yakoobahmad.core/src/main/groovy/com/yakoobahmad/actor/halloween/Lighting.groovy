package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import akka.actor.Props
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.event.SoundDetectionCalculationComplete
import com.yakoobahmad.halloween.light.Hue
import groovy.util.logging.Log

@Log
class Lighting extends BaseActor {

    ActorRef lightRearLeft
    ActorRef lightRearCenter
    ActorRef lightRearRight

    Lighting(){
        Hue.withNewSession {
            lightRearLeft = context.system().actorOf(Props.create(Light.class, Hue.findByNode("1")), "lightRearLeft")
            lightRearCenter = context.system().actorOf(Props.create(Light.class, Hue.findByNode("2")), "LightRearCenter")
            lightRearRight = context.system().actorOf(Props.create(Light.class, Hue.findByNode("4")), "lightRearRight")
        }

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof SoundDetectionCalculationComplete){
            lightRearLeft.tell(message, self)
            lightRearCenter.tell(message, self)
            lightRearRight.tell(message, self)
        }

    }

}
