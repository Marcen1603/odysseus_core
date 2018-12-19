package de.uniol.inf.is.odysseus.client.model;

import de.uniol.inf.is.odysseus.client.communication.socket.SocketReceiver;

import java.util.Map;

/**
 * @author Tobias on 31.03.2017.
 */
public class SchemaEvent extends Event {

    private Map<String, String> attributes;

    public SchemaEvent(SocketReceiver connection, Map<String, String> attributes) {
        super(connection);
        this.attributes = attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
