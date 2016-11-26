package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import akka.actor.Props
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.config.GlobalConfig
import grails.util.Holders
import groovy.util.logging.Log

@Log
class Manager extends BaseActor implements GlobalConfig {

    def akkaService = Holders.applicationContext.getBean("akkaService")

    private ActorRef projector = null
    private ActorRef smokeMachine = null
    private ActorRef lighting = null

    Manager() {
        if (halloweenEnabled){
            smokeMachine = context.system().actorOf(Props.create(com.yakoobahmad.actor.halloween.Smoke.class), "SmokeMachine")
            projector = context.system().actorOf(Props.create(Projector.class), "Projector")
            lighting = context.system().actorOf(Props.create(Lighting.class), "Lighting")
        }
        log.info "Halloween manager started"
    }

    @Override
    void onReceive(Object message) throws Exception {
        if (halloweenEnabled){
            projector.tell(message, self)
            lighting.tell(message, self)
            smokeMachine.tell(message, self)
        }
    }

}
