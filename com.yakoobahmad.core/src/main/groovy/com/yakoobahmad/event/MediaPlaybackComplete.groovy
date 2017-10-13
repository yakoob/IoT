package com.yakoobahmad.event

import groovy.transform.AutoClone
import groovy.transform.ToString

@AutoClone
@ToString(includeSuper = true)
class MediaPlaybackComplete extends Event {
    Boolean next = false
}
