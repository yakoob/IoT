package com.yakoobahmad.config

import org.springframework.beans.factory.annotation.Value

trait GlobalConfig {

    @Value('${app.mode.christmas}')
    def christmasEnabled

    @Value('${app.mode.hallweeen}')
    def halloweenEnabled

    Boolean getHalloweenEnabled(){
        if (this.halloweenEnabled instanceof Boolean){
            return this.halloweenEnabled
        } else if (this.halloweenEnabled instanceof String) {
            return this.halloweenEnabled == 'true' ? true:false
        }
        return false
    }

    Boolean getChristmasEnabled(){
        if (this.christmasEnabled instanceof Boolean){
            return this.christmasEnabled
        } else if (this.christmasEnabled instanceof String) {
            return this.christmasEnabled == 'true' ? true:false
        }
        return false
    }

}