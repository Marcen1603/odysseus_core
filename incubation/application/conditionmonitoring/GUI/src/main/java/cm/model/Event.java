package cm.model;

import cm.communication.socket.SocketReceiver;

import java.util.Map;

/**
 * @author Tobias
 * @since 03.04.2015.
 */
public class Event {

    private SocketReceiver connection;
    private Map<String, String> attributes;

    public Event(SocketReceiver connection, Map<String, String> attributes) {
        this.connection = connection;
        this.attributes = attributes;
    }

    public SocketReceiver getConnection() {
        return connection;
    }

    public void setConnection(SocketReceiver connection) {
        this.connection = connection;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
