package ApplicationLayer.SensorReading.CANReaders;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Clase para coordinar acceso a BUS CAN entre todos los componentes
 */
public class CANReaderCoordinator implements Runnable
{
    BlockingQueue<CANReader> FIFOReaders;

    public CANReaderCoordinator(){
        this.FIFOReaders = new LinkedBlockingQueue<>();
    }

    public void enQueueForBUS(CANReader canReader){
        try {
            this.FIFOReaders.put(canReader);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Double dispatch. Método que actúa como mutex de BUS CAN
     * @param canReader Reader que actuará ahora
     * @return valores nuevos de la lectura del canReader
     */
    public synchronized void accessCANAndRead(CANReader canReader){
        canReader.read();
    }


    @Override
    public void run() {
        while(true){
            while(!FIFOReaders.isEmpty()){
                accessCANAndRead(this.FIFOReaders.poll());
            }
        }
    }
}
