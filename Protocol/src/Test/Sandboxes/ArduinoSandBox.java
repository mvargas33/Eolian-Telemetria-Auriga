package Test.Sandboxes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.*;

/**
 * Clase que busca implementar una correcta comunicación Arduino-Java
 */
public class ArduinoSandBox implements SerialPortEventListener{

    SerialPort serialPort;
    /** The port we're normally going to use. */
    private static final String[] PORT_NAMES = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyUSB0", // Linux
            "COM7", // Windows
    };
    private BufferedReader input;
    private OutputStream output;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;

    private ArrayList<String> buffer;
    private String header;

    /**
     * Abre el puerto serial con todas las configuraciones señaladas
     */
    public void initialize() {
            buffer = new ArrayList<>();
            header = "";
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
                serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
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
     * Cierra el puerto serial abierto
     */
    public synchronized void close() {
            if (serialPort != null) {
                serialPort.removeEventListener();
                serialPort.close();
            }
        }

        public synchronized void serialEvent(SerialPortEvent oEvent) {
            if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    String inputLine=null;
                    while (input.ready()) {

                        inputLine = input.readLine();

                        int pos = 0;
                        int end = inputLine.indexOf(' ', pos);
                        header = inputLine.substring(pos, end);
                        pos = end + 1;

                        if(header.equals("BMS_ORIGEN")){
                            while ((end = inputLine.indexOf(' ', pos)) >= 0) {
                                buffer.add(inputLine.substring(pos, end));
                                pos = end + 1;
                            }
                        }

                        double[] values = new double[buffer.size()];
                        for (int i=0;i<buffer.size();i++){
                            values[i] = Double.parseDouble(buffer.get(i));
                        }
                        System.out.println(Arrays.toString(values));

                        buffer.clear();

                    }

                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
            // Ignore all the other eventTypes, but you should consider the other ones.
        }

        public static void main(String[] args) throws Exception {
            ArduinoSandBox main = new ArduinoSandBox();
            main.initialize();
            Thread t= new Thread(() -> {
                //the following line will keep this app alive for 1000    seconds,
                //waiting for events to occur and responding to them    (printing incoming messages to console).
                try {Thread.sleep(1000000);} catch (InterruptedException    ie) {}
            });
            t.start();
            System.out.println("Started");
        }
    }


