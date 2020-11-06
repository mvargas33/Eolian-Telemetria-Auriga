package ApplicationLayer.AppComponents;

import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.StateReceiver;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.Message;

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
                    //this.updateFromReceiving(myPresentationState.getMyValues());    // 3 : Update double[] de valores reales
                    super.informToServices();                                       // 4 : Informar a suscripciones
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Mismo que run() pero en forma secuencial
     */
    public void sequentialRun(double[] newRealValues){
        try {
            this.valoresRealesActuales = newRealValues;
            super.sequentialInformToServices();                             // 4 : Informar a suscripciones uno a uno
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
