package Protocol.Receiving;

import java.util.concurrent.BlockingQueue;

// PRODUCER
public class XbeeReceiver implements Runnable{
    private final BlockingQueue<byte[]> sharedQueue;

    public XbeeReceiver(BlockingQueue<byte[]> sharedQueue){
        this.sharedQueue = sharedQueue;
    }

    /**
     * Pruduce() : Lee bytes de la Xbee y los pone en el buffer compartido
     *  Por dejabo hace notify() hacia los consumers (creo)
     * */
    @Override
    public void run() {
        while(true){
            try{
               // Read XBee
                byte[] data = new byte[] {0, 1, 0, 1};// xbee.read
                // Update sharedQueue synchronized
                sharedQueue.put(data);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}
