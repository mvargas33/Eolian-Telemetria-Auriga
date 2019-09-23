package Components;

import LocalSystems.LocalMasterAdmin;
import Protocol.Messages.Message;
import SensorReading.DataAdmin;

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
        int inicio;
        int fin;
        int mi_inicio;

        private MessagesWithIndexes(Message m, int inicio, int fin, int mi_inicio) {
            this.message = m;
            this.inicio = inicio;
            this.fin = fin;
            this.mi_inicio = mi_inicio;
        }
    }

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
     * Recibe un Id de mensaje porque ese mensaje ha actualizado sus valores. El componente debe tomar el objeto mensaje,
     * buscando en su Map con el id, xtraer los valores que le conciernen y hacer update de 'myRawBytes y 'myValues'.
     * Luego debe avisar a 'LocalMasterAdmin' que lo revise para extraer todos sus valores y guardarlos en la base de
     * datos y mostrar en la aplicación Web.
     * @param msgId : Id del mensaje que acaba de actualizarse
     */
    public void updateMesg(char msgId){
        MessagesWithIndexes m = myMessages.get(msgId);
        byte[] newBytes = m.message.getBytes();     // Get new bytes
        int position = getPosition(m.mi_inicio);    // Indice de bitRange y My Values
        int end = m.fin - m.inicio + m.mi_inicio;
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
    public int getPosition(int bitInicio){
        int i = 0;
        int pos = 0;
        while(i < bitInicio){
            i += this.bitRange[i]; // Avanzo hasta alcanzar el bitDeInicio
            pos++;
        }
        return pos;
    }


    public void addNewMessage(Message m, int inicio, int fin, int myInicio){
        this.myMessages.put(m.getHeader(), new MessagesWithIndexes(m,inicio, fin,myInicio));
    }
}
