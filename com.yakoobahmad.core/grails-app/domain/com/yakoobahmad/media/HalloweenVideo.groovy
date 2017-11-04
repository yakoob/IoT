package com.yakoobahmad.media

import com.yakoobahmad.domain.media.Media
import com.yakoobahmad.domain.utils.DomainSerializable
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = "name")
class HalloweenVideo implements DomainSerializable, Media {

    enum Name {
        WOODS,
        GRIM_GRINNING_GHOST,
        KIDNAP_SANDY_CLAWS,
        MONSTER_MASH,
        THIS_IS_HALLOWEEN,
        WHATS_THIS,
        OOGIE_BOOGIE_PUMPKINS,
        TIMEWARP,
        THRILLER,
        GHOSTBUSTERS,
        SAM_NOCOSTUME,
        SAM_SYMPHONY,
        SAM_SCARE1,
        SAM_SCARE2,
        SAM_SCARE3,
        SAM_SCARE4,
        BONEYARD_BAND,
        BONEYARD_PUMPKIN
    }
    Name name
    String command
    String event

    enum Type {PUMPKINS,HOLOGRAM,DISABLED}
    Type type

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
