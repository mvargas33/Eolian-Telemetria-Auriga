package ApplicationLayer.AppComponents;

import PresentationLayer.Packages.Components.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AppSender extends  AppComponent implements Runnable{
    private final BlockingQueue<double[]> newValuesQueue; // Cola de valores nuevos puestos por el Sensor Reader
    public int[] valoresAEnviar;            // Valores en formato de capa de presentación. (Sólo por optimización de memoria)

    /**
     * SimpleComponent sólo se caracteriza por sus valores mínimos, máximos, y su ID que se usará para muchas cosas.
     * Incluyendo el nombre de eventos en Socket.IO
     *
     * @param id                Nombre del SimpleComponente
     * @param minimosConDecimal Valores mínimos de cada valor del componente
     * @param maximosConDecimal Valores máximos de cada valor del componente
     * @param c                 Componente de capa interior correspondiente
     */
    public AppSender(String id, double[] minimosConDecimal, double[] maximosConDecimal, Component c) {
        super(id, minimosConDecimal, maximosConDecimal, c);
        this.newValuesQueue = new LinkedBlockingQueue<>();
        this.valoresAEnviar = new int[len];
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
        // Ejecutar llamadas de interfaz Componente-Mensaje hasta quedar en Queue de Xbee Sender
        this.myPresentationComponent.replaceMyValues(this.valoresAEnviar);
    }

    /**
     * Método principal de cada App Component.
     * 1 : Lee de su buffer en busca de nuevos valores entregados por su SensorReader
     * 2 : Actualiza los valores localmente
     * 3 : Se coloca en cola del objeto WebSocketService y DatabaseService
     * 4 : Hace las llamadas necesarias a Component-Message para hacer update de los mensajes
     * 5 : Si los mensajes están listos (todos los comp. que participan de él han actualizado una parte) se ponen los
     *     mensajes en cola de Xbee Sender (Objeto Message, no byte[])
     */
    @Override
    public void run() {
        try {
            while(true) {
                while (!newValuesQueue.isEmpty()) {
                    double[] newValues = newValuesQueue.poll(); // 1 : Leer buffer en busca de nuevos valores entregados
                    super.updateValues(newValues);              // 2: Actualizar valores localmente
                    super.informToServices();                   // 3: Mandar a ponerse en cola de servicios
                    this.updatePresentationValuesAndEnQueue();  // 4-5: Update de Messages asociados en capas inferiores/Ponerse en cola de envío
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
