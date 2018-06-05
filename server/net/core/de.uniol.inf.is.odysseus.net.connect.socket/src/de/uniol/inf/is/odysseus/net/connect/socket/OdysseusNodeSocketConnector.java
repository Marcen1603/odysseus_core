package de.uniol.inf.is.odysseus.net.connect.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentAdapter;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionListener;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnector;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectorCallback;
import de.uniol.inf.is.odysseus.net.connect.OdysseusNetConnectionException;
import de.uniol.inf.is.odysseus.net.util.InetAddressUtil;

public class OdysseusNodeSocketConnector extends OdysseusNetComponentAdapter implements IOdysseusNodeConnector, IOdysseusNetComponent, IServerSocketAcceptListener, IOdysseusNodeConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeSocketConnector.class);

	private static final String SERVER_ADDRESS_PROPERTY_KEY = "serverAddress";
	private static final String SERVER_PORT_PROPERTY_KEY = "serverPort";
	private static final String SERVER_HOST_NAME = "serverHostname";

	private final Map<IOdysseusNode, IOdysseusNodeConnection> connections = Maps.newHashMap();
	private final Map<OdysseusNodeID, Socket> acceptedSocketsMap = Maps.newHashMap();

	private int serverPort;
	private IOdysseusNode localNode;

	private ServerSocket serverSocket;
	private ServerSocketAcceptThread acceptThread;

	@Override
	public void init(IOdysseusNode localNode) throws OdysseusNetException {
		serverPort = findFreePort();
		LOG.info("Choosing port {} as server port", serverPort);

		// port and address will be shared with other nodes
		localNode.addProperty(SERVER_PORT_PROPERTY_KEY, "" + serverPort);
		Optional<String> optAddress = InetAddressUtil.getRealInetAddress();
		if( optAddress.isPresent() ) {
			localNode.addProperty(SERVER_ADDRESS_PROPERTY_KEY, optAddress.get());
		} else {
			throw new OdysseusNetException("Could not determine own inet-address");
		}
		
		Optional<String> optHostName = InetAddressUtil.getHostName();
		if( optHostName.isPresent() ) {
			localNode.addProperty(SERVER_HOST_NAME, optHostName.get());
		}

		this.localNode = localNode;
	}

	@Override
	public void start() throws OdysseusNetException {
		startServer(serverPort);
	}

	@Override
	public void connect(IOdysseusNode node, IOdysseusNodeConnectorCallback callback) throws OdysseusNetConnectionException {
		Thread waitingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Socket asClientSocket = null;
				Socket asServerSocket = null;
				try {
					LOG.debug("Beginning connecting to node {}", node);

					// 1. Connect to their server
					String serverAddress = determineServerAddress(node);
					int serverPort = determineServerPort(node);
					LOG.debug("Address is {}, port is {}", serverAddress, serverPort);

					asClientSocket = new Socket(serverAddress, serverPort);
					LOG.debug("Socket created for node {}", node);

					// 2. Send own id
					OutputStream clientOut = asClientSocket.getOutputStream();
					byte[] idBytes = localNode.getID().toString().getBytes();
					clientOut.write(idBytes);
					clientOut.flush();
					LOG.debug("Send own id {} to node {}", localNode.getID(), node);

					// 3. Wait for them connecting to us
					LOG.debug("Waiting for client-connection from other node {}", node);
					while (asServerSocket == null) {
						synchronized (acceptedSocketsMap) {
							asServerSocket = acceptedSocketsMap.remove(node.getID());
						}
						if (asServerSocket == null) {
							waitSomeTime();
						}
					}
					LOG.debug("Got socket from other node {}", node);
					
					// 4. Return connection to caller
					OdysseusNodeSocketConnection connection = new OdysseusNodeSocketConnection(asClientSocket, asServerSocket, node);
					connection.addListener(OdysseusNodeSocketConnector.this);
					
					connections.put(node, connection);
					callback.successfulConnection(connection);
					
					LOG.debug("Connection to node {} finished", node);
					
				} catch (IOException e) {
					callback.failedConnection(new OdysseusNetConnectionException("Could not connect to node " + node, e));
					tryCloseSocket(asClientSocket);
					tryCloseSocket(asServerSocket);
				} catch (OdysseusNetConnectionException e) {
					callback.failedConnection(e);
					tryCloseSocket(asClientSocket);
					tryCloseSocket(asServerSocket);
				}
			}

		});
		waitingThread.setName("Connecting to " + node.getID());
		waitingThread.setDaemon(true);
		waitingThread.start();
	}

	private static void tryCloseSocket(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	private static void waitSomeTime() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
	}

	private static String determineServerAddress(IOdysseusNode node) throws OdysseusNetConnectionException {
		Optional<String> optServerAddress = node.getProperty(SERVER_ADDRESS_PROPERTY_KEY);
		if (optServerAddress.isPresent()) {
			return optServerAddress.get();
		}
		throw new OdysseusNetConnectionException("Following node has no " + SERVER_ADDRESS_PROPERTY_KEY + " property: " + node);
	}

	private static int determineServerPort(IOdysseusNode node) throws OdysseusNetConnectionException {
		Optional<String> optNodeServerPortStr = node.getProperty(SERVER_PORT_PROPERTY_KEY);
		if (optNodeServerPortStr.isPresent()) {
			Optional<Integer> optNodeServerPort = tryConvertToInt(optNodeServerPortStr.get());
			if (optNodeServerPort.isPresent()) {
				return optNodeServerPort.get();
			}
			throw new OdysseusNetConnectionException("Could not determine server port of node " + node);
		}
		throw new OdysseusNetConnectionException("Property 'serverPort' is not available for node " + node);
	}

	@Override
	public void disconnect(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node must not be null!");

		if (connections.containsKey(node)) {
			LOG.info("Disconnecting node {}", node);

			IOdysseusNodeConnection connection = connections.remove(node);
			connection.disconnect();
		}
	}

	@Override
	public void stop() {
		stopServer();
	}

	@Override
	public boolean isConnected(IOdysseusNode node) {
		return connections.containsKey(node);
	}

	private static Optional<Integer> tryConvertToInt(String text) {
		try {
			return Optional.of(Integer.parseInt(text));
		} catch (Throwable t) {
			return Optional.absent();
		}
	}

	private void startServer(int port) throws OdysseusNetConnectionException {
		try {
			LOG.info("Starting server at port {}", port);
			serverSocket = new ServerSocket(port);
			acceptThread = new ServerSocketAcceptThread(serverSocket, this);
			acceptThread.start();

			LOG.info("Starting server finished");
		} catch (IOException e) {
			throw new OdysseusNetConnectionException("Could not start server for connections", e);
		}
	}

	private void stopServer() {
		if (acceptThread != null) {
			LOG.info("Stopping server");

			acceptThread.stopRunning();
			acceptThread = null;

			try {
				serverSocket.close();
			} catch (IOException e) {
			}
			serverSocket = null;
			LOG.info("Stopping server finished");
		}
	}

	private static int findFreePort() throws OdysseusNetException {
		try {
			ServerSocket s = new ServerSocket(0);
			s.setReuseAddress(true);
			int port = s.getLocalPort();
			try {
				s.close();
			} catch (IOException ignore) {
			}

			return port;
		} catch (IOException e) {
			throw new OdysseusNetException("Could not find a free port", e);
		}
	}

	@Override
	public void acceptedConnection(Socket clientSocket) {
		LOG.info("Starting acception connection from unknown client");

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream inputStream = clientSocket.getInputStream();
					byte[] buffer = new byte[1024];

					LOG.debug("Waiting for nodeID from unknown client connection");
					int readBytes = inputStream.read(buffer);

					byte[] uuidStrBuffer = new byte[readBytes];
					System.arraycopy(buffer, 0, uuidStrBuffer, 0, readBytes);

					if (readBytes > 0) {
						LOG.debug("Read {} bytes", readBytes);
						String uuidStr = new String(uuidStrBuffer);
						OdysseusNodeID nodeID = OdysseusNodeID.fromString(uuidStr);
						LOG.debug("Got nodeID {}", nodeID);

						synchronized (acceptedSocketsMap) {
							acceptedSocketsMap.put(nodeID, clientSocket);
						}

						LOG.info("Unknown client has nodeid {}", nodeID);
						return;
					}

				} catch (IOException e) {
					LOG.warn("Could not accept connection from unknown client {}", clientSocket.getInetAddress(), e);
				}

				// if we are here, something got wrong
				try {
					clientSocket.close();
				} catch (IOException e) {
				}
			}
		});

		t.setName("Accepting connection to client waiting: " + clientSocket.getInetAddress());
		t.setDaemon(true);
		t.start();
	}

	// called for node connections
	@Override
	public void messageReceived(IOdysseusNodeConnection connection, byte[] data) {
		// do nothing
	}

	// called for node connections
	@Override
	public void disconnected(IOdysseusNodeConnection connection) {
		connections.remove(connection.getOdysseusNode());
	}

	@Override
	public Optional<IOdysseusNodeConnection> getConnection(IOdysseusNode node) {
		Preconditions.checkNotNull(node, "node must not be null!");

		return Optional.fromNullable(connections.get(node));
	}

	@Override
	public Collection<IOdysseusNodeConnection> getConnections() {
		return Lists.newArrayList(connections.values());
	}
}
