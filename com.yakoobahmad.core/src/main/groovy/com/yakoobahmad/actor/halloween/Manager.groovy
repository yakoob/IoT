package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import akka.actor.Props
import com.yakoobahmad.actor.BaseActor
import grails.util.Holders
import groovy.util.logging.Log

@Log
class Manager extends BaseActor {

    def akkaService = Holders.applicationContext.getBean("akkaService")

    private ActorRef projector = null
    private ActorRef smokeMachine = null
    private ActorRef lighting = null

    Manager() {
        log.info "Halloween manager started"
        smokeMachine = context.system().actorOf(Props.create(com.yakoobahmad.actor.halloween.Smoke.class), "SmokeMachine")
        projector = context.system().actorOf(Props.create(Projector.class), "Projector")
        lighting = context.system().actorOf(Props.create(Lighting.class), "Lighting")
    }

    @Override
    void onReceive(Object message) throws Exception {

        projector.tell(message, self)

        lighting.tell(message, self)

        smokeMachine.tell(message, self)

    }

}
