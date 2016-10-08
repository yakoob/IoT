#include <PubSubClient.h>

#include <Bridge.h>
#include <BridgeServer.h>
#include <BridgeClient.h>
#include <Servo.h>

BridgeServer server;
Servo projector;

BridgeClient net;
IPAddress mqttServerAddress(192, 168, 20, 114);

PubSubClient mqttClient(net);

int soundAnalogPin = 0;
int sound_value = 0;

long lastReconnectAttempt = 0;
long lastAudioSensorPublish = 0;

boolean reconnect() {
  if (mqttClient.connect("arduinoClient157")) {
    mqttClient.publish("arduionoClient_reconnected","OK");
  }
  return mqttClient.connected();
}

void setup() {

  projector.attach(6);

  Bridge.begin();

  server.listenOnLocalhost();
  server.begin();

  Serial.begin(9600);

  mqttClient.setServer(mqttServerAddress,1883);
  mqttClient.connect("arduinoClient_157");

}

void loop() {

  if (!mqttClient.connected()) {
    long now = millis();
    if (now - lastReconnectAttempt > 5000) {
      lastReconnectAttempt = now;
      // Attempt to reconnect
      if (reconnect()) {
        lastReconnectAttempt = 0;
      }
    }
  } else {
    mqttClient.loop();
  }

  long audioSensorNow = millis();
  if (audioSensorNow - lastAudioSensorPublish > 50) {
    lastAudioSensorPublish = audioSensorNow;
    sound_value = analogRead(soundAnalogPin);
    if (sound_value) {
      String str = "{'Value':'";
      str += sound_value;
      str = str + "'}";
      int str_len = str.length() + 1;
      char char_array[str_len];
      str.toCharArray(char_array, str_len);

      Serial.println("sound value:" + sound_value);
      
      mqttClient.publish("Aurduino/HomeAutomation.Audio/102/event",char_array);
    }
  }

}

