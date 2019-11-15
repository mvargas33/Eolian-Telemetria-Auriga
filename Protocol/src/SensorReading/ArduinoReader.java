package SensorReading;

import Components.Component;
import SensorReading.SensorsReader;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.HashMap;
import java.util.LinkedList;

public class ArduinoReader extends SensorsReader implements SerialPortEventListener {



    public ArduinoReader(HashMap<String, Component> allComponents, LinkedList<Component> componentLinkedList) {
        super(allComponents, componentLinkedList);
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {

    }

    @Override
    public void run() {

    }
}
