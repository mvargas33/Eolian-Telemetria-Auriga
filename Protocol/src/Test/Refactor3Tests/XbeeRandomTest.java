package Test.Refactor3Tests;
import ApplicationLayer.AppComponents.AppComponent;
import ApplicationLayer.AppComponents.AppReceiver;
import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.LocalServices.PrintService;
import ApplicationLayer.LocalServices.WirelessService.WirelessReceiver;
import ApplicationLayer.LocalServices.WirelessService.WirelessSender;
import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving.XbeeReceiver;
import ApplicationLayer.SensorReading.RandomReaders.RandomReader;
import ApplicationLayer.SensorReading.SensorsReader;
import ApplicationLayer.SensorReading.SequentialReaderExecutor;
import ExcelToAppComponent.CSVToAppComponent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class XbeeRandomTest {
    XbeeReceiver xbeeReceiver;

    void receiverSetup() throws Exception {
        String dir = "src/ExcelToAppComponent/Eolian_fenix";
        List<AppReceiver> appReceivers = CSVToAppComponent.CSVs_to_AppReceivers(dir);

        // High Level Services
        PrintService printService = new PrintService();
        WirelessReceiver wirelessReceiver = new WirelessReceiver(appReceivers, "COM6");

        //this.xbeeReceiver = wirelessReceiver.getXbeeReceiver(); // Save globally to pass to xbeeSender

        for (AppComponent ac: appReceivers) {ac.subscribeToService(printService); }

        // Execute threads
        ExecutorService mainExecutor = Executors.newFixedThreadPool(2);

        // Init threads
        mainExecutor.submit(wirelessReceiver);
        mainExecutor.submit(printService);

        mainExecutor.shutdown();
    }

    /**
     * Execute afet recevierSetup due to xbeeReceiver to pass to xbeeSender (testing only)
     * @throws Exception
     */
    void senderSetup() throws Exception {
        String dir = "src/ExcelToAppComponent/Eolian_fenix";
        List<AppSender> appSenders = CSVToAppComponent.CSVs_to_AppSenders(dir);
        LinkedList<RandomReader> randomReaders = new LinkedList<>();

        // Random reader
        for (AppSender as: appSenders) {
            randomReaders.add(new RandomReader(as, 1000));
        }

        // Sequential executor of readers
        SequentialReaderExecutor sequentialReaderExecutor = new SequentialReaderExecutor();
        for (SensorsReader sr : randomReaders) { sequentialReaderExecutor.addReader(sr); }

        // High Level Services
        PrintService printService = new PrintService();
        WirelessSender wirelessSender = new WirelessSender(appSenders, "/dev/ttyUSB0");

        for (AppComponent ac: appSenders) {
            ac.subscribeToService(printService);
            ac.subscribeToService(wirelessSender);
        }

        // Execute threads
        ExecutorService mainExecutor = Executors.newFixedThreadPool(4);

        // Init threads
        mainExecutor.submit((wirelessSender.getXbeeSender()));  // XbeeSender thred
        mainExecutor.submit(wirelessSender);                    // serve(AppComponent) to putInXbeeQueue() thread

        mainExecutor.submit(sequentialReaderExecutor);
        mainExecutor.submit(printService);

        mainExecutor.shutdown();
    }


    public static void main(String[] args) throws Exception{
        XbeeRandomTest xbeeRandomTest = new XbeeRandomTest();
        String device = "PC"; // Use Raspberry otherwise

        if (device.equals("PC")){
            xbeeRandomTest.receiverSetup();
        }else if (device.equals("RASPBERRY")){
            xbeeRandomTest.senderSetup();
        }

    }

}
