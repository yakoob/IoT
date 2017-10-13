package com.yakoobahmad.actor

import akka.actor.ActorRef
import akka.actor.Props
import com.yakoobahmad.config.GlobalConfig
import com.yakoobahmad.event.Event
import grails.util.Holders

class HomeManager extends BaseActor implements GlobalConfig{

    def akkaService = Holders.applicationContext.getBean("akkaService")

    public static ActorRef christmasProjector = null
    public static ActorRef halloweenProjector = null
    public static ActorRef halloweenProjectorSam = null

    HomeManager() {

        if (halloweenEnabled){
            halloweenProjector = context.system().actorOf(Props.create(com.yakoobahmad.actor.halloween.Projector.class), "HalloweenProjector")
            halloweenProjectorSam = context.system().actorOf(Props.create(com.yakoobahmad.actor.halloween.Projector2.class), "HalloweenProjectorSam")
        }


        if (christmasEnabled)
            christmasProjector = context.system().actorOf(Props.create(com.yakoobahmad.actor.christmas.Projector.class), "ChristmasProjector")

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (christmasProjector)
            christmasProjector.tell(message, self)

        if (halloweenProjector) {
            halloweenProjector.tell(message, self)
        }


    }

}
