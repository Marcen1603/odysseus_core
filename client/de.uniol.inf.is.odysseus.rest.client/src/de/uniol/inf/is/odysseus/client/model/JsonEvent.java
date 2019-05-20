package de.uniol.inf.is.odysseus.client.model;

import de.uniol.inf.is.odysseus.client.communication.socket.SocketReceiver;

/**
 * @author Tobias on 31.03.2017.
 */
public class JsonEvent extends Event {

    private String json;

    public JsonEvent(SocketReceiver connection, String json) {
        super(connection);
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
