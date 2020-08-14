package PresentationLayer.Packages.Components;

import PresentationLayer.Packages.Messages.Message;
import PresentationLayer.Packages.Messages.SentMessage;
import Utilities.BitOperations;
import ZigBeeLayer.Sending.SenderAdmin;

import java.util.LinkedList;

public class StateSender extends State{
    private SenderAdmin mySenderAdmin;
    private LinkedList<MessagesWithIndexes> listOfMyMessagesWithIndexes;    // SENDING : Para uso en for() y actualizar mensajes que me corresponden

    /**
     * Sender State, encargado de lecturas directas de sensores y envío de datos por SenderAdmin
     * @param valores : Array de valores del componente
     * @param bitsSignificativos : Array de bits significativos de cada valor en valores[]
     * @param ID : ID del Componente
     */
    public StateSender(String ID, int[] valores, int[] bitsSignificativos) {
        super(ID, valores, bitsSignificativos);
        this.listOfMyMessagesWithIndexes = new LinkedList<>();
    }

    /**
     * El senderAdmin se setea a posteriori
     * @param mySenderAdmin: A quien informa sobre nuevos valores leídos
     */
    public void setMySenderAdmin(SenderAdmin mySenderAdmin){
        this.mySenderAdmin = mySenderAdmin; // Thread de Sender Admin debe ser creado antes que todos los componentes.
    }

    /*-------------------------------------------------- INITIALIZING --------------------------------------------------*/

    /**
     * Añade un Mensaje e informacion extra a este Componente. Para que luego sepa como actualizarse, sabiendo que
     * bit le corresponden. Se añade como MessageWithHeader en un Map para tener coste O(1).
     * @param m Message: MUST BE SentMessage CLASS
     * @param raw_inicio Bit de inicio en Mensaje
     * @param raw_fin Bit de fin en Mensaje
     * @param bitSigInicio Bit de inicio en componente
     * @param componentNumber : Numero del componente
     */
    public void addNewMessage(Message m, int raw_inicio, int raw_fin, int bitSigInicio, int componentNumber){
        this.listOfMyMessagesWithIndexes.add(new MessagesWithIndexes(m,raw_inicio, raw_fin,bitSigInicio, componentNumber));
    }

    /*---------------------------------------------------- SENDING ----------------------------------------------------*/

    /**
     * UPDATE: Reemplazo directo de array values[] del componente.
     * Luego actualiza todos los mensajes que corresponden a este componente, para luego ponerse en cola de envío.
     * @param newValues : Array a reemplzar en Componente
     */
    public void replaceMyValues(int[] newValues){
        this.myValues = newValues; // Reemplaza valores directamente

        for (MessagesWithIndexes mi : this.listOfMyMessagesWithIndexes
        ) {
            this.updateMsg(mi); // Actualizar valores en mensaje y ponerlo en cola de envío
        }
    }

    /**
     * SENDING: Recibe mensaje porque el componente ha actualizado sus valores. El componente debe tomar el objeto mensaje,
     * extraer los valores que le conciernen y hacer update de 'myRawBytes y 'myValues'.
     * Luego debe avisar a 'SenderAdmin' que lo revise para extraer todos sus valores y guardarlos en la base de
     * datos y mostrar en la aplicación Web. Mensaje se pone en Queue de envío.
     * @param m : Mensaje que acaba de actualizarse
     */
    public void updateMsg( MessagesWithIndexes m ){
        byte[] bytes = m.message.getBytes();           // Bytes antiguos de Message
        // Resetear a 0 los valores, porque si continuamos con antiguos, hacen conflicto con 'updateByteArrayFromValues'
        // al usar OR como operando intermedio, i.e. los valores anterior preservan y hacen OR con nuevos, causando update erróneo
        // Header se preserva
        BitOperations.resetToZeroBitRange(bytes, m.raw_inicio, m.raw_fin);
        BitOperations.updateByteArrayFromValues(myValues, bytes, bitSig, m.myBitSig_inicio,  m.raw_inicio, m.raw_fin); // Update Messsage con lo que me corresponde
        //m.message.updateRawBytes(bytes); // TODO: Ver si esta linea es necesaria | Reemplazo directo de bytes de mensaje
        //m.message.bytes = bytes; // Update myself
        SentMessage mm = (SentMessage) m.message;
        mm.marcarActualizacionDeComponente(m.componentNumber); // Marcar que este componente esta rdy en mensaje
        if(mm.isReadyToSend()){ // Si yo fui el último que faltaba para enviar el mensaje, calculo CRC8 y lo pongo en la queue
            //byte crc = BitOperations.calcCRC8(m.message.getBytes(), m.message.getLargoEnBytes() - 2); // Se calcula hasta antes del ultimo byte
            //m.message.getBytes()[m.message.getLargoEnBytes() - 1] = crc; // Update del CRC
            this.mySenderAdmin.putMessageInQueue(m.message); // Poner en Queue de Sender Admin
        }
    }
    /*--------------------------------------------------- RESOURCES ---------------------------------------------------*/

    /**
     * Retorna la representación en String de los mensajes que tiene este componente
     * @return : String de mensajes con indices del componente actual
     */
    public String printMessagesWithIndexes(){
        StringBuilder sb = new StringBuilder();
        Message m;
        for (MessagesWithIndexes mi: listOfMyMessagesWithIndexes
        ) {
            m = mi.message;
            sb.append("Message: ");sb.append(m.toString());
            sb.append("BitSig_inicio: ");sb.append(mi.myBitSig_inicio);
            sb.append(" | Raw_inicio: ");sb.append(mi.raw_inicio);
            sb.append(" | Raw_fin:    ");sb.append(mi.raw_fin);sb.append("\n");
        }
        return sb.toString();
    }
}
