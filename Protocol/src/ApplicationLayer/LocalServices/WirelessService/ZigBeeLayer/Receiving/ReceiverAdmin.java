package ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving;

import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Encryption.CryptoAdmin;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.Message;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.ReceivedMessage;


import java.util.HashMap;

// CONSUMER
public class ReceiverAdmin implements Runnable{
    private XbeeReceiver xbeeReceiver; // Quien debe recibir mensajes y solo eso, ponerlos en la lista
    private HashMap<Character, Message> allMessages; // Diccionario con todos los mensajes del sistema
    private CryptoAdmin myCryptoAdmin;

    /**
     *

     * @param xbeeReceiver : Quien tiene la Queue de bytes[] a consumir
     * @param allMessages : Diccionario con todos los mensajes
     */
    public ReceiverAdmin(XbeeReceiver xbeeReceiver, HashMap<Character, Message> allMessages, CryptoAdmin myCryptoAdmin){
        this.xbeeReceiver = xbeeReceiver;
        this.allMessages = allMessages;
        this.myCryptoAdmin = myCryptoAdmin;
    }

    /**
     * Consume un byte[] de la Queue del XbeeReceiver. Luego ve a qué mensaje le corresponden estos bytes, y extrae el mensaje.
     * Hace update de bytes[] del mensaje y luego ejecuta cadena de llamados para actualización de los componentes correspondientes.
     * DEBE CHECKEAR CRC DEL MENSAJE
     */
    public void consumeByteArrayFromQueue() throws Exception{
        System.out.println("consumeByteArrayFromQueue");
        while(true) {
            for (int i= 0; i < this.xbeeReceiver.sizeOfQueue(); i++) {      // Saca de una pasada tantos byte[] como habían en cola hasta evaluar la condición
                byte[] b = this.xbeeReceiver.consumeByteFromQueue();        // Extraer bytes RAW
                byte[] unEncryptedBytes = this.myCryptoAdmin.decrypt(b);    // Desencriptar mensaje

                char header = (char) unEncryptedBytes[0];                   // Extraer header
                ReceivedMessage m = (ReceivedMessage) this.allMessages.get(header);
                m.updateRawBytes(unEncryptedBytes); // Esto hace llamada en cadena hasta que todos los componentes que se actualizaron queden en la Queue de LocalMasterAdmin
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
        System.out.println("Receiver admin inicializado ...");
        while (true){
            try {
                consumeByteArrayFromQueue();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
