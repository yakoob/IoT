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

    ActorRef lightKitchenIsland
    ActorRef lightPumpkinLeft
    ActorRef lightPumpkinRight

    Lighting(){

        Hue.withNewSession {

            lightRearLeft = context.system().actorOf(Props.create(Light.class, Hue.findByNode("1")), "lightRearLeft")
            lightRearCenter = context.system().actorOf(Props.create(Light.class, Hue.findByNode("2")), "lightRearCenter")
            lightRearRight = context.system().actorOf(Props.create(Light.class, Hue.findByNode("4")), "lightRearRight")

            lightKitchenIsland = context.system().actorOf(Props.create(Light.class, Hue.findByNode("3")), "lightKitchenIsland")
            lightPumpkinLeft = context.system().actorOf(Props.create(Light.class, Hue.findByNode("5")), "lightPumpkinLeft")
            lightPumpkinRight = context.system().actorOf(Props.create(Light.class, Hue.findByNode("6")), "lightPumpkinRight")
        }

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof SoundDetectionCalculationComplete){

            lightRearLeft.tell(message, self)
            lightRearCenter.tell(message, self)
            lightRearRight.tell(message, self)

            lightKitchenIsland.tell(message, self)
            lightPumpkinLeft.tell(message, self)
            lightPumpkinRight.tell(message, self)

        }

    }

}
