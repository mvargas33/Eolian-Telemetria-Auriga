package PresentationLevel.Receiving;

import PresentationLevel.Messages.Message;
import Utilities.BitOperations;

import java.util.HashMap;

// CONSUMER
public class ReceiverAdmin implements Runnable{
    private XbeeReceiver xbeeReceiver; // Quien debe recibir mensajes y solo eso, ponerlos en la lista
    private HashMap<Character, Message> allMessages; // Diccionario con todos los mensajes del sistema
    private final int id;

    /**
     *
     * @param id : ID del thread que corre ReceiverAdmin
     * @param xbeeReceiver : Quien tiene la Queue de bytes[] a consumir
     * @param allMessages : Diccionario con todos los mensajes
     */
    public ReceiverAdmin(int id, XbeeReceiver xbeeReceiver, HashMap<Character, Message> allMessages){
        this.id = id;
        this.xbeeReceiver = xbeeReceiver;
        this.allMessages = allMessages;
    }

    /**
     * Consume un byte[] de la Queue del XbeeReceiver. Luego ve a qué mensaje le corresponden estos bytes, y extrae el mensaje.
     * Hace update de bytes[] del mensaje y luego ejecuta cadena de llamados para actualización de los componentes correspondientes.
     * DEBE CHECKEAR CRC DEL MENSAJE
     */
    public void consumeByteArrayFromQueue(){
        while(!this.xbeeReceiver.isQueueEmpty()){
            byte[] b = this.xbeeReceiver.consumeByteFromQueue();
            int largo = b.length;
            byte crc = BitOperations.calcCRC8(b, largo - 2); // Hasta antes del CRC que viene en mensaje

            if(crc == b[largo - 1]){ // Check CRC
                char header = (char) b[0];
                Message m = this.allMessages.get(header);
                m.updateRawBytes(b); // Esto hace llamada en cadena hasta que todos los componentes que se actualizaron queden en la Queue de LocalMasterAdmin
            }
        }
    }


    /**
     * Consume() : Lee un dato del buffer compartido. Obtiene el mensaje correspondiente según el ID (header)
     * y le pasa el nuevo valor a este objeto Message. Después el Message le hace notify() de Observer a
     * su(s) Componente(s) correspondiente(s).
     */
    @Override
    public void run() {
        while (true){
            try {
                consumeByteArrayFromQueue();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
