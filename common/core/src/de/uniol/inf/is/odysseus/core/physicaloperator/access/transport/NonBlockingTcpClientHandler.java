/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.ConnectorSelectorHandler;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioTcpConnection;
import de.uniol.inf.is.odysseus.core.connection.SelectorThread;
import de.uniol.inf.is.odysseus.core.connection.TCPConnector;
import de.uniol.inf.is.odysseus.core.connection.TCPConnectorListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * Handler for generic TCP Client
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NonBlockingTcpClientHandler extends AbstractTransportHandler
		implements IAccessConnectionListener<ByteBuffer>, IConnectionListener,
		TCPConnectorListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(NonBlockingTcpClientHandler.class);
	private SelectorThread selector;
	private String host;
	private int port;
	private TCPConnector connector;
	private NioTcpConnection connection;

	/** In and output for data transfer */
	@SuppressWarnings("unused")
	private InputStream input;
	private OutputStream output;
	private int readBufferSize;
	private int writeBufferSize;

	public NonBlockingTcpClientHandler() {
		super();
	}

	public NonBlockingTcpClientHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		
		readBufferSize = options.containsKey("read") ? Integer
				.parseInt(options.get("read")) : 10240;
		writeBufferSize = options.containsKey("write") ? Integer
				.parseInt(options.get("write")) : 10240;
		host = options.containsKey("host") ? options.get("host")
				: "127.0.0.1";
		port = options.containsKey("port") ? Integer.parseInt(options
				.get("port")) : 8080;
		try {
			selector = SelectorThread.getInstance();
			final InetSocketAddress address = new InetSocketAddress(
					host, port);
			if (options.containsKey("logininfo")) {
				connector = new TCPConnector(selector, address,
						this, options.get("logininfo"));
			} else {
				connector = new TCPConnector(selector, address,
						this);
			}
		} catch (final IOException e) {
			NonBlockingTcpClientHandler.LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void send(final byte[] message) throws IOException {
		if (this.connection != null) {
			this.connection.write(message);
		} else {
			NonBlockingTcpClientHandler.LOG.error("Not connected");
		}
	}

	@Override
	public ITransportHandler createInstance(
			final IProtocolHandler<?> protocolHandler,
			final OptionMap options) {
		final NonBlockingTcpClientHandler handler = new NonBlockingTcpClientHandler(
				protocolHandler, options);
		return handler;
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public String getName() {
		return "TCPClient";
	}

	@Override
	public void process(final ByteBuffer buffer) throws ClassNotFoundException {
		super.fireProcess(buffer);
	}

	@Override
	public void done() {

	}

	@Override
	public OutputStream getOutputStream() {
		return this.output;
	}

	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		try {
			this.connector.connect();
		} catch (final IOException e) {
			NonBlockingTcpClientHandler.LOG.error(e.getMessage(), e);
			throw new OpenFailedException(e);
		}
	}

	@Override
	public void processOutOpen() throws UnknownHostException, IOException {
		try {
			this.connector.connect();
		} catch (final IOException e) {
			NonBlockingTcpClientHandler.LOG.error(e.getMessage(), e);
			throw new OpenFailedException(e);
		}
		this.output = new TcpOutputStream();
	}

	@Override
	public void processInClose() throws IOException {
		this.connector.disconnect();
		if (this.connection != null) {
			this.connection.close();
		}
	}

	@Override
	public void processOutClose() throws IOException {
		this.connector.disconnect();
		if (this.connection != null) {
			this.connection.close();
		}
	}

	@Override
	public void notify(final IConnection connection,
			final ConnectionMessageReason reason) {
		switch (reason) {
		case ConnectionAbort:
			super.fireOnDisconnect();
			break;
		case ConnectionClosed:
			super.fireOnDisconnect();
			break;
		case ConnectionRefused:
			super.fireOnDisconnect();
			break;
		case ConnectionOpened:
			super.fireOnConnect();
			break;
		default:
			break;
		}
	}

	@Override
	public void connectionEstablished(final ConnectorSelectorHandler connector,
			final SocketChannel channel) {
		try {
			channel.socket().setReceiveBufferSize(this.readBufferSize);
			channel.socket().setSendBufferSize(this.writeBufferSize);
			this.connection = new NioTcpConnection(channel, this.selector, this);
		} catch (final IOException e) {
			NonBlockingTcpClientHandler.LOG.error(e.getMessage(), e);
		}
		super.fireOnConnect();
	}

	@Override
	public void connectionFailed(final ConnectorSelectorHandler connector,
			final Exception cause) {
		NonBlockingTcpClientHandler.LOG.error(cause.getMessage(), cause);
	}

	@Override
	public void socketDisconnected() {
		super.fireOnDisconnect();

	}

	@Override
	public void socketException(final Exception e) {
		NonBlockingTcpClientHandler.LOG.error(e.getMessage(), e);
	}

	private class TcpOutputStream extends OutputStream {
		private ByteBuffer buffer = ByteBuffer.allocate(1024);

		public TcpOutputStream() {

		}

		@Override
		public void write(final int b) throws IOException {
			if ((1 + this.buffer.position()) >= this.buffer.capacity()) {
				final ByteBuffer newBuffer = ByteBuffer
						.allocate((1 + this.buffer.position()) * 2);
				final int pos = this.buffer.position();
				this.buffer.flip();
				newBuffer.put(this.buffer);
				this.buffer = newBuffer;
				this.buffer.position(pos);
				NonBlockingTcpClientHandler.LOG.debug("Extending buffer to "
						+ this.buffer.capacity());
			}
			this.buffer.put((byte) b);
		}

		@Override
		public void flush() throws IOException {
			this.buffer.flip();
			if (connection != null) {
				connection.write(buffer);
			}
			this.buffer.clear();
		}

		@Override
		public void close() throws IOException {
			this.buffer.clear();
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) {
		if (!(o instanceof NonBlockingTcpClientHandler)) {
			return false;
		}
		NonBlockingTcpClientHandler other = (NonBlockingTcpClientHandler) o;
		if (!this.host.equals(other.host)) {
			return false;
		} else if (this.port != other.port) {
			return false;
		} else if (this.readBufferSize != other.readBufferSize) {
			return false;
		} else if (this.writeBufferSize != other.writeBufferSize) {
			return false;
		}

		return true;
	}
}
