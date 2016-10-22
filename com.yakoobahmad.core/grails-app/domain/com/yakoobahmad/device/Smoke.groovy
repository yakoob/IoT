package com.yakoobahmad.device

import com.yakoobahmad.domain.utils.DomainSerializable
import com.yakoobahmad.fsm.state.Off
import com.yakoobahmad.fsm.state.On

class Smoke implements DomainSerializable {

    enum Name {HALLOWEEN_REAR}
    enum State {OFF,ON}

    Name name
    State state
    int position

    void setPosition(int pos){
        this.position = pos
    }

    static Smoke.State getSmokeState(com.yakoobahmad.fsm.state.State s){
        if (s instanceof On)
            return com.yakoobahmad.device.Smoke.State.ON
        if (s instanceof Off)
            return com.yakoobahmad.device.Smoke.State.OFF
    }

    @Override
    public String getJsonTemplatePath(){
        return "/device/smoke/_smoke"
    }

    @Override
    String toString(){
        return "${this.class.simpleName} >> id: $id | $name | $state: $position"
    }

    static constraints = {

    }
}
