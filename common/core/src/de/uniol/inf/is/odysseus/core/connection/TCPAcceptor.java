package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class TCPAcceptor implements AcceptorSelectorHandler {
	private final SelectorThread ioThread;
	private final int port;
	private final TCPAcceptorListener listener;
	private ServerSocketChannel serverSocketChannel;

	public TCPAcceptor(final int port, final SelectorThread ioThread,
			final TCPAcceptorListener listener) {
		this.ioThread = ioThread;
		this.port = port;
		this.listener = listener;
	}

	public void open() throws IOException {
		this.serverSocketChannel = ServerSocketChannel.open();
		final InetSocketAddress address = new InetSocketAddress(this.port);
		serverSocketChannel.bind(address, 100);
		serverSocketChannel.socket().setReuseAddress(true);
				
		
		this.ioThread.registerChannel(this.serverSocketChannel,
				SelectionKey.OP_ACCEPT, this, new CallbackErrorHandler() {
					@SuppressWarnings("unused")
					public void handleError(final Exception ex) {
						TCPAcceptor.this.listener.socketError(TCPAcceptor.this,
								ex);
					}
				});
	}

	@Override
	public void onAccept() {
		SocketChannel channel = null;
		try {
			channel = this.serverSocketChannel.accept();
			this.ioThread.addChannelInterestNow(this.serverSocketChannel,
					SelectionKey.OP_ACCEPT);
		} catch (final IOException e) {
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
							ioThread.removeChannelInterestNow(serverSocketChannel,
									SelectionKey.OP_ACCEPT);
							ioThread.unregisterChannelNow(serverSocketChannel);
							TCPAcceptor.this.serverSocketChannel.close();
							TCPAcceptor.this.serverSocketChannel.socket()
									.close();
							
							

							ioThread.close();
						} catch (final Exception e) {
							TCPAcceptor.this.listener.socketError(
									TCPAcceptor.this, e);
						}
					}
				}
			});
		} catch (final InterruptedException e) {
			listener.socketError(this, e);
		}
	}
}
