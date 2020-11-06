package ApplicationLayer.AppComponents;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AppSender extends  AppComponent implements Runnable{
    private final BlockingQueue<double[]> newValuesQueue; // Cola de valores nuevos puestos por el Sensor Reader

    /**
     * SimpleComponent sólo se caracteriza por sus valores mínimos, máximos, y su ID que se usará para muchas cosas.
     * Incluyendo el nombre de eventos en Socket.IO
     *
     * @param id                Nombre del SimpleComponente
     * @param minimosConDecimal Valores mínimos de cada valor del componente
     * @param maximosConDecimal Valores máximos de cada valor del componente
     */
    public AppSender(String id, double[] minimosConDecimal, double[] maximosConDecimal, String[] nombreParametros) {
        super(id, minimosConDecimal, maximosConDecimal, nombreParametros);
        this.newValuesQueue = new LinkedBlockingQueue<>();
    }

    /**
     * Método que llaman los sensorsReaders para encolar nuevos valores del componente
     * @param values Nuevo arreglo de valores para el componente
     * @throws Exception Errores de put()
     */
    public void enqueueNewValues(double[] values){
        try {
            this.newValuesQueue.put(values);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Método principal de cada App State.
     * 1 : Lee de su buffer en busca de nuevos valores entregados por su SensorReader
     * 2 : Actualiza los valores localmente
     * 3 : Se coloca en cola del objeto WebSocketService y DatabaseService
     * 4 : Hace las llamadas necesarias a State-Message para hacer update de los mensajes
     * 5 : Si los mensajes están listos (todos los comp. que participan de él han actualizado una parte) se ponen los
     *     mensajes en cola de Xbee Sender (Objeto Message, no byte[])
     */
    @Override
    public void run() {
        while(true) {
            try {
                while (!newValuesQueue.isEmpty()) {
                    //System.out.println("Queue nuevos valores para " + this.ID + ": " + this.newValuesQueue.size());
                    double[] newValues = newValuesQueue.poll(); // 1 : Leer buffer en busca de nuevos valores entregados
                    super.updateValues(newValues);              // 2: Actualizar valores localmente
                    super.informToServices();                   // 3: Mandar a ponerse en cola de servicios
                    //this.updatePresentationValuesAndEnQueue();  // 4-5: Update de Messages asociados en capas inferiores/Ponerse en cola de envío
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método que ejecuta SensorReader para seguir secuencialmente lo que haría AppSender en run() en forma paralela
     * @param newValues valores nuevos desde RandomReader
     */
    public void sequentialRun(double[] newValues){
        try {
            super.updateValues(newValues);              // 2: Actualizar valores localmente
            super.sequentialInformToServices();         // 3: Pasar por todos los servicios ejecutando los serve(this) en cada caso
            //this.updatePresentationValuesAndEnQueue();  // 4-5: Update de Messages asociados en capas inferiores/Ponerse en cola de envío
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
