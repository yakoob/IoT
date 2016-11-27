package com.yakoobahmad.actor

import akka.actor.ActorRef
import akka.actor.Props
import com.yakoobahmad.config.GlobalConfig
import grails.util.Holders

class HomeManager extends BaseActor implements GlobalConfig{

    def akkaService = Holders.applicationContext.getBean("akkaService")

    private ActorRef christmasProjector = null
    private ActorRef halloweenProjector = null

    HomeManager() {

        if (halloweenEnabled)
            halloweenProjector = context.system().actorOf(Props.create(com.yakoobahmad.actor.halloween.Projector.class), "HalloweenProjector")

        if (christmasEnabled)
            christmasProjector = context.system().actorOf(Props.create(com.yakoobahmad.actor.christmas.Projector.class), "ChristmasProjector")

    }

    @Override
    void onReceive(Object message) throws Exception {

        if (christmasProjector)
            christmasProjector.tell(message, self)

        if (halloweenProjector)
            halloweenProjector.tell(message, self)

    }

}
