package com.yakoobahmad.actor.christmas

import com.yakoobahmad.actor.BaseActor
import grails.util.Holders

class Manager extends BaseActor {

    def akkaService = Holders.applicationContext.getBean("akkaService")

    // private ActorRef lightManager = null

    Manager() {
        // lightManager = context.system().actorOf(Props.create(LightManager.class), "global.zombieinvasion.LightManager")
    }

    @Override
    void onReceive(Object message) throws Exception {




    }

}
