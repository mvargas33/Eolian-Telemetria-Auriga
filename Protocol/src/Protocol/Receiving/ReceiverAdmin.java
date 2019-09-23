package Protocol.Receiving;

import java.util.concurrent.BlockingQueue;

// CONSUMER
public class ReceiverAdmin implements Runnable{
    //private XbeeReceiver xbeeReceiver; // Quien debe recibir mensajes y solo eso, ponerlos en la lista
    private final BlockingQueue<byte[]> buffer; // Buffer principal de bytes leídos por la Xbee
    private final int id;

    public ReceiverAdmin(BlockingQueue<byte[]> buffer, int id){
        this.buffer = buffer;
        this.id = id;
    }

    /**
     * Consume() : Lee un dato del buffer compartido. Obtiene el mensaje correspondiente según el ID (header)
     * y le pasa el nuevo valor a este objeto Message. Después el Message le hace notify() de Observer a
     * su(s) Componente(s) correspondiente(s).
     */
    @Override
    public void run() {

    }
}
