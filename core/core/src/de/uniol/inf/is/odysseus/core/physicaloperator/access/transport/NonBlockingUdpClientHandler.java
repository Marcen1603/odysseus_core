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
import java.nio.channels.DatagramChannel;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.ConnectorSelectorHandler;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioUdpConnection;
import de.uniol.inf.is.odysseus.core.connection.SelectorThread;
import de.uniol.inf.is.odysseus.core.connection.UDPConnector;
import de.uniol.inf.is.odysseus.core.connection.UDPConnectorListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * Handler for generic UDP Client
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NonBlockingUdpClientHandler extends AbstractTransportHandler
		implements IAccessConnectionListener<ByteBuffer>, IConnectionListener,
		UDPConnectorListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(NonBlockingUdpClientHandler.class);
	private SelectorThread selector;
	private String host;
	private int port;
	private UDPConnector connector;
	private NioUdpConnection connection;
	private int readBufferSize;
	private int writeBufferSize;

	public NonBlockingUdpClientHandler() {
		super();
	}

	public NonBlockingUdpClientHandler(final IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void send(final byte[] message) throws IOException {
		if (this.connection != null) {
			this.connection.write(message);
		} else {
			NonBlockingUdpClientHandler.LOG.error("Not connected");
		}
	}

	@Override
	public ITransportHandler createInstance(
			final IProtocolHandler<?> protocolHandler,
			final Map<String, String> options) {
		final NonBlockingUdpClientHandler handler = new NonBlockingUdpClientHandler(
				protocolHandler);
		handler.readBufferSize = options.containsKey("read") ? Integer
				.parseInt(options.get("read")) : 10240;
		handler.writeBufferSize = options.containsKey("write") ? Integer
				.parseInt(options.get("write")) : 10240;
		handler.host = options.containsKey("host") ? options.get("host")
				: "127.0.0.1";
		handler.port = options.containsKey("port") ? Integer.parseInt(options
				.get("port")) : 8080;
		try {
			handler.selector = SelectorThread.getInstance();
			final InetSocketAddress address = new InetSocketAddress(
					handler.host, handler.port);
			handler.connector = new UDPConnector(handler.selector, address,
					handler);
		} catch (final IOException e) {
			NonBlockingUdpClientHandler.LOG.error(e.getMessage(), e);
		}

		return handler;
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public String getName() {
		return "UDPClient";
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
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		try {
			this.connector.connect();
		} catch (final IOException e) {
			throw new OpenFailedException(e);
		}
	}

	@Override
	public void processOutOpen() throws UnknownHostException, IOException {
		try {
			this.connector.connect();
		} catch (final IOException e) {
			throw new OpenFailedException(e);
		}
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
	public void socketDisconnected() {
		super.fireOnDisconnect();

	}

	@Override
	public void socketException(final Exception cause) {
		NonBlockingUdpClientHandler.LOG.error(cause.getMessage(), cause);

	}

	@Override
	public void connectionEstablished(final ConnectorSelectorHandler connector,
			final DatagramChannel channel) {
		try {
			channel.socket().setReceiveBufferSize(this.readBufferSize);
			channel.socket().setSendBufferSize(this.writeBufferSize);
			this.connection = new NioUdpConnection(channel, this.selector, this);
		} catch (final IOException e) {
			NonBlockingUdpClientHandler.LOG.error(e.getMessage(), e);
		}

		super.fireOnConnect();
	}

	@Override
	public void connectionFailed(final ConnectorSelectorHandler connector,
			final Exception cause) {
		NonBlockingUdpClientHandler.LOG.error(cause.getMessage(), cause);
	}

}
