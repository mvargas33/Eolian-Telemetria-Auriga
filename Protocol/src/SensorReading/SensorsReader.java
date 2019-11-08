package SensorReading;

import Components.Component;

import java.util.HashMap;
import java.util.Random;

public class SensorsReader {
    private HashMap<String, Component> allComponents;

    public SensorsReader(HashMap<String, Component> allComponents){
        this.allComponents = allComponents;
    }

    /**
     * Actualiza el array de enteros de un componente por el array de 'newValues'
     * @param componentName : Componente a actualizar
     * @param newValues : Nuevos valores a poner en array de componente
     */
    public void updateDirectly(String componentName, int[] newValues){
        allComponents.get(componentName).replaceMyValues(newValues);
    }

    /**
     * Método para testing
     * Lectura directa del BMS, luego reemplaza valores int[] en Component BMS.
     */
    public void readBMSTest(String bmsName, int sizeOfValues){
        Random r = new Random();
        int[] up = new int[sizeOfValues]; // Debe ser el orden exacto que hay en el Componente
        int[] bitsig = allComponents.get(bmsName).getBitSig();

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
        updateDirectly(bmsName, up);
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
