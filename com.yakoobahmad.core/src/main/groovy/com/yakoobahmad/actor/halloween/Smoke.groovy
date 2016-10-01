package com.yakoobahmad.actor.halloween

import com.yakoobahmad.command.Command
import com.yakoobahmad.command.halloween.BlowSmoke
import com.yakoobahmad.command.halloween.StopSmoke
import com.yakoobahmad.fsm.Guard
import com.yakoobahmad.fsm.state.Any
import com.yakoobahmad.fsm.state.Off
import com.yakoobahmad.fsm.state.On
import com.yakoobahmad.fsm.state.State
import groovy.util.logging.Log

@Log
class Smoke extends com.yakoobahmad.actor.device.Smoke {

    public Smoke() {
        startStateMachine(Off)
        configureFsmDsl()
    }

    @Override
    void onReceive(Object message) throws Exception {
        super.onReceive(message)
    }

    @Override
    void configureFsmDsl() {

        super.configureFsmDsl()

        fsm.record().onCommands([StopSmoke]).fromState(Any).goToState(Off).transition = { Command command ->
            toggleSmokeMachine(new Off())
        }

        fsm.record().onCommands([BlowSmoke]).fromState(Off).goToState(On).transition = { Command command ->

            if (smokeCoolOffTimer) {
                return new Guard(reason: "can not turn On smoke machine so often", payload: command)
            }

            smokeCoolOffTimer()

            toggleSmokeMachine(new On())

        }

    }

    private void toggleSmokeMachine(State state){

        com.yakoobahmad.device.Smoke.withNewSession {

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

        }

    }
}
