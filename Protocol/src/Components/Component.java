package Components;

import LocalSystems.LocalMasterAdmin;
import Protocol.Messages.Message;
import Protocol.Sending.SenderAdmin;
import SensorReading.DataAdmin;
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
public abstract class Component {
    /* Estructura necesaria para guardar correlación mensage-componente, guarda que intervalos de bits de un mensaje le conciernen a qué intervalos de bits en este componente */
    public class MessagesWithIndexes {
        Message message; // RAW bits
        int raw_inicio; // De donde este componente inicia sus bits en mensaje
        int raw_fin; // Donde este componente termina sus bits en mensaje
        int myBitSig_inicio; // Desde que bit en mi array tengo que poner en mensaje

        private MessagesWithIndexes(Message m, int raw_inicio, int raw_fin, int myBitSig_inicio) {
            this.message = m;
            this.raw_inicio = raw_inicio;
            this.raw_fin = raw_fin;
            this.myBitSig_inicio = myBitSig_inicio;
        }
    }

    private String ID; // Component ID, can be the name
    private int[] myValues;     // True values
    private int[] bitSig;     // Bits significativos, MUST match myvalues[] lenght

    private LinkedList<MessagesWithIndexes> listOfMyMessagesWithIndexes;    // SENDING : Para uso en for() y actualizar mensajes que me corresponden
    private SenderAdmin mySenderAdmin;                                      // SENDING : A quien informo de updates en valores para enviar

    private HashMap<Character, MessagesWithIndexes> hashOfMyMessagesWithIndexes;             // RECEIVING : Para extracción en O(1) y actualizar mis valores
    private LocalMasterAdmin myLocalMasterAdmin;                            // RECEIVING : Queue admin que entrega info a DatabaseADmin y ServerAdmin


    /**
     * Sender Component, encargado de lecturas directas de sensores y envío de datos por SenderAdmin
     * @param mySenderAdmin : A quien informa sobre nuevos valores leídos
     * @param valores : Array de valores del componente
     * @param bitsSignificativos : Array de bits significativos de cada valor en valores[]
     */
    public Component(SenderAdmin mySenderAdmin, int[] valores, int[] bitsSignificativos, String ID) {
        this.ID = ID;
        this.myValues = valores;
        this.bitSig = bitsSignificativos;
        this.mySenderAdmin = mySenderAdmin; // Thread de Sender Admin debe ser creado antes que todos los componentes.
        this.listOfMyMessagesWithIndexes = new LinkedList<>();
        this.hashOfMyMessagesWithIndexes = new HashMap<>();
    }

    /**
     * Receiver Component, encargado de transformar Messages recibidos en valores e informar al LocalMasterAdmin
     * @param myLocalMasterAdmin : A quien informo sobre nuevos valores recibidos
     * @param valores : Array de valores del componente
     * @param bitsSignificativos : Array de bits significativos de cada valor en valores[]
     */
    public Component(LocalMasterAdmin myLocalMasterAdmin, int[] valores, int[] bitsSignificativos, String ID) {
        this.ID = ID;
        this.myValues = valores;
        this.bitSig = bitsSignificativos;
        this.myLocalMasterAdmin = myLocalMasterAdmin; // Thread de myLocalMasterAdmin debe ser creado antes que todos los componentes.
        this.listOfMyMessagesWithIndexes = new LinkedList<>();
        this.hashOfMyMessagesWithIndexes = new HashMap<>();
    }


    /*-------------------------------------------------- INITIALIZING -------------------------------------------------*/
    /**
     * Añade un Mensaje e informacion extra a este Componente. Para que luego sepa como actualizarse, sabiendo que
     * bit le corresponden. Se añade como MessageWithHeader en un Map para tener coste O(1).
     * @param m Message
     * @param raw_inicio Bit de inicio en Mensaje
     * @param raw_fin Bit de fin en Mensaje
     * @param bitSigInicio Bit de inicio en componente
     */
    public void addNewMessage(Message m, int raw_inicio, int raw_fin, int bitSigInicio){
        this.listOfMyMessagesWithIndexes.add(new MessagesWithIndexes(m,raw_inicio, raw_fin,bitSigInicio));
        this.hashOfMyMessagesWithIndexes.put(m.getHeader(), new MessagesWithIndexes(m,raw_inicio, raw_fin,bitSigInicio));
    }

    /*---------------------------------------------------- SENDING ----------------------------------------------------*/
    /**
     * READING: Reemplazo directo de array values[] del componente.
     * Luego actualiza todos los mensajes que corresponden a este componente, para luego ponerse en cola de envío.
     * @param newValues : Array a reemplzar en Componente
     */
    public void replaceMyValues(int[] newValues){
        this.myValues = newValues; // Reemplaza valores directamente
        // TODO: Verificar que el valor leído está dentro del intervalo de bits significativos
        for (MessagesWithIndexes mi : this.listOfMyMessagesWithIndexes
             ) {
            this.updateMsg(mi); // Actualizar valores en mensaje y ponerlo en cola de envío
        }
    }

    /**
     * SENDING: Recibe mensaje porque el componente ha actualizado sus valores. El componente debe tomar el objeto mensaje,
     * extraer los valores que le conciernen y hacer update de 'myRawBytes y 'myValues'.
     * Luego debe avisar a 'SenderAdmin' que lo revise para extraer todos sus valores y guardarlos en la base de
     * datos y mostrar en la aplicación Web.
     * @param m : Mensaje que acaba de actualizarse
     */
    public void updateMsg( MessagesWithIndexes m ){
        byte[] bytes = m.message.getBytes();           // Get old bytes from Message
        BitOperations.updateByteArrayFromValues(myValues, bytes, bitSig, m.myBitSig_inicio,  m.raw_inicio, m.raw_fin); // Update Messsage con lo que me corresponde
        //m.message.updateRawBytes(bytes); // TODO: Ver si esta linea es necesaria | Reemplazo directo de bytes de mensaje
        //m.message.bytes = bytes; // Update myself
        this.mySenderAdmin.putMessageInQueue(m.message); // Poner en Queue de Sender Admin
    }

    /*--------------------------------------------------- RECEIVING ---------------------------------------------------*/

    /**
     * Toma un mensaje, luego busca en su diccionario los bytes de ese mensaje que le corresponden. Luego hace update de
     * su array de valores según los valores encontrados. Finalmente se pone como Componente en la Queue de LocalMasterAdmin,
     * para que éste actualice en la base de datos y visualización los valores de este componente.
     * @param messageID : ID del Mensaje recibido
     */
    public void updateMyValues(Character messageID){
        MessagesWithIndexes m = this.hashOfMyMessagesWithIndexes.get(messageID); // Obtengo mensaje correspondiente con indices
        byte[] bytes = m.message.getBytes();           // Get new bytes from Message
        BitOperations.updateValuesFromByteArray(myValues, bytes, bitSig, m.myBitSig_inicio, m.raw_inicio, m.raw_fin); // Update de values[] míos según el mensaje que acabo de leer
        this.myLocalMasterAdmin.putComponentInQueue(this); // Poner en la Queue del LocalMasterAdmin para que procese el componente
    }

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
        sb.append("Component ID       : ");sb.append(this.ID);sb.append("\n");
        sb.append("Valores            : ");sb.append(valuesToString());sb.append("\n");
        sb.append("Bits significativos: ");sb.append(bitSigToString());sb.append("\n");
        return sb.toString();
    }

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
