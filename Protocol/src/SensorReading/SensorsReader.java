package SensorReading;

import Components.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SensorsReader {
    private HashMap<String, Component> allComponents;

    SensorsReader(HashMap<String, Component> allComponents){
        this.allComponents = allComponents;
    }

    public void updateDirectly(String componentName, int[] newValues){
        allComponents.get(componentName).replaceMyValues(newValues);
    }

    /**
     * Lectura directa del BMS, luego reemplaza valores int[] en Component BMS.
     */
    private void readBMS(){
        Random r = new Random();
        int[] up = new int[10]; // Debe ser el orden exacto que hay en el Componente
        for (int val: up
             ) {
            val = r.nextInt(255);
        }
        updateDirectly("BMS", up);
    }

}
