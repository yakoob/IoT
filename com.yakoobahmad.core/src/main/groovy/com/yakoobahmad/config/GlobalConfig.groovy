package com.yakoobahmad.config

import org.springframework.beans.factory.annotation.Value

trait GlobalConfig {

    @Value('${app.mode.christmas}')
    def christmasEnabled = false

    @Value('${app.mode.hallweeen}')
    def halloweenEnabled = true

    Boolean getHalloweenEnabled(){
        return true
        if (this.halloweenEnabled instanceof Boolean){
            return this.halloweenEnabled
        } else if (this.halloweenEnabled instanceof String) {
            return this.halloweenEnabled == 'true' ? true:false
        }
        return false
    }

    Boolean getChristmasEnabled(){
        return false
        if (this.christmasEnabled instanceof Boolean){
            return this.christmasEnabled
        } else if (this.christmasEnabled instanceof String) {
            return this.christmasEnabled == 'true' ? true:false
        }
        return false
    }

}