package Configuration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase que toma un archivo tipo state.json de Vuex, y lo transforma en componentes del Protocolo.
 * El nombre del atributo en el JSON será el nombre del Componente en Java.
 * IMPORTANTE! Cada valor de los componentes tiene que tener un valor de referencia para inferir cantidad de decimales necesarios!!
 */
public class JSONtoComponent {

    public static void main(String[] args) {
        JSONParser jsonParser = new JSONParser();

        System.out.println(new File(".").getAbsolutePath());

        try (FileReader reader = new FileReader("src/Configuration/state.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            //JSONArray employeeList = (JSONArray) obj;
            System.out.println(obj);
            JSONObject maindata = (JSONObject) obj;
            System.out.println(maindata.get("mainData"));
            //Iterate over employee array
            //employeeList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseEmployeeObject(JSONObject employee)
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) employee.get("employee");

        //Get employee first name
        String firstName = (String) employeeObject.get("firstName");
        System.out.println(firstName);

        //Get employee last name
        String lastName = (String) employeeObject.get("lastName");
        System.out.println(lastName);

        //Get employee website name
        String website = (String) employeeObject.get("website");
        System.out.println(website);
    }

}
