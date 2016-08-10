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
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * Handler for generic TCP Client
 *
 * @author Christian Kuka <christian@kuka.cc>
 */
public class NonBlockingTcpClientHandler extends AbstractTransportHandler
		implements IAccessConnectionListener<ByteBuffer>, IConnectionListener, TCPConnectorListener {
	private class TcpOutputStream extends OutputStream {
		private ByteBuffer buffer = ByteBuffer.allocate(1024);

		public TcpOutputStream() {

		}

		@Override
		public void close() throws IOException {
			this.buffer.clear();
		}

		@Override
		public void flush() throws IOException {
			this.buffer.flip();
			if (NonBlockingTcpClientHandler.this.connection != null) {
				NonBlockingTcpClientHandler.this.connection.write(this.buffer);
			}
			this.buffer.clear();
		}

		@Override
		public void write(final int b) throws IOException {
			if ((1 + this.buffer.position()) >= this.buffer.capacity()) {
				final ByteBuffer newBuffer = ByteBuffer.allocate((1 + this.buffer.position()) * 2);
				final int pos = this.buffer.position();
				this.buffer.flip();
				newBuffer.put(this.buffer);
				this.buffer = newBuffer;
				this.buffer.position(pos);
				NonBlockingTcpClientHandler.LOG.debug("Extending buffer to " + this.buffer.capacity());
			}
			this.buffer.put((byte) b);
		}
	}

	public static final String NAME = "TCPClient";

	// The host name
	public static final String HOST = "host";
	// list of host names
	public static final String HOSTS = "hosts";
	// The port
	public static final String PORT = "port";
	// list of ports
	public static final String PORTS = "ports";
	// The read buffer size
	public static final String READ_BUFFER = "read";
	// The write buffer size
	public static final String WRITE_BUFFER = "write";
	// Auto reconnect on disconnect
	public static final String AUTORECONNECT = "autoreconnect";
	// The initialization command
	public static final String INITIALIZE = "init";
	// Deprecated
	public static final String INITIALIZE_ALIAS = "logininfo";
	// Deprecated
	public static final String PASSWORD = "password";
	// Deprecated
	public static final String USERNAME = "user";

	static final Logger LOG = LoggerFactory.getLogger(NonBlockingTcpClientHandler.class);
	private TCPConnector connector;
	private List<String> hosts = new LinkedList<String>();
	private List<Integer> ports = new LinkedList<Integer>();
	private int currentHost = -1;
	private byte[] initializeCommand;
	/** In and output for data transfer */
	@SuppressWarnings("unused")
	private InputStream input;

	private OutputStream output;
	private int readBufferSize;
	private SelectorThread selector;
	private int writeBufferSize;

	/**
	 * Time in ms to wait between two reconnect tries. <br />
	 * -1 for disabling auto reconnect
	 */
	private long autoreconnectTime = -1l;
	private Timer autoreconnectTimer;
	NioTcpConnection connection;

	static final InfoService infoService = InfoServiceFactory
			.getInfoService(NonBlockingTcpClientHandler.class.getName());

	public NonBlockingTcpClientHandler() {
		super();
	}

	public NonBlockingTcpClientHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		super(protocolHandler, options);
		this.init();
		try {
			this.selector = SelectorThread.getInstance();
			currentHost = 0;
			connect();
		} catch (final IOException e) {
			if (autoreconnectTime > 0) {
				NonBlockingTcpClientHandler.LOG.warn(e.getMessage(), e);
			} else {
				NonBlockingTcpClientHandler.LOG.error(e.getMessage(), e);
			}
		}
	}

	private void connect() {
		if (hosts.size() > 0) {
			final InetSocketAddress address = new InetSocketAddress(hosts.get(currentHost), ports.get(currentHost));
			this.connector = new TCPConnector(this.selector, address, this, this.getInitializeCommand());
		}
	}

	@Override
	public void connectionEstablished(final ConnectorSelectorHandler handler, final SocketChannel channel) {
		try {
			channel.socket().setReceiveBufferSize(this.readBufferSize);
			channel.socket().setSendBufferSize(this.writeBufferSize);
			this.connection = new NioTcpConnection(channel, this.selector, this);
		} catch (final IOException e) {
			if (autoreconnectTime > 0) {
				NonBlockingTcpClientHandler.LOG.warn(e.getMessage(), e);
			} else {
				NonBlockingTcpClientHandler.LOG.error(e.getMessage(), e);
			}
		}
		if (this.autoreconnectTimer != null) {
			this.autoreconnectTimer.cancel();
			this.autoreconnectTimer = null;
		}
		super.fireOnConnect();
	}

	@Override
	public void connectionFailed(final ConnectorSelectorHandler handler, final Exception cause) {
		if (currentHost + 1 < hosts.size()) {
			infoService.warning("Could not connect to server " + hosts.get(currentHost) + " on port "
					+ ports.get(currentHost) + " Tryin' next server", cause);
			currentHost++;
			connect();
			try {
				this.connector.connect();
			} catch (IOException e) {
			}
		} else {
			if (autoreconnectTime > 0) {
				currentHost = 0;
			} else {
				infoService.error("Could not connect any server " + hosts + " on ports " + ports, cause);
				NonBlockingTcpClientHandler.LOG
						.error(cause.getMessage() + " for server " + hosts + " on ports " + ports, cause);
			}
		}
	}

	@Override
	public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		final NonBlockingTcpClientHandler handler = new NonBlockingTcpClientHandler(protocolHandler, options);
		return handler;
	}

	@Override
	public void done() {
		// Nothing to do
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public String getName() {
		return NonBlockingTcpClientHandler.NAME;
	}

	@Override
	public OutputStream getOutputStream() {
		return this.output;
	}

	@Override
	public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
		if (!(o instanceof NonBlockingTcpClientHandler)) {
			return false;
		}
		final NonBlockingTcpClientHandler other = (NonBlockingTcpClientHandler) o;
		if (!this.hosts.equals(other.hosts)) {
			return false;
		} else if (this.ports.equals(other.ports)) {
			return false;
		} else if (this.readBufferSize != other.readBufferSize) {
			return false;
		} else if (this.writeBufferSize != other.writeBufferSize) {
			return false;
		} else if (this.initializeCommand.equals(other.initializeCommand)) {
			return false;
		} else if (this.autoreconnectTime != other.autoreconnectTime) {
			return false;
		}
		return true;
	}

	@Override
	public void notify(final IConnection con, final ConnectionMessageReason reason) {
		infoService.warning("Connection information " + con + " --> " + reason);
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
	public void process(long callerId, final ByteBuffer buffer) throws ClassNotFoundException {
		super.fireProcess(callerId, buffer);
	}

	@Override
	public void processInClose() throws IOException {
		this.connector.disconnect();
		if (this.connection != null) {
			this.connection.close();
		}
	}

	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		try {
			this.connector.connect();
		} catch (final IOException e) {
			if (autoreconnectTime > 0) {
				infoService.warning(e.getMessage(), e);
			} else {
				NonBlockingTcpClientHandler.LOG.error(e.getMessage(), e);
				infoService.error(e.getMessage(), e);
				throw new OpenFailedException(e);
			}
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
	public void processOutOpen() throws UnknownHostException, IOException {
		try {
			this.connector.connect();
		} catch (final IOException e) {
			if (autoreconnectTime > 0) {
				infoService.warning(e.getMessage(), e);
			} else {
				NonBlockingTcpClientHandler.LOG.error(e.getMessage(), e);
				infoService.error(e.getMessage(), e);
				throw new OpenFailedException(e);
			}
		}
		this.output = new TcpOutputStream();
	}

	@Override
	public void send(final byte[] message) throws IOException {
		if (this.connection != null) {
			this.connection.write(message);
		} else {
			NonBlockingTcpClientHandler.LOG.error("Not connected");
		}
	}

	// /**
	// * @param host
	// * the host to set
	// */
	// public void setHost(final String host) {
	// this.host = host;
	// }
	//
	// /**
	// * @return the host
	// */
	// public String getHost() {
	// return this.host;
	// }

	/**
	 * @param initializeCommand
	 *            the initializeCommand to set
	 */
	public void setInitializeCommand(final String initializeCommand) {
		if (initializeCommand != null) {
			this.initializeCommand = initializeCommand.replace("\\\\n", "\n").replace("\\n", "\n").getBytes();
		} else {
			this.initializeCommand = null;
		}
	}

	/**
	 * @param initializeCommand
	 *            the initializeCommand to set
	 */
	public void setInitializeCommand(final byte[] initializeCommand) {
		this.initializeCommand = initializeCommand;
	}

	/**
	 * @return the initializeCommand
	 */
	public byte[] getInitializeCommand() {
		return this.initializeCommand;
	}

	//
	// /**
	// * @param port
	// * the port to set
	// */
	// public void setPort(final int port) {
	// this.port = port;
	// }
	//
	// /**
	// * @return the port
	// */
	// public int getPort() {
	// return this.port;
	// }

	/**
	 * @param readBufferSize
	 *            the readBufferSize to set
	 */
	public void setReadBufferSize(final int readBufferSize) {
		this.readBufferSize = readBufferSize;
	}

	/**
	 * @return the readBufferSize
	 */
	public int getReadBufferSize() {
		return this.readBufferSize;
	}

	/**
	 * @param writeBufferSize
	 *            the writeBufferSize to set
	 */
	public void setWriteBufferSize(final int writeBufferSize) {
		this.writeBufferSize = writeBufferSize;
	}

	/**
	 * @return the writeBufferSize
	 */
	public int getWriteBufferSize() {
		return this.writeBufferSize;
	}

	@Override
	public void socketDisconnected() {
		super.fireOnDisconnect();

	}

	@Override
	public void socketException(final Exception e) {
		NonBlockingTcpClientHandler.LOG.warn(e.getMessage(), e);

		if (this.autoreconnectTime > 0) {
			infoService.info("Try to reconnect every " + this.autoreconnectTime + " ms.");
			this.autoreconnectTimer = new Timer();
			this.autoreconnectTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					try {
						NonBlockingTcpClientHandler.this.connector.connect();
					} catch (IOException e) {
						// Try it again in <code>autoreconnectTime</code> ms
					}
				}

			}, this.autoreconnectTime, this.autoreconnectTime);
		}
	}

	private void init() {
		final OptionMap options = this.getOptionsMap();

		this.hosts.clear();
		if (!options.containsKey(HOST) && !options.containsKey(HOSTS)) {
			hosts.add("127.0.0.1");
		} else {
			if (options.containsKey(HOST)) {
				hosts.add(options.get(HOST));
			}
			if (options.containsKey(HOSTS)) {
				String hostList = options.get(HOSTS);
				String[] h = hostList.split(",");
				for (String hostEntry : h) {
					hosts.add(hostEntry);
				}
			}
		}

		this.ports.clear();
		if (!options.containsKey(PORT) && !options.containsKey(PORTS)) {
			ports.add(8080);
		} else {
			if (options.containsKey(PORT)) {
				ports.add(options.getInt(PORT, -1));
			}
			if (options.containsKey(PORTS)) {
				String portList = options.get(PORTS);
				String[] h = portList.split(",");
				for (String portEntry : h) {
					ports.add(Integer.parseInt(portEntry));
				}
			}

		}
		setReadBufferSize(options.getInt(READ_BUFFER, 10240));
		setWriteBufferSize(options.getInt(WRITE_BUFFER, 10240));

		if (options.containsKey(NonBlockingTcpClientHandler.INITIALIZE_ALIAS)) {
			this.setInitializeCommand(options.get(NonBlockingTcpClientHandler.INITIALIZE_ALIAS));
			NonBlockingTcpClientHandler.LOG.warn("Parameter " + NonBlockingTcpClientHandler.INITIALIZE_ALIAS
					+ " is deprecated. Please use " + NonBlockingTcpClientHandler.INITIALIZE);
			infoService.warning("Parameter " + NonBlockingTcpClientHandler.INITIALIZE_ALIAS
					+ " is deprecated. Please use " + NonBlockingTcpClientHandler.INITIALIZE);
		}
		if (options.containsKey(NonBlockingTcpClientHandler.USERNAME)) {
			this.setInitializeCommand(options.get(NonBlockingTcpClientHandler.USERNAME) + "\n"
					+ options.get(NonBlockingTcpClientHandler.PASSWORD) + "\n");
			NonBlockingTcpClientHandler.LOG.warn("Parameter " + NonBlockingTcpClientHandler.USERNAME
					+ " is deprecated. Please use " + NonBlockingTcpClientHandler.INITIALIZE);
		}
		if (options.containsKey(NonBlockingTcpClientHandler.INITIALIZE)) {
			this.setInitializeCommand(options.get(NonBlockingTcpClientHandler.INITIALIZE));
		}
		if (options.containsKey(AUTORECONNECT)) {
			this.autoreconnectTime = Long.parseLong(options.get(AUTORECONNECT));
		}
	}
}
