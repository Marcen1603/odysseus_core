package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TCPAcceptor implements AcceptorSelectorHandler {

    private final SelectorThread   ioThread;
    private final int              port;
    private final TCPAcceptorListener listener;
    private ServerSocketChannel    serverSocketChannel;

    public TCPAcceptor(final int port, final SelectorThread ioThread, final TCPAcceptorListener listener) {
        this.ioThread = ioThread;
        this.port = port;
        this.listener = listener;
    }

    public void open() throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        final InetSocketAddress address = new InetSocketAddress(this.port);
        this.serverSocketChannel.socket().bind(address, 100);

        this.ioThread.registerChannel(this.serverSocketChannel, SelectionKey.OP_ACCEPT, this,
                new CallbackErrorHandler() {
                    public void handleError(final Exception ex) {
                        TCPAcceptor.this.listener.socketError(TCPAcceptor.this, ex);
                    }
                });
    }

    @Override
    public void onAccept() {
        SocketChannel channel = null;
        try {
            channel = this.serverSocketChannel.accept();
            final Socket s = channel.socket();
            this.ioThread.addChannelInterestNow(this.serverSocketChannel, SelectionKey.OP_ACCEPT);
        }
        catch (final IOException e) {
            this.listener.socketError(this, e);
        }
        if (channel != null) {
            this.listener.socketConnected(this, channel);
        }
    }

    public void close() {
        try {
            this.ioThread.blockingInvoke(new Runnable() {
                @Override
                public void run() {
                    if (TCPAcceptor.this.serverSocketChannel != null) {
                        try {
                            TCPAcceptor.this.serverSocketChannel.close();
                        }
                        catch (final IOException e) {
                            // Ignore
                        }
                    }
                }
            });
        }
        catch (final InterruptedException e) {
            // Ignore
        }
    }
}
