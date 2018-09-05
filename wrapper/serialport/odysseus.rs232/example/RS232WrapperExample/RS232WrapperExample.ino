int SENSOR_PIN = 0;
int LED_PIN = 13;


String line;


void setup() {
  Serial.begin(9600);
}

void loop() {
  int value = 0;

  value = analogRead(SENSOR_PIN);


  Serial.print(millis());
  Serial.print(",");
  Serial.println(value);

  delay(100);
  while (Serial.available() > 0){
    line = Serial.readStringUntil('\n');
    if (line.equalsIgnoreCase("'off'")) {
      digitalWrite(LED_PIN, LOW);
    } 
    else if (line.equalsIgnoreCase("'on'")) {
      digitalWrite(LED_PIN, HIGH);
    }
    line = ""; 
  }

  delay(1000); 
}










