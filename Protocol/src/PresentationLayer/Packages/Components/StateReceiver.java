package PresentationLayer.Packages.Components;

import ApplicationLayer.AppComponents.AppReceiver;
import PresentationLayer.Packages.Messages.Message;
import Utilities.BitOperations;

import java.util.HashMap;

public class StateReceiver extends State{
    private HashMap<Character, MessagesWithIndexes> hashOfMyMessagesWithIndexes;             // RECEIVING : Para extracción en O(1) y actualizar mis valores
    private AppReceiver myAppReceiver;

    /**
     * Base State, encargado de lecturas directas de sensores y envío de datos por SenderAdmin
     *
     * @param valores            : Array de valores del componente
     * @param bitsSignificativos : Array de bits significativos de cada valor en valores[]
     * @param ID                 : ID del Componente
     */
    public StateReceiver(String ID, int[] valores, int[] bitsSignificativos) {
        super(ID, valores, bitsSignificativos);
        this.hashOfMyMessagesWithIndexes = new HashMap<>();
    }

    /*-------------------------------------------------- INITIALIZING --------------------------------------------------*/

    /**
     * Añade un Mensaje e informacion extra a este Componente. Para que luego sepa como actualizarse, sabiendo que
     * bit le corresponden. Se añade como MessageWithHeader en un Map para tener coste O(1).
     * @param m Message: MUST BE ReceivedMessage CLASS
     * @param raw_inicio Bit de inicio en Mensaje
     * @param raw_fin Bit de fin en Mensaje
     * @param bitSigInicio Bit de inicio en componente
     * @param componentNumber : Numero del componente
     */
    public void addNewMessage(Message m, int raw_inicio, int raw_fin, int bitSigInicio, int componentNumber){
        this.hashOfMyMessagesWithIndexes.put(m.getHeader(), new MessagesWithIndexes(m,raw_inicio, raw_fin,bitSigInicio, componentNumber));
    }

    public void setAppReceiver(AppReceiver appReceiver){
        this.myAppReceiver = appReceiver;
    }

    public AppReceiver getAppReceiver(){
        return this.myAppReceiver;
    }

    /*--------------------------------------------------- RECEIVING ---------------------------------------------------*/

    /**
     * Toma un mensaje, luego busca en su diccionario los bytes de ese mensaje que le corresponden. Luego hace update de
     * su array de valores según los valores encontrados. Finalmente el AppComponente preguntará por su array de int[] values.
     * @param messageID : ID del Mensaje recibido
     */
    public void updateMyValues(Character messageID){
        MessagesWithIndexes m = this.hashOfMyMessagesWithIndexes.get(messageID); // Obtengo mensaje correspondiente con indices
        byte[] bytes = m.message.getBytes();           // Get new bytes from Message
        BitOperations.updateValuesFromByteArray(myValues, bytes, bitSig, m.myBitSig_inicio, m.raw_inicio, m.raw_fin); // Update de values[] míos según el mensaje que acabo de leer
    }

    /*--------------------------------------------------- RESOURCES ---------------------------------------------------*/
}
