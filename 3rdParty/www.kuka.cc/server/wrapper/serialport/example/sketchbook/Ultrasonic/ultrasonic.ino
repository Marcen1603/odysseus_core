#include "Wire.h"
#include "SRF02.h"

//SRF02 sensor(0x70, SRF02_CENTIMETERS);
SRF02 sensor(0x70, SRF02_MICROSECONDS);

unsigned long nextPrint = 0;

void setup()
{
  Serial.begin(9600);
  Wire.begin();
}

void loop()
{
  SRF02::update();
  if (millis() > nextPrint)
  {
    Serial.print(millis());
    Serial.print(",");
    Serial.println(sensor.read());
    nextPrint = millis () + 250;
  }
}
