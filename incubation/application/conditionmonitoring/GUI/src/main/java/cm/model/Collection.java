package cm.model;

import cm.communication.dto.SocketInfo;
import cm.communication.socket.SocketReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias
 * @since 15.03.2015.
 */
public class Collection {

    private String name;
    private List<SocketInfo> connections;

    public Collection(String name) {
        this.name = name;
        this.connections = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addConnection(SocketInfo connection) {
        this.connections.add(connection);
    }

    public List<SocketInfo> getConnections() {
        return connections;
    }
}
