package ApplicationLayer.SensorReading.CANReaders;

import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.SensorReading.SensorsReader;

/**
 * Clase que lee componentes del vehículo mediante interfaz CAN-Serial en el puerto USB
 */
public abstract class CANReader extends SensorsReader {
    private final CANReaderCoordinator myCoordinator;

    public CANReader(AppSender myComponent, long readingDelayInMS, CANReaderCoordinator myCoordinator) {
        super(myComponent, readingDelayInMS);
        this.myCoordinator = myCoordinator;
    }

    /**
     * Método que ejecuta la lectura real usando el bus CAN en forma exclusiva
     * @return valores actualizados del componente asociado
     */
    public abstract double[] actualRead();

    @Override
    public double[] read() {
        return myCoordinator.accessCANAndRead(this);
    }
}
