package ApplicationLayer.SensorReading;

import PresentationLayer.Components.Component;

import java.util.HashMap;

/**
 * Clase que lee componentes del vehículo mediante interfaz CAN-Serial en el puerto USB
 */
public class USBCANReader extends SensorsReader{
    private String PORT; // USB Port


    public USBCANReader(HashMap<String, Component> allComponents, String PORT) {
        super(allComponents);
    }

    @Override
    public void run() {

    }
}
