package Test;

import Components.BMS;
import Components.Component;
import LocalSystems.LocalMasterAdmin;
import Protocol.Initializer.Initializer;
import Protocol.Receiving.ReceiverAdmin;
import Protocol.Receiving.XbeeReceiver;
import Protocol.Sending.SenderAdmin;
import Protocol.Sending.XbeeSender;
import SensorReading.SensorsReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class InitializerTest {

    @org.junit.jupiter.api.Test
    void testGenMessagesWithJustOneComponent() throws Exception{

        XbeeReceiver xbeeReceiver = new XbeeReceiver();
        XbeeSender xbeeSender = new XbeeSender(xbeeReceiver); // TODO: DELETE DESPUES DE TEST EXITOSO, ESTE ES EL PUENTE

        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender);

        LocalMasterAdmin localMasterAdmin = new LocalMasterAdmin();

        // 2 4 8 16 32 64 128 256 512 1024 2048
        // 1 2 3 4  5  6  7   8   9   10   11
        int[] valores = {200, 189, 0, 111, 30};
        int[] bitSig = {8, 8, 2, 8, 5};
        Component BMS_origen = new BMS(senderAdmin, valores, bitSig, "BMS_ORIGEN");

        int[] valores_0 = {0, 0, 0, 0, 0};
        Component BMS_destino = new BMS(localMasterAdmin, valores_0, bitSig, "BMS_DESTINO");

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

}