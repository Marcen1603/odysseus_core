package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class UDPConnector implements ConnectorSelectorHandler {
    private final UDPConnectorListener listener;
    private final InetSocketAddress    remoteAddress;
    private final SelectorThread       selectorThread;
    private DatagramChannel            channel;

    public UDPConnector(final SelectorThread selector, final InetSocketAddress remoteAddress,
            final UDPConnectorListener listener) {
        this.selectorThread = selector;
        this.remoteAddress = remoteAddress;
        this.listener = listener;
    }

    public void connect() throws IOException {
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        this.channel.connect(this.remoteAddress);
        onConnect();
    }

    @Override
    public void onConnect() {
        this.listener.connectionEstablished(this, this.channel);
    }

    public void disconnect() throws IOException {
        this.channel.close();
    }

}
