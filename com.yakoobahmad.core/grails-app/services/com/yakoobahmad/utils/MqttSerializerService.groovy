package com.yakoobahmad.utils

import com.google.gson.Gson
import com.yakoobahmad.event.MediaPlaybackComplete
import com.yakoobahmad.event.MediaPlaybackStarted
import com.yakoobahmad.event.MotionDetected
import com.yakoobahmad.event.SoundDetected
import com.yakoobahmad.media.Video
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class MqttSerializerService {

    def serialize(String topic, String message) {

        if (topic.contains("HomeGenie/HomeAutomation.ZWave/8/event")){
            def JsonObject = JSON.parse(message)
            if (JsonObject.Name == "Sensor.Tamper"){
                def res = new MotionDetected()
                return res
            }
            return
        }

        if (topic.contains("Aurduino/HomeAutomation.Audio/102/event")){
            def JsonObject = JSON.parse(message)
            def res = SoundDetected.newInstance()
            res.level = JsonObject.Value
            return res
        }

        if (topic.contains("ActorSystem/Halloween/Projector")){

            def json = JSON.parse(message)
            Gson gson = new Gson()

            if (json.command == "songComplete"){
                return new MediaPlaybackComplete()
            }

            if (json.event == "playbackStarted"){
                return new MediaPlaybackStarted(media:new Gson().fromJson(message, Video.class))
            }
        }
    }

}
