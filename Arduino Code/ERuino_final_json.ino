
#include "Arduino.h"
#include <Stepper.h>
#include <Servo.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <math.h>
//#include <String>
#include <map>
#include <vector>
#include <sstream>
#include <ArduinoJson.h>
//using namespace std;



// Define number of steps per rotation:
// Connections to A4988
const int dirPin = 0;  // Direction
const int stepPin = 2; // Step

 
// Motor steps per rotation
const int STEPS_PER_REV = 200;

const char* ssid = "Aseel12";
const char* password = "12345678";


//Your Domain name with URL path or IP address with path
String serverName = "https://us-central1-eruino-1b4be.cloudfunctions.net/";
String updateCloud= "https://us-central1-eruino-1b4be.cloudfunctions.net/update?";
String getCloud= "https://us-central1-eruino-1b4be.cloudfunctions.net/get?switch=gear";


// lifting house cloud link
//String liftStr = "https://console.firebase.google.com/project/eruino-1b4be/database/eruino-1b4be/data/~2Fgear~2Flift";


// the following variables are unsigned longs because the time, measured in
// milliseconds, will quickly become a bigger number than can be stored in an int.
//unsigned long lastTime = 0;
// Timer set to 10 minutes (600000)
//unsigned long timerDelay = 600000;
// Set timer to 5 seconds (5000)
//unsigned long timerDelay = 5000;

//Sensors
int water_sensor = 34;      // select the pin for the LED
int gas_sensor = 35;       //gas sensor
int motion_sensor = 33 ;
int flame_sensor = 32;

//Gear
int relay_1 = 5 ;
int relay_alarm = 25;
int servo = 18 ;
Servo myServo;
int pos = 0;
//const int stepsPerRevolution = 2048;

// Wiring:
// Pin 8 to IN1 on the ULN2003 driver 25
// Pin 9 to IN2 on the ULN2003 driver 26 
// Pin 10 to IN3 on the ULN2003 driver 27 
// Pin 11 to IN4 on the ULN2003 driver 14
bool fire_danger = false;
bool flood_danger = false;
bool gas_leakage_danger = false;
bool theift_danger = false;
int gasThres = 900;
int waterThres = 2500;
int flameThres = 2000;
int motionThres = 1000;
int gas_detected = 0;
int housePositionFlag = 0;

// sensors reading

int water_reading = 0;
int gas_reading   = 0;
int motion_reading= 0;
int flame_reading = 0;
int travel_mode = 0;

//switches mode

String* fan = new String();
String* window = new String();
String* lift = new String();
String* travelMode = new String();




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////// setup ////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void setup() {

  Serial.begin(9600); 

  WiFi.begin(ssid, password);
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
 
  Serial.println("Timer set to 5 seconds (timerDelay variable), it will take 5 seconds before publishing the first reading.");

  pinMode(water_sensor, INPUT);
  pinMode(gas_sensor, INPUT);
  pinMode(motion_sensor, INPUT);
  pinMode(flame_sensor, INPUT);

  pinMode(relay_1, OUTPUT);
  pinMode(relay_alarm, OUTPUT);

  pinMode(servo, OUTPUT);

// Setup the pins as Outputs
  pinMode(stepPin,OUTPUT); 
  pinMode(dirPin,OUTPUT);
 

  myServo.attach(servo);
  myServo.write(90); // init the servo motor 

  digitalWrite(relay_alarm, LOW);
  digitalWrite(relay_1, LOW);
  *window = "CLOSED";
  *fan = "OFF";
  
}





/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////// loop /////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void loop() {
      
   getCloudHTTP();
// motion dected while travel mode on -> alarm
   HandleMotion();
  

//check if we need to put the house down, acording to the lift value in firebase.
  putTheHouseDown();

 //open window if requested by the user.
  if(!(theift_danger || fire_danger || gas_leakage_danger || flood_danger)){
      openWindow();
  }
 // Serial.printf("Water is %d , gas is %d , motion is %d , flame is %d \n",water_reading,gas_reading,motion_reading,flame_reading);
  
// water flood -> lift the house.
    HandleFlood();  
  

// gas leakage detected -> open open window + turn on the fan
   HandleGasLeakage(); 
  

 
// flame detected -> alarm
   HandleFlame();
   

 // Turn vent on or off if requested.
    turnVentOnOff();
 


// get the switches mode from the cloud.  
 //  getCloudHTTP();


// update senors&gear in the cloud.
   updateCloudHTTP();

   delay(2500);


}


void HandleFlood(){

 water_reading  = analogRead(water_sensor);
  if(water_reading < waterThres && housePositionFlag == 0) {

    // dected water leakage -> raise the house
    flood_danger = true;
    // Set motor direction counterclockwise
    stepperlevatate(LOW);
    housePositionFlag=1;
    //updateGearHttp("lift","UP");
    *lift = "UP";
  }else{
    flood_danger = false;
  }
  
}


void HandleGasLeakage(){
  
  gas_reading = analogRead(gas_sensor);
  if(gas_reading > gasThres) {  
    gas_detected=1;
    digitalWrite(relay_1, HIGH);
    for(; pos<=90; pos++){
      myServo.write(pos);
      delay(50);
    }
    gas_leakage_danger = true;
  } else{
    digitalWrite(relay_1, LOW);
    gas_detected=0;
    for(; pos>=0; pos--){
      myServo.write(pos);
      delay(30);
      gas_leakage_danger = false;
    }
 }
}

void HandleFlame(){

  flame_reading  = analogRead(flame_sensor);
  if(flame_reading < flameThres ) {
    digitalWrite(relay_alarm, HIGH);
    fire_danger = true;
  }else{
    if(!theift_danger){
      digitalWrite(relay_alarm, LOW);
    }
    fire_danger = false;
  }

}

void HandleMotion(){

  motion_reading = analogRead(motion_sensor);
  
    if(motion_reading > motionThres ) {
      // need to check if travelMode is on , and if it's on send a notificiation to the user!
      if(*travelMode == "ON"){
      theift_danger = true;
      digitalWrite(relay_alarm, HIGH);
      }
      else if(!fire_danger){
        digitalWrite(relay_alarm, LOW);
      }
      
    }
    else {
      if(!fire_danger){
        digitalWrite(relay_alarm, LOW);
      }
      theift_danger = false;
    } 
  
}

void openWindow(){
// check window mode   
 if (*window == "OPEN"){
    myServo.write(90);
 }else{
    if(!gas_detected){
       myServo.write(0);
    }
  }
}

void stepperlevatate(int num){
    digitalWrite(dirPin,num);

  for(int i=0; i<30;i++){
  // Spin motor two rotations quickly
    for(int x = 0; x < (STEPS_PER_REV * 2); x++) {
      digitalWrite(stepPin,HIGH);
      delayMicroseconds(500);
      digitalWrite(stepPin,LOW);
      delayMicroseconds(500);
    }
  }
}

void putTheHouseDown(){
// put the condition
  if (*lift == "DOWN" && housePositionFlag){
      stepperlevatate(HIGH);
      housePositionFlag =0;
  }
}

void turnVentOnOff(){
  if(*fan == "ON" && !gas_leakage_danger){
   digitalWrite(relay_1, HIGH); 
  }else if(!gas_leakage_danger){
    digitalWrite(relay_1, LOW); 
  }
}

void getCloudHTTP(){

  if(WiFi.status()== WL_CONNECTED){
      HTTPClient http;
      http.begin(getCloud.c_str());
      
      // Send HTTP GET request
      int httpResponseCode = http.GET();
      
      if (httpResponseCode>0) {
        String gear_json = http.getString();
        DynamicJsonDocument doc(1024);
        deserializeJson(doc, gear_json);
        JsonObject obj = doc.as<JsonObject>();

        *fan = obj["fan"].as<String>();
        *lift= obj["lift"].as<String>();
        *travelMode= obj["travelMode"].as<String>();
        *window= obj["windows"].as<String>();  
        Serial.printf("Fan %s lift %s travel %s window %s\n",*fan,*lift,*travelMode,*window);
      }else {
        Serial.print("Error code: ");
        Serial.println(httpResponseCode);
      }
      // Free resources
      http.end();   
  }else{
    Serial.println("WiFi Disconnected");
  }
}

void updateCloudHTTP(){

  if(WiFi.status()== WL_CONNECTED){
      HTTPClient http;
      String water = "water=" + String(water_reading) +"&";
      String gas = "gas=" + String(gas_reading) + "&";
      String flame = "flame=" + String(flame_reading) + "&";
      String motion = "motion=" + String(motion_reading) + "&";
      String fan_str = "fan=" + *fan + "&";
      String lift_str = "lift=" + *lift + "&";
      String window_str = "window=" + *window ;
      String gear = water + gas + flame + motion + fan_str + lift_str + window_str;
      String serverPath = updateCloud + gear;
            // Your Domain name with URL path or IP address with path
      http.begin(serverPath.c_str());
      
      // Send HTTP GET request
      int httpResponseCode = http.GET();
      
      if (httpResponseCode>0) {
      //  Serial.print("HTTP Response code: ");
     //   Serial.println(httpResponseCode);
        String payload = http.getString();
     //   Serial.println(payload);
      }
      else {
        Serial.print("Error code: ");
        Serial.println(httpResponseCode);
      }
      // Free resources
      http.end();
  }
  else{
    Serial.println("WiFi Disconnected");
  }
}


  
