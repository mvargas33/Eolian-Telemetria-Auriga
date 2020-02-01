long BMS[5];

void setup() {
  BMS[0] = 65535;
  BMS[1] = 255;
  BMS[2] = 26;
  BMS[3] = 72;
  BMS[4] = 1;
  Serial.begin(9600);
}

void loop() {
  Serial.print("BMS_ORIGEN");
  Serial.print(" ");
  for(int i=0; i<5; i++){
    Serial.print(BMS[i]);
    Serial.print(" ");
  }
  if(BMS[4]){
    BMS[4] = 0;
  }else{
    BMS[4] = 1;
  }
  Serial.print('\n');
  delay(150);
  
}
