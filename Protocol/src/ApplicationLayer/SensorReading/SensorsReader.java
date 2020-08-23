package ApplicationLayer.SensorReading;

import ApplicationLayer.AppComponents.AppSender;

public abstract class SensorsReader implements Runnable {
    public AppSender myComponent;  // Componente al cual le encolará valores nuevos
    private long currentTime;               // Tiempos para verificar delay
    private long lastTime;
    private long delayTime;
    private double[] values;                // Por optimización de memoria

    /**
     * Constructor base. Todos los SensorReaders están linkeados a un sólo AppSender. No funcionan con receivers
     * @param myComponent AppComponent linkeado
     * @param readingDelayInMS Frecuencia de muestre
     */
    public SensorsReader(AppSender myComponent, long readingDelayInMS) {
        this.myComponent = myComponent;
        this.lastTime = System.currentTimeMillis();
        this.delayTime = readingDelayInMS;
    }

    /**
     * Métodos que deben implementar todos los tipos de lectores.
     * @return array de valores double[] con los nuevos valores del componente
     */
    public abstract double[] read();

    /**
     * Para control de delay
     * @param delayTimeMS
     */
    public synchronized void setDelayTime(long delayTimeMS){
        this.delayTime = delayTimeMS;
    }

    public synchronized AppSender getMyComponent(){
        return this.myComponent;
    }

    /**
     * 0: Verifica que haya pasado tiempo suficiente para volver a leer.
     * 1: Lee nuevos valores.
     * 2: Los encola en el AppSender correspondiente.
     * 3: Actualiza tiempos de lectura
     */
    @Override
    public void run() {
        while (true){
            try{
                currentTime = System.currentTimeMillis();
                if(currentTime - lastTime >= this.delayTime){   // 0: Si ya pasó el tiempo de delay y me toca leer
                    this.values = this.read();                  // 1: Leer nuevos valores
                    //myComponent.enqueueNewValues(values);       // 2: Encola nuevos valores
                    myComponent.sequentialRun(values);          // Ejecuta secuancialmente todas las acciones hasta dejar los valores byte[] en la cola del Xbee
                    lastTime = currentTime;                     // 3: Actualiza tiempos de lectura
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Same as run(), without while() statement. Used by Sensor<Type>Admin
     */
    public void sequentialRun() {
        try{
            currentTime = System.currentTimeMillis();
            if(currentTime - lastTime >= this.delayTime){   // 0: Si ya pasó el tiempo de delay y me toca leer
                this.values = this.read();                  // 1: Leer nuevos valores
                myComponent.sequentialRun(values);          // Ejecuta secuancialmente todas las acciones hasta dejar los valores byte[] en la cola del Xbee
                lastTime = currentTime;                     // 3: Actualiza tiempos de lectura
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}