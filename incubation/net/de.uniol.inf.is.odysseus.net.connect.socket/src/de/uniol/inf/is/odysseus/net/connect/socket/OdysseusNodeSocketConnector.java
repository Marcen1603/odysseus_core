package de.uniol.inf.is.odysseus.net.connect.socket;

import java.io.IOException;
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
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnector;
import de.uniol.inf.is.odysseus.net.connect.OdysseusNetConnectionException;

public class OdysseusNodeSocketConnector implements IOdysseusNodeConnector, IOdysseusNetComponent {

	private static final String SERVER_ADDRESS_PROPERTY_KEY = "serverAddress";
	private static final String SERVER_PORT_PROPERTY_KEY = "serverPort";

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeSocketConnector.class);

	private int serverPort;
	private IOdysseusNode localNode;
	
	private ServerSocket serverSocket;
	private ServerSocketAcceptThread acceptThread;
	private Map<IOdysseusNode, SocketReceiveThread> connectionsAsClient = Maps.newHashMap();

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
		if( localNode.getID().compareTo(node.getID()) == 1 ) {
			// we are server and do nothing special
			// the other node has to connect to us
			LOG.info("Connection to node {} where other node is client");
			
		} else {
			// we are client and have to connect to the node (which is the server in this case)
			int nodeServerPort = determineServerPort(node);
			String nodeServerAddress = determineServerAddress(node);
			LOG.info("Connection to node {} where other node is server with port {}", node.getID(), nodeServerPort);
			
			try {
				Socket clientSocket = new Socket(nodeServerAddress, nodeServerPort);
				SocketReceiveThread thread = new SocketReceiveThread(clientSocket);
				thread.start();
				
				connectionsAsClient.put(node, thread);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				
				thread.write("AWESOME".getBytes());
				
			} catch( IOException e ) {
				throw new OdysseusNetConnectionException("Could not connect to node " + node, e);
			}
		}
		
		return null;
	}

	private static String determineServerAddress(IOdysseusNode node) throws OdysseusNetConnectionException {
		Optional<String> optServerAddress = node.getProperty(SERVER_ADDRESS_PROPERTY_KEY);
		if( optServerAddress.isPresent() ) {
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

		if( connectionsAsClient.containsKey(node)) {
			SocketReceiveThread thread = connectionsAsClient.remove(node);
			try {
				thread.getClientSocket().close();
			} catch (IOException e) {
			}
			thread.stopRunning();
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
			acceptThread = new ServerSocketAcceptThread(serverSocket);
			acceptThread.start();

			LOG.info("Starting server finished");
		} catch (IOException e) {
			throw new OdysseusNetConnectionException("Could not start server for connections", e);
		}
	}

	private void stopServer() {
		if( acceptThread != null ) {
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
}
