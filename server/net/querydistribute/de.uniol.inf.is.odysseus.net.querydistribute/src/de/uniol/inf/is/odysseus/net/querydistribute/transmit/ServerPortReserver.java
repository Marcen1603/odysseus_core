package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.io.IOException;
import java.net.ServerSocket;

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
	private static final int MINPORT_DEFAULT = 10000;
	private static final int MAXPORT_DEFAULT = 20000;

	private IOdysseusNodeCommunicator nodeCommunicator;
	
	private int minPort;
	private int maxPort;
	private int nextFreePort;
	
	public void start(IOdysseusNodeCommunicator communicator) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		nodeCommunicator = communicator;
		
		
		minPort = determineMinPort();
		maxPort = determineMaxPort();
		nextFreePort = minPort;

		nodeCommunicator.registerMessageType(ReserveServerPortMessage.class);
		nodeCommunicator.registerMessageType(ServerPortReservedMessage.class);
		
		nodeCommunicator.addListener(this, ReserveServerPortMessage.class);
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
		if( message instanceof ReserveServerPortMessage) {
			int reservedPort = reservePort();
			
			ServerPortReservedMessage answer = new ServerPortReservedMessage(reservedPort);
			try {
				communicator.send(senderNode, answer);
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer for port reservation", e);
			}
		}
	}

	private synchronized int reservePort() {
		for( int port = nextFreePort; port <= maxPort; port++ ) {
			try {
				ServerSocket s = new ServerSocket(port);
				s.close();
				
				nextFreePort = port + 1;
				if( nextFreePort > maxPort ) {
					nextFreePort = minPort; // loop
				}
				
				return port;
				
			} catch (IOException e) {
				// failed to create server socket --> port not free
			}
		}
		
		LOG.error("Could not reserve a port between {} and {}", minPort, maxPort);
		
		return -1; // failed to reserve port
	}
	
	private static int determineIntConfigValue( String key, int defaultValue ) {
		String portStr = OdysseusNetConfiguration.get(key, defaultValue + "");
		try {
			return Integer.valueOf(portStr);
		} catch( Throwable t ) {
			LOG.error("Could not determine minimum port from config", t);
			return MINPORT_DEFAULT;
		}		
	}

}
