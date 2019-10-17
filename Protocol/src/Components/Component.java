package Components;

import LocalSystems.LocalMasterAdmin;
import Protocol.Messages.Message;
import SensorReading.DataAdmin;

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
    private int[] bitRange;     // Bits significativos, MUST match myvalues[] lenght
    private LocalMasterAdmin myAdmin;           // Queue admin que entrega info a DatabaseADmin y ServerAdmin
    private HashMap<Character, MessagesWithIndexes> myMessages; // Los mensajes que contienen información de mi componente
    private DataAdmin myadmin;                  // Quien me actualiza directamente, para luego avisarle a mis mensajes que se actualices, y encvíen.

    public Component(int[] valores, int[] bitsSignificativos) {
        this.myValues = valores;
        this.bitRange = bitsSignificativos;
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
     * buscando en su Map con el id, xtraer los valores que le conciernen y hacer update de 'myRawBytes y 'myValues'.
     * Luego debe avisar a 'LocalMasterAdmin' que lo revise para extraer todos sus valores y guardarlos en la base de
     * datos y mostrar en la aplicación Web.
     * @param msgId : Id del mensaje que acaba de actualizarse
     */
    public void updateMsg(char msgId){
        MessagesWithIndexes m = myMessages.get(msgId);
        byte[] newBytes = m.message.getBytes();     // Get new bytes
        int position = getPosition(m.myBitSig_inicio);    // Indice de bitRange y My Values
        int end = m.raw_fin - m.raw_inicio + m.myBitSig_inicio;
        // Mientras hayan bits que consumir
        while(end > 0){
            // TODO: Hacer update de myRawBytes con newBytes,
            end -= this.bitRange[position];
            position++;
        }
    }

    /**
     * Recibe un bit de inicio y retorna la posicion del valor correspondiente
     * @param bitInicio : bit de Inicio
     * @return Posición del valor correspondiente a ese bit de inicio
     */
    private int getPosition(int bitInicio){
        int i = 0;
        int pos = 0;
        while(i < bitInicio){
            i += this.bitRange[i]; // Avanzo hasta alcanzar el bitDeInicio
            pos++;
        }
        return pos;
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
