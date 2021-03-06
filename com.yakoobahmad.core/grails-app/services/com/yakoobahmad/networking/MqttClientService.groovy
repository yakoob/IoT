package com.yakoobahmad.networking

import com.yakoobahmad.config.GlobalConfig
import com.yakoobahmad.event.MediaPlaybackComplete
import com.yakoobahmad.event.MediaPlaybackStarted
import com.yakoobahmad.event.MotionDetected
import com.yakoobahmad.event.SoundDetected
import groovy.util.logging.Slf4j
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.beans.factory.annotation.Value

@Slf4j
class MqttClientService implements MqttCallback {

    public MqttClient mqttClient

    @Value('${mqtt.client.connectOnStartUp}')
    boolean connectOnStartup

    @Value('${mqtt.client.ip}')
    String ip

    @Value('${mqtt.client.port}')
    String port

    def akkaService
    def serverService
    def mqttSerializerService

    void init() {
        if (connectOnStartup) {
            mqttClient = new MqttClient("tcp://${ip}:${port}", "${serverService.thisServer.name}", mqttPersistence)
            mqttClient.connect()
            mqttClient.setCallback(this)
            mqttClient.setTimeToWait(3000)
            mqttClient.subscribe("#") // subscript to all topics
            log.info "mqtt client connected"
        }
    }

    private MemoryPersistence getMqttPersistence(){
        return new MemoryPersistence()
    }

    @Override
    public void connectionLost(Throwable me) {

        log.warn("msg "+me?.getMessage())
        log.warn("loc "+me?.getLocalizedMessage())
        log.warn("cause "+me?.getCause())
        log.warn("excep "+me?.toString())
        me?.printStackTrace()

        sleep(5000)
        mqttClient = null
        init()
    }

    @Override
    public void messageArrived(String topic, MqttMessage m) {
        try {

            def message = mqttSerializerService.serialize(topic, m?.toString())

            if (message instanceof SoundDetected) {

                akkaService.soundDetection?.tell(message, akkaService.actorNoSender())

            }

            else if (message instanceof MediaPlaybackComplete) {
                akkaService.homeManager?.tell(message, akkaService.actorNoSender())
            }

            else if (message instanceof MediaPlaybackStarted ) {
                akkaService.homeManager?.tell(message, akkaService.actorNoSender())
            }

            else if (message instanceof MotionDetected ){
                akkaService.homeManager?.tell(message, akkaService.actorNoSender())
            }

            // log.debug "mqtt messageArrived >> topic:$topic | ${m.toString()}"

        } catch (e) {
            println e?.printStackTrace()
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // log.debug "mqtt deliveryComplete: ${token.toString()}"
    }

    def publish(String topic, String payload){
        println ">>> publish topic: $topic | payload: $payload"

        if (mqttClient) {
            MqttMessage message=new MqttMessage()
            message.setPayload(payload.bytes)
            mqttClient.publish(topic, message)
        } else {
            log.error("can not publish message $payload because mqttClient not configured")
        }
    }

}
