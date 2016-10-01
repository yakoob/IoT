package com.yakoobahmad.command

import groovy.transform.AutoClone

@AutoClone
class Command implements Serializable {

    public String getName(){
        this.class.simpleName
    }

}
