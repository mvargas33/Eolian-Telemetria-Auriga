package ApplicationLayer.AppComponents;

import PresentationLayer.Packages.Components.State;
import PresentationLayer.Packages.Components.StateReceiver;
import PresentationLayer.Packages.Components.StateSender;
import ZigBeeLayer.Sending.SenderAdmin;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AppSender extends  AppComponent implements Runnable{
    private final BlockingQueue<double[]> newValuesQueue; // Cola de valores nuevos puestos por el Sensor Reader
    public int[] valoresAEnviar;            // Valores en formato de capa de presentación. (Sólo por optimización de memoria)
    StateSender myPresentationState;              // Estado correspondiente de capa inferior

    /**
     * SimpleComponent sólo se caracteriza por sus valores mínimos, máximos, y su ID que se usará para muchas cosas.
     * Incluyendo el nombre de eventos en Socket.IO
     *
     * @param id                Nombre del SimpleComponente
     * @param minimosConDecimal Valores mínimos de cada valor del componente
     * @param maximosConDecimal Valores máximos de cada valor del componente
     */
    public AppSender(String id, double[] minimosConDecimal, double[] maximosConDecimal) {
        super(id, minimosConDecimal, maximosConDecimal);
        this.newValuesQueue = new LinkedBlockingQueue<>();
        this.valoresAEnviar = new int[len];

        // Crea estado de capa inferior, con los datos deducidos de esta capa.
        this.myPresentationState = new StateSender(id, this.valoresAEnviar, this.bitSignificativos);
    }

    /**
     * SenderAdmin se setea a posteriori. Para poder crear el AppSender por separado
     * @param mySenderAdmin : A quien se informa sobre nuevos valores y se pone ne cola
     */
    public void setSenderAdmin(SenderAdmin mySenderAdmin){
        this.myPresentationState.setMySenderAdmin(mySenderAdmin);
    }

    public State getState(){
        return this.myPresentationState;
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
     * SENDING DATA
     * Calcula valores para capa de Presentación. "Compresión".
     * Se usa variable global para optimizar uso de memoria.
     * Pasa los valores actuales de capa de presentación a capa de presentación.
     */
    public void updatePresentationValuesAndEnQueue(){
        // Update de valores int[]
        for (int i = 0; i < len; i++) {
            this.valoresAEnviar[i] = (int) Math.floor( valoresRealesActuales[i] * Math.pow(10, decimales[i]) ) + offset[i];
        }
        // Ejecutar llamadas de interfaz State-Mensaje hasta quedar en Queue de Xbee Sender
        this.myPresentationState.replaceMyValues(this.valoresAEnviar);
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
                    System.out.println("Queue nuevos valores para " + this.ID + ": " + this.newValuesQueue.size());
                    double[] newValues = newValuesQueue.poll(); // 1 : Leer buffer en busca de nuevos valores entregados
                    super.updateValues(newValues);              // 2: Actualizar valores localmente
                    super.informToServices();                   // 3: Mandar a ponerse en cola de servicios
                    this.updatePresentationValuesAndEnQueue();  // 4-5: Update de Messages asociados en capas inferiores/Ponerse en cola de envío
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
