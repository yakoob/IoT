package com.yakoobahmad.networking

import groovy.util.logging.Log
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.beans.factory.annotation.Value

@Log
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
        log.severe "connectionLost: ${cause.message} ${cause.stackTrace}"
        sleep(5000)
        mqttClient = null
        init()
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {

            /*
            Command command = messageSerializerService.serialize(topic, state?.toString())
            if (command)
                akkaService.getHalloweenManager().tell(command, akkaService.actorNoSender())
              */
            log.info "mqtt messageArrived >> topic:$topic | ${message.toString()}"
        } catch (e) {
            log.severe e.stackTrace
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info "mqtt deliveryComplete: ${token.toString()}"
    }

    def publish(String topic, String payload){
        if (mqttClient) {
            MqttMessage message=new MqttMessage()
            message.setPayload(payload.bytes)
            mqttClient.publish(topic, message)
        }
        log.severe("can not publish message $payload because mqttClient not configured")
    }

}
