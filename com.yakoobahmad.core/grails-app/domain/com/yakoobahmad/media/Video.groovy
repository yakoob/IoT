package com.yakoobahmad.media

import com.yakoobahmad.domain.media.Media
import com.yakoobahmad.domain.utils.DomainSerializable

class Video implements DomainSerializable, Media {

    def name
    String command
    String event

    @Override
    public String getJsonTemplatePath(){
        return "this path should not be called"
    }

    @Override
    String toString(){
        return "${this.class.simpleName} >> id: $id | $name | $command"
    }

    static constraints = {
        command nullable: true
        event nullable: true
    }
}

