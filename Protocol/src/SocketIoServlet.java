import io.socket.emitter.Emitter;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoServerOptions;
import io.socket.socketio.server.SocketIoNamespace;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/socket.io/*")
public class SocketIoServlet extends HttpServlet {
    private final EngineIoServerOptions engineIoServerOptions = EngineIoServerOptions.newFromDefault();
    private final EngineIoServer mEngineIoServer = new EngineIoServer();
    private final SocketIoServer mSocketIoServer = new SocketIoServer(mEngineIoServer);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mEngineIoServer.handleRequest(request, response);
    }

    public void x(){
        SocketIoNamespace namespace = mSocketIoServer.namespace("/");

        namespace.on("connection", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                SocketIoSocket socket = (SocketIoSocket) args[0];
                // Do something with socket
            }
        });

        // Attaching to 'foo' event
        namespace.on("foo", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Arugments from client available in 'args'
            }
        });

        while(true){
            // Broadcasting event 'foo' with args 'bar arg' to room 'room'
            namespace.broadcast("room", "foo", "bar arg");
        }

    }

    public static void main(String[] args) {
        SocketIoServlet socketIoServlet = new SocketIoServlet();
        socketIoServlet.x();

    // Do something with namespace
    }
}