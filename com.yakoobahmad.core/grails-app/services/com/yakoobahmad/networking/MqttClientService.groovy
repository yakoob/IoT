package com.yakoobahmad.networking

import com.yakoobahmad.event.MediaPlaybackComplete
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
            mqttClient.subscribe("#") // subscript to all topics
            log.info "mqtt client connected"
        }
    }

    private MemoryPersistence getMqttPersistence(){
        return new MemoryPersistence()
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.error "connectionLost: ${cause.message} ${cause.stackTrace}"
        sleep(5000)
        mqttClient = null
        init()
    }

    @Override
    public void messageArrived(String topic, MqttMessage m) {
        try {

            def message = mqttSerializerService.serialize(topic, m?.toString())

            if (message instanceof SoundDetected)
                akkaService.soundDetection.tell(message, akkaService.actorNoSender())
            else if (message instanceof MediaPlaybackComplete)
                akkaService.halloweenManager.tell(message, akkaService.actorNoSender())

            log.debug "mqtt messageArrived >> topic:$topic | ${m.toString()}"

        } catch (e) {
            log.error e.stackTrace
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug "mqtt deliveryComplete: ${token.toString()}"
    }

    def publish(String topic, String payload){
        if (mqttClient) {
            MqttMessage message=new MqttMessage()
            message.setPayload(payload.bytes)
            mqttClient.publish(topic, message)
        } else {
            log.error("can not publish message $payload because mqttClient not configured")
        }
    }

}
