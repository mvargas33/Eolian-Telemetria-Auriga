//package Test.OldTests;
//
//import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
//import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Initializer.Initializer;
//import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving.XbeeReceiver;
//import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Sending.SenderAdmin;
//import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Sending.XbeeSender;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//
//class InitializerTest {
//
//    @org.junit.jupiter.api.Test
//    void testGenMessagesWithJustOneComponent1() throws Exception{
//        XbeeReceiver xbeeReceiver = new XbeeReceiver();
//        XbeeSender xbeeSender = new XbeeSender(xbeeReceiver); // TODO: DELETE DESPUES DE TEST EXITOSO, ESTE ES EL PUENTE
//        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender);
//        LocalMasterAdmin localMasterAdmin = new LocalMasterAdmin(false);
//
//        // 2 4 8 16 32 64 128 256 512 1024 2048
//        // 1 2 3 4  5  6  7   8   9   10   11
//        int[] valores = {200, 189, 0, 111, 30};
//        int[] bitSig = {8, 8, 2, 8, 5};
//        State BMS_origen = new State(senderAdmin, valores, bitSig, "BMS_ORIGEN");
//
//        int[] valores_0 = {0, 0, 0, 0, 0};
//        State BMS_destino = new State(localMasterAdmin, valores_0, bitSig, "BMS_DESTINO");
//
//        HashMap<String, State> allComponents = new HashMap<>();
//        allComponents.put(BMS_origen.getID(), BMS_origen);
//        LinkedList<State> listAllStates = new LinkedList<>();
//        listAllStates.add(BMS_origen);
//
//
//        char baseHeader = 'A';
//        int msgLimitSize = 24; // Bits
//        Initializer initializer = new Initializer(listAllStates, msgLimitSize, baseHeader);
//        initializer.genMessages();
//
//        System.out.println(BMS_origen.printMessagesWithIndexes());
//
//        //ReceiverAdmin receiverAdmin = new ReceiverAdmin(1, xbeeReceiver,);
//
//    }
//
//    @org.junit.jupiter.api.Test
//    void testGenMessagesWithJustOneComponent2() throws Exception{
//        XbeeReceiver xbeeReceiver = new XbeeReceiver();
//        XbeeSender xbeeSender = new XbeeSender(xbeeReceiver); // TODO: DELETE DESPUES DE TEST EXITOSO, ESTE ES EL PUENTE
//        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender);
//        LocalMasterAdmin localMasterAdmin = new LocalMasterAdmin(false);
//
//        // 2 4 8 16 32 64 128 256 512 1024 2048
//        // 1 2 3 4  5  6  7   8   9   10   11
//        int[] valores = {200, 189, 0, 111, 30};
//        int[] bitSig = {8, 8, 2, 8, 5};
//        State BMS_origen = new State(senderAdmin, valores, bitSig, "BMS_ORIGEN");
//
//        int[] valores1 = {30,26,34};
//        int[] bitSig1 = {5,6,6};
//        State MPPT1_origen = new State(senderAdmin, valores1, bitSig1, "MPPT1");
//
//        int[] valores2 = {10, 20, 30};
//        int[] bitSig2 = {5,6,6};
//        State MPPT2_origen = new State(senderAdmin, valores2, bitSig2, "MPPT2");
//
//
//        LinkedList<State> listAllStates = new LinkedList<>();
//        listAllStates.add(BMS_origen);
//        listAllStates.add(MPPT1_origen);
//        listAllStates.add(MPPT2_origen);
//
//        char baseHeader = 'A';
//        int msgLimitSize = 24; // Bits
//        Initializer initializer = new Initializer(listAllStates, msgLimitSize, baseHeader);
//        initializer.genMessages();
//
//        for (State c: listAllStates
//             ) {
//            System.out.println(c.toString());
//            System.out.println(c.printMessagesWithIndexes());
//        }
//
//        //ReceiverAdmin receiverAdmin = new ReceiverAdmin(1, xbeeReceiver,);
//
//    }
//
//}