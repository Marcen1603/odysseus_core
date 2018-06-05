package de.uniol.inf.is.odysseus.core.connection;

import java.nio.channels.DatagramChannel;

public interface UDPAcceptorListener {
    public void socketConnected(AcceptorSelectorHandler acceptor, DatagramChannel channel);

    public void socketError(AcceptorSelectorHandler acceptor, Exception ex);
}
