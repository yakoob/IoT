package com.yakoobahmad.utils

import com.yakoobahmad.command.video.Play
import com.yakoobahmad.event.SoundDetected
import com.yakoobahmad.halloween.Video
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class MqttSerializerService {

    def serialize(String topic, String message) {

        if (topic.contains("Aurduino/HomeAutomation.Audio/102/event")){
            def JsonObject = JSON.parse(message)
            def res = SoundDetected.newInstance()
            res.level = JsonObject.Value
            return res
        }

        else if (topic.contains("ActorSystem/Halloween/Projector")){
            def json = JSON.parse(message)
            if (json.command == "songComplete")
                return new Play(media: Video.findByName(Video.Name.WOODS))
        }

    }
}
