package ApplicationLayer.SensorReading.FenixArduinoReader;

import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.SensorReading.SensorsReader;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

public class ArduinoReader extends SensorsReader implements SerialPortEventListener {
    private SerialPort serialPort; /** The port we're normally going to use. */
    private String[] PORT_NAMES;
    private BufferedReader input;
    private OutputStream output;
    private int TIME_OUT; // Para buscar puerto
    private int DATA_RATE; // Baudrate

    private String inputLine; // Buffer RAW de lectura
    private ArrayList<String> buffer; // Aquí se ponen los siguientes split de números doubles
    private String header; // Lectura del primer split
    private State actualStateInRead; // Espacio para guardar el componente actual

    public ArduinoReader(AppSender myComponent, long readingDelayInMS, String PORT, int BAUD_RATE, int TIME_OUT) {
        super(myComponent, readingDelayInMS);
        this.PORT_NAMES = new String[]{
                "/dev/tty.usbserial-A9007UX1", // Mac OS X
                "/dev/ttyUSB0", // Linux
                PORT}; // COM7 Windows
        this.DATA_RATE = BAUD_RATE; // = 9600 default
        this.TIME_OUT = TIME_OUT; // = 2000 ms default
        buffer = new ArrayList<>();
        header = "";
        initialize(); // Abrir el puerto
    }

    /**
     * Inicializa y abre el puerto serial con las configuraciones señaladas
     */
    private void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("No se encuentra puerto COM");
            return;
        }

        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Cierra el puerto Serial
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
    // TODO: Refactor para nuevo modelo. No es prioridad aún

    @Override
    public double[] read() {
        return new double[0];
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {

    }

    /**
     * Listener de cuando hay datos en el bus Serial
     * @param oEvent : Evento
     */

    /*
    @Override
    public void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) { // Evento de data available
            try {
                inputLine = null;
                actualStateInRead = null;
                while (input.ready()) {
                    inputLine = input.readLine(); // Se lee la primera línea completa

                    int pos = 0;
                    int end = inputLine.indexOf(' ', pos); // Se usa indexOf() que tiene el mejor desempeño
                    header = inputLine.substring(pos, end); // Leer primer valor
                    pos = end + 1;

                    State c = allComponents.get(header);

                    if(c != null){ // Si leí un componente que existe acá también
                        System.out.println("Leído el componente: " + c.getID());
                        while ((end = inputLine.indexOf(' ', pos)) >= 0) { // Hacer split() hasta el final y poner en buffer
                            buffer.add(inputLine.substring(pos, end));
                            pos = end + 1;
                        }

                        // Se crea cada vez porque los componentes tienen tamaños de values diferentes
                        int[] values = new int[buffer.size()]; // TODO: cambiar a double[] y Double.parseDouble(), pero con escalamiento
                        for (int i=0;i<buffer.size();i++){
                            values[i] = Integer.parseInt(buffer.get(i));
                        }

                        if(values.length == c.getMyValues().length){ // Si el largo es el mismo
                            updateDirectly(c.getID(), values); // Hago update d evalores del componente, que actu;iza sus mensajes y se ponen en cola de envío
                        }
                        System.out.println(Arrays.toString(values));
                    }
                    buffer.clear(); // Limpiar la lista de Strings
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }*/

}
