package SensorReading;

import Components.Component;
import SensorReading.SensorsReader;
import sun.management.Sensor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Clase que se encarga de generar datos al azar para simular componentes
 */
public class RandomReader extends SensorsReader implements Runnable{

    public RandomReader(HashMap<String, Component> allComponents, LinkedList<Component> componentLinkedList) {
        super(allComponents, componentLinkedList);
    }

    /**
     * En cada iteración actualiza componente a componente con valores random
     */
    @Override
    public void run() {
        while(true){
            try {
                for (Component c : componentLinkedList
                ) {
                    randomData(c.getID());
                    Thread.sleep(80); // Límite entre 70-75 ms antes que Queue de XbeeSender cresca sin fin
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Lectura directa del BMS, luego reemplaza valores int[] en Component BMS.
     */
    public void randomData(String componentName){

        Random r = new Random();
        int[] up = new int[allComponents.get(componentName).getMyValues().length]; // Debe ser el orden exacto que hay en el Componente
        int[] bitsig = allComponents.get(componentName).getBitSig();

        do{ // Generar hasta que todos los valores sen representables
            for (int i = 0; i < up.length; i++) {
                up[i] = Math.abs(r.nextInt((int) Math.pow(2, bitsig[i]))); // Generar con límites de BitSig
            }
        } while(!readingsAreCorrect(up, bitsig));

        System.out.print("Array random generado : [ ");
        for (int i = 0; i < up.length; i++){
            System.out.print(up[i] + " ");
        }
        System.out.println("]");
        updateDirectly(componentName, up);
    }

    /**
     * Retorna true, si los valores pueden ser representados por la cantidad de bits indicada
     * @param values : array de valores
     * @param bitSig : array de bits significativos para cada valor
     * @return : true si todos los valores pueden ser representados
     */
    public boolean readingsAreCorrect(int[] values, int[] bitSig){
        int val;
        int bits;
        boolean correct = true;
        for(int i = 0; i < values.length; i++){
            val = values[i];
            bits = bitSig[i];
            if(!(val < Math.pow(2, bits))){ // El valor no puede ser representado por la cantidad de bits que se asignaron
                correct = false;
            }
        }
        return correct;
    }
}
