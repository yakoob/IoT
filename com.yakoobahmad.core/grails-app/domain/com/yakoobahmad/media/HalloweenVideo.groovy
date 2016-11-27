package com.yakoobahmad.media

import com.yakoobahmad.domain.media.Media
import com.yakoobahmad.domain.utils.DomainSerializable

class HalloweenVideo implements DomainSerializable, Media {

    enum Name {WOODS,GRIM_GRINNING_GHOST,KIDNAP_SANDY_CLAWS,MONSTER_MASH,THIS_IS_HALLOWEEN,WHATS_THIS,OOGIE_BOOGIE_PUMPKINS}

    Name name
    String command
    String event

    @Override
    public String getJsonTemplatePath(){
        return "/halloween/_video"
    }

    @Override
    String toString(){
        return "${this.class.simpleName} >> id: $id | $name | $command"
    }

    static constraints = {
        name nullable: true
        command nullable: true
        event nullable: true
    }
}
