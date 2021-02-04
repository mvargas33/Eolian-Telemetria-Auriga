package ApplicationLayer.LocalServices;

import ApplicationLayer.AppComponents.AppComponent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Se encarga de guardar la información de los componentes a la base de datos.
 * Debe haber una tabla para cada AppComponent en la base de datos.
 * Hace insert de los datos actuales con un timestamp.
 */
public class DatabaseService extends Service implements Runnable {

    public String absolute_path; // path to /data folder
    public String[] components;
    public String date_dir; // path to /data/{date} folder

    public DatabaseService() {
        super();

        absolute_path = System.getProperty("user.dir") + "\\data";

        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        Date date = new Date();


        date_dir = absolute_path + "\\" + formatter.format(date);

        // creates a file object with specified path
        File file = new File(date_dir);

        /*
        iniciar los .csv de todos los componentes o esperar a que llamen initDataLog

         */
    }

    /**
     * Debe tomar el AppComponent y guardar sus valores en su tabla respectiva en la base de datos
     * Debe guardar el timestamp también
     *
     * @param c AppComponent a guardar en la base de datos
     */
    @Override
    protected void serve(AppComponent c) {
        try {
            // INSERT double[] en su tabla, sacar el timestamp del momento en que guarda
            // en este punto ya se debio haber llamado a initDataLog con los argumentos correspondientes
            writeValues(c.getValoresRealesActuales(), c.getID());

        } catch (Exception e) {
            e.printStackTrace(); // Sólo se hace print, el sistema no se puede caer
        }
    }

    /**
     * Inicia un .csv en la carpeta "data/date/" con el nombre {ID}.csv. Le agrega una fila con los valores de values.
     *
     * @param values Valores a agregar, en general deberían ser headers.
     * @param ID     Nombre del archivo de salida
     * @throws IOException
     */
    public void initDataLog(String[] values, String ID) throws IOException { //ver cuando llamar a initDataLog
        String fileName = date_dir + "\\" + ID + ".csv"; // el filename sera el mismo id?;
        FileWriter fileWriter = new FileWriter(fileName, true); // append = true
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.print("TIMESTAMP,");

        for (int i = 0; i < values.length - 1; i++) {
            printWriter.printf("%s,", values[i]);
        }
        printWriter.printf("%s\n", values[values.length - 1]);

        printWriter.close();
    }

    /**
     * Añade una fila a ID.csv con los valores de "values".
     * @param values Valores a agregar.
     * @param ID Nombre del archivo a modificar.
     * @throws IOException
     */
    public void writeValues(double[] values, String ID) throws IOException {
        String fileName = date_dir+"\\"+ID+".csv"; // el filename sera el mismo id?;
        FileWriter fileWriter = new FileWriter(fileName, true); // append = true
        PrintWriter printWriter = new PrintWriter(fileWriter);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss"); //esto hay que revisarlo en concreto con la bd
        Date date = new Date();
        printWriter.print(formatter.format(date)+",");

        for(int i = 0; i < values.length-1; i++) {
            printWriter.printf("%f,", values[i]);
        }
        printWriter.printf("%f\n", values[values.length-1]);

        printWriter.close();
    }
}
