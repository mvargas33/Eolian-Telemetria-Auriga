//package Test.OldTests;
//
//import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
//import ApplicationLayer.LocalServices.DatabaseService;
//import ApplicationLayer.LocalServices.WebSocketService;
//import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Initializer.Initializer;
//import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.Message;
//import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving.ReceiverAdmin;
//import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving.XbeeReceiver;
//import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Sending.SenderAdmin;
//import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Sending.XbeeSender;
//import ApplicationLayer.SensorReading.RandomReaders.RandomReader;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//class LocalXbeeTest {
//
//    @org.junit.jupiter.api.Test
//    public static void main(String ... args) throws Exception{
//        ///////////////////////// Main Reciver /////////////////////////
//        /*--------------------- Constantes ---------------------*/
//        char baseHeader = 'A';
//        int msgLimitSize = 8 + 16 + 8; // 8 bit header, 16 bit contenido, 8 bit CRC8
//        int BAUD_RATE = 9600;
//        String PORT_RECEIVER = "COM6";
//        //String REMOTE_IDENTIFIER = "EOLIAN FENIX"; // Yo tengo este nombre en mi Xbee, no necesario, ahora se hace broadcast
//
//        /*------------------ Clases de recibir ------------------*/
//
//        XbeeReceiver xbeeReceiver = new XbeeReceiver(BAUD_RATE, PORT_RECEIVER);
//        WebSocketService webSocketService = new WebSocketService("http://localhost:3000/update");
//        DatabaseService databaseService = new DatabaseService();
//        LocalMasterAdmin localMasterAdmin = new LocalMasterAdmin(webSocketService, databaseService, false); // Todos los componentes lo deben conocer para ponerse en su Queue y que el LocalMasterAdmin los revise
//
//        /*--------------------- Componentes ---------------------*/
//        int[] valores_0 = {0, 0, 0, 0, 0};
//        int[] bitSig_0 = {8, 8, 2, 8, 5};
//        State BMS_destino = new State(localMasterAdmin, valores_0, bitSig_0, "BMS_DESTINO");
//
//        LinkedList<State> listAllComponents_destino = new LinkedList<>();
//        listAllComponents_destino.add(BMS_destino);
//
//        /*--------------------- Inicializador ---------------------*/
//
//        Initializer initializer_detino = new Initializer(listAllComponents_destino, msgLimitSize, baseHeader);
//        HashMap<Character, Message> messages_destino = initializer_detino.genMessages();
//
//        /*----------------- Generador de mensajes -----------------*/
//
//        ReceiverAdmin receiverAdmin = new ReceiverAdmin(1, xbeeReceiver, messages_destino);
//
//        /*---------------- Threads de cada clase -----------------*/
//
//        ExecutorService xbeeReceiverExecutor = Executors.newFixedThreadPool(1);
//        ExecutorService receiverAdminExecutor = Executors.newFixedThreadPool(1);
//        ExecutorService localMasterAdminExecutor = Executors.newFixedThreadPool(1);
//
//        xbeeReceiverExecutor.submit(xbeeReceiver);
//        receiverAdminExecutor.submit(receiverAdmin);
//        localMasterAdminExecutor.submit(localMasterAdmin);
//
//        ///////////////////////// Main Sender /////////////////////////
//
//        /*--------------------- Constantes ---------------------*/
//
//        String PORT_SENDER = "COM4";
//
//        /*------------------- Clases de envío -------------------*/
//
//        XbeeSender xbeeSender = new XbeeSender(BAUD_RATE, PORT_SENDER);
//        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender);
//
//        /*--------------------- Componentes ---------------------*/
//
//        // 2 4 8 16 32 64 128 256 512 1024 2048
//        // 1 2 3 4  5  6  7   8   9   10   11
//        int[] valores = {200, 189, 0, 111, 30};
//        int[] bitSig = {8, 8, 2, 8, 5};
//        State BMS_origen = new State(senderAdmin, valores, bitSig, "BMS_ORIGEN");
//
//        HashMap<String, State> allComponents = new HashMap<>();
//        allComponents.put(BMS_origen.getID(), BMS_origen);
//
//        LinkedList<State> listAllComponents_origen = new LinkedList<>();
//        listAllComponents_origen.add(BMS_origen);
//
//        /*--------------------- Inicializador ---------------------*/
//
//        Initializer initializer_origen = new Initializer(listAllComponents_origen, msgLimitSize, baseHeader);
//        initializer_origen.genMessages(); // Para enviar no se necesita HashMap de Messages
//
//        System.out.println("Initializaer origen: ");
//        System.out.println(BMS_origen.printMessagesWithIndexes());
//
//
//        /*------------------ Generador de datos ------------------*/
//
//        System.out.println("BMS DESTINO ANTES DE ENVIO DE MENSAJE : \n" + BMS_destino.toString());
//
//        RandomReader sensorsReader = new RandomReader(allComponents, listAllComponents_origen);
//
//        /*---------------- Threads de cada clase -----------------*/
//
//        ExecutorService xbeeSenderExecutor = Executors.newFixedThreadPool(1);
//        ExecutorService senderAdminExecutor = Executors.newFixedThreadPool(1);
//        ExecutorService sensorsReaderExecutor = Executors.newFixedThreadPool(1);
//
//        xbeeSenderExecutor.submit(xbeeSender);
//        senderAdminExecutor.submit(senderAdmin);
//        sensorsReaderExecutor.submit(sensorsReader);
//
//
//        /////////////////////////// ShutDown //////////////////////////
//
//        // Al finalizar, finalizar también los executors
//        xbeeReceiverExecutor.shutdown();
//        receiverAdminExecutor.shutdown();
//        localMasterAdminExecutor.shutdown();
//        // Sender
//        xbeeSenderExecutor.shutdown();
//        senderAdminExecutor.shutdown();
//        sensorsReaderExecutor.shutdown();
//    }
//
//}