package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryTupleSendResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryUpdatePipeMessage;

/**
 * Entity to handle received pipe update instructions. <br />
 * Handles incoming instructions and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public class UpdatePipeReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(UpdatePipeReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static UpdatePipeReceiver cInstance = new UpdatePipeReceiver();

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link UpdatePipeReceiver}.
	 */
	public static UpdatePipeReceiver getInstance() {
		return cInstance;
	}

	/**
	 * Handling of a received pipe update message.
	 * 
	 * @param message
	 *            The received message. <br />
	 *            Must be not null.
	 * @param sender
	 *            The sender of the message. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 */
	public void receivedMessage(RecoveryUpdatePipeMessage message,
			PeerID sender, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(sender);
		Preconditions.checkNotNull(communicator);

		IMessage response = null;
		try {
			if (message.isSenderUpdateInstruction()) {
				// TODO nothing to do?
			} else {
				updateReceiver(message.getNewPeerId(), message.getPipeId(),
						message.getSharedQueryId());
			}
			response = new RecoveryTupleSendResponseMessage(message.getUUID());
		} catch (DataTransmissionException e) {
			response = new RecoveryTupleSendResponseMessage(message.getUUID(),
					e.getMessage());
		}

		try {
			communicator.send(sender, response);
		} catch (PeerCommunicationException e) {
			LOG.error(
					"Could not send tuple send instruction response message!",
					e);
		}

	}
	
	private void updateReceiver(PeerID newSender, PipeID pipeId,
			ID sharedQueryId) throws DataTransmissionException {

		if (!RecoveryCommunicator.getExecutor().isPresent()) {

			LOG.error("No executor bound!");
			return;

		}

		// 1. Get the receiver, which we have to update
		Collection<IPhysicalQuery> queries = RecoveryCommunicator.getExecutor()
				.get().getExecutionPlan().getQueries();
		for (IPhysicalQuery query : queries) {
			for (IPhysicalOperator op : query.getAllOperators()) {
				if (op instanceof JxtaReceiverPO) {
					JxtaReceiverPO<?> receiver = (JxtaReceiverPO<?>) op;
					if (receiver.getPipeIDString().equals(pipeId.toString())) {
						// This should be the receiver we have to update
						receiver.receiveFromNewPeer(newSender.toString());
						break;
					}
				}
			}
		}
	}
	
}