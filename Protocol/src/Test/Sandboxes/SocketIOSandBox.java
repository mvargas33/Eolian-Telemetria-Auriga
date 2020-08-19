package Test.Sandboxes;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.simple.JSONObject;

public class SocketIOSandBox {
    static private Socket socket;
    static final int PORT = 3000;
    static SocketIOServer server;
    static JSONObject json = new JSONObject(); // JSON de envío ed datos

    public static void main(String[] args) throws InterruptedException {
        Thread ts = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        ts.start();
        /*
        try {
            client();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/
    }

    public static void server() throws InterruptedException, UnsupportedEncodingException {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(PORT);
        server = new SocketIOServer(config);

        double[] data = {56, 3.4, 1.024, 33, 4.048, 3.876, 34.5, 27.9};
        //json.put("componente", "mainData"); // Update demensaje
        json.put("data", data);

        server.addEventListener("toServer", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                client.sendEvent("toClient", "server recieved " + data);
            }
        });

        server.addEventListener("message", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                client.sendEvent("toClient", "message from server " + data);
            }
        });

        server.start();
        while (true){
            server.getBroadcastOperations().sendEvent("hola", "hola front-end");
            server.getBroadcastOperations().sendEvent("mainData", json);
            Thread.sleep(1000);
        }
        //Thread.sleep(10000);
        //server.stop();
    }
    public static void client() throws URISyntaxException, InterruptedException {
        socket = IO.socket("http://localhost:" + PORT);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                socket.emit("toServer", "connected");
                socket.send("test");
            }
        });
        socket.on("toClient", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Client recievd : " + args[0]);

            }
        });
        socket.connect();
        while (!socket.connected())
            Thread.sleep(50);
        socket.send("another test");
        Thread.sleep(10000);
        socket.disconnect();
    }
}