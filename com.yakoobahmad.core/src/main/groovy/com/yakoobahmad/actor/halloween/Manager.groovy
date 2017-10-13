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

    private ActorRef projectorPumpkins = null
    // private ActorRef projectorSam = null
    private ActorRef smokeMachine = null
    private ActorRef lighting = null

    Manager() {

        if (halloweenEnabled){
            // smokeMachine = context.system().actorOf(Props.create(com.yakoobahmad.actor.halloween.Smoke.class), "SmokeMachine")
            projectorPumpkins = context.system().actorOf(Props.create(Projector.class), "ProjectorPumpkins")
            // projectorSam = context.system().actorOf(Props.create(Projector2.class), "ProjectorSam")
            lighting = context.system().actorOf(Props.create(Lighting.class), "Lighting")
            println "Halloween manager started"
        } else {
            println "CAN NOT START - halloween not enabled"
        }

    }

    @Override
    void onReceive(Object message) throws Exception {
        if (halloweenEnabled){
            projectorPumpkins.tell(message, self)
            // projectorSam.tell(message, self)
            lighting.tell(message, self)
            // smokeMachine.tell(message, self)
        }
    }

}
