package ExcelToAppComponent;

import ApplicationLayer.AppComponents.AppReceiver;
import ApplicationLayer.AppComponents.AppSender;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que toma los CSV desde un directorio y los transforma en lista de AppComponents dependiendo si son AppSenders
 * o AppReceivers. Los CSV deben seguir el formato correcto indicado en el README.md. La idea es que esta clase se use
 * desde los métodos de inicialización, para siempre inicilizar los AppComponents desde los últimos CSVs.
 */
public class CSVToAppComponent {

    private static final String COMMA_DELIMITER = ",";

    /**
     * Lista todos los archivos .CSV desde un directorio
     * @param directory Directorio base
     * @return Lista de nombre de archivos .csv
     */
    public static List<String> listFilesForFolder(String directory) {
        List<String> filenames = new LinkedList<String>();
        File folder = new File(directory);

        File[] list = folder.listFiles();
        if (list != null) {
            for (File fileEntry : list) {
                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry.getAbsolutePath());
                } else {
                    if (fileEntry.getName().contains(".csv"))
                        filenames.add(fileEntry.getName());
                }
            }
        }
        return filenames;
    }

    /**
     * Toma un nombre de archivo CSV y retorna una lista de listas de String, que son las filas del CSV
     * @param file_dir_name Nombre de archivo CSV. EJ: BMS.csv
     * @return Lista de Lista de Strings que son las filas del CSV
     */
    public static List<List<String>> readCSV(String file_dir_name){
        List<List<String>> records = new ArrayList<>();
        try {
            FileReader fd = new FileReader(file_dir_name);
            BufferedReader br = new BufferedReader(fd);
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }


    public static List<AppSender> CSVs_to_AppSenders(String directory){
        LinkedList<AppSender> list = new LinkedList<>();
        List<String> components = listFilesForFolder(directory);

        for (String comp : components){
            List<List<String>> values = readCSV(directory + "/" + comp);
            String[] params = Arrays.copyOf(values.get(0).toArray(), values.get(0).toArray().length, String[].class);
            String[] min_str = Arrays.copyOf(values.get(1).toArray(), values.get(1).toArray().length, String[].class);
            String[] max_str = Arrays.copyOf(values.get(2).toArray(), values.get(2).toArray().length, String[].class);
            double[] min = new double[min_str.length];
            double[] max = new double[max_str.length];
            for (int i = 0; i < min_str.length; i++) {
                min[i] = Double.parseDouble(min_str[i]);
                max[i] = Double.parseDouble(max_str[i]);
            }
            list.add(new AppSender(comp.split("\\.")[0], min, max, params));
        }

        return list;
    }

    public static List<AppReceiver> CSVs_to_AppReceivers(String directory){
        LinkedList<AppReceiver> list = new LinkedList<>();
        List<String> components = listFilesForFolder(directory);

        for (String comp : components){
            List<List<String>> values = readCSV(directory + "/" + comp);

            String[] params = Arrays.copyOf(values.get(0).toArray(), values.get(0).toArray().length, String[].class);
            String[] min_str = Arrays.copyOf(values.get(1).toArray(), values.get(1).toArray().length, String[].class);
            String[] max_str = Arrays.copyOf(values.get(2).toArray(), values.get(2).toArray().length, String[].class);
            double[] min = new double[min_str.length];
            double[] max = new double[max_str.length];
            for (int i = 0; i < min_str.length; i++) {
                min[i] = Double.parseDouble(min_str[i]);
                max[i] = Double.parseDouble(max_str[i]);
            }
            list.add(new AppReceiver(comp.split("\\.")[0] + "_R", min, max, params));
        }

        return list;
    }
}
