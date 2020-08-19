package Test.UnitTests;

import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;

import java.util.Arrays;

/**
 * Clase que comprueba empíricamente la capacidad de transmisión de las Xbee en API mode
 */
public class StressXbee {

    /**
     * Clase que implementa el método de recepción de mensajes en la Xbee destino
     */
    public static class MyStressListener implements IDataReceiveListener {

        /**
         * Recepción consta de un print
         * @param xbeeMessage : Mensaje recibido
         */
        @Override
        public void dataReceived(XBeeMessage xbeeMessage) {
            /*
            System.out.format("From %s >> %s | %s%n", xbeeMessage.getDevice().get64BitAddress(),
                    HexUtils.prettyHexString(HexUtils.byteArrayToHexString(xbeeMessage.getData())),
                    new String(xbeeMessage.getData()));

             */
        }
    }

    /**
     * Programa principal que hará pruebas de stress
     * @param args : Argumentos del programa
     * @throws Exception : Excepcipon por errores de las Xbees
     */
    public static void main(String ... args) throws Exception {
        long start = System.currentTimeMillis();

        // Constantes
        String PORT_RECEIVE = "COM4";
        String PORT_SEND = "COM3";
        int BAUD_RATE = 230400;

        // ------------------- RECEIVER ------------------- //
        /*XBeeDevice myDeviceR = new XBeeDevice(PORT_RECEIVE, BAUD_RATE);

        // Esto abre la Xbee y añade el Listener, nada más, la deja abierta cosa que cada vez que llegue un mensaje, se activa el listener
        try {
            myDeviceR.open();
            myDeviceR.addDataListener(new MyStressListener());
            System.out.println("Xbee destino inicializada correctamente ...");
        } catch (XBeeException e) {
            e.printStackTrace();
            System.exit(1);
        }*/

        // ------------------- SENDER ------------------- //
        XBeeDevice myDevice; // Xbee para envío de bytes
        myDevice = new XBeeDevice(PORT_SEND, BAUD_RATE);
        //this.REMOTE_NODE_IDENTIFIER = REMOTE_NODE_IDENTIFIER;
        myDevice.open(); // Esto se hace acá para hacerlo una sola vez, mejora la eficiencia de envío de mensajes


        // Stress TEST
        int intentos_por_largo = 1000;
        int intento_actual = 0;
        int largo_actual = 114; // en Bytes
        int largo_max = 114; // MAX VALOR POSIBLE, SINO XBEE TIRA ERROR: Error: Data payload too large (0x74)

        double[] promedio_mensajes_sec = new double[largo_max];
        long initial_time ;
        long last_time ;

        String str_data = "";
        for (int i = 0; i < largo_max; i++){
            str_data += "0";
        }

        byte[] data = str_data.getBytes();

        // El mensaje crece cada vez
        while (largo_actual <= largo_max) {
            System.out.println("Largo de mensaje actual: " + largo_actual);
            initial_time = System.currentTimeMillis();

            while (intento_actual < intentos_por_largo) {

                try {
                    myDevice.sendBroadcastData(data);
                } catch (XBeeException e) {
                    System.out.println("Error al enviar mensaje a Xbee Destino");
                    e.printStackTrace();
                    System.exit(1);
                }
                intento_actual++;
            }
            last_time = System.currentTimeMillis();

            promedio_mensajes_sec[largo_actual-1] = intentos_por_largo / ((double) (last_time  - initial_time) / 1000.0); // # mensajes / tiempo trnascurrido
            System.out.println("Mensajes por seguno para largo " + largo_actual + " : " + promedio_mensajes_sec[largo_actual - 1]);
            intento_actual = 0;
            largo_actual++;
            str_data += "0";
            data = str_data.getBytes();
        }

        System.out.println("Promedio de mensajes/segundo : ");
        System.out.println(Arrays.toString(promedio_mensajes_sec));

        double[] bytes_sec = new double[largo_max];

        for (int i = 0; i < promedio_mensajes_sec.length; i++){
            bytes_sec[i] = promedio_mensajes_sec[i] * (i + 1); // # mensajes * largo / segundos = # bytes / segundo
        }

        System.out.println("Promedio de bytes/segundo : ");
        System.out.println(Arrays.toString(bytes_sec));


        System.out.print("Size| Bytes/s\n");
        for(int j = 0; j < bytes_sec.length; j++){
            System.out.print(j+1);System.out.print("\t");System.out.println(bytes_sec[j]);
        }


        System.out.print("Tiempo total transcurrido en segundos: " + ((System.currentTimeMillis() - start) / 1000.0));
        System.exit(0);

    }




}
