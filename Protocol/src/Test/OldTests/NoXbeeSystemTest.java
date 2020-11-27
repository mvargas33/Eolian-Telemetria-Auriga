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
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//
//import static org.junit.jupiter.api.Assertions.assertArrayEquals;
//
//class NoXbeeSystemTest {
//
//    @Test
//    void noXbeeSendReceiveLinearTest() throws Exception{
//        XbeeReceiver xbeeReceiver = new XbeeReceiver(); // Constructor especial para este Test
//        XbeeSender xbeeSender = new XbeeSender(xbeeReceiver); // TODO: DELETE DESPUES DE TEST EXITOSO, ESTE ES EL PUENTE
//
//        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender);
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
//        RandomReader sensorsReader = new RandomReader(allComponents, listAllComponents_origen);
//
//        char baseHeader = 'A';
//        int msgLimitSize = 24 + 8; // Bits
//        Initializer initializer_origen = new Initializer(listAllComponents_origen, msgLimitSize, baseHeader);
//
//        initializer_origen.genMessages(); // Para enviar no se necesita HashMap de Messages
//
//        System.out.println("Initializaer origen: ");
//        System.out.println(BMS_origen.printMessagesWithIndexes());
//
//        LocalMasterAdmin localMasterAdmin = new LocalMasterAdmin(false);
//
//        int[] valores_0 = {0, 0, 0, 0, 0};
//        int[] bitSig_0 = {8, 8, 2, 8, 5};
//        State BMS_destino = new State(localMasterAdmin, valores_0, bitSig_0, "BMS_DESTINO");
//
//        LinkedList<State> listAllComponents_destino = new LinkedList<>();
//        listAllComponents_destino.add(BMS_destino);
//
//        Initializer initializer_detino = new Initializer(listAllComponents_destino, msgLimitSize, baseHeader);
//        HashMap<Character, Message> messages_destino = initializer_detino.genMessages();
//
//        System.out.println("Initializaer destino: ");
//        System.out.println(BMS_destino.printMessagesWithIndexes());
//
//        ReceiverAdmin receiverAdmin = new ReceiverAdmin(1, xbeeReceiver, messages_destino);
//
//        /* ----------------------------- TESTING -----------------------------*/
//
//        sensorsReader.randomData("BMS_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//
//
//        System.out.println("Mensajes en cola de SenderAdmin: " + senderAdmin.messageQueueSize());
//
//        while(!senderAdmin.isMessageQueueEmpty()){
//            senderAdmin.putMessageInByteQueue(); // Saca de queue de Message y pone en queue de byte[] de Xbee Sender
//            xbeeSender.sendByteOffline(); // Envia byte[] a xbeeRecevier y queda en la queue de byte[] del xBeeReceiver
//        }
//        while(!xbeeReceiver.isQueueEmpty()) {
//            receiverAdmin.consumeByteArrayFromQueue(); // Consume un Byte de la queue del xbeeReceiver, se ponen componentes actualizado en la queue de localmasteradmin
//            localMasterAdmin.consumeComponent(); // Se muestra componente como llega
//        }
//        assertArrayEquals(BMS_origen.getMyValues(), BMS_destino.getMyValues()); // Origen y destino con mismos valores
//
//    }
//
//    @Test
//    void noXbeeSendReceiveLinearTestComplex() throws Exception{
//        XbeeReceiver xbeeReceiver = new XbeeReceiver();
//        XbeeSender xbeeSender = new XbeeSender(xbeeReceiver); // TODO: DELETE DESPUES DE TEST EXITOSO, ESTE ES EL PUENTE
//
//        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender);
//
//        // 2 4 8 16 32 64 128 256 512 1024 2048 4096
//        // 1 2 3 4  5  6  7   8   9   10   11 12
//        int[] bitSig0 = {8, 8, 2, 8, 5, 21, 21, 21, 11, 22, 9};
//        int[] valores0 = new int[bitSig0.length];
//        State BMS_origen = new State(senderAdmin, valores0, bitSig0, "BMS_ORIGEN");
//
//        int[] bitSig1 = {1, 1, 1, 1, 1, 2, 3, 11, 8, 9};
//        int[] valores1 = new int[bitSig1.length];
//        State MPPT1_origen = new State(senderAdmin, valores1, bitSig1, "MPPT1_ORIGEN");
//
//        int[] bitSig2 = {1, 1, 1, 1, 1, 2, 3, 11, 8, 9};
//        int[] valores2 = new int[bitSig2.length];
//        State MPPT2_origen = new State(senderAdmin, valores2, bitSig2, "MPPT2_ORIGEN");
//
//        int[] bitSig3 = {1, 1, 1, 1, 1, 2, 3, 11, 8, 9};
//        int[] valores3 = new int[bitSig3.length];
//        State MPPT3_origen = new State(senderAdmin, valores3, bitSig3, "MPPT3_ORIGEN");
//
//        int[] bitSig4 = {12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};
//        int[] valores4 = new int[bitSig4.length];
//        State BMS_TEMP_origen = new State(senderAdmin, valores4, bitSig4, "BMS_TEMP_ORIGEN");
//
//        HashMap<String, State> allComponents = new HashMap<>();
//        allComponents.put(BMS_origen.getID(), BMS_origen);
//        allComponents.put(MPPT1_origen.getID(), MPPT1_origen);
//        allComponents.put(MPPT2_origen.getID(), MPPT2_origen);
//        allComponents.put(MPPT3_origen.getID(), MPPT3_origen);
//        allComponents.put(BMS_TEMP_origen.getID(), BMS_TEMP_origen);
//
//        LinkedList<State> listAllComponents_origen = new LinkedList<>();
//        listAllComponents_origen.add(BMS_origen);
//        listAllComponents_origen.add(MPPT1_origen);
//        listAllComponents_origen.add(MPPT2_origen);
//        listAllComponents_origen.add(MPPT3_origen);
//        listAllComponents_origen.add(BMS_TEMP_origen);
//
//        RandomReader sensorsReader = new RandomReader(allComponents, listAllComponents_origen);
//
//        char baseHeader = 'A';
//        int msgLimitSize = 8*8 + 8 + 8; // Bits
//        Initializer initializer_origen = new Initializer(listAllComponents_origen, msgLimitSize, baseHeader);
//
//        initializer_origen.genMessages(); // Para enviar no se necesita HashMap de Messages
//
//        System.out.println("Initializaer origen: ");
//        System.out.println(BMS_origen.printMessagesWithIndexes());
//        System.out.println(MPPT1_origen.printMessagesWithIndexes());
//        System.out.println(MPPT2_origen.printMessagesWithIndexes());
//        System.out.println(MPPT3_origen.printMessagesWithIndexes());
//        System.out.println(BMS_TEMP_origen.printMessagesWithIndexes());
//
//        WebSocketService webSocketService = new WebSocketService("http://localhost:3000/update");
//        DatabaseService databaseService = new DatabaseService();
//        LocalMasterAdmin localMasterAdmin = new LocalMasterAdmin(webSocketService, databaseService, false); // Todos los componentes lo deben conocer para ponerse en su Queue y que el LocalMasterAdmin los revise
//
//
//        int[] bitSig00 = {8, 8, 2, 8, 5, 21, 21, 21, 11, 22, 9};
//        int[] valores00 = new int[bitSig0.length];
//        State BMS_destino = new State(localMasterAdmin, valores00, bitSig00, "BMS_DESTINO");
//
//        int[] bitSig11 = {1, 1, 1, 1, 1, 2, 3, 11, 8, 9};
//        int[] valores11 = new int[bitSig11.length];
//        State MPPT1_destino = new State(localMasterAdmin, valores11, bitSig11, "MPPT1_DESTINO");
//
//        int[] bitSig22 = {1, 1, 1, 1, 1, 2, 3, 11, 8, 9};
//        int[] valores22 = new int[bitSig22.length];
//        State MPPT2_destino = new State(localMasterAdmin, valores22, bitSig22, "MPPT2_DESTINO");
//
//        int[] bitSig33 = {1, 1, 1, 1, 1, 2, 3, 11, 8, 9};
//        int[] valores33 = new int[bitSig33.length];
//        State MPPT3_destino = new State(localMasterAdmin, valores33, bitSig33, "MPPT3_DESTINO");
//
//        int[] bitSig44 = {12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};
//        int[] valores44 = new int[bitSig44.length];
//        State BMS_TEMP_destino = new State(localMasterAdmin, valores44, bitSig44, "BMS_TEMP_DESTINO");
//
//        LinkedList<State> listAllComponents_destino = new LinkedList<>();
//        listAllComponents_destino.add(BMS_destino);
//        listAllComponents_destino.add(MPPT1_destino);
//        listAllComponents_destino.add(MPPT2_destino);
//        listAllComponents_destino.add(MPPT3_destino);
//        listAllComponents_destino.add(BMS_TEMP_destino);
//
//        Initializer initializer_detino = new Initializer(listAllComponents_destino, msgLimitSize, baseHeader);
//        HashMap<Character, Message> messages_destino = initializer_detino.genMessages();
//
//        System.out.println("Initializaer destino: ");
//        System.out.println(BMS_destino.printMessagesWithIndexes());
//        System.out.println(MPPT1_destino.printMessagesWithIndexes());
//        System.out.println(MPPT2_destino.printMessagesWithIndexes());
//        System.out.println(MPPT3_destino.printMessagesWithIndexes());
//        System.out.println(BMS_TEMP_destino.printMessagesWithIndexes());
//
//        ReceiverAdmin receiverAdmin = new ReceiverAdmin(1, xbeeReceiver, messages_destino);
//
//        /* ----------------------------- TESTING -----------------------------*/
//
//        sensorsReader.randomData("BMS_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//        sensorsReader.randomData("MPPT1_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//        sensorsReader.randomData("MPPT2_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//        sensorsReader.randomData("MPPT3_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//        sensorsReader.randomData("BMS_TEMP_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//
//
//        System.out.println("Mensajes en cola de SenderAdmin: " + senderAdmin.messageQueueSize());
//
//
//        senderAdmin.putMessageInByteQueue(); // Saca a TODOS de queue de Message y pone en queue de byte[] de Xbee Sender
//        xbeeSender.sendByteOffline(); // Envia TODOS los byte[] a xbeeRecevier y queda en la queue de byte[] del xBeeReceiver
//
//
//
//        receiverAdmin.consumeByteArrayFromQueue(); // Consume TODOS Byte de la queue del xbeeReceiver, se ponen componentes actualizado en la queue de localmasteradmin
//        localMasterAdmin.consumeComponent(); // Se muestra componente como llega
//
//        assertArrayEquals(BMS_origen.getMyValues(), BMS_destino.getMyValues()); // Origen y destino con mismos valores
//        assertArrayEquals(MPPT1_origen.getMyValues(), MPPT1_destino.getMyValues()); // Origen y destino con mismos valores
//        assertArrayEquals(MPPT2_origen.getMyValues(), MPPT2_destino.getMyValues()); // Origen y destino con mismos valores
//        assertArrayEquals(MPPT3_origen.getMyValues(), MPPT3_destino.getMyValues()); // Origen y destino con mismos valores
//        assertArrayEquals(BMS_TEMP_origen.getMyValues(), BMS_TEMP_destino.getMyValues()); // Origen y destino con mismos valores
//
//        /* -------------- SEGUNDO ROUND --------------*/
//        sensorsReader.randomData("BMS_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//        sensorsReader.randomData("MPPT1_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//        sensorsReader.randomData("MPPT2_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//        sensorsReader.randomData("MPPT3_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//        sensorsReader.randomData("BMS_TEMP_ORIGEN"); // Lee, actualiza y deja en queue de sender admin
//
//
//        System.out.println("Mensajes en cola de SenderAdmin: " + senderAdmin.messageQueueSize());
//
//
//        senderAdmin.putMessageInByteQueue(); // Saca a TODOS de queue de Message y pone en queue de byte[] de Xbee Sender
//        xbeeSender.sendByteOffline(); // Envia TODOS los byte[] a xbeeRecevier y queda en la queue de byte[] del xBeeReceiver
//
//        receiverAdmin.consumeByteArrayFromQueue(); // Consume TODOS los Byte de la queue del xbeeReceiver, se ponen componentes actualizado en la queue de localmasteradmin
//        localMasterAdmin.consumeComponent(); // Se muestraj TODOS componente como llega
//
//        assertArrayEquals(BMS_origen.getMyValues(), BMS_destino.getMyValues()); // Origen y destino con mismos valores
//        assertArrayEquals(MPPT1_origen.getMyValues(), MPPT1_destino.getMyValues()); // Origen y destino con mismos valores
//        assertArrayEquals(MPPT2_origen.getMyValues(), MPPT2_destino.getMyValues()); // Origen y destino con mismos valores
//        assertArrayEquals(MPPT3_origen.getMyValues(), MPPT3_destino.getMyValues()); // Origen y destino con mismos valores
//        assertArrayEquals(BMS_TEMP_origen.getMyValues(), BMS_TEMP_destino.getMyValues()); // Origen y destino con mismos valores
//
//    }
//
//}