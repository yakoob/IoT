package com.yakoobahmad.actor.halloween

import akka.actor.ActorRef
import com.yakoobahmad.command.TurnOff
import com.yakoobahmad.command.TurnOn
import com.yakoobahmad.event.SoundDetectionCalculationComplete
import com.yakoobahmad.fsm.state.On
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.command.Command
import com.yakoobahmad.fsm.FSM
import com.yakoobahmad.fsm.state.Off
import com.yakoobahmad.halloween.light.Hue
import com.yakoobahmad.visualization.Color
import com.yakoobahmad.visualization.ColorHue
import grails.util.Holders
import groovy.util.logging.Log4j

@Log4j
class Light extends BaseActor implements FSM {

    Hue light

    List<Color> colorList = []

    def httpClientService = Holders.applicationContext.getBean("httpClientService")

    Light(Hue hue) {

        Hue.withNewSession {

            light = hue


            colorList << Color.findByDescription(Color.Name.PURPLE)
            colorList << Color.findByDescription(Color.Name.ORANGE)
            colorList << Color.findByDescription(Color.Name.BLUE)
            colorList << Color.findByDescription(Color.Name.GREEN)
            colorList << Color.findByDescription(Color.Name.PINK)
            colorList << Color.findByDescription(Color.Name.RED)
            colorList << Color.findByDescription(Color.Name.WHITE)
            colorList << Color.findByDescription(Color.Name.BLACK)
        }

        startStateMachine(Off)
        configureFsmDsl()

    }

    @Override
    void onReceive(Object message) throws Exception {


        if (message instanceof String && message == "SHOW_CURRENT_STATE") {

            fsm.showState()

        } else if (message instanceof Command) {

            fsm.fire(message)

        } else if (message instanceof SoundDetectionCalculationComplete) {

            log.debug "fsmCS:${this.fsm.currentState}"

            double avg = message.avg?.toDouble()
            double sum = message.sum?.toDouble()


            if (avg < 1) {

                if (this.fsm.currentState == On.name)
                    self.tell(new TurnOff(), ActorRef.noSender())

            } else {

                if (fsm.currentState == Off.name)
                    self.tell(new TurnOn(payload: message?.clone()), ActorRef.noSender())

                setLightLevel(avg)
                setLightColor(avg)

            }


        } else if (message instanceof String) {

            log.debug message

        }

    }

    @Override
    void configureFsmDsl() {

        fsm.record().onCommands([TurnOff]).fromState(On).goToState(Off).transition = { Command command ->
            turnLightOff()
        }

        fsm.record().onCommands([TurnOn]).fromState(Off).goToState(On).transition = { Command command ->
            turnLightOn()
        }


    }

    void turnLightOn() {
        httpClientService.execute("http://192.168.20.114/api/HomeAutomation.PhilipsHue/${light.node}", "Control.On")
    }

    void turnLightOff() {
        httpClientService.execute("http://192.168.20.114/api/HomeAutomation.PhilipsHue/${light.node}", "Control.Off")
    }

    void setLightLevel(level) {
        httpClientService.execute("http://192.168.20.114/api/HomeAutomation.PhilipsHue/${light.node}/Control.Level", "${level.toString()}")
    }

    void setLightColor(level) {

        if (!level)
            return

        double val = level?.toDouble()

        def res = []





        if (val == 0) {
            // do nothing
        } else if (val <= 5 && fsm.currentState == On.name) {

            res << colorList.find{it.description==Color.Name.GREEN}

        } else if (val <= 10 && fsm.currentState == On.name) {

            res << colorList.find{it.description==Color.Name.BLUE}

        } else if (val <= 25 && fsm.currentState == On.name) {

            res << colorList.find{it.description==Color.Name.PURPLE}

        } else if (val <= 50 && fsm.currentState == On.name) {

            if (self.path().name().contains("lightRearLeft")) {
                res << colorList.find{it.description==Color.Name.ORANGE}
            }

            if (self.path().name().contains("lightRearCenter")) {
                res << colorList.find{it.description==Color.Name.PURPLE}
            }

            if (self.path().name().contains("lightRearRight")) {
                res << colorList.find{it.description==Color.Name.PURPLE}
            }

            if (self.path().name().contains("lightKitchenIsland")) {
                res << colorList.find{it.description==Color.Name.PURPLE}
            }

            if (self.path().name().contains("lightPumpkinLeft")) {
                res << colorList.find{it.description==Color.Name.PURPLE}
            }

            if (self.path().name().contains("lightPumpkinRight")) {
                res << colorList.find{it.description==Color.Name.ORANGE}
            }

        } else if (val <= 75 && fsm.currentState == On.name) {

            if (self.path().name().contains("lightRearLeft")) {
                res << colorList.find{it.description==Color.Name.PURPLE}
            }

            if (self.path().name().contains("lightRearCenter")) {
                res << colorList.find{it.description==Color.Name.ORANGE}
            }

            if (self.path().name().contains("lightRearRight")) {
                res << colorList.find{it.description==Color.Name.PINK}
            }


            if (self.path().name().contains("lightKitchenIsland")) {
                res << colorList.find{it.description==Color.Name.ORANGE}
            }

            if (self.path().name().contains("lightPumpkinLeft")) {
                res << colorList.find{it.description==Color.Name.ORANGE}
            }

            if (self.path().name().contains("lightPumpkinRight")) {
                res << colorList.find{it.description==Color.Name.PINK}
            }


        } else if (val > 75 && fsm.currentState == On.name) {

            res << colorList.find{it.description==Color.Name.PURPLE}
            res << colorList.find{it.description==Color.Name.ORANGE}
            res << colorList.find{it.description==Color.Name.PURPLE}

        }

        if (res.size()) {
            res.each { Color color ->
                if (color instanceof ColorHue) {
                    httpClientService.execute("http://192.168.20.114/api/HomeAutomation.PhilipsHue/${light.node}/Control.ColorHsb", "${color.RGB}")
                }
            }
        }


    }

}