/*
  Exposes rest methods for triggering the door circuit, and reading the status of the two sensors

  Largely based on Arduino Yun Bridge example:
  http://www.arduino.cc/en/Tutorial/Bridge
*/

#include <Bridge.h>
#include <BridgeServer.h>
#include <BridgeClient.h>

// Listen to the default port 5555, the Yún webserver
// will forward there all the HTTP requests you send
BridgeServer server;

void setup() {
  // set up the pins
  pinMode(3, OUTPUT);
  pinMode(A0, INPUT);
  pinMode(A1, INPUT);
  
  // Bridge startup
  pinMode(13, OUTPUT);
  digitalWrite(13, LOW);
  Bridge.begin();
  digitalWrite(13, HIGH);

  // Listen for incoming connection only from localhost
  // (no one from the external network could connect)
  server.listenOnLocalhost();
  server.begin();
}

void loop() {
 
  // Get clients coming from server
  BridgeClient client = server.accept();

  // There is a new client?
  if (client) {
    // Process request
    process(client);

    // Close connection and free resources.
    client.stop();
  }

  delay(50); // Poll every 50ms
}

void process(BridgeClient client) {
  // read the command
  String command = client.readStringUntil('/');

  if (command == "door") {
    triggerDoor();
    client.println(F("Door Triggered"));
  }
  if (command == "status") {
    getStatus(client);
  }
  
}

void triggerDoor() {
  digitalWrite(3, HIGH);
  delay(500);
  digitalWrite(3, LOW);
}

void getStatus(BridgeClient client) {
  int redPinValue = analogRead(A0);
  int yellowPinValue = analogRead(A1);
  client.print(F("{ red: "));
  client.print(redPinValue);
  client.print(F(", yellow: "));
  client.print(yellowPinValue);
  client.println(F(" }"));
}

