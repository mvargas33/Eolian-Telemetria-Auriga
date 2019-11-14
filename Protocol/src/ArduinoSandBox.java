import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import Utilities.BitOperations;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;
import java.util.LinkedList;

/**
 * Clase que busca implementar una correcta comunicación Arduino-Java
 */
public class ArduinoSandBox implements SerialPortEventListener{

        SerialPort serialPort;
        /** The port we're normally going to use. */
        private static final String[] PORT_NAMES = {                  "/dev/tty.usbserial-A9007UX1", // Mac OS X
                "/dev/ttyUSB0", // Linux
                "COM7", // Windows
        };
        private BufferedReader input;
        private OutputStream output;
        private static final int TIME_OUT = 2000;
        private static final int DATA_RATE = 9600;
        private char[] buffer;
        private int index;

        public void initialize() {
            buffer = new char[512*4*2+4];
            index = 0;
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

        public synchronized void serialEvent(SerialPortEvent oEvent) {
            if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    String inputLine=null;
                    while (input.ready()) {
                        //byte[] buff = new char[400];
                        //buffer += (char) input.read();
                        //input.read(buff);
                        if(index == buffer.length - 1)
                            index = 0;
                        buffer[index] = (char) input.read();
                        index++;
                        //inputLine = input.readLine();
                        //byte[] b = inputLine.getBytes();

                        //System.out.println("START " + BitOperations.ArraytoString(b) + " END");
                        //System.out.println(buff);
                        //System.out.println(inputLine);
                    }

                    System.out.print("START ");
                    for (char c : buffer
                         ) {
                        //System.out.print(c.toBinaryString());
                        System.out.print(" ");
                    }
                    //System.out.print(buffer);
                    System.out.println("END");



                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
            // Ignore all the other eventTypes, but you should consider the other ones.
        }

        public static void main(String[] args) throws Exception {
            ArduinoSandBox main = new ArduinoSandBox();
            main.initialize();
            Thread t=new Thread() {
                public void run() {
                    //the following line will keep this app alive for 1000    seconds,
                    //waiting for events to occur and responding to them    (printing incoming messages to console).
                    try {Thread.sleep(1000000);} catch (InterruptedException    ie) {}
                }
            };
            t.start();
            System.out.println("Started");
        }
    }


