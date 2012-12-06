package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class UDPAcceptor implements AcceptorSelectorHandler {
    private final SelectorThread      ioThread;
    private final int                 port;
    private final UDPAcceptorListener listener;
    private DatagramChannel           datagramChannel;

    public UDPAcceptor(final int port, final SelectorThread ioThread, final UDPAcceptorListener listener) {
        this.ioThread = ioThread;
        this.port = port;
        this.listener = listener;
    }

    public void open() throws IOException {
        this.datagramChannel = DatagramChannel.open();
        final InetSocketAddress address = new InetSocketAddress(this.port);
        this.datagramChannel.socket().bind(address);
        this.onAccept();
    }

    @Override
    public void onAccept() {
        this.listener.socketConnected(this, this.datagramChannel);
    }

    public void close() {
        try {
            this.ioThread.blockingInvoke(new Runnable() {
                @Override
                public void run() {
                    if (UDPAcceptor.this.datagramChannel != null) {
                        try {
                            UDPAcceptor.this.datagramChannel.close();
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
