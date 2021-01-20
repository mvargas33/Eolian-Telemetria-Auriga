// demo: CAN-BUS Shield, receive data with check mode
// send data coming to fast, such as less than 10ms, you can use this way
// loovee, 2014-6-13


#include <SPI.h>
#include "mcp_can.h"

/*SAMD core*/
#ifdef ARDUINO_SAMD_VARIANT_COMPLIANCE
    #define SERIAL SerialUSB
#else
    #define SERIAL Serial
#endif

// the cs pin of the version after v1.1 is default to D9
// v0.9b and v1.0 is default D10
const int SPI_CS_PIN = 9;

MCP_CAN CAN(SPI_CS_PIN);                                    // Set CS pin

void setup() {
    SERIAL.begin(115200);

    while (CAN_OK != CAN.begin(CAN_500KBPS)) {            // init can bus : baudrate = 500k
        SERIAL.println("CAN BUS Shield init fail");
        SERIAL.println(" Init CAN BUS Shield again");
        delay(100);
    }
    SERIAL.println("CAN BUS Shield init ok!");
}


void loop() {
    unsigned char len = 0;
    unsigned char buf[8];

    if (CAN_MSGAVAIL == CAN.checkReceive()) {         // check if data coming
        CAN.readMsgBuf(&len, buf);    // read data,  len: data length, buf: data buf

        unsigned long canId = CAN.getCanId();

        if(canId == 0x620){
          SERIAL.println("-----------------------------");
          SERIAL.print("ID: 0x");SERIAL.println(canId, HEX);
  
          for (int i = 0; i < len; i++) { // print the data
              SERIAL.write(buf[i]);
              SERIAL.print("\t");
          }
          SERIAL.println();
        }
        else if (canId == 0x621){
          SERIAL.println("-----------------------------");
          SERIAL.print("ID: 0x");SERIAL.println(canId, HEX);
  
          for (int i = 0; i < len; i++) { // print the data
              SERIAL.write(buf[i]);
              SERIAL.print("\t");
          }
          SERIAL.println();
        }
        else if (canId == 0x622){
          SERIAL.println("-----------------------------");
          SERIAL.print("ID: 0x");SERIAL.println(canId, HEX);

          unsigned int state = buf[0];
          unsigned int timer = (buf[1] << 8) | buf[2];
          unsigned int flags = buf[3];
          unsigned int fault_code = buf[4];
          unsigned int level_faults = buf[5];
          unsigned int warnings = buf[6];
          
          SERIAL.print("state: ");SERIAL.println(state);
          SERIAL.print("timer: ");SERIAL.println(timer);
          SERIAL.print("flags: ");SERIAL.println(flags);
          SERIAL.print("fault_code: ");SERIAL.println(fault_code);
          SERIAL.print("level_faults: ");SERIAL.println(level_faults);
          SERIAL.print("warnings: ");SERIAL.println(warnings);
          SERIAL.println();
        }
        else if (canId == 0x623){
          SERIAL.println("-----------------------------");
          SERIAL.print("ID: 0x");SERIAL.println(canId, HEX);

          unsigned int pack_voltage = (buf[0] << 8) | buf[1];
          unsigned int min_vtg = buf[2];
          unsigned int min_vtg_id = buf[3];
          unsigned int max_vtg = buf[4];
          unsigned int max_vtg_id = buf[5];

          SERIAL.print("pack_voltage[V]: ");SERIAL.println(pack_voltage);
          SERIAL.print("min_vtg [V]: ");SERIAL.println(((double)min_vtg)/10);
          SERIAL.print("min_vtg_id: ");SERIAL.println(min_vtg_id);
          SERIAL.print("max_vtg [V]: ");SERIAL.println(((double)max_vtg)/10);
          SERIAL.print("max_vtg_id: ");SERIAL.println(max_vtg_id);
          SERIAL.println();
        }
        else if (canId == 0x624){
          SERIAL.println("-----------------------------");
          SERIAL.print("ID: 0x");SERIAL.println(canId, HEX);

          unsigned int current = (buf[0] << 8) | buf[1];
          int charge_limit = (buf[2] << 8) | buf[3];
          unsigned int discharge_limit = (buf[4] << 8) | buf[5];


          SERIAL.print("current [A]: ");SERIAL.println(current);
          SERIAL.print("charge_limit[kA]: ");SERIAL.println(charge_limit);
          SERIAL.print("discharge_limit[A]: ");SERIAL.println(discharge_limit);

          SERIAL.println();
        }
        else if (canId == 0x625){
          SERIAL.println("-----------------------------");
          SERIAL.print("ID: 0x");SERIAL.println(canId, HEX);

          unsigned int battery_energy_in = (buf[0] << 8*3) | (buf[1] << 8*2) | (buf[2] << 8*1) | buf[3];
          unsigned int battery_energy_out = (buf[4] << 8*3) | (buf[5] << 8*2) | (buf[6] << 8*1) | buf[7];

          SERIAL.print("battery_energy_in [kWH]: ");SERIAL.println(battery_energy_in);
          SERIAL.print("battery_energy_out[kWH]: ");SERIAL.println(battery_energy_out);
          SERIAL.println();
        }
        else if (canId == 0x626){
          SERIAL.println("-----------------------------");
          SERIAL.print("ID: 0x");SERIAL.println(canId, HEX);

          unsigned int soc = buf[0];
          unsigned int depth_of_discharge = (buf[1] << 8) | buf[2];
          unsigned int capacity_of_pack = (buf[3] << 8) | buf[4];
          unsigned int state_of_health = buf[6];

          SERIAL.print("SOC [%]: ");SERIAL.println(soc);
          SERIAL.print("Depth Of Discharge [kAH]: ");SERIAL.println(depth_of_discharge);
          SERIAL.print("Capacity of Pack [kAH]: ");SERIAL.println(capacity_of_pack);
          SERIAL.print("State of Health [%]: ");SERIAL.println(state_of_health);
          SERIAL.println();
        }
        else if (canId == 0x627){
          SERIAL.println("-----------------------------");
          SERIAL.print("ID: 0x");SERIAL.println(canId, HEX);

          unsigned int temp = buf[0];
          unsigned int min_temp = buf[2];
          unsigned int min_temp_id = buf[3];
          unsigned int max_temp = buf[4];
          unsigned int max_temp_id = buf[5];

          SERIAL.print("temp: ");SERIAL.println(temp);
          SERIAL.print("min_temp: ");SERIAL.println(min_temp);
          SERIAL.print("min_temp_id: ");SERIAL.println(min_temp_id);
          SERIAL.print("max_temp: ");SERIAL.println(max_temp);
          SERIAL.print("max_temp_id: ");SERIAL.println(max_temp_id);
          SERIAL.println();
        }
        else if (canId == 0x628){
          SERIAL.println("-----------------------------");
          SERIAL.print("ID: 0x");SERIAL.println(canId, HEX);

          unsigned int pack_resistance = (buf[0] << 8) | buf[1];
          unsigned int min_res = buf[2];
          unsigned int min_res_id = buf[3];
          unsigned int max_res = buf[4];
          unsigned int max_res_id = buf[5];

          SERIAL.print("pack_resistance: ");SERIAL.println(pack_resistance);
          SERIAL.print("min_res: ");SERIAL.println(min_res);
          SERIAL.print("min_res_id: ");SERIAL.println(min_res_id);
          SERIAL.print("max_res: ");SERIAL.println(max_res);
          SERIAL.print("max_res_id: ");SERIAL.println(max_res_id);
          SERIAL.println();
        }
    }
}

/*********************************************************************************************************
    END FILE
*********************************************************************************************************/
