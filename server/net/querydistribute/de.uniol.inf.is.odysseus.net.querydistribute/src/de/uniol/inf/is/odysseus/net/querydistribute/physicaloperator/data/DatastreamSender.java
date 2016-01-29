package de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator.data;

import java.net.Socket;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.querydistribute.activator.QueryDistributionPlugIn;

public class DatastreamSender implements IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(DatastreamSender.class);
	
	private final OdysseusNodeID odysseusNodeID;
	private final UUID connectionID;
	private final IOdysseusNodeCommunicator nodeCommunicator;
	
	private Exception failureException;

	public DatastreamSender(OdysseusNodeID odysseusNodeID, UUID connectionID) {
		Preconditions.checkNotNull(odysseusNodeID, "odysseusNodeID must not be null!");
		Preconditions.checkNotNull(connectionID, "connectionID must not be null!");

		this.odysseusNodeID = odysseusNodeID;
		this.connectionID = connectionID;
		this.nodeCommunicator = QueryDistributionPlugIn.getNodeCommunicator();
		this.nodeCommunicator.addListener(this, PortMessage.class);
		
		LOG.debug("Created datastream sender for connectionid {}", connectionID);
		
	}

	public void checkConnection() throws Exception {
		if( failureException != null ) {
			throw failureException;
		}
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (!senderNode.getID().equals(odysseusNodeID)) {
			return;
		}

		if (message instanceof PortMessage) {
			try {
				PortMessage portMessage = (PortMessage) message;
				if( portMessage.getConnectionID().equals(connectionID)) {
					LOG.debug("Got port message for connectionid {}", connectionID);
					
					int port = portMessage.getPort();
					LOG.debug("Port is {}", port);
					
					Optional<String> optAddressString = senderNode.getProperty("serverAddress");
					if( optAddressString.isPresent() ) {
						LOG.debug("Node address is {}", optAddressString.get());
						
						Socket socket = new Socket(optAddressString.get(), port);
						LOG.debug("Opened socket");
					} else {
						throw new OdysseusNetException("Could not determine ip address of node " + senderNode);
					}
					
				}
			} catch( Exception e ) {
				failureException = e;
			}
		}
	}

}
