package com.yakoobahmad.halloween.light

import com.yakoobahmad.device.Light
import com.yakoobahmad.visualization.ColorHue

class Hue extends Light {

    ColorHue color

    static constraints = {

    }

    @Override
    public String toString(){
        return "${this.class.simpleName} node:$node with state:$state <> $color"
    }
}
