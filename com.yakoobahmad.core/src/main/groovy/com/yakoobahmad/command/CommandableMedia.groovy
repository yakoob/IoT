package com.yakoobahmad.command

import com.yakoobahmad.domain.Media

trait CommandableMedia {

    Media media

    public void setMedia(Media m){
        media = m
        media.command = this.class.simpleName
    }


}