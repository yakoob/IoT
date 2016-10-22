package com.yakoobahmad.device

class Light {

    enum State {OFF,ON}

    String node
    State state

    static mapping = {
    }

    static constraints = {
    }

    @Override
    public String toString(){
        return "${this.class.simpleName} node:$node with state:$state"
    }

}
