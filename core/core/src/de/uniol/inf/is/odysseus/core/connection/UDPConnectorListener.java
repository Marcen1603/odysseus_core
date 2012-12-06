package de.uniol.inf.is.odysseus.core.connection;

import java.nio.channels.DatagramChannel;

public interface UDPConnectorListener {
    void connectionEstablished(ConnectorSelectorHandler connector, DatagramChannel channel);

    void connectionFailed(ConnectorSelectorHandler connector, Exception cause);
}
