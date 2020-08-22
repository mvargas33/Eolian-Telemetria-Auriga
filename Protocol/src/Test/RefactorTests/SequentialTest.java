package Test.RefactorTests;

import ApplicationLayer.AppComponents.AppComponent;
import ApplicationLayer.AppComponents.AppReceiver;
import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.LocalServices.PrintService;
import ApplicationLayer.LocalServices.WebSocketService;
import ApplicationLayer.SensorReading.RandomReaders.RandomReader;
import ApplicationLayer.SensorReading.SensorsReader;
import ApplicationLayer.SensorReading.SequentialReaderExecutor;
import ExcelToAppComponent.CSVToAppComponent;
import PresentationLayer.Encryption.CryptoAdmin;
import PresentationLayer.Encryption.KeyAdmin;
import PresentationLayer.Packages.Components.State;
import PresentationLayer.Packages.Initializer.ReceiverInitializer;
import PresentationLayer.Packages.Initializer.SenderInitializer;
import PresentationLayer.Packages.Messages.Message;
import ZigBeeLayer.Receiving.ReceiverAdmin;
import ZigBeeLayer.Receiving.XbeeReceiver;
import ZigBeeLayer.Sending.SenderAdmin;
import ZigBeeLayer.Sending.XbeeSender;
import com.digi.xbee.api.XBeeDevice;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SequentialTest {
    // Cryptography common parameters
    int MAC_SIG_BYTES = 6; // Estos valores son los más suceptibles a usar por el tamaño del mensaje de las Xbee
    int IV_SIG_BYTES = 12;
    int CONTENT_SIG_BYTES = 16*5 + 15; // 5 bloques de 16 bytes, + 1 bloque de 15 bytes + 1 bloque de 16 bytes (MAC+IV)

    double MSG_RAW_SIZE_BYTES = CONTENT_SIG_BYTES + 1 + IV_SIG_BYTES + MAC_SIG_BYTES; // Tamaño de cada mensaje. Se usa para estimar delays lectura

    // Xbee common parameters
    int XBEE_BAUD = 230400;
    double XBEE_MAX_BYTES_PER_SEC_LIMIT = 7200; // Sólo puede enviar 7200 bytes por segundo A ESTE BAUDRATE y tamaño de mensaje de 114 bytes. Dato empírico
    double XBEE_MAX_MSG_PER_SEC_LIMIT = (XBEE_MAX_BYTES_PER_SEC_LIMIT/MSG_RAW_SIZE_BYTES) ;       // Mensajes por segundo (63.1) que puede enviar la Xbee a ese baudrate y tamaño de mensaje. (-1) Para holgura
    double XBEE_MAX_MSG_PERIOD_MS = ((1.0/XBEE_MAX_MSG_PER_SEC_LIMIT) * 1000) + 1;  // Delay en MS del Xbee (16ms). (+1) para holgura

    // Protocol paramseters
    int MSG_SIZE_BITS = 8*(16*5);
    int FIRST_HEADER = 56;

    // Data parameters
    int READ_PERIOD = 0; // 1000ms = 1seg

    // WebSockets parameters
    int SOCKET_PORT = 3000;
    String HOSTNAME = "localhost";

    /**
     * Ejecutarlo para cambiar las keys de sender/receiver
     * @throws NoSuchAlgorithmException Error de AES (no saldrá nunca)
     */
    void genNewKeyAndIV() throws NoSuchAlgorithmException {
        KeyAdmin keyAdmin = new KeyAdmin();
        keyAdmin.genNewKey();
        System.out.println("Key: " + keyAdmin.getKeyAsEncodedString());
        keyAdmin.genNewIV();
        System.out.println("IV : " + keyAdmin.getIVAsEncodedString());
    }

    CryptoAdmin setupCryptoAdmin() throws Exception {
        String encryptionKey = "uBb2BqdBtfJYyqOh5BmBX+HlqPGLz8/wdiXRgg8WnMs=";
        String IV = "eoCvPqhwOTO6FvGXGGPyhw==";
        KeyAdmin keyAdmin = new KeyAdmin();
        keyAdmin.setKeyFromEncodedString(encryptionKey);
        keyAdmin.setIVFromEncodedString(IV);

        return new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);
    }


    void senderSetup() throws Exception {
        // Exclusive Xbee parameters
        String XBEE_PORT = "COM3";

        // Object initialization
        CryptoAdmin cryptoAdmin = setupCryptoAdmin();
        XbeeSender xbeeSender = new XbeeSender(XBEE_BAUD, XBEE_PORT, (int) MSG_RAW_SIZE_BYTES);
        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender, cryptoAdmin);

        String dir = "src/ExcelToAppComponent/Eolian_fenix";
        List<AppSender> appSenders = CSVToAppComponent.CSVs_to_AppSenders(dir);

        // Lista de estados de capa inferior para incializar mensajes-state
        LinkedList<State> state_list = new LinkedList<>();
        LinkedList<RandomReader> randomReaders = new LinkedList<>();

        for (AppSender as: appSenders) {
            as.setSenderAdmin(senderAdmin); // Add senderAdmin
            state_list.add(as.getState());  // Extract State from presentationLayer
        }

        // Initializer of States/Messages
        SenderInitializer senderInitializer = new SenderInitializer(state_list,MSG_SIZE_BITS, FIRST_HEADER);
        HashMap<Character, Message> map = senderInitializer.genMessages();

        // Delay reading calc
        int numberOfMessagesToBeSend = map.size();
        double bytesToBeSend = numberOfMessagesToBeSend * MSG_RAW_SIZE_BYTES;
        //this.READ_PERIOD = (int) (numberOfMessagesToBeSend * XBEE_MAX_MSG_PERIOD_MS + 10.0);
        this.READ_PERIOD = 30;

        System.out.println("Messages to send of 114 bytes: " + numberOfMessagesToBeSend + " In bytes: " + bytesToBeSend);
        System.out.println("Delay of each Message: " + READ_PERIOD);

        // Random reader
        for (AppSender as: appSenders) {
            System.out.println(as.getMessages().size());
            randomReaders.add(new RandomReader(as, READ_PERIOD * as.getMessages().size())); // DELAY * # MSG IN PARTICIPATION
        }

        // Sequential executor of readers
        SequentialReaderExecutor sequentialReaderExecutor = new SequentialReaderExecutor();
        for (SensorsReader sr : randomReaders) { sequentialReaderExecutor.addReader(sr); }

        // High Level Services
        PrintService printService = new PrintService();
        WebSocketService webSocketService = new WebSocketService(SOCKET_PORT, HOSTNAME);

        for (AppComponent ac: appSenders) {
            //ac.subscribeToService(printService);
            ac.subscribeToService(webSocketService);
        }

        // Execute threads
        ExecutorService mainExecutor = Executors.newFixedThreadPool(2);

        // Init threads
        mainExecutor.submit(xbeeSender);
        //mainExecutor.submit(senderAdmin);
        mainExecutor.submit(sequentialReaderExecutor);
        //for (AppSender as: appSenders ) { mainExecutor.submit(as); }
        //for (RandomReader rd:randomReaders) {mainExecutor.submit(rd);}
        //mainExecutor.submit(printService);
        //mainExecutor.submit(webSocketService);

        /////////////////////////// ShutDown //////////////////////////
        mainExecutor.shutdown();

        /*XBeeDevice myDevice = new XBeeDevice(XBEE_PORT, XBEE_BAUD);
        myDevice.open();
        byte[] data = new byte[114];
        while(true) {
            try {
                myDevice.sendBroadcastData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        /////////////////////////// Control delays //////////////////////////
        /*int queueSizeAnterior = 0;
        int queueSizeActual = 0;
        int threshhold = 0;
        int confirmaciones = 0;
        while(true){
            Thread.sleep(1000);
            queueSizeActual = xbeeSender.getQueueSize();
            if(queueSizeActual > queueSizeAnterior){
                if(confirmaciones == 5){ // 5 confirmaciones seguidas
                    threshhold = this.READ_PERIOD;
                    this.READ_PERIOD = this.READ_PERIOD * 2;
                    confirmaciones = 0;
                }else{
                    confirmaciones += 1;
                }
            }else{
                confirmaciones = 0; // basta 1 vez que baje la cola para reiniciar el contador
                if (this.READ_PERIOD - 1 > threshhold){
                    this.READ_PERIOD -= 1;
                }
            }
            System.out.println("New period: " + this.READ_PERIOD+ " Last queue size: " + queueSizeActual);
            for (RandomReader rd:randomReaders) {rd.setDelayTime(this.READ_PERIOD * rd.getMyComponent().getMessages().size());}
            queueSizeAnterior = queueSizeActual;
        }*/

        //System.out.println("END");
    }

    void receiverSetup() throws Exception {
        // Exclusive Xbee parameters
        String XBEE_PORT = "COM4";

        // Object initialization
        CryptoAdmin cryptoAdmin = setupCryptoAdmin();
        XbeeReceiver xbeeReceiver = new XbeeReceiver(XBEE_BAUD, XBEE_PORT);


        String dir = "src/ExcelToAppComponent/Eolian_fenix";
        List<AppReceiver> appReceivers = CSVToAppComponent.CSVs_to_AppReceivers(dir);

        // Lista de estados de capa inferior para incializar mensajes-state
        LinkedList<State> state_list = new LinkedList<>();
        for (AppReceiver as: appReceivers) {
            as.ID = as.ID + "_R";
            state_list.add(as.getState());
        }

        // Initializer of States/Messages
        ReceiverInitializer receiverInitializer = new ReceiverInitializer(state_list, MSG_SIZE_BITS, FIRST_HEADER);
        HashMap<Character, Message> map = receiverInitializer.genMessages();

        // Receiver Admin
        ReceiverAdmin receiverAdmin = new ReceiverAdmin(xbeeReceiver, map, cryptoAdmin);

        // High Level Services
        PrintService printService = new PrintService();

        for (AppComponent ac: appReceivers) { ac.subscribeToService(printService); }

        // Execute threads
        ExecutorService mainExecutor = Executors.newFixedThreadPool(2+appReceivers.size()+1);

        // Init threads
        mainExecutor.submit(xbeeReceiver);
        mainExecutor.submit(receiverAdmin);
        for (AppReceiver ap: appReceivers ) { mainExecutor.submit(ap); }
        mainExecutor.submit(printService);

        /////////////////////////// ShutDown //////////////////////////
        mainExecutor.shutdown();
    }


    public static void main(String[] args) throws Exception {
        SequentialTest it = new SequentialTest();
        it.senderSetup();
        //it.receiverSetup();
    }
}
