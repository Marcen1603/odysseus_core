package de.uniol.inf.is.odysseus.net.data.impl.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataCreator;
import de.uniol.inf.is.odysseus.net.data.impl.message.CreateDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCreatedMessage;

public class DistributedDataServer implements IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedDataServer.class);
	
	private final IDistributedDataCreator creator;
	
	public DistributedDataServer( IOdysseusNodeCommunicator communicator, IDistributedDataCreator creator ) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		Preconditions.checkNotNull(creator, "creator must not be null!");

		this.creator = creator;
		
		communicator.addListener(this, CreateDistributedDataMessage.class);
	}

	public void stop() {
		
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if( message instanceof CreateDistributedDataMessage) {
			CreateDistributedDataMessage msg = (CreateDistributedDataMessage)message;
			
			try {
				IDistributedData createdData = creator.create(senderNode.getID(), msg.getData(), msg.getName(), msg.isPersistent(), msg.getLifetime());
				
				DistributedDataCreatedMessage answer = new DistributedDataCreatedMessage(createdData);
				communicator.send(senderNode, answer);
				
			} catch( DistributedDataException e ) {
				// TODO: Handle it!
				
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of create distributed data message", e);
			}
		}
	}
}
