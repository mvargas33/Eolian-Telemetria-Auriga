package ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages;

import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.StateSender;

import java.util.LinkedList;

public class SentMessage extends Message{
    private LinkedList<StateSender> myStates;


    public SentMessage(char header, int largoEnBytes) {
        super(header, largoEnBytes);
        this.myStates = new LinkedList<>();
    }

    /*-------------------------------------------------- INITIALIZING -------------------------------------------------*/

    /**
     * Añande un stateSender a la lista de stateSenders
     * @param c StateSender!
     */
    public void addState(State c){
        this.myStates.add((StateSender) c);
        this.numOfComponents++;
        this.initialValue = ~ (((int) Math.pow(2,numOfComponents)) - 1); // Valor reset de allComponentsUpdated, al estilo 11111111.....11111100000, con 0's numOfComponents
        this.allComponentsUpdated = this.initialValue;
    }

    /*--------------------------------------------------- SENDING ---------------------------------------------------*/

    /**
     * Marca en allComponentsUpdated con el bit asignado, pasando de 111 ... 000 a 111 ... 010 si el bit asignado fue 1.
     * Cuando todos los componentes hayan invocado este metodo, entonces allComponentsUpdated valdra -1 : 111 ... 111
     * Se usa para que el mensaje sólo este 'listo' cuando todos los componentes invoquen este metodo
     * @param bitAsignado : Bit asignado del componente que invoca este método
     */
    public void marcarActualizacionDeComponente(int bitAsignado){
        int mask = 1 << bitAsignado;
        allComponentsUpdated |= mask; // Se hace OR con el bit señalado, que representa el componente bit-ésimo del mensaje
    }


    /**
     * Retorna true si todos los componentes han actualizado su parte en este mensaje.
     * Si falta un componente por actualizarse, entonces el mensaje no está listo para enviarse
     * Además resetea el valor de allComponentsUpdated al estar en true
     * @return true si todos los componentes actualziaron su bit en allComponentsUpdated, diciendo que estan listos
     */
    public boolean isReadyToSend(){
        if(allComponentsUpdated == -1){ // 111111111111 ... 1111111111111111)
            allComponentsUpdated = initialValue; // Reset
            return true;
        }else{
            return false;
        }
    }
}
