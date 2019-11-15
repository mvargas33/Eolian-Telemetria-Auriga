package Protocol.Receiving;

import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;
import com.digi.xbee.api.utils.HexUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// PRODUCER
public class XbeeReceiver implements Runnable{
    private final BlockingQueue<byte[]> bytesReceived;
    private XBeeDevice myDeviceR; // Xbee que recibirá los mensajes

    /**
     * Listener de mensajes recibidos, este método se invoca cada vez que llega un mensaje
     */
    public static class MyListener implements IDataReceiveListener {
        private final BlockingQueue<byte[]> bytesReceived; // Queue de bytes compartida

        MyListener(BlockingQueue<byte[]> bytesReceived){
            this.bytesReceived = bytesReceived; // La deja en su objeto
        }

        @Override
        public void dataReceived(XBeeMessage xbeeMessage) {
            System.out.format("From %s >> %s | %s%n", xbeeMessage.getDevice().get64BitAddress(),
                    HexUtils.prettyHexString(HexUtils.byteArrayToHexString(xbeeMessage.getData())),
                    new String(xbeeMessage.getData()));
            try{
                this.bytesReceived.put(xbeeMessage.getData()); // Pone datos en la Queue compartida por esta clase y XbeeReceiver
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Constructor para Testing
     */
    public XbeeReceiver(){
        this.bytesReceived = new LinkedBlockingQueue<>();
    }

    /**
     * Constructor de caso de uso
     */
    public XbeeReceiver(int BAUD_RATE, String PORT_RECEIVE){
        this.bytesReceived = new LinkedBlockingQueue<>();
        this.myDeviceR = new XBeeDevice(PORT_RECEIVE, BAUD_RATE);

        // Esto abre la Xbee y añade el Listener, nada más, la deja abierta cosa que cada vez que llegue un mensaje, se activa el listener
        try {
            myDeviceR.open();
            myDeviceR.addDataListener(new XbeeReceiver.MyListener(this.bytesReceived));
            System.out.println("Xbee destino inicializada correctamente ...");
        } catch (XBeeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Método para Testing
     * Método para recibir bytes desde otra Xbee
     * @throws Exception Por poner en Queue
     */
    public void receiveByteOffline(byte[] b) throws Exception{
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
     * Retorna true si la queue no tiene más bytes
     * @return : true si queue no tiene más bytes
     */
    public boolean isQueueEmpty(){
        return bytesReceived.isEmpty();
    }

    /**
     * Pruduce() : Lee bytes de la Xbee y los pone en el buffer compartido
     *  Por dejabo hace notify() hacia los consumers (creo)
     * */
    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000000);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

}
