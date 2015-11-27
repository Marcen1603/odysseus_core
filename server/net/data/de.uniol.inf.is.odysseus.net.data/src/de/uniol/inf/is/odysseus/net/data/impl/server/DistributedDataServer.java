package de.uniol.inf.is.odysseus.net.data.impl.server;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
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
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCreatedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataDestroyedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.MultipleDistributedDataDestroyedMessage;

public class DistributedDataServer implements IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedDataServer.class);
	
	private final IDistributedDataCreator creator;
	private final IOdysseusNodeCommunicator communicator;
	
	public DistributedDataServer( IOdysseusNodeCommunicator communicator, IDistributedDataCreator creator ) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		Preconditions.checkNotNull(creator, "creator must not be null!");

		this.creator = creator;
		this.communicator = communicator;
		
		communicator.addListener(this, CreateDistributedDataMessage.class);
		communicator.addListener(this, DestroyDistributedDataWithUUIDMessage.class);
		communicator.addListener(this, DestroyDistributedDataWithNameMessage.class);
	}

	public void stop() {
		communicator.removeListener(this, CreateDistributedDataMessage.class);
		communicator.removeListener(this, DestroyDistributedDataWithUUIDMessage.class);
		communicator.removeListener(this, DestroyDistributedDataWithNameMessage.class);
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
		} else if( message instanceof DestroyDistributedDataWithUUIDMessage ) {
			DestroyDistributedDataWithUUIDMessage msg = (DestroyDistributedDataWithUUIDMessage)message;
			
			try {
				Optional<IDistributedData> optDestroyedData = creator.destroy(senderNode.getID(), msg.getUUID());
				if( optDestroyedData.isPresent() ) {
					IDistributedData destroyedData = optDestroyedData.get();
					
					DistributedDataDestroyedMessage answer = new DistributedDataDestroyedMessage(destroyedData);
					communicator.send(senderNode, answer);
				} else {
					// TODO: Handle it!
				}
				
			} catch (DistributedDataException e) {
				// TODO: Handle it!
				
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of destroy distributed data message", e);
			}
		} else if( message instanceof DestroyDistributedDataWithNameMessage ) {
			DestroyDistributedDataWithNameMessage msg = (DestroyDistributedDataWithNameMessage)message;
			
			try {
				Collection<IDistributedData> destroyedData = creator.destroy(senderNode.getID(), msg.getName());
				
				MultipleDistributedDataDestroyedMessage answer = new MultipleDistributedDataDestroyedMessage(destroyedData);
				communicator.send(senderNode, answer);
			} catch (DistributedDataException e) {
				// TODO: Handle it!
				
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of destroy distributed data message", e);
			}
		}
	}
}
