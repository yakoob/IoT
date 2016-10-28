package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import com.yakoobahmad.command.Command
import com.yakoobahmad.command.halloween.BlowSmoke
import com.yakoobahmad.command.halloween.StopSmoke
import com.yakoobahmad.fsm.Guard
import com.yakoobahmad.fsm.state.Any
import com.yakoobahmad.fsm.state.Off
import com.yakoobahmad.fsm.state.On
import com.yakoobahmad.fsm.state.State
import grails.util.Holders
import groovy.util.logging.Slf4j
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

@Slf4j
class Smoke extends com.yakoobahmad.actor.device.Smoke {

    def httpClientService = Holders.applicationContext.getBean("httpClientService")

    public Smoke() {
        startStateMachine(Off)
        configureFsmDsl()
    }

    @Override
    void onReceive(Object message) throws Exception {
        log.debug message.toString()
        super.onReceive(message)
        if (message instanceof Command)
            fsm.fire(message)
    }

    @Override
    void configureFsmDsl() {

        super.configureFsmDsl()

        fsm.record().onCommands([StopSmoke]).fromState(Any).goToState(Off).transition = { Command command ->
            toggleSmokeMachine(new Off())
        }

        fsm.record().onCommands([BlowSmoke]).fromState(Off).goToState(On).transition = { Command command ->

            log.debug "blow smoke"

            if (smokeCoolOffTimer) {
                return new Guard(reason: "can not turn On smoke machine so often", payload: command)
            }

            smokeCoolOffTimer()

            toggleSmokeMachine(new On())

            /**
             * turn smoke off after 2 secons
             */
            context.system().scheduler().scheduleOnce(Duration.create(2, TimeUnit.SECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        self.tell(StopSmoke.newInstance(), ActorRef.noSender())
                    }
                }, context.system().dispatcher()
            )


        }

    }

    private void toggleSmokeMachine(State state){

        com.yakoobahmad.device.Smoke.withNewSession {

            if (state instanceof On)
                httpClientService.get("http://192.168.20.217/arduino/servo/5/60")
            if (state instanceof Off)
                httpClientService.get("http://192.168.20.217/arduino/servo/5/95")

            /*
            def smoke = com.yakoobahmad.device.Smoke.findByNameAndState(
                com.yakoobahmad.device.Smoke.Name.HALLOWEEN_REAR,
                com.yakoobahmad.device.Smoke.getSmokeState(state)
            )

            if (smoke?.jsonTemplatePath) {
                // send to mqtt so arduino can process
                mqttClientService.publish(
                        "/halloween/smoke",
                        jsonService.toJsonFromDomainTemplate(smoke)
                )
            }
            */

        }

    }
}
