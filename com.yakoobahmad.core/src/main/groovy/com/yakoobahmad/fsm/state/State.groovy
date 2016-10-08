package com.yakoobahmad.fsm.state

import com.yakoobahmad.fsm.FiniteStateMachine

trait State implements Serializable {

    public Boolean is(FiniteStateMachine fsm){
        if (this.class.name == fsm.currentState)
            return true
        return false
    }

}