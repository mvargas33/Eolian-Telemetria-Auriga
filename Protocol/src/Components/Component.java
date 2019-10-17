package Components;

import LocalSystems.LocalMasterAdmin;
import Protocol.Messages.Message;
import Protocol.Sending.SenderAdmin;
import SensorReading.DataAdmin;
import Utilities.BitOperations;

import java.util.HashMap;

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

    public String ID; // Component ID, can be the name
    private int[] myValues;     // True values
    private int[] bitSig;     // Bits significativos, MUST match myvalues[] lenght
    private LocalMasterAdmin myAdmin;           // Queue admin que entrega info a DatabaseADmin y ServerAdmin
    private HashMap<Character, MessagesWithIndexes> myMessages; // Los mensajes que contienen información de mi componente
    private DataAdmin myadmin;                  // Quien me actualiza directamente, para luego avisarle a mis mensajes que se actualices, y encvíen.

    private SenderAdmin mySenderAdmin;

    public Component(SenderAdmin mySenderAdmin, int[] valores, int[] bitsSignificativos) {
        this.myValues = valores;
        this.bitSig = bitsSignificativos;
        this.mySenderAdmin = mySenderAdmin; // Thread de Sender Admin debe ser creado antes que todos los componentes.
    }


    /**
     * Direct replacement of values[]. Then updates all the messages of this component
     * @param newValues
     */
    public void replaceMyValues(int[] newValues){
        this.myValues = newValues; // Just replace the values
    }

    /* Test */
    /**
     * Recibe un Id de mensaje porque ese mensaje ha actualizado sus valores. El componente debe tomar el objeto mensaje,
     * buscando en su Map con el id, extraer los valores que le conciernen y hacer update de 'myRawBytes y 'myValues'.
     * Luego debe avisar a 'SenderAdmin' que lo revise para extraer todos sus valores y guardarlos en la base de
     * datos y mostrar en la aplicación Web.
     * @param msgId : Id del mensaje que acaba de actualizarse
     */
    public void updateMsg(char msgId){
        MessagesWithIndexes m = myMessages.get(msgId); // Get MessageWithIndexes
        byte[] bytes = m.message.getBytes();           // Get old bytes from Message
        BitOperations.updateByteArrayFromValues(myValues, bytes, bitSig, m.myBitSig_inicio,  m.raw_inicio, m.raw_fin); // Update Messsage con lo que me corresponde
        m.message.replaceBytes(bytes); // TODO: Ver si esta linea es necesaria | Reemplazo directo de bytes de mensaje
        this.mySenderAdmin.putMessageInQueue(m.message);
    }


    /**
     * Añade un Mensaje e informacion extra a este Componente. Para que luego sepa como actualizarse, sabiendo que
     * bit le corresponden. Se añade como MessageWithHeader en un Map para tener coste O(1).
     * @param m Message
     * @param inicio Bit de inicio en Mensaje
     * @param fin Bit de fin en Mensaje
     * @param myInicio Bit de inicio en componente
     */
    public void addNewMessage(Message m, int inicio, int fin, int myInicio){
        this.myMessages.put(m.getHeader(), new MessagesWithIndexes(m,inicio, fin,myInicio));
    }
}
