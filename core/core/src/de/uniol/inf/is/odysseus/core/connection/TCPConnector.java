package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class TCPConnector implements ConnectorSelectorHandler {
    private final TCPConnectorListener listener;
    private final InetSocketAddress    remoteAddress;
    private final SelectorThread       selectorThread;
    private SocketChannel              channel;

    public TCPConnector(final SelectorThread selector, final InetSocketAddress remoteAddress,
            final TCPConnectorListener listener) {
        this.selectorThread = selector;
        this.remoteAddress = remoteAddress;
        this.listener = listener;
    }

    public void connect() throws IOException {
        this.channel = SocketChannel.open();
        this.channel.configureBlocking(false);
        this.channel.connect(this.remoteAddress);
        this.selectorThread.registerChannel(this.channel, SelectionKey.OP_CONNECT, this, new CallbackErrorHandler() {
            @SuppressWarnings("unused")
            public void handleError(final Exception ex) {
                TCPConnector.this.listener.connectionFailed(TCPConnector.this, ex);
            }
        });
    }

    @Override
    public void onConnect() {
        try {
            if (!this.channel.finishConnect()) {
                this.listener.connectionFailed(this, null);
                return;
            }
            this.listener.connectionEstablished(this, this.channel);
        }
        catch (final IOException ex) {
            this.listener.connectionFailed(this, ex);
        }
    }

    public void disconnect() throws IOException {
        this.channel.close();
    }
}
