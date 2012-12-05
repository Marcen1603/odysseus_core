package de.uniol.inf.is.odysseus.core.connection;

import java.nio.channels.SocketChannel;

public interface ConnectorListener {
    void connectionEstablished(TCPConnector connector, SocketChannel sc);

    void connectionFailed(TCPConnector connector, Exception cause);
}
