package de.uniol.inf.is.odysseus.client.model;

import de.uniol.inf.is.odysseus.client.communication.socket.SocketReceiver;


/**
 * @author Tobias Brandt
 * @since 03.04.2015.
 */
public class Event {

    private SocketReceiver connection;


    public Event(SocketReceiver connection) {
        this.connection = connection;

    }

    public SocketReceiver getConnection() {
        return connection;
    }

    public void setConnection(SocketReceiver connection) {
        this.connection = connection;
    }

}
