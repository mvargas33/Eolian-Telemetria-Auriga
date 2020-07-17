package ApplicationLayer.LocalServices;


//import com.sun.org.apache.xpath.internal.operations.String;
import ApplicationLayer.AppComponent;
import PresentationLayer.Packages.Components.Component;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import io.socket.client.Socket;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

import java.util.Arrays;


/**
 * Se encarga de enviar la información de los componentes al servidor web para su visualización.
 * Para ello se basa en el sistema de eventos + JSONs de Websockets.
 * Debe haber un listener para cada AppComponent, con nombre ID del AppComponent.
 */
public class WebSocketService extends Service{
    SocketIOServer server; // Actuamos como servidor, Cliente es la App en Vuejs
    Configuration config;

    /**
     * Constructor con valores default de parámetros
     */
    public WebSocketService(){
        super();
        this.config = new Configuration();
        this.config.setHostname("localhost");
        this.config.setPort(3000);
        this.server = new SocketIOServer(config);
    }

    /**
     * Constructor con parámetros no default
     * @param PORT Puerto del hostname
     * @param HOSTNAME Host a donde enviar eventos
     */
    public WebSocketService(int PORT, String HOSTNAME) {
        super();
        this.config = new Configuration();
        this.config.setHostname(HOSTNAME);
        this.config.setPort(PORT);
        this.server = new SocketIOServer(config);
        this.server.start();
    }

    /**
     * WebScoketService manda eventos del estilo ("BMS", {"name": "BMS", "data": [ ... ]})
     * Al host donde se deberán escuchar los eventos de nombre de cada componente
     * @param c AppComponent a consumir
     */
    @Override
    void serve(AppComponent c) {
        try {
            server.getBroadcastOperations().sendEvent(c.getName(), c.getMyJSON()); // Enviar evento a WebSocket del componente específico
        }catch (Exception e){
            e.printStackTrace(); // Sólo se hace print, el sistema no se puede caer
        }
    }
}
