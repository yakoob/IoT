package com.yakoobahmad.actor.device

import akka.actor.Cancellable
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.fsm.FSM
import com.yakoobahmad.fsm.state.*
import com.yakoobahmad.command.*
import grails.util.Holders
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import groovy.util.logging.Log

@Log
class Smoke extends BaseActor implements FSM {

    def jsonService = Holders.applicationContext.getBean("jsonService")
    def mqttClientService = Holders.applicationContext.getBean("mqttClientService")

    Cancellable smokeCoolOffTimer

    Smoke(){
        log.info "smoke machine started"
        startStateMachine(Off)
        configureFsmDsl()
    }

    @Override
    void onReceive(Object message) throws Exception {

        if (message instanceof String && message == "SHOW_CURRENT_STATE")
            fsm.showState()

        else if (message instanceof Command)
            fsm.fire(message)

    }

    @Override
    void configureFsmDsl() {
        log.info "!!!! configure configureFsmDsl() | currentState: ${fsm.currentState}"
    }

    void smokeCoolOffTimer(){
        smokeCoolOffTimer = context.system().scheduler().scheduleOnce(Duration.create(20, TimeUnit.SECONDS),
            new Runnable() {
                @Override
                public void run() {
                    smokeCoolOffTimer = null
                    log.info "smoke machine ready"
                }
            }, context.system().dispatcher());
    }

}
