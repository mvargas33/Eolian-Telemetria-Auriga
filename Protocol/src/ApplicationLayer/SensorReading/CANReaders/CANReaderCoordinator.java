package ApplicationLayer.SensorReading.CANReaders;

/**
 * Clase para coordinar acceso a BUS CAN entre todos los componentes
 */
public class CANReaderCoordinator {

    /**
     * Double dispatch. Método que actúa como mutex de BUS CAN
     * @param canReader Reader que actuará ahora
     * @return valores nuevos de la lectura del canReader
     */
    public synchronized double[] accessCANAndRead(CANReader canReader){
        return canReader.read();
    }


}
