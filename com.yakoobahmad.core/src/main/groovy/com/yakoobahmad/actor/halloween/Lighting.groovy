package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import akka.actor.Props
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.event.SoundDetectionCalculationComplete
import com.yakoobahmad.device.light.Hue
import groovy.util.logging.Log

@Log
class Lighting extends BaseActor {

    ActorRef pumpkin1
    ActorRef pumpkin2
    ActorRef pumpkin3

    ActorRef frontDoor

    ActorRef garage1
    ActorRef garage2
    ActorRef garage3

    ActorRef door1
    ActorRef door2
    ActorRef door3
    ActorRef door4

    Lighting(){

        Hue.withNewSession {

            def p1 = Hue.findByDescription("Pumpkin_left")
            pumpkin1 = context.system().actorOf(Props.create(Light.class, p1), p1.description)

            def p2 = Hue.findByDescription("Pumpkin_center")
            pumpkin2 = context.system().actorOf(Props.create(Light.class, p2), p2.description)

            def p3 = Hue.findByDescription("Pumpkin_right")
            pumpkin3 = context.system().actorOf(Props.create(Light.class, p3), p3.description)

            def p0 = Hue.findByDescription("Front_Door")
            frontDoor = context.system().actorOf(Props.create(Light.class, p0), p0.description)

            def g1 = Hue.findByDescription("Garage_1")
            garage1 = context.system().actorOf(Props.create(Light.class, g1), g1.description)

            def g2 = Hue.findByDescription("Garage_2")
            garage2 = context.system().actorOf(Props.create(Light.class, g2), g2.description)

            def g3 = Hue.findByDescription("Garage_3")
            garage3 = context.system().actorOf(Props.create(Light.class, g3), g3.description)

            def d1 = Hue.findByDescription("Door_1")
            door1 = context.system().actorOf(Props.create(Light.class, d1), d1.description)

            def d2 = Hue.findByDescription("Door_2")
            door2 = context.system().actorOf(Props.create(Light.class, d2), d2.description)

            def d3 = Hue.findByDescription("Door_3")
            door3 = context.system().actorOf(Props.create(Light.class, d3), d3.description)

            def d4 = Hue.findByDescription("Door_4")
            door4 = context.system().actorOf(Props.create(Light.class, d4), d4.description)

        }

    }

    @Override
    void onReceive(Object message) throws Exception {

        // println "lighting " + message.dump()

        if (message instanceof SoundDetectionCalculationComplete){

            // println "lighting : SoundDetectionCalculationComplete: " + message.dump()

            pumpkin1?.tell(message, self)
            pumpkin2?.tell(message, self)
            pumpkin3?.tell(message, self)

            frontDoor?.tell(message, self)

            garage1?.tell(message, self)
            garage2?.tell(message, self)
            garage3?.tell(message, self)

            door1?.tell(message, self)
            door2?.tell(message, self)
            door3?.tell(message, self)
            door4?.tell(message, self)

        }

    }

}
