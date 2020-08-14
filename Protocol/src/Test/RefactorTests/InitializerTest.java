package Test.RefactorTests;

import ApplicationLayer.AppComponents.AppComponent;
import ApplicationLayer.AppComponents.AppReceiver;
import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.LocalServices.PrintService;
import ApplicationLayer.SensorReading.RandomReaders.RandomReader;
import PresentationLayer.Encryption.CryptoAdmin;
import PresentationLayer.Encryption.KeyAdmin;
import PresentationLayer.Packages.Components.State;
import PresentationLayer.Packages.Components.StateSender;
import PresentationLayer.Packages.Initializer.Initializer;
import PresentationLayer.Packages.Initializer.ReceiverInitializer;
import PresentationLayer.Packages.Initializer.SenderInitializer;
import PresentationLayer.Packages.Messages.Message;
import PresentationLayer.Packages.Messages.ReceivedMessage;
import ZigBeeLayer.Receiving.ReceiverAdmin;
import ZigBeeLayer.Receiving.XbeeReceiver;
import ZigBeeLayer.Sending.SenderAdmin;
import ZigBeeLayer.Sending.XbeeSender;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class InitializerTest {
    // Cryptography common parameters
    int MAC_SIG_BYTES = 6; // Estos valores son los más suceptibles a usar por el tamaño del mensaje de las Xbee
    int IV_SIG_BYTES = 12;
    int CONTENT_SIG_BYTES = 16*5 + 15; // 5 bloques de 16 bytes, + 1 bloque de 15 bytes + 1 bloque de 16 bytes (MAC+IV)

    // Xbee common parameters
    int XBEE_BAUD = 230400;

    // Protocol paramseters
    int MSG_SIZE_BITS = 8*(16*5);
    int FIRST_HEADER = 56;

    // Data parameters
    int READ_FRECUENCY = 1; // 1000ms = 1seg

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
        XbeeSender xbeeSender = new XbeeSender(XBEE_BAUD, XBEE_PORT);
        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender, cryptoAdmin);

        // Main AppComponents and readers
        double[] bms_min = {0, 0.0, -100.01, 5.01};
        double[] bms_max = {1, 10.0, 100.01, 9.01};
        AppSender bms = new AppSender("BMS", bms_min, bms_max);

        bms.setSenderAdmin(senderAdmin);
        RandomReader bms_reader = new RandomReader(bms, READ_FRECUENCY);

        double[] motor_min = {0, 0, 0.01, -100.01};
        double[] motor_max = {1, 1, 300.01, 100.01};
        AppSender motor = new AppSender("MOTOR", motor_min, motor_max);

        motor.setSenderAdmin(senderAdmin);
        RandomReader motor_reader = new RandomReader(motor, READ_FRECUENCY);

        // Lista de AppSenders para suscribir todos a servicios
        LinkedList<AppSender> list = new LinkedList<>();
        list.add(bms);list.add(motor);

        // Lista de estados de capa inferior para incializar mensajes-state
        LinkedList<State> state_list = new LinkedList<>();
        for (AppSender as: list) {
            state_list.add(as.getState());
        }

        // Initializer of States/Messages
        SenderInitializer senderInitializer = new SenderInitializer(state_list,MSG_SIZE_BITS, FIRST_HEADER);
        senderInitializer.genMessages();

        // Check map
        //StateSender bms_State = (StateSender) bms.getState();
        //System.out.println(bms_State.printMessagesWithIndexes());

        // High Level Services
        PrintService printService = new PrintService();

        for (AppComponent ac: list) {
            ac.subscribeToService(printService);
        }

        // Execute threads
        ExecutorService mainExecutor = Executors.newFixedThreadPool(7);

        // Init threads
        mainExecutor.submit(xbeeSender);
        mainExecutor.submit(senderAdmin);
        mainExecutor.submit(bms);
        mainExecutor.submit(motor);
        mainExecutor.submit(bms_reader);
        mainExecutor.submit(motor_reader);
        mainExecutor.submit(printService);

        /////////////////////////// ShutDown //////////////////////////
        mainExecutor.shutdown();

        System.out.println("END");
    }

    void receiverSetup() throws Exception {
        // Exclusive Xbee parameters
        String XBEE_PORT = "COM4";

        // Object initialization
        CryptoAdmin cryptoAdmin = setupCryptoAdmin();
        XbeeReceiver xbeeReceiver = new XbeeReceiver(XBEE_BAUD, XBEE_PORT);

        // Main AppComponents and readers
        double[] bms_min = {0, 0.0, -100.01, 5.01};
        double[] bms_max = {1, 10.0, 100.01, 9.01};
        AppReceiver bms = new AppReceiver("BMS_R", bms_min, bms_max);

        double[] motor_min = {0, 0, 0.01, -100.01};
        double[] motor_max = {1, 1, 300.01, 100.01};
        AppReceiver motor = new AppReceiver("MOTOR_R", motor_min, motor_max);

        // Lista de AppReceiver para suscribir todos a servicios
        LinkedList<AppReceiver> list = new LinkedList<>();
        list.add(bms);list.add(motor);

        // Lista de estados de capa inferior para incializar mensajes-state
        LinkedList<State> state_list = new LinkedList<>();
        for (AppReceiver as: list) {
            state_list.add(as.getState());
        }

        // Initializer of States/Messages
        ReceiverInitializer receiverInitializer = new ReceiverInitializer(state_list, MSG_SIZE_BITS, FIRST_HEADER);
        HashMap<Character, Message> map = receiverInitializer.genMessages();

        // Receiver Admin
        ReceiverAdmin receiverAdmin = new ReceiverAdmin(xbeeReceiver, map, cryptoAdmin);

        // High Level Services
        PrintService printService = new PrintService();

        for (AppComponent ac: list) {
            ac.subscribeToService(printService);
        }

        // Execute threads
        ExecutorService mainExecutor = Executors.newFixedThreadPool(7);

        // Init threads
        mainExecutor.submit(xbeeReceiver);
        mainExecutor.submit(receiverAdmin);
        mainExecutor.submit(bms);
        mainExecutor.submit(motor);
        mainExecutor.submit(printService);

        /////////////////////////// ShutDown //////////////////////////
        mainExecutor.shutdown();
    }



    @Test
    void basicTest() throws Exception {
        //genNewKeyAndIV();
        senderSetup();
        receiverSetup();
        //ExecutorService xbeeReceiverExecutor = Executors.newFixedThreadPool(1);

    }

    public static void main(String[] args) throws Exception {
        InitializerTest it = new InitializerTest();
        it.senderSetup();
        it.receiverSetup();
    }
}