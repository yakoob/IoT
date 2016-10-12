#include <PubSubClient.h>

#include <Bridge.h>
#include <YunServer.h>
#include <YunClient.h>
#include <Servo.h>

YunServer server;
Servo smoke;

YunClient net;
IPAddress mqttServerAddress(192, 168, 20, 114);

PubSubClient mqttClient(net);

long lastReconnectAttempt = 0;
long lastAudioSensorPublish = 0;

boolean reconnect() {
  if (mqttClient.connect("arduinoClient161")) {
    mqttClient.publish("arduionoClient_reconnected","OK");
  }
  return mqttClient.connected();
}

void setup() {

  smoke.attach(5);
  Bridge.begin();

  server.listenOnLocalhost();
  server.begin();

  Serial.begin(9600);

  mqttClient.setServer(mqttServerAddress,1883);
  mqttClient.connect("arduinoClient_161");

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

  YunClient client = server.accept();

  if (client) {
    process(client);
    client.stop();
  }

}

void process(YunClient client) {

  String command = client.readStringUntil('/');

  // Check if the url contains the word "servo"
  if (command == "servo") {
    servoCommand(client);
  }

}

void servoCommand(YunClient client) {

  int pin;
  int value;

  // Get the servo Pin
  pin = client.parseInt();

  // Check if the url string contains a value (/servo/6/VALUE)
  if (client.read() == '/') {

    value = client.parseInt();

    // smoke
    if (pin == 5) {
      smoke.write(value);
      if (value == 25) { // smoke on
        mqttClient.publish("Aurduino/HomeAutomation.Servo/101/event","{'Value':'1'}");
      } else {
        mqttClient.publish("Aurduino/HomeAutomation.Servo/101/event","{'Value':'0'}");
      }
    }

  }

}
