//package Main;
//
//import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
//import ApplicationLayer.LocalServices.DatabaseService;
//import ApplicationLayer.LocalServices.WebSocketService;
//import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Initializer.Initializer;
//import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.Message;
//import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving.ReceiverAdmin;
//import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving.XbeeReceiver;
////import com.sun.security.ntlm.Server;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class MainReceiver {
//    public static void main(String ... args) throws Exception {
//        /*--------------------- Constantes ---------------------*/
//        char baseHeader = 'A';
//        int msgLimitSize = 8 + 16 + 8; // 8 bit header, 16 bit contenido, 8 bit CRC8
//        int BAUD_RATE = 9600;
//        String PORT_RECEIVER = "COM6";
//        String REMOTE_IDENTIFIER = "EOLIAN FENIX"; // Yo tengo este nombre en mi Xbee
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
//
//        // Al finalizar, finalizar también los executors
//        xbeeReceiverExecutor.shutdown();
//        receiverAdminExecutor.shutdown();
//        localMasterAdminExecutor.shutdown();
//
//    }
//}
