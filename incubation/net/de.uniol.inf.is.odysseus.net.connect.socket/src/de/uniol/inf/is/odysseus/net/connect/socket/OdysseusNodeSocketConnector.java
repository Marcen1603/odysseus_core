package de.uniol.inf.is.odysseus.net.connect.socket;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnector;
import de.uniol.inf.is.odysseus.net.connect.OdysseusNetConnectionException;

public class OdysseusNodeSocketConnector implements IOdysseusNodeConnector, IOdysseusNetComponent {

	private static final String SERVER_PORT_PROPERTY_KEY = "serverPort";

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeSocketConnector.class);

	private int serverPort;
	private ServerSocket serverSocket;
	private ServerSocketAcceptThread acceptThread;

	@Override
	public void init(IOdysseusNode localNode) throws OdysseusNetException {
		serverPort = findFreePort();
		LOG.info("Choosing port {} as server port", serverPort);

		// port will be shared with other nodes
		localNode.addProperty(SERVER_PORT_PROPERTY_KEY, "" + serverPort);
	}

	@Override
	public void start() throws OdysseusNetException {
		startServer(serverPort);
	}

	@Override
	public IOdysseusNodeConnection connect(IOdysseusNode node) throws OdysseusNetConnectionException {
		int nodeServerPort = determineServerPort(node);
		LOG.info("Connection to node {} at its server port {}", node.getID(), nodeServerPort);

		// TODO: Connect to node as a client
		
		return null;
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
		// TODO: remove connection
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
