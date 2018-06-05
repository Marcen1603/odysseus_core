package de.uniol.inf.is.odysseus.core.connection;

import java.nio.channels.SocketChannel;

public interface TCPConnectorListener {
    void connectionEstablished(ConnectorSelectorHandler connector, SocketChannel channel);

    void connectionFailed(ConnectorSelectorHandler connector, Exception cause);
}
