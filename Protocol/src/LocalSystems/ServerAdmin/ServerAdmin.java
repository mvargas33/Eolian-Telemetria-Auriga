package LocalSystems.ServerAdmin;


import Components.Component;
import com.sun.org.apache.xpath.internal.operations.String;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

import java.util.Arrays;


/*
    Se encarga de enviar la información de los componentes al servidor web para su visualización.
    Esta atento a los eventos (heads-up) de los componentes para enviar información actualizada.
 */
public class ServerAdmin {
    private JSONObject json;
    private HttpPost request;


    public ServerAdmin(java.lang.String serverDestinationURL) throws Exception{
        this.json = new JSONObject(); // JSON de envío ed datos
        this.request = new HttpPost(serverDestinationURL);
        this.request.addHeader("content-type", "application/json");
    }

    public void sendToServer(java.lang.String ComponentName, int[] values) throws Exception{
        HttpClient httpClient = HttpClientBuilder.create().build(); // Se debe crear cada vez, porque causa conflicto si se reutiliza

        json.put("componente", ComponentName); // Update demensaje
        json.put("valores", Arrays.toString(values));

        request.setEntity(new StringEntity(json.toString())); // Se lo pasamos al objeto request
        httpClient.execute(request); // Se envía.

        //HttpResponse response = httpClient.execute(request); // Se envía. Sin handling de resonse  HttpResponse response =
        //System.out.print(response.toString());
    }


    public static void main(java.lang.String[] args) throws Exception {
        ServerAdmin serverAdmin = new ServerAdmin("http://localhost:3000/update");
        serverAdmin.sendToServer("BMS", new int[]{1, 2, 3, 4, 5});

        /*java.lang.String componentID = "BMS";
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
        }*/
    }
}
