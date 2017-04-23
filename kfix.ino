#include <dht.h>
#define pinTempHum 7
#define pinFans 6
#define pinRelayWater 10
#define pinRelayFan1 12
#define pinRelayFan2 13
#define pinSoil 0

dht DHT;
int fadeAmount = 50; 
int fanPower =0;
void setup() {
  Serial.begin(9600);
  while(! Serial);
  
  pinMode(pinFans,OUTPUT);
  pinMode(pinSoil,INPUT);
  pinMode(pinRelayWater,OUTPUT);  
  pinMode(pinRelayFan1,OUTPUT);
  pinMode(pinRelayFan2,OUTPUT);
  
  //digitalWrite(pinRelayWater,HIGH);
  digitalWrite(pinFans,HIGH);
}

void loop() 
{ 
  int chk = DHT.read11(pinTempHum);
  Serial.print("'temperature' : ");
  Serial.print("'");
  Serial.print(DHT.temperature);
  Serial.println("'");
  Serial.print("'humidity' : ");
  Serial.print("'");
  Serial.print(DHT.humidity);
  Serial.println("'");
  
  /*if(digitalRead(pinRelay)==HIGH)
  {
    Serial.println("water: on");
    //digitalWrite(pinRelay,LOW);
    delay(1000);
  }
  else if(digitalRead(pinRelay)==LOW)
  {
    Serial.println("water: off");
    //digitalWrite(pinRelay,HIGH);
    delay(1000);
  }
*/
  
  //analogWrite(pinFans,fanPower);
  int val = analogRead(0);

   
  if(DHT.temperature > 25)  
  {
    digitalWrite(pinRelayFan1,HIGH);
    digitalWrite(pinRelayFan2,HIGH);
    //fanPower = 255;  
  }
  else if(DHT.temperature >= 20 || DHT.temperature <= 25)
  {
    //fanPower = 255;
  }
  else if (DHT.temperature <= 20)
  {
    digitalWrite(pinRelayFan1,LOW);
    digitalWrite(pinRelayFan2,LOW);
    //fanPower = 10;
  }
  
 
  
  Serial.print("'soil' : ");
  if(val > 430 )
  {
    Serial.println("'dry'");
    digitalWrite(pinRelayWater,HIGH);      
  }
  else if( val <= 430 || val >= 350 )
  {
    Serial.println("'wet'");
    digitalWrite(pinRelayWater,LOW);  
  }
  else if( val <= 350 || val >= 260)
  {
    Serial.println("'water'");
  }
    
  
  delay(1000);
}
