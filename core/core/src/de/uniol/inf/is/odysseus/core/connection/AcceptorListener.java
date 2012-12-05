package de.uniol.inf.is.odysseus.core.connection;

import java.nio.channels.SocketChannel;

public interface AcceptorListener {
    public void socketConnected(TCPAcceptor acceptor, SocketChannel sc);

    public void socketError(TCPAcceptor acceptor, Exception ex);
}
