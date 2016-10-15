package com.yakoobahmad.visualization

import com.yakoobahmad.HueEffect

class Color {

    enum Name {PURPLE,BLUE,GREEN,PINK,ORANGE,RED,BLACK,WHITE}

    Name description

    String red
    String blue
    String green

    @Override
    public String toString(){
        return "${this.class.simpleName}|$description"
    }

}
