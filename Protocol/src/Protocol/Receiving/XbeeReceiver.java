package Protocol.Receiving;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// PRODUCER
public class XbeeReceiver implements Runnable{
    private final BlockingQueue<byte[]> bytesReceived;

    public XbeeReceiver(){
        this.bytesReceived = new LinkedBlockingQueue<>();
    }

    /**
     * Método para recibir bytes desde otra Xbee
     * @throws Exception Por poner en Queue
     */
    public void receiveByte(byte[] b) throws Exception{
        // TODO: ONLY FOR TESTING, DELETE AFTER REAL TESING : byte[] b should be deleted
        // TODO: Xbee.read()
        bytesReceived.put(b);
    }

    /**
     * Consume() . Utilizado por los ReceiverAdmin. Consumen un byte[] de la Queue
     * @return byte[] sacado de la Queue
     */
    public byte[] consumeByteFromQueue(){
        return bytesReceived.poll();
    }


    /**
     * Pruduce() : Lee bytes de la Xbee y los pone en el buffer compartido
     *  Por dejabo hace notify() hacia los consumers (creo)
     * */
    @Override
    public void run() {
        while(true){
            try{
               // receiveByte(); // TODO: ONLY FOR TESTING, REVIVIR AFTER REAL TESING
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

}
