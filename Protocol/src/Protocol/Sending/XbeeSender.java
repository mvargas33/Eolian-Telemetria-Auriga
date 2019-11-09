package Protocol.Sending;

import Protocol.Receiving.XbeeReceiver;
import Utilities.BitOperations;
import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.utils.HexUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Clase que se encarga de enviar bytes[] por la Xbee. Tiene una Queue o cola de bytes a enviar.
 * SenderAdmin es quien pone bytes[] en la cola.
 */
public class XbeeSender implements Runnable{
    private BlockingQueue<byte[]> bytesToSend; // Queue de bytes[] para enviar por Xbee
    private XbeeReceiver myReceiver; // ONLY FOR TESTING, DELETE AFTER REAL TESING
    private XBeeDevice myDevice; // Xbee para envío de bytes
    private String REMOTE_NODE_IDENTIFIER; // Dirección de Xbee destino
    private XBeeNetwork xbeeNetwork;
    private RemoteXBeeDevice remoteDevice;


    /**
     * Constructor sólo se encarga de iniciar cola con implementación de LinkedBlockingQueue()
     * Constructor tipo TEST
     */
    public XbeeSender(XbeeReceiver xbeeReceiver){
        this.bytesToSend = new LinkedBlockingQueue<>();
        this.myReceiver = xbeeReceiver;
    }

    /**
     * Constructor de XbeeSender real
     */
    public XbeeSender(int BAUD_RATE, String PORT_SEND, String REMOTE_NODE_IDENTIFIER) throws Exception{
        this.bytesToSend = new LinkedBlockingQueue<>();
        this.myDevice = new XBeeDevice(PORT_SEND, BAUD_RATE);
        this.REMOTE_NODE_IDENTIFIER = REMOTE_NODE_IDENTIFIER;
        this.myDevice.open();
        this.xbeeNetwork = myDevice.getNetwork();
        this.remoteDevice = xbeeNetwork.discoverDevice(REMOTE_NODE_IDENTIFIER);
    }

    /**
     * Pone un byte[] array y lo pone en la Queue de envío
     * @param bytes : byte[] a poner para enviar
     */
    public void putByteInQueue(byte[] bytes){
       this.bytesToSend.add(bytes);
    }

    /**
     * Método de TEST, para cuando no ha Xbees
     * Toma un byte[] array de la Queue y lo envía a través de las Xbees
     */
    public void sendByteOffline() throws Exception{
        byte[] b = this.bytesToSend.poll(); // Get byte array from queue
        if(b != null){
            this.myReceiver.receiveByteOffline(b);
        }
    }

    /**
     * Toma un byte[] array de la Queue y lo envía a través de las Xbees
     */
    public void sendByte() throws Exception{
        while(!this.bytesToSend.isEmpty()){
            byte[] data = this.bytesToSend.poll(); // Sacar byte[] de la Queue
            System.out.println("Tamaño de Queue de XbeeSender: " + this.bytesToSend.size());
            try {
                // Obtain the remote XBee device from the XBee network.

                if (remoteDevice == null) {
                    System.out.println("No se encuentra Xbee destino con identificador :" + REMOTE_NODE_IDENTIFIER);
                    System.exit(1);
                }

                System.out.println("Enviando datos a Xbee Destino: " + BitOperations.ArraytoString(data));
                myDevice.sendData(remoteDevice, data);
                System.out.println("Enviado.");

            } catch (XBeeException e) {
                System.out.println("Error al enviar mensaje a Xbee Destino");
                e.printStackTrace();
                System.exit(1);
            }
        }
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
