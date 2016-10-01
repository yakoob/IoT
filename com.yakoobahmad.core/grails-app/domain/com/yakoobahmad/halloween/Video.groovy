package com.yakoobahmad.halloween

import com.yakoobahmad.domain.DomainSerializer
import com.yakoobahmad.domain.Media

class Video implements DomainSerializer, Media {

    enum Name {NONE,WOODS,GRIM_GRINNING_GHOST,KIDNAP_SANDY_CLAWS,MONSTER_MASH,THIS_IS_HALLOWEEN,WHATS_THIS}

    Name name
    String command

    @Override
    public String getJsonTemplatePath(){
        return "/halloween/_video"
    }

    @Override
    String toString(){
        return "${this.class.simpleName} >> id: $id | $name | $command"
    }

    static constraints = {
        command nullable: true
    }
}