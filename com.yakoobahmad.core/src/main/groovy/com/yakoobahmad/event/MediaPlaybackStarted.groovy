package com.yakoobahmad.event

import com.yakoobahmad.domain.media.Media
import groovy.transform.ToString

@ToString(includeSuper = true)
class MediaPlaybackStarted extends Event {
    Media media
}
