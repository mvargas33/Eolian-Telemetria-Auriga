union Data {
  unsigned long valor; // 32 bit
  byte bytes[4];
};

union Data d[512];

void setup() {
  Serial.begin(9600);
  
}

void loop() {
  Serial.write(127);
  Serial.write(126);

  
  for(int i = 0; i<512;i++){
    d[i].valor = i+ 0;
    Serial.write(d[i].bytes[3]);
    Serial.write(d[i].bytes[2]);
    Serial.write(d[i].bytes[1]);
    Serial.write(d[i].bytes[0]);
  }

  Serial.write(126);
  Serial.write(127);
  delay(2000);
}
