package Protocol.Sending;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Clase que se encarga de enviar bytes[] por la Xbee. Tiene una Queue o cola de bytes a enviar.
 * SenderAdmin es quien pone bytes[] en la cola.
 */
public class XbeeSender implements Runnable{
     private BlockingQueue<byte[]> bytesToSend; // Queue de bytes[] para enviar por Xbee

    /**
     * Constructor sólo se encarga de iniciar cola con implementación de LinkedBlockingQueue()
     */
    XbeeSender(){
        this.bytesToSend = new LinkedBlockingQueue<>();
    }

    /**
     * Pone un byte[] array y lo pone en la Queue de envío
     * @param bytes : byte[] a poner para enviar
     */
    public void putByteInQueue(byte[] bytes){
       this.bytesToSend.add(bytes);
    }

    /**
     * Toma un byte[] array de la Queue y lo envía a través de las Xbees
     */
    public void sendByte(){
        byte[] b = this.bytesToSend.poll(); // Get byte array from queue
        // TODO: SEND THROW XBEE()
    }

    /**
     * Como Thread, XbeeSender siempre intentará invocar sendByte() en cada instante.
     * Para sacar byte[] y enviarlo lo antes posible.
     */
    @Override
    public void run() {
        while(true){
            sendByte(); // Sacar byte[] de la cola y enviarlo
        }
    }
}
