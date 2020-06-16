package ApplicationLayer.SensorReading;

import PresentationLayer.Packages.Components.Component;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class SensorsReader implements Runnable {
    HashMap<String, Component> allComponents;
    LinkedList<Component> componentLinkedList;

    /**
     * Constructor para Testing
     *
     * @param allComponents : HashMap de componentes
     */
    public SensorsReader(HashMap<String, Component> allComponents) {
        this.allComponents = allComponents;
    }

    /**
     * Constructor para generar datos random
     * @param allComponents : HashMap de Componentes
     * @param componentLinkedList : Linked list de Componentes
     */
    public SensorsReader(HashMap<String, Component> allComponents, LinkedList<Component> componentLinkedList) {
        this.allComponents = allComponents;
        this.componentLinkedList = componentLinkedList;
    }


    /**
     * Actualiza el array de enteros de un componente por el array de 'newValues'
     *
     * @param componentName : Componente a actualizar
     * @param newValues     : Nuevos valores a poner en array de componente
     */
    public void updateDirectly(String componentName, int[] newValues) {
        allComponents.get(componentName).replaceMyValues(newValues);
    }
}