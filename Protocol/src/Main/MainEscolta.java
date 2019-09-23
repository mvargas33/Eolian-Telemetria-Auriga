package Main;

import Protocol.Receiving.ReceiverAdmin;
import Protocol.Receiving.XbeeReceiver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MainEscolta {
    public static void main(String args[]) {
        int consumers = 50;
        BlockingQueue<byte[]> sharedQueue = new LinkedBlockingQueue<>();     // Buffer

        ExecutorService pes = Executors.newFixedThreadPool(1);      // One producer only: XbeeReceiver
        ExecutorService ces = Executors.newFixedThreadPool(consumers);       // Multiple consumers: ReceiverAdmin

        XbeeReceiver xbeeReceiver = new XbeeReceiver(sharedQueue);           // Main producer
        pes.submit(xbeeReceiver);                                            // Submit task .run()
        for(int i = 0; i < consumers; i++){
            ces.submit(new ReceiverAdmin(sharedQueue, i));                      // Consumers & submit task .run()
        }


        pes.shutdown();
        ces.shutdown();

    }
}
