package LocalSystems.ServerAdmin;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Arrays;


/*
    Se encarga de enviar la información de los componentes al servidor web para su visualización.
    Esta atento a los eventos (heads-up) de los componentes para enviar información actualizada.
 */
public class ServerAdmin {

    public static void main(String[] args) {
        String componentID = "BMS";
        double[] valores = {1.23, 2.33, 3, 4, 52.33, 6, 7.2, 8, 9.13};

        JSONObject json = new JSONObject();
        json.put("componente", componentID);
        json.put("valores", Arrays.toString(valores));

        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        try {

            HttpPost request = new HttpPost("http://localhost:3000/update");
            StringEntity params =new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.print(response.toString());

            //handle response here...

        }catch (Exception ex) {

            //handle exception here

        } finally {
            //Deprecated
            // httpClient.getConnectionManager().shutdown();
        }
    }
}
