package ApplicationLayer.SensorReading.GPSReader;

import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.SensorReading.SensorsReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;

public class GPSReader extends SensorsReader {

    public GPSReader(AppSender myComponent, long readingDelayInMS) {
        super(myComponent, readingDelayInMS);
    }

    void readMessage(String message) {
        String[] msg = message.split(",");
        switch (msg[0]) {
            // esto se podria hacer con una clase 'NMEAsentenceReader' implementada para cada tipo de mensaje, pero no se
            // que tanto valga la pena, primero se planea implementar asi, luego evaluar si es mejor abstraerlo mas.
            case "$GPGLL":
                //GPGLLreader
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
                readMessage(line)
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
        return new double[0];
    }
}
