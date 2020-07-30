package ApplicationLayer.SensorReading.CANReaders;

import ApplicationLayer.AppComponents.AppSender;

/**
 * Clase específica para leer datos del motor por el Bus CAN
 */
public class MotorReader extends CANReader{

    public MotorReader(AppSender myComponent, long readingDelayInMS, CANReaderCoordinator myCoordinator) {
        super(myComponent, readingDelayInMS, myCoordinator);
    }

    @Override
    public double[] actualRead() {
        return new double[0];
    }
}
