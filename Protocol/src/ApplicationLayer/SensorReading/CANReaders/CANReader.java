package ApplicationLayer.SensorReading.CANReaders;

import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.SensorReading.SensorsReader;

/**
 * Clase que lee componentes del vehículo mediante interfaz CAN-Serial en el puerto USB
 */
public abstract class CANReader extends SensorsReader {
    private final CANReaderCoordinator myCoordinator;
    public double[] newvalues;

    public CANReader(AppSender myComponent, long readingDelayInMS, CANReaderCoordinator myCoordinator) {
        super(myComponent, readingDelayInMS);
        this.myCoordinator = myCoordinator;
        this.newvalues = null;
    }

    /**
     * Método que ejecuta la lectura real usando el bus CAN en forma exclusiva
     * @return valores actualizados del componente asociado
     */
    public abstract double[] actualRead();

//    @Override
//    public double[] read() {
//        myCoordinator.enQueueForBUS(this);
//        while(newvalues == null){
//            // TODO, EN VEZ DE BUSY WAITING, USAR ASYNC FUNCTIONS
//        }
//        // Ya tengo valores nuevos
//
//    }
}
