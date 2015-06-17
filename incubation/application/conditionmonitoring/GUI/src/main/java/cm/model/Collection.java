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

    public static final String OK_STATE = "OK";
    public static final String BAD_STATE = "BAD";

    private String name;
    private String state;
    private List<SocketInfo> connections;

    public Collection(String name, String state) {
        this.name = name;
        this.state = state;
        this.connections = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void addConnection(SocketInfo connection) {
        this.connections.add(connection);
    }

    public List<SocketInfo> getConnections() {
        return connections;
    }
}
