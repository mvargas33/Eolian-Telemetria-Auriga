package Test;

import Components.Component;
import LocalSystems.LocalMasterAdmin;
import Protocol.Initializer.Initializer;
import Protocol.Receiving.XbeeReceiver;
import Protocol.Sending.SenderAdmin;
import Protocol.Sending.XbeeSender;
import SensorReading.SensorsReader;

import java.util.HashMap;
import java.util.LinkedList;

class InitializerTest {

    @org.junit.jupiter.api.Test
    void testGenMessagesWithJustOneComponent1() throws Exception{
        XbeeReceiver xbeeReceiver = new XbeeReceiver();
        XbeeSender xbeeSender = new XbeeSender(xbeeReceiver); // TODO: DELETE DESPUES DE TEST EXITOSO, ESTE ES EL PUENTE
        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender);
        LocalMasterAdmin localMasterAdmin = new LocalMasterAdmin();

        // 2 4 8 16 32 64 128 256 512 1024 2048
        // 1 2 3 4  5  6  7   8   9   10   11
        int[] valores = {200, 189, 0, 111, 30};
        int[] bitSig = {8, 8, 2, 8, 5};
        Component BMS_origen = new Component(senderAdmin, valores, bitSig, "BMS_ORIGEN");

        int[] valores_0 = {0, 0, 0, 0, 0};
        Component BMS_destino = new Component(localMasterAdmin, valores_0, bitSig, "BMS_DESTINO");

        HashMap<String, Component> allComponents = new HashMap<>();
        allComponents.put(BMS_origen.getID(), BMS_origen);
        LinkedList<Component> listAllComponents = new LinkedList<>();
        listAllComponents.add(BMS_origen);

        SensorsReader sensorsReader = new SensorsReader(allComponents);

        char baseHeader = 'A';
        int msgLimitSize = 24; // Bits
        Initializer initializer = new Initializer(listAllComponents, msgLimitSize, baseHeader);
        initializer.genMessages();

        System.out.println(BMS_origen.printMessagesWithIndexes());

        //ReceiverAdmin receiverAdmin = new ReceiverAdmin(1, xbeeReceiver,);

    }

    @org.junit.jupiter.api.Test
    void testGenMessagesWithJustOneComponent2() throws Exception{
        XbeeReceiver xbeeReceiver = new XbeeReceiver();
        XbeeSender xbeeSender = new XbeeSender(xbeeReceiver); // TODO: DELETE DESPUES DE TEST EXITOSO, ESTE ES EL PUENTE
        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender);
        LocalMasterAdmin localMasterAdmin = new LocalMasterAdmin();

        // 2 4 8 16 32 64 128 256 512 1024 2048
        // 1 2 3 4  5  6  7   8   9   10   11
        int[] valores = {200, 189, 0, 111, 30};
        int[] bitSig = {8, 8, 2, 8, 5};
        Component BMS_origen = new Component(senderAdmin, valores, bitSig, "BMS_ORIGEN");

        int[] valores1 = {30,26,34};
        int[] bitSig1 = {5,6,6};
        Component MPPT1_origen = new Component(senderAdmin, valores1, bitSig1, "MPPT1");

        int[] valores2 = {10, 20, 30};
        int[] bitSig2 = {5,6,6};
        Component MPPT2_origen = new Component(senderAdmin, valores2, bitSig2, "MPPT2");


        LinkedList<Component> listAllComponents = new LinkedList<>();
        listAllComponents.add(BMS_origen);
        listAllComponents.add(MPPT1_origen);
        listAllComponents.add(MPPT2_origen);

        char baseHeader = 'A';
        int msgLimitSize = 24; // Bits
        Initializer initializer = new Initializer(listAllComponents, msgLimitSize, baseHeader);
        initializer.genMessages();

        for (Component c: listAllComponents
             ) {
            System.out.println(c.toString());
            System.out.println(c.printMessagesWithIndexes());
        }

        //ReceiverAdmin receiverAdmin = new ReceiverAdmin(1, xbeeReceiver,);

    }

}