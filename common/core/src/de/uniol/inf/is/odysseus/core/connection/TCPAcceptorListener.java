package de.uniol.inf.is.odysseus.core.connection;

import java.nio.channels.SocketChannel;

public interface TCPAcceptorListener {
    public void socketConnected(AcceptorSelectorHandler acceptor, SocketChannel sc);

    public void socketError(AcceptorSelectorHandler acceptor, Exception ex);
}
