package com.yakoobahmad.actor.halloween

import akka.actor.Cancellable
import groovy.util.logging.Log
import com.yakoobahmad.actor.BaseActor
import com.yakoobahmad.command.Command
import com.yakoobahmad.fsm.FSM
import com.yakoobahmad.fsm.state.Off
import com.yakoobahmad.halloween.light.Hue
import com.yakoobahmad.visualization.Color
import com.yakoobahmad.visualization.ColorHue
import grails.util.Holders
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit

@Log
class Light extends BaseActor implements FSM {

    Hue light

    Cancellable lightTimer

    LinkedList easyColorList = new LinkedList<ColorHue>()
    Boolean colorAlternator = true

    def httpClientService = Holders.applicationContext.getBean("httpClientService")

    Light(Hue hue){

        Hue.withNewSession {

            light = hue

            easyColorList.addLast(ColorHue.findByDescription(Color.Name.GREEN))
            easyColorList.addLast(ColorHue.findByDescription(Color.Name.BLUE))
            Collections.shuffle(easyColorList)

        }

        startStateMachine(Off)
        configureFsmDsl()

        startLightTimer()
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

    }

    private void remoteDispatch(Command command){

    }

    private void startLightTimer(){

        lightTimer = context.system().scheduler().schedule(Duration.Zero(), Duration.create(10, TimeUnit.SECONDS),
        new Runnable() {
            @Override
            public void run() {
                httpClientService.execute("http://192.168.20.114/api/HomeAutomation.PhilipsHue/${light.node}/Control.ColorHsb","${alternateEasyColor().RGB}")
            }
        }, context.system().dispatcher())

        context.system().scheduler().schedule(Duration.create(light.node.toInteger(), TimeUnit.SECONDS), Duration.create(1000, TimeUnit.MILLISECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        lightLevel()
                    }
                }, context.system().dispatcher())
    }


    public void lightLevel(){
        def level = new Random().nextInt(100) + 1
        httpClientService.execute("http://192.168.20.114/api/HomeAutomation.PhilipsHue/${light.node}/Control.Level","$level")
    }

    public ColorHue alternateEasyColor(){

        if (colorAlternator){
            colorAlternator = false
            return easyColorList.first()
        } else {
            colorAlternator = true
            return easyColorList.last()
        }

    }




}
