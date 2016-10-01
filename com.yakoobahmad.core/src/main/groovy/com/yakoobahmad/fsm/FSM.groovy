package com.yakoobahmad.fsm

import com.yakoobahmad.fsm.state.State

trait FSM {

    FiniteStateMachine fsm

    void startStateMachine(Class<State> state){
        fsm = FiniteStateMachine.newInstance(state)
    }

    abstract void configureFsmDsl()

}