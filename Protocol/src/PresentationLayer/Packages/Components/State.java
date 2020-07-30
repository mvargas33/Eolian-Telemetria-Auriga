package PresentationLayer.Packages.Components;

import PresentationLayer.Packages.Messages.Message;
import ZigBeeLayer.Sending.SenderAdmin;
import Utilities.BitOperations;

import java.util.HashMap;
import java.util.LinkedList;

/*
    Los Componentes virtualizan los componentes del auto. Tienen un arreglo de valores int y un arreglo de bits significativos.
    Además guardan qué mensajes contienen bits (información) relevante para el mismo, para extraerla cuando se actualizan.
    Receiving:  Siempre está atento a heads-ups de Mensajes para actualizarse, luego avisa al LocalMasterAdmin.
    Sending:    Recive información directa del DataAdmin (lecturals locales). Luego avisa a sus mensajes que se actualicen.
                Luego ellos se ponen en contacto con QueueAdmin para ser enviados.
 */
public abstract class State {
    /* Estructura necesaria para guardar correlación mensage-state, guarda que intervalos de bits de un mensaje le conciernen a qué intervalos de bits en este componente */
    public class MessagesWithIndexes {
        Message message;        // RAW bits
        int raw_inicio;         // De donde este componente inicia sus bits en mensaje
        int raw_fin;            // Donde este componente termina sus bits en mensaje
        int myBitSig_inicio;    // Desde que bit en mi array tengo que poner en mensaje
        int componentNumber;    // Para indicar el bit que se asigna en mensaje para marcar el 'ready'

        MessagesWithIndexes(Message m, int raw_inicio, int raw_fin, int myBitSig_inicio, int componentNumber) {
            this.message = m;
            this.raw_inicio = raw_inicio;
            this.raw_fin = raw_fin;
            this.myBitSig_inicio = myBitSig_inicio;
            this.componentNumber = componentNumber;
        }
    }

    String ID;          // State ID, can be the name
    int[] myValues;     // True values
    int[] bitSig;       // Bits significativos, MUST match myvalues[] lenght


    /**
     * Base State, encargado de lecturas directas de sensores y envío de datos por SenderAdmin
     * @param valores : Array de valores del componente
     * @param bitsSignificativos : Array de bits significativos de cada valor en valores[]
     * @param ID : ID del Componente
     */
    public State(String ID, int[] valores, int[] bitsSignificativos){
        this.ID = ID;
        this.myValues = valores;
        this.bitSig = bitsSignificativos;
    }


    /*-------------------------------------------------- INITIALIZING -------------------------------------------------*/
    /**
     * Añade un Mensaje e informacion extra a este Componente. Para que luego sepa como actualizarse, sabiendo que
     * bit le corresponden. Se añade como MessageWithHeader en un Map para tener coste O(1).
     * @param m Message
     * @param raw_inicio Bit de inicio en Mensaje
     * @param raw_fin Bit de fin en Mensaje
     * @param bitSigInicio Bit de inicio en componente
     * @param componentNumber : Numero del componente
     */
    public abstract void addNewMessage(Message m, int raw_inicio, int raw_fin, int bitSigInicio, int componentNumber);


    /*--------------------------------------------------- RESOURCES ---------------------------------------------------*/

    /**
     * Retorna el ID del componente
     * @return : ID del componente
     */
    public String getID(){
        return this.ID;
    }

    /**
     * Retorna int[] de values
     * @return : Array de valores reales
     */
    public int[] getMyValues(){return this.myValues;}

    /**
     * Retorna int[] de bitSig
     * @return : Retorna array de bits significativos
     */
    public int[] getBitSig(){return this.bitSig;}

    /**
     * Retorna el array de valores como String
     * @return : Array de valores como String
     */
    public String valuesToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int valor: this.myValues
             ) {
            sb.append(valor);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Retorna el array de bits significativos como String
     * @return : Array de bits significativos como String
     */
    public String bitSigToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int valor: this.bitSig
        ) {
            sb.append(valor);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Retorna visualización de Componente como String
     * @return : Componente como String
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("State ID       : ");sb.append(this.ID);sb.append("\n");
        sb.append("Valores            : ");sb.append(valuesToString());sb.append("\n");
        sb.append("Bits significativos: ");sb.append(bitSigToString());sb.append("\n");
        return sb.toString();
    }


}
