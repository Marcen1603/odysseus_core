package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.net.ServerSocket;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;

public class ServerPortReserver implements IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(ServerPortReserver.class);

	private static final String MINPORT_CONFIG_KEY = "net.querydistribute.minport";
	private static final String MAXPORT_CONFIG_KEY = "net.querydistribute.maxport";
	private static final String RANDOMPORT_CONFIG_KEY = "net.querydistribute.randomport";
	private static final int MINPORT_DEFAULT = 10000;
	private static final int MAXPORT_DEFAULT = 20000;
	private static final boolean RANDOMPORT_DEFAULT = false;
	private static final int MAX_RANDOM_TRIES = 10;

	private static final Random RAND = new Random();

	private IOdysseusNodeCommunicator nodeCommunicator;

	private int minPort;
	private int maxPort;
	private boolean randomPort;
	private int nextFreePort;

	public void start(IOdysseusNodeCommunicator communicator) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		nodeCommunicator = communicator;

		minPort = determineMinPort();
		maxPort = determineMaxPort();
		randomPort = determineRandomPort();
		nextFreePort = minPort;

		nodeCommunicator.registerMessageType(ReserveServerPortMessage.class);
		nodeCommunicator.registerMessageType(ServerPortReservedMessage.class);

		nodeCommunicator.addListener(this, ReserveServerPortMessage.class);

		if (LOG.isDebugEnabled()) {
			LOG.debug("ServerPortReserver started. MinPort is {}, maxPort is {}", minPort, maxPort);
			if (randomPort) {
				LOG.debug("ServerPortReserver chooses randomly");
			} else {
				LOG.debug("ServerPortReserver chooses linear from min to max");
			}
		}
	}

	private static boolean determineRandomPort() {
		return determineBooleanConfigValue(RANDOMPORT_CONFIG_KEY, RANDOMPORT_DEFAULT);
	}

	private static int determineMinPort() {
		return determineIntConfigValue(MINPORT_CONFIG_KEY, MINPORT_DEFAULT);
	}

	private static int determineMaxPort() {
		return determineIntConfigValue(MAXPORT_CONFIG_KEY, MAXPORT_DEFAULT);
	}

	public void stop() {
		nodeCommunicator.unregisterMessageType(ReserveServerPortMessage.class);
		nodeCommunicator.unregisterMessageType(ServerPortReservedMessage.class);
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (message instanceof ReserveServerPortMessage) {
			LOG.debug("Beginning reserving port for node {}", senderNode);
			int reservedPort = reservePort();
			LOG.debug("Reserved port is {}", reservedPort);

			ServerPortReservedMessage answer = new ServerPortReservedMessage(reservedPort);
			try {
				communicator.send(senderNode, answer);
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer for port reservation", e);
			}
		}
	}

	private synchronized int reservePort() {
		if (!randomPort) {
			for (int port = nextFreePort; port <= maxPort; port++) {
				if (isPortFree(port)) {
					nextFreePort = port + 1;
					if (nextFreePort > maxPort) {
						nextFreePort = minPort; // loop
					}

					return port;
				}
			}
		} else {
			for( int tryIndex = 0; tryIndex < MAX_RANDOM_TRIES; tryIndex++ ) {
				int port = RAND.nextInt(maxPort - minPort) + minPort;
				if(isPortFree(port)) {
					return port;
				}
			}
		}

		LOG.error("Could not reserve a port between {} and {}", minPort, maxPort);

		return -1; // failed to reserve port
	}

	private boolean isPortFree(int port) {
		try {
			ServerSocket s = new ServerSocket(port);
			s.close();
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	private static int determineIntConfigValue(String key, int defaultValue) {
		String cfgValue = OdysseusNetConfiguration.get(key, defaultValue + "");
		try {
			return Integer.valueOf(cfgValue);
		} catch (Throwable t) {
			LOG.error("Could not determine minimum port from config", t);
			return defaultValue;
		}
	}

	private static boolean determineBooleanConfigValue(String key, boolean defaultValue) {
		String cfgValue = OdysseusNetConfiguration.get(key, defaultValue + "");
		try {
			return Boolean.valueOf(cfgValue);
		} catch (Throwable t) {
			LOG.error("Could not determine minimum port from config", t);
			return defaultValue;
		}
	}
}
