package com.yakoobahmad.event

import groovy.transform.ToString

@ToString(includeSuper = true)
class SoundDetected extends Event {

    String level

}
