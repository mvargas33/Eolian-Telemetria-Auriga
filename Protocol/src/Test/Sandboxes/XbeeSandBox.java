package Test.Sandboxes;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;
import com.digi.xbee.api.utils.HexUtils;

/**
 * Clase para experimentar con comunicacion directa de Xbees
 */
public class XbeeSandBox {
    /**
     * Class to manage the XBee received data that was sent by other modules in the
     * same network.
     *
     * <p>Acts as a data listener by implementing the
     * {@link IDataReceiveListener} interface, and is notified when new
     * data for the module is received.</p>
     *
     * @see IDataReceiveListener
     *
     */
    public static class MyDataReceiveListener implements IDataReceiveListener {

        @Override
        public void dataReceived(XBeeMessage xbeeMessage) {
            System.out.format("From %s >> %s | %s%n", xbeeMessage.getDevice().get64BitAddress(),
                    HexUtils.prettyHexString(HexUtils.byteArrayToHexString(xbeeMessage.getData())),
                    new String(xbeeMessage.getData()));
        }
    }

    /* Constants */

    private static final String PORT_RECEIVE = "COM6";
    private static final int BAUD_RATE = 230400;

    /*
    public static void main(String[] args) {
        System.out.println(" +-----------------------------------------+");
        System.out.println(" |  XBee Java Library Receive Data Sample  |");
        System.out.println(" +-----------------------------------------+\n");

        XBeeDevice myDeviceR = new XBeeDevice(PORT_RECEIVE, BAUD_RATE);

        try {
            myDeviceR.open();

            myDeviceR.addDataListener(new MyDataReceiveListener());

            System.out.println("\n>> Waiting for data...");

        } catch (XBeeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
*/
/*----------------------------------------------------------------------------------------*/

    private static final String PORT_SEND = "COM4";

    private static final String DATA_TO_SEND = "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567";

    private static final String REMOTE_NODE_IDENTIFIER = "EOLIAN FENIX";

    /**
     * Application main method.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        System.out.println(" +--------------------------------------+");
        System.out.println(" |  XBee Java Library Send Data Sample  |");
        System.out.println(" +--------------------------------------+\n");

        //XBeeDevice myDevice = new XBeeDevice(PORT_SEND, BAUD_RATE);
        //byte[] dataToSend = DATA_TO_SEND.getBytes();
        byte[] dataToSend = {0,1,2,3,4,5};

        //try {
            /*----------- RECEIVER -------------*/
            XBeeDevice myDeviceR = new XBeeDevice(PORT_RECEIVE, BAUD_RATE);

            try {
                myDeviceR.open();

                myDeviceR.addDataListener(new MyDataReceiveListener());

                System.out.println("\n>> Waiting for data...");

            } catch (XBeeException e) {
                e.printStackTrace();
                System.exit(1);
            }
            /*------------------------------------*/
/*
            myDevice.open();

            // Obtain the remote XBee device from the XBee network.
            XBeeNetwork xbeeNetwork = myDevice.getNetwork();
            RemoteXBeeDevice remoteDevice = xbeeNetwork.discoverDevice(REMOTE_NODE_IDENTIFIER);
            if (remoteDevice == null) {
                System.out.println("Couldn't find the remote XBee device with '" + REMOTE_NODE_IDENTIFIER + "' Node Identifier.");
                System.exit(1);
            }

            System.out.format("Sending data to %s >> %s | %s... ", remoteDevice.get64BitAddress(),
                    HexUtils.prettyHexString(HexUtils.byteArrayToHexString(dataToSend)),
                    new String(dataToSend));

            myDevice.sendData(remoteDevice, dataToSend);

            System.out.println("Success");
*/
        //}
        /* catch (XBeeException e) {
            System.out.println("Error");
            e.printStackTrace();
            System.exit(1);
        }/* finally {
            myDeviceR.close();
        }*/
    }
}
