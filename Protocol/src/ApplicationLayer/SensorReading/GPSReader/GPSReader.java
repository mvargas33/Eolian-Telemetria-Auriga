package ApplicationLayer.SensorReading.GPSReader;

import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.SensorReading.SensorsReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;

public class GPSReader extends SensorsReader {
    //temporal, por ahora solo va a leer longitud y latitud
    private double longitud;
    private double latitud;
    private int lat_dir; // 0 == N (North), 1 == S (South)
    private int long_dir; // 0 == E (East), 1 == W (West)
    private double[] values = new double[2]; //por ahora solo guarda 2 valores, values[0] = lat y values[1] = long

    public GPSReader(AppSender myComponent, long readingDelayInMS) {
        super(myComponent, readingDelayInMS);
    }

    public void GLLreader(String[] msg) {
        //aca habria que agregar bloqueadores en toda la operacion porque es una seccion critica
        //referencia (ojo que esta corrida la nomenclatura en el link) https://gpsd.gitlab.io/gpsd/NMEA.html#_gll_geographic_position_latitudelongitude
        //por ahora esto sería t_odo
        //esto requiere setters? todo: setters
        latitud = Double.parseDouble(msg[1]);
        switch (msg[2]) {
            case "N":
                lat_dir = 0;
                break;
            case "S":
                lat_dir = 1;
                break;
            default:
                System.out.println("ERROR: Valor "+msg[2]+" no identificado como direccion de latitud.");
        }
        longitud = Double.parseDouble(msg[3]);
        switch (msg[4]) {
            case "E":
                long_dir = 0;
                break;
            case "W":
                long_dir = 1;
                break;
            default:
                System.out.println("ERROR: Valor "+msg[4]+" no identificado como direccion de longitud.");
                // todo: no se si un mensaje de aviso basta o es mejor tirar un error.
        }
    }

    void readMessage(String message) {
        String[] msg = message.split(",");
        switch (msg[0]) {
            // esto se podria hacer con una clase 'NMEAsentenceReader' implementada para cada tipo de mensaje, pero no se
            // que tanto valga la pena, primero se planea implementar asi, luego evaluar si es mejor abstraerlo mas.
            case "$GPGLL":
                GLLreader(msg);
                break;
            case "$GPMDA":
                //GPMDAreader
                break;
            case "$GPMWV":
                //GPMWVreader
                break;
            case "$GPRMC":
                //GPRMCreader
                break;
            case "$GPTLL":
                //GPTLLreader
                break;
            case "$GPVBW":
                //GPVBWreader
                break;
            default:
                System.out.println("Mensaje, "+msg[0]+" no soportado para lectura");
                break;
        }
    }

    void startReading() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "echo 'Hello World'");

        ArrayList<String> msgs = new ArrayList<String>();

        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                readMessage(line);
            }

            //para ver si termino
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Se cierra la lectura.");
                System.exit(0);
            } else {
                //abnormal...
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double[] read() {
        values[0] = latitud;
        values[1] = longitud;
        return values;
    }
}
