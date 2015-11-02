package de.uniol.inf.is.odysseus.net.connect.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnector;
import de.uniol.inf.is.odysseus.net.connect.OdysseusNetConnectionException;

public class OdysseusNodeSocketConnector implements IOdysseusNodeConnector, IOdysseusNetComponent, IServerSocketAcceptListener {

	private static final String SERVER_ADDRESS_PROPERTY_KEY = "serverAddress";
	private static final String SERVER_PORT_PROPERTY_KEY = "serverPort";

	private static final long MAX_WAIT_TIME_MILLIS = 30 * 1000;

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeSocketConnector.class);

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
		localNode.addProperty(SERVER_ADDRESS_PROPERTY_KEY, determineOwnInetAddress());

		this.localNode = localNode;
	}

	private static String determineOwnInetAddress() throws OdysseusNetException {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			throw new OdysseusNetException("Could not determine own inet-address", e);
		}
	}

	@Override
	public void start() throws OdysseusNetException {
		startServer(serverPort);
	}

	@Override
	public IOdysseusNodeConnection connect(IOdysseusNode node) throws OdysseusNetConnectionException {
		if (localNode.getID().compareTo(node.getID()) == 1) {
			// we are server and do nothing special
			// the other node has to connect to us
			LOG.info("Connection to node {} where other node is client", node);
			LOG.debug("Waiting for accepted connection from node {}", node);

			long startWaitTime = System.currentTimeMillis();

			while (true) {
				boolean accepted = false;

				synchronized (acceptedSocketsMap) {
					accepted = acceptedSocketsMap.containsKey(node.getID());
				}
				
				if( accepted ) {
					break;
				}
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}

				if (System.currentTimeMillis() - startWaitTime > MAX_WAIT_TIME_MILLIS) {
					throw new OdysseusNetConnectionException("Node " + node + " did not connect as client to us");
				}
			}

			LOG.debug("Got client socket for {}", node);
			Socket clientSocket = null;
			synchronized( acceptedSocketsMap ) {
				clientSocket = acceptedSocketsMap.remove(node.getID());
			}
			
			try {
				IOdysseusNodeConnection connection = new OdysseusNodeSocketConnection(clientSocket, node);
				LOG.info("Connection to node {} where other node is client", node.getID());

				connections.put(node, connection);
				return connection;

			} catch (IOException e) {
				throw new OdysseusNetConnectionException("Could not connect to node " + node, e);
			}
		}

		// we are client and have to connect to the node (which is the server in
		// this case)
		int nodeServerPort = determineServerPort(node);
		String nodeServerAddress = determineServerAddress(node);

		try {
			Socket clientSocket = new Socket(nodeServerAddress, nodeServerPort);
			IOdysseusNodeConnection connection = new OdysseusNodeSocketConnection(clientSocket, node);
			LOG.info("Connection to node {} where other node is server with port {}", node.getID(), nodeServerPort);

			LOG.debug("Sending our node id {}", localNode.getID());
			connection.send(localNode.getID().toString().getBytes());

			connections.put(node, connection);
			return connection;

		} catch (IOException e) {
			throw new OdysseusNetConnectionException("Could not connect to node " + node, e);
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
			IOdysseusNodeConnection connection = connections.remove(node);
			connection.disconnect();
		}
	}

	@Override
	public void stop() {
		stopServer();
	}

	@Override
	public void terminate(IOdysseusNode localNode) {
		// do nothing
	}

	@Override
	public boolean isConnected(IOdysseusNode node) {
		// TODO check connection
		return false;
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
						String uuidStr = new String(uuidStrBuffer);
						OdysseusNodeID nodeID = OdysseusNodeID.fromString(uuidStr);
						LOG.debug("Got nodeID {}", nodeID);

						synchronized( acceptedSocketsMap ) {
							acceptedSocketsMap.put(nodeID, clientSocket);
						}
						
						LOG.info("Unknown client has nodeid {}", nodeID);
					}

				} catch (IOException e) {
					LOG.error("Could not accept connection from unknown client {}", clientSocket.getInetAddress(), e);
				}
			}
		});

		t.setName("Accepting connection to client waiting: " + clientSocket.getInetAddress());
		t.setDaemon(true);
		t.start();
	}
}
