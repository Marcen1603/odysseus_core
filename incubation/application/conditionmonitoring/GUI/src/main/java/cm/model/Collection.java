package cm.model;

import cm.communication.dto.SocketInfo;
import cm.communication.socket.SocketReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 15.03.2015.
 */
public class Collection {

    public static final String OK_STATE = "OK";
    public static final String BAD_STATE = "BAD";

    private int id;
    private String name;
    private String state;
    private List<SocketInfo> connections;

    public Collection(int id, String name, String state) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.connections = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collection collection = (Collection) o;

        return id == collection.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
