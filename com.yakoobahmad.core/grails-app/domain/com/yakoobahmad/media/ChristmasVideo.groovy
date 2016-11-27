package com.yakoobahmad.media

import com.yakoobahmad.domain.media.Media
import com.yakoobahmad.domain.utils.DomainSerializable

class ChristmasVideo implements DomainSerializable, Media {

    enum Name {DECK_THE_HALLS, GREAT_GIFT_WRAP, MARCH_WOODEN_SOLDIER, PACKING_SANTA_SLEIGH, TOY_TINKERING}

    Name name
    String command
    String event

    @Override
    public String getJsonTemplatePath(){
        return "/christmas/_video"
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