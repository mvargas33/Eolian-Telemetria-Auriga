package SensorReading;

import Components.Component;
import SensorReading.SensorsReader;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

public class ArduinoReader extends SensorsReader implements SerialPortEventListener {
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


    public ArduinoReader(HashMap<String, Component> allComponents, LinkedList<Component> componentLinkedList) {
        super(allComponents, componentLinkedList);
    }

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
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);
            //serialPort.disableReceiveTimeout();
            //serialPort.enableReceiveThreshold(1);
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

    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    @Override
    public void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine=null;
                while (input.ready()) {
                    //byte[] buff = new char[400];
                    //buffer += (char) input.read();
                    //input.read(buff);
                    //if(index == buffer.length - 1)
                    //    index = 0;
                    //buffer[index] = (char) input.read();
                    //index++;
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

                    //System.out.println(stringSplit.toString());

                    //byte[] b = inputLine.getBytes();

                    //System.out.println("START " + BitOperations.ArraytoString(b) + " END");
                    //System.out.println(buff);
                    //System.out.println(inputLine);
                }
/*
                    System.out.print("START ");
                    for (char c : buffer
                         ) {
                        //System.out.print(c.toBinaryString());
                        System.out.print(" ");
                    }
                    //System.out.print(buffer);
                    System.out.println("END");
*/


            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    /**
     * Debe estar siempre antento al puerto serial, leer y actualizar componentes
     */
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
