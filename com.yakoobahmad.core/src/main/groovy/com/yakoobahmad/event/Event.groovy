package com.yakoobahmad.event

import groovy.transform.AutoClone

@AutoClone
class Event implements Serializable {
    enum Node {ONE,TWO}
    Node node
}
