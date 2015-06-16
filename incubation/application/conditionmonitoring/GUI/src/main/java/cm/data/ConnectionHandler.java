package cm.data;

import cm.communication.dto.SocketInfo;
import cm.communication.socket.SocketReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias
 * @since 16.06.2015.
 */
public class ConnectionHandler {

    private static ConnectionHandler instance;
    private List<SocketReceiver> connections;


    private ConnectionHandler() {
        connections = new ArrayList<>();
    }

    public static ConnectionHandler getInstance() {
        if (instance == null) {
            instance = new ConnectionHandler();
        }
        return instance;
    }

    public void addConnection(SocketReceiver info) {
        connections.add(info);
    }

    public List<SocketReceiver> getConnections() {
        return this.connections;
    }
}
