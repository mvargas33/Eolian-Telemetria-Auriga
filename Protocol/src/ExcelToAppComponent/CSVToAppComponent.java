package ExcelToAppComponent;

import ApplicationLayer.AppComponents.AppReceiver;
import ApplicationLayer.AppComponents.AppSender;

import java.util.LinkedList;
import java.util.List;

/**
 * Clase que toma los CSV desde un directorio y los transforma en lista de AppComponents dependiendo si son AppSenders
 * o AppReceivers. Los CSV deben seguir el formato correcto indicado en el README.md. La idea es que esta clase se use
 * desde los métodos de inicialización, para siempre inicilizar los AppComponents desde los últimos CSVs.
 */
public class CSVToAppComponent {

    public static List<AppSender> CSVs_to_AppSenders(String directory){
        LinkedList<AppSender> list = new LinkedList<>();

        return list;
    }

    public static List<AppReceiver> CSVs_to_AppReceivers(String directory){
        LinkedList<AppReceiver> list = new LinkedList<>();

        return list;
    }
}
