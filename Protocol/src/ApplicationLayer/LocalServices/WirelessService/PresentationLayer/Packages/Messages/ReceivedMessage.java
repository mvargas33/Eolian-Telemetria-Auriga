package ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages;

import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.StateReceiver;

import java.util.LinkedList;

public class ReceivedMessage extends Message{
    private LinkedList<StateReceiver> myStates;

    public ReceivedMessage(char header, int largoEnBytes) {
        super(header, largoEnBytes);
        this.myStates = new LinkedList<>();
    }

    /*-------------------------------------------------- INITIALIZING -------------------------------------------------*/

    /**
     * Añande un StateRecevier a la lista de StateReceivers
     * @param c StateReceiver!
     */
    public void addState(State c){
        this.myStates.add((StateReceiver) c);
        this.numOfComponents++;
        this.initialValue = ~ (((int) Math.pow(2,numOfComponents)) - 1); // Valor reset de allComponentsUpdated, al estilo 11111111.....11111100000, con 0's numOfComponents
        this.allComponentsUpdated = this.initialValue;
    }

    /*--------------------------------------------------- RECEIVING ---------------------------------------------------*/

    /**
     * Actualiza los bytes del Mensaje por los actualizados. Luego llama a uno a uno todos los componentes que le corresponden
     * para que miren el mensaje y estos extraigan los bits que necesiten.
     * @param newBytes : Valores actualizados de los bytes del mensaje
     */
    public void updateRawBytes(byte[] newBytes) throws Exception{
        this.bytes = newBytes; // Update myself
        // Notify my components to check at my values
        for (StateReceiver s: this.myStates
        ) {
            //s.getAppReceiver().enqueueNewMessages(this); // Se pone en cola de cada AppComponent para que haga llamadas a sus States que lo actualicen
            s.updateMyValues(this.header); // Update de values de los States a los cuales pertenesco
            //s.getAppReceiver().sequentialRun(this); // Llamada secuencial. Luego subo a actualizar valores de presentación
        }
    }
}
