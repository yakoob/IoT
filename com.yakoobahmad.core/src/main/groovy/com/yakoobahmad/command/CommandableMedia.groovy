package com.yakoobahmad.command

import com.yakoobahmad.domain.media.Media

/**
 * Created by yakoobahmad on 9/29/16.
 */
trait CommandableMedia {

    Media media

    public void setMedia(Media m){
        if (m){
            media = m
            media.command = this.class.simpleName
        }
    }
}