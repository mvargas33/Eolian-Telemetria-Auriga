package ApplicationLayer.SensorReading.RandomReaders;

import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.SensorReading.SensorsReader;
import PresentationLayer.Packages.Components.State;
//import ApplicationLayer.SensorReading.SensorsReader;
//import sun.management.Sensor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Clase que se encarga de generar datos al azar para simular componentes
 */
public class RandomReader extends SensorsReader implements Runnable{
    private final Random r;

    /**
     * Constructor base. Todos los SensorReaders están linkeados a un sólo AppSender. No funcionan con receivers
     *
     * @param myComponent      AppComponent linkeado
     * @param readingDelayInMS Frecuencia de muestre
     */
    public RandomReader(AppSender myComponent, long readingDelayInMS) {
        super(myComponent, readingDelayInMS);
        this.r = new Random();
    }

    @Override
    public double[] read() {
        double[] max = this.myComponent.maximosConDecimal;
        double[] min = this.myComponent.minimosConDecimal;
        double[] random = new double[max.length];
        for (int i = 0; i < random.length; i++) {
            random[i] = min[i] + (max[i] - min[i]) * this.r.nextDouble(); // Generar valor random en el rango adecuado
        }
        return random;
    }



}
