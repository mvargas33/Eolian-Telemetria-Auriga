package Test.Sandboxes;

import java.util.concurrent.*;
import java.util.Random;

public class BlockingQueueSandbox {
    public static void main(String args[]) {
        // 0000  0001  0010  0011  0100  0101  0110  0111  1000  1001  1010  1011  1100  1101  1110  1111
        // 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F

        BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<>();

        ExecutorService pes = Executors.newFixedThreadPool(2);
        ExecutorService ces = Executors.newFixedThreadPool(2);

        pes.submit(new Producer(sharedQueue, 1));
        pes.submit(new Producer(sharedQueue, 2));
        for(int i = 0; i < 2; i++){
            ces.submit(new Consumer(sharedQueue, i));
        }

        pes.shutdown();
        ces.shutdown();

        //int[] a = {1,2,3,4,5};
        //System.out.println(ts(a));
        //calc(a);
        //System.out.println(ts(a));
    }

}

/* Different producers produces a stream of integers continuously to a shared queue, 
which is shared between all Producers and consumers */

class Producer implements Runnable {
    private final BlockingQueue<Integer> sharedQueue;
    private int threadNo;
    private Random random = new Random();
    public Producer(BlockingQueue<Integer> sharedQueue,int threadNo) {
        this.threadNo = threadNo;
        this.sharedQueue = sharedQueue;
    }
    @Override
    public void run() {
        // Test.Sandboxes.Producer produces a continuous stream of numbers for every 200 milli seconds
        while (true) {
            try {
                int number = random.nextInt(1000);
                System.out.println("Produced:" + number + ":by thread:"+ threadNo);
                sharedQueue.put(number);
                Thread.sleep(1000);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}
/* Different consumers consume data from shared queue, which is shared by both producer and consumer threads */
class Consumer implements Runnable {
    private final BlockingQueue<Integer> sharedQueue;
    private int threadNo;
    public Consumer (BlockingQueue<Integer> sharedQueue,int threadNo) {
        this.sharedQueue = sharedQueue;
        this.threadNo = threadNo;
    }
    @Override
    public void run() {
        // Test.Sandboxes.Consumer consumes numbers generated from Test.Sandboxes.Producer threads continuously
        while(true){
            try {
                int num = sharedQueue.take();
                System.out.println("Consumed: "+ num + ":by thread:"+threadNo);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}