package Test;

import Components.BMS;
import Components.Component;
import LocalSystems.LocalMasterAdmin;
import Protocol.Initializer.Initializer;
import Protocol.Messages.Message;
import Protocol.Receiving.ReceiverAdmin;
import Protocol.Receiving.XbeeReceiver;
import Protocol.Sending.SenderAdmin;
import Protocol.Sending.XbeeSender;
import SensorReading.SensorsReader;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class NoXbeeSystemTest {

    @org.junit.jupiter.api.Test
    void noXbeeSendReceiveLinearTest() throws Exception{
        XbeeReceiver xbeeReceiver = new XbeeReceiver();
        XbeeSender xbeeSender = new XbeeSender(xbeeReceiver); // TODO: DELETE DESPUES DE TEST EXITOSO, ESTE ES EL PUENTE

        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender);

        // 2 4 8 16 32 64 128 256 512 1024 2048
        // 1 2 3 4  5  6  7   8   9   10   11
        int[] valores = {200, 189, 0, 111, 30};
        int[] bitSig = {8, 8, 2, 8, 5};
        Component BMS_origen = new BMS(senderAdmin, valores, bitSig, "BMS_ORIGEN");

        HashMap<String, Component> allComponents = new HashMap<>();
        allComponents.put(BMS_origen.getID(), BMS_origen);

        LinkedList<Component> listAllComponents_origen = new LinkedList<>();
        listAllComponents_origen.add(BMS_origen);

        SensorsReader sensorsReader = new SensorsReader(allComponents);

        char baseHeader = 'A';
        int msgLimitSize = 24 + 8; // Bits
        Initializer initializer_origen = new Initializer(listAllComponents_origen, msgLimitSize, baseHeader);

        initializer_origen.genMessages(); // Para enviar no se necesita HashMap de Messages

        System.out.println("Initializaer origen: ");
        System.out.println(BMS_origen.printMessagesWithIndexes());

        LocalMasterAdmin localMasterAdmin = new LocalMasterAdmin();

        int[] valores_0 = {0, 0, 0, 0, 0};
        int[] bitSig_0 = {8, 8, 2, 8, 5};
        Component BMS_destino = new BMS(localMasterAdmin, valores_0, bitSig_0, "BMS_DESTINO");

        LinkedList<Component> listAllComponents_destino = new LinkedList<>();
        listAllComponents_destino.add(BMS_destino);

        Initializer initializer_detino = new Initializer(listAllComponents_destino, msgLimitSize, baseHeader);
        HashMap<Character, Message> messages_destino = initializer_detino.genMessages();

        System.out.println("Initializaer destino: ");
        System.out.println(BMS_destino.printMessagesWithIndexes());

        ReceiverAdmin receiverAdmin = new ReceiverAdmin(1, xbeeReceiver, messages_destino);

        /* ----------------------------- TESTING -----------------------------*/

        sensorsReader.readBMSTest("BMS_ORIGEN", valores.length); // Lee, actualiza y deja en queue de sender admin

        System.out.println("Mensajes en cola de SenderAdmin: " + senderAdmin.messageQueueSize());

        while(!senderAdmin.isMessageQueueEmpty()){
            senderAdmin.putMessageInByteQueue(); // Saca de queue de Message y pone en queue de byte[] de Xbee Sender
            xbeeSender.sendByte(); // Envia byte[] a xbeeRecevier y queda en la queue de byte[] del xBeeReceiver
        }
        while(!xbeeReceiver.isQueueEmpty()) {
            receiverAdmin.consumeByteArrayFromQueue(); // Consume un Byte de la queue del xbeeReceiver, se ponen componentes actualizado en la queue de localmasteradmin
            localMasterAdmin.consumeComponent(); // Se muestra componente como llega
        }

    }

}