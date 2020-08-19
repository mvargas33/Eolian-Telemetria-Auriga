package ApplicationLayer.AppComponents;

import PresentationLayer.Packages.Components.State;
import PresentationLayer.Packages.Components.StateReceiver;
import PresentationLayer.Packages.Messages.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class AppReceiver extends AppComponent implements Runnable{
    private final BlockingQueue<Message> messageQueue; // Cola de mensajes arrivados. XbeeReceiver los pone acá
    StateReceiver myPresentationState;              // Estado correspondiente de capa inferior

    /**
     * SimpleComponent sólo se caracteriza por sus valores mínimos, máximos, y su ID que se usará para muchas cosas.
     * Incluyendo el nombre de eventos en Socket.IO
     *
     * @param id                Nombre del SimpleComponente
     * @param minimosConDecimal Valores mínimos de cada valor del componente
     * @param maximosConDecimal Valores máximos de cada valor del componente
     */
    public AppReceiver(String id, double[] minimosConDecimal, double[] maximosConDecimal, String[] nombreParametros) {
        super(id, minimosConDecimal, maximosConDecimal, nombreParametros);
        this.messageQueue = new LinkedBlockingDeque<>();

        // Crea estado de capa inferior, con los datos deducidos de esta capa. Lo más importante son los bits significativos.
        this.myPresentationState = new StateReceiver(this.ID, new int[this.len], this.bitSignificativos);
        this.myPresentationState.setAppReceiver(this);
    }

    /**
     * Retorna el StateReceiver. Se usa para inicializar el state con mensajes
     * @return StateReceiver
     */
    public State getState(){
        return this.myPresentationState;
    }


    /**
     * Método que llaman los ReceiverAdmin para encolar nuevos mensajes del componente
     * @param message Nuevo arreglo de valores para el componente
     * @throws Exception Errores de put()
     */
    public void enqueueNewMessages(Message message){
        try {
            this.messageQueue.put(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * RECEIVING DATA
     * Método que ejecuta un Componente de capa de Presentación, para avisarle al Componente de Aplicación que llegaron nuevos valores.
     * Traduce los valores de la capa inferior a valores reales en double.
     * @param valoresDeCapaPresentacion Array de int[] proveniente de capa de Presentación
     * @throws Exception Si el largo del array int[] no coincide con el largo de precongifuraciones
     */
    public void updateFromReceiving(int[] valoresDeCapaPresentacion) throws Exception{
        if(valoresDeCapaPresentacion.length != len){
            throw new Exception("updateFromReceiving: Array de valores nuevo no tiene el mismo largo que preconfiguraciones");
        }

        // Update directo, sin reemplazo de objetos
        for (int i = 0; i < len; i++) {
            this.valoresRealesActuales[i] = (valoresDeCapaPresentacion[i] - offset[i]) * Math.pow(10, -decimales[i]);
        }
    }

    /**
     * Método principal de cada App State.
     * 1 : Lee de su buffer en busca de nuevos mensajes arrivados (Objetos Messages, no byte[])
     * 2 : Usa la interfaz State-Message para actualizar los valores locales que le conciernen del mensaje, en componente presentación
     * 3 : Update de double[] locales
     * 4 : Se coloca en cola del objeto WebSocketService y DatabaseService
     */
    @Override
    public void run() {
        try{
            while(true){
                while(!messageQueue.isEmpty()){
                    Message m = messageQueue.poll();                                // 1 : Leer buffer en busca de nuevos valores entregados
                    this.myPresentationState.updateMyValues(m.getHeader());         // 2 : Update int[] de Componente presentación
                    this.updateFromReceiving(myPresentationState.getMyValues());    // 3 : Update double[] de valores reales
                    super.informToServices();                                       // 4 : Informar a suscripciones
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
