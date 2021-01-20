package Test.XbeeTest;

import Test.Sandboxes.XbeeSandBox;
import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;
import com.digi.xbee.api.utils.HexUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConnectionTests {
    int BAUD_RATE;
    String PORT_RECEIVE;
    String PORT_SEND;
    String DATA_TO_SEND;
    byte[] DATA_TO_SEND_BYTES;
    String REMOTE_NODE_IDENTIFIER;
    XBeeDevice myDeviceR;
    XBeeDevice myDeviceS;
    RemoteXBeeDevice myRemoteDevice;
    XBeeDevice myDeviceRS;

    /**
     * Handler genérico para Xbee Receiver a usar en todos los tests
     */
    class testReceiveListener implements IDataReceiveListener {
        @Override
        public void dataReceived(XBeeMessage xbeeMessage) {
            System.out.format("From %s >> %s | %s%n", xbeeMessage.getDevice().get64BitAddress(),
                    HexUtils.prettyHexString(HexUtils.byteArrayToHexString(xbeeMessage.getData())),
                    new String(xbeeMessage.getData()));
        }
    }

    /**
     * Setup de parámetros de los tests. Baudrate, nombre de los puertos de las Xbee.
     * Handler genérico del Xbee Receiver
     */
    @BeforeEach
    public void xbeeSetup(){
        BAUD_RATE = 230400;
        PORT_RECEIVE = "COM6";
        PORT_SEND = "COM4";
        DATA_TO_SEND = "Hola! Probando ...";
        DATA_TO_SEND_BYTES = DATA_TO_SEND.getBytes();
        REMOTE_NODE_IDENTIFIER = "EOLIAN FENIX";
        myDeviceR = null;
        myDeviceS = null;
        myRemoteDevice = null;
    }


    public void xbeeReceiverSetup(XBeeDevice xBeeDevice){
        xBeeDevice = new XBeeDevice(PORT_RECEIVE, BAUD_RATE);
        try {
            xBeeDevice.open();
            xBeeDevice.addDataListener(new testReceiveListener());
            System.out.println("\n>> Xbee Receiver: Waiting for data...");
        } catch (XBeeException e) {
            e.printStackTrace();
        }
    }

    public void xbeeSenderTargetSetup(XBeeDevice xBeeDevice, RemoteXBeeDevice remoteXBeeDevice){
        xBeeDevice = new XBeeDevice(PORT_SEND, BAUD_RATE);
        // Obtain the remote XBee device from the XBee network.
        XBeeNetwork xbeeNetwork = xBeeDevice.getNetwork();

        try {
            remoteXBeeDevice = xbeeNetwork.discoverDevice(REMOTE_NODE_IDENTIFIER);
        } catch (XBeeException e) {
            e.printStackTrace();
        }
        if (remoteXBeeDevice == null) {
            System.out.println("Couldn't find the remote XBee device with '" + REMOTE_NODE_IDENTIFIER + "' Node Identifier.");
            System.exit(1);
        }
    }

    public void xbeeSenderBroadcastSetup(XBeeDevice xBeeDevice){
        xBeeDevice = new XBeeDevice(PORT_SEND, BAUD_RATE);
    }

    public void sendMessageWithTarget(XBeeDevice xBeeDevice, RemoteXBeeDevice remoteXBeeDevice){
        try {
            xBeeDevice.sendData(remoteXBeeDevice, DATA_TO_SEND_BYTES);
            System.out.println("XbeeSender Success: Mensaje enviado a Xbee Recevier");
        } catch (XBeeException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageBroadcast(XBeeDevice xBeeDevice){
        try {
            xBeeDevice.sendBroadcastData(DATA_TO_SEND_BYTES);
            System.out.println("XbeeSender Success: Mensaje enviado como broadcast");
        } catch (XBeeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prueba que una conexión simple entre dos Xbees conectadas en el mismo dispitivo
     */
    @Test
    public void dualConnectionSameDeviceTargetTest(){
        receiverTest();
        senderTargetTest();
    }

    @Test
    public void dualConnectionSameDeviceBroadcastTest(){
        receiverTest();
        senderBroadcastTest();
    }

    /**
     * Prueba iniciar un dispositivo enviador que hace broadcast de mensajes
     */
    @Test
    public void senderBroadcastTest() {
        xbeeSenderBroadcastSetup(myDeviceS);
        System.out.format("Sending broadcast data ...");

        // Enviar 10 smensajes y parar
        for (int i = 0; i < 10; i++) {
            try {
                sendMessageBroadcast(myDeviceS);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }

    @Test
    public void senderTargetTest(){
        xbeeSenderTargetSetup(myDeviceS, myRemoteDevice);

        System.out.format("Sending data to device %s ...", myRemoteDevice.get64BitAddress());

        // Enviar 10 smensajes y parar
        for(int i = 0; i < 10; i++) {
            try {
                sendMessageWithTarget(myDeviceS, myRemoteDevice);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void receiverTest(){
        xbeeReceiverSetup(myDeviceR);
    }

    /**
     * Configura una Xbee como Receiver y como Sender.
     * La idea es que una Xbee externa envíe datos a esta Xbee,
     * y que esta Xbee mande mensajes a una Xbee externa,
     * en forma de Broadcast
     */
    @Test
    public void sendBroadcastAndReceiveTest(){
        xbeeReceiverSetup(myDeviceRS);
        xbeeSenderBroadcastSetup(myDeviceRS);

        // Enviar 10 smensajes y parar
        for(int i = 0; i < 10; i++) {
            try {
                sendMessageBroadcast(myDeviceRS);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
