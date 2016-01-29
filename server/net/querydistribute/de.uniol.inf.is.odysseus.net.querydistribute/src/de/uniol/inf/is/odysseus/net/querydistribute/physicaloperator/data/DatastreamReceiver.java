package de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator.data;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.querydistribute.activator.QueryDistributionPlugIn;

public class DatastreamReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(DatastreamReceiver.class);
	
	private final OdysseusNodeID odysseusNodeID;
	private final UUID connectionID;
	private final IOdysseusNodeCommunicator nodeCommunicator;
	
	private IOdysseusNode odysseusNode;
	private Exception connectionFailure;
	
	public DatastreamReceiver(OdysseusNodeID odysseusNodeID, UUID connectionID) {
		Preconditions.checkNotNull(odysseusNodeID, "odysseusNodeID must not be null!");
		Preconditions.checkNotNull(connectionID, "connectionID must not be null!");

		this.odysseusNodeID = odysseusNodeID;
		this.connectionID = connectionID;
		this.nodeCommunicator = QueryDistributionPlugIn.getNodeCommunicator();
		
		LOG.debug("Created datastream receiver for connection {}", connectionID);
		
	}

	public void connect() {
		LOG.debug("Beginning connection for connectionid {}", connectionID);
		
		try {
			Optional<IOdysseusNode> optNode = QueryDistributionPlugIn.getNodeManager().getNode(odysseusNodeID);
			if( optNode.isPresent() ) {
				odysseusNode = optNode.get();
				LOG.debug("Node connecting to is {}", odysseusNode);
				
			} else {
				throw new OdysseusNetException("Could not determine node with id " + odysseusNodeID);
			}

			ServerSocket serverSocket = new ServerSocket(0);
			LOG.debug("Opened server socket on port {}", serverSocket.getLocalPort());
			
			sendPort(serverSocket.getLocalPort());
			
			// TODO: async
			LOG.debug("Waiting for socket accept...");
			Socket socket = serverSocket.accept();
			
			LOG.debug("Socket accepted!");
			
			// ...
			
		} catch (Exception e) {
			connectionFailure = e;
			
			LOG.error("Datastream receiver connection failure", e);
		}
	}

	private void sendPort(int port) throws OdysseusNodeCommunicationException {
		PortMessage message = new PortMessage(connectionID, port);
		nodeCommunicator.send(odysseusNode, message);
	}

	public void checkConnection() throws Exception {
		if( connectionFailure != null ) {
			throw connectionFailure;
		}
	}
}
