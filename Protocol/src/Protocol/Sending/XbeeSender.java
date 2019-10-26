package Protocol.Sending;

import Protocol.Receiving.XbeeReceiver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Clase que se encarga de enviar bytes[] por la Xbee. Tiene una Queue o cola de bytes a enviar.
 * SenderAdmin es quien pone bytes[] en la cola.
 */
public class XbeeSender implements Runnable{
     private BlockingQueue<byte[]> bytesToSend; // Queue de bytes[] para enviar por Xbee
    private XbeeReceiver myReceiver; // TODO: ONLY FOR TESTING, DELETE AFTER REAL TESING

    /**
     * Constructor sólo se encarga de iniciar cola con implementación de LinkedBlockingQueue()
     */
    XbeeSender(XbeeReceiver xbeeReceiver){
        this.bytesToSend = new LinkedBlockingQueue<>();
        this.myReceiver = xbeeReceiver; // TODO: ONLY FOR TESTING, DELETE AFTER REAL TESING
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
    public void sendByte() throws Exception{
        byte[] b = this.bytesToSend.poll(); // Get byte array from queue
        // TODO: SEND THROW XBEE()
        this.myReceiver.receiveByte(b); // TODO: ONLY FOR TESTING, DELETE AFTER REAL TESING
    }

    /**
     * Como Thread, XbeeSender siempre intentará invocar sendByte() en cada instante.
     * Para sacar byte[] y enviarlo lo antes posible.
     */
    @Override
    public void run() {
        while(true){
            try{
                sendByte(); // Sacar byte[] de la cola y enviarlo
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
