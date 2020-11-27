package ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components;

import ApplicationLayer.AppComponents.AppReceiver;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.Message;
import ApplicationLayer.LocalServices.WirelessService.Utilities.BitOperations;

import java.util.HashMap;

public class StateReceiver extends State{
    private HashMap<Character, MessagesWithIndexes> hashOfMyMessagesWithIndexes;             // RECEIVING : Para extracción en O(1) y actualizar mis valores
    private AppReceiver myAppReceiver;

    private double[] valoresRealesActuales; // Para optimizar memoria

    /**
     * Base State, encargado de lecturas directas de sensores y envío de datos por SenderAdmin
     *
     * @param ID                 : ID del Componente
     */
    public StateReceiver(String ID, double[] minimosConDecimal, double[] maximosConDecimal, AppReceiver myAppReceiver) {
        super(ID, minimosConDecimal, maximosConDecimal);
        this.valoresRealesActuales = new double[minimosConDecimal.length];
        this.myAppReceiver = myAppReceiver;
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

    /*--------------------------------------------------- RECEIVING ---------------------------------------------------*/
    /**
     * RECEIVING DATA
     * Método que ejecuta un Componente de capa de Presentación, para avisarle al Componente de Aplicación que llegaron nuevos valores.
     * Traduce los valores de la capa inferior a valores reales en double.
     * @throws Exception Si el largo del array int[] no coincide con el largo de precongifuraciones
     */
    public void updateFromReceiving() throws Exception{
        if(myValues.length != len){
            throw new Exception("updateFromReceiving: Array de valores nuevo no tiene el mismo largo que preconfiguraciones");
        }

        // Update directo, sin reemplazo de objetos
        for (int i = 0; i < len; i++) {
            this.valoresRealesActuales[i] = (myValues[i] - offset[i]) * Math.pow(10, -decimales[i]);
        }
    }

    /**
     * Toma un mensaje, luego busca en su diccionario los bytes de ese mensaje que le corresponden. Luego hace update de
     * su array de valores según los valores encontrados. Finalmente el AppComponente preguntará por su array de int[] values.
     * @param messageID : ID del Mensaje recibido
     */
    public void updateMyValues(Character messageID) throws Exception{
        MessagesWithIndexes m = this.hashOfMyMessagesWithIndexes.get(messageID); // Obtengo mensaje correspondiente con indices
        byte[] bytes = m.message.getBytes();           // Get new bytes from Message
        BitOperations.updateValuesFromByteArray(myValues, bytes, bitSignificativos, m.myBitSig_inicio, m.raw_inicio, m.raw_fin); // Update de values[] míos según el mensaje que acabo de leer

        // Inform new values to AppReceiver and follow the sequential run
        this.updateFromReceiving();                     // Calculate int[] -> double[] real values
        this.myAppReceiver.sequentialRun(this.valoresRealesActuales);
    }

    /*--------------------------------------------------- RESOURCES ---------------------------------------------------*/
}
