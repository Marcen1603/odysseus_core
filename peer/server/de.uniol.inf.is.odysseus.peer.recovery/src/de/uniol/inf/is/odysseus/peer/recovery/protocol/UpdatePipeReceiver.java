package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Collection;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryUpdatePipeMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryUpdatePipeResponseMessage;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;

/**
 * Entity to handle received pipe update instructions. <br />
 * Handles incoming instructions and sends acknowledge/failure messages.
 * 
 * Update a pipe: This means, that the peer has to update a JxtaSender or JxtaReceiver with the given pipeId (from the
 * message). The peer updates the PeerId which he sends to or receives from. The "pipe" itself won't be changed.
 * 
 * @author Michael Brand
 *
 */
public class UpdatePipeReceiver extends AbstractRepeatingMessageReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UpdatePipeReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static UpdatePipeReceiver cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link UpdatePipeReceiver}.
	 */
	public static UpdatePipeReceiver getInstance() {
		return cInstance;
	}

	/**
	 * The executor, if there is one bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * Binds an executor. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to bind. <br />
	 *            Must be not null.
	 */
	public static void bindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);
		cExecutor = Optional.of((IServerExecutor) serv);
		LOG.debug("Bound {} as an executor.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds an executor, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);

		if (cExecutor.isPresent() && cExecutor.get() == (IServerExecutor) serv) {

			cExecutor = Optional.absent();
			LOG.debug("Unbound {} as an executor.", serv.getClass().getSimpleName());

		}

	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryUpdatePipeMessage.class);
		serv.addListener(this, RecoveryUpdatePipeMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryUpdatePipeMessage.class);
		serv.removeListener(this, RecoveryUpdatePipeMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);

		if (message instanceof RecoveryUpdatePipeMessage) {
			RecoveryUpdatePipeMessage upMessage = (RecoveryUpdatePipeMessage) message;
			if (!mReceivedUUIDs.contains(upMessage.getUUID())) {
				mReceivedUUIDs.add(upMessage.getUUID());
			} else {
				return;
			}

			RecoveryUpdatePipeResponseMessage response = null;
			try {
				if (upMessage.isSenderUpdateInstruction()) {
					updateSender(upMessage.getNewPeerId(), upMessage.getPipeId());
				} else {
					updateReceiver(upMessage.getNewPeerId(), upMessage.getPipeId());
				}
				response = new RecoveryUpdatePipeResponseMessage(upMessage.getUUID());
			} catch (DataTransmissionException e) {
				response = new RecoveryUpdatePipeResponseMessage(upMessage.getUUID(), e.getMessage());
			}

			try {
				communicator.send(senderPeer, response);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send UpdatePipeResponse message!", e);
			}

		}
	}

	private void updateReceiver(PeerID newSender, PipeID pipeId) throws DataTransmissionException {

		if (!cExecutor.isPresent()) {

			LOG.error("No executor bound!");
			return;

		}

		// 1. Get the receiver which we have to update
		Collection<IPhysicalQuery> queries = cExecutor.get().getExecutionPlan().getQueries();
		for (IPhysicalQuery query : queries) {
			for (IPhysicalOperator op : query.getAllOperators()) {
				if (op instanceof JxtaReceiverPO) {
					JxtaReceiverPO<?> receiver = (JxtaReceiverPO<?>) op;
					if (receiver.getPipeIDString().equals(pipeId.toString())) {
						// This should be the receiver we have to update
						receiver.receiveFromNewPeer(newSender.toString());

						// There should be just one receiver with this pipeId since the ids are unique. We can stop
						// searching here.
						return;
					}
				}
			}
		}
	}

	private void updateSender(PeerID newReceiver, PipeID pipeId) throws DataTransmissionException {

		if (!cExecutor.isPresent()) {

			LOG.error("No executor bound!");
			return;

		}

		// 1. Get the sender which we have to update
		Collection<IPhysicalQuery> queries = cExecutor.get().getExecutionPlan().getQueries();
		for (IPhysicalQuery query : queries) {
			for (IPhysicalOperator op : query.getAllOperators()) {
				if (op instanceof JxtaSenderPO) {
					JxtaSenderPO<?> sender = (JxtaSenderPO<?>) op;
					if (sender.getPipeIDString().equals(pipeId.toString())) {
						// This should be the receiver we have to update
						sender.sendToNewPeer(newReceiver.toString());
						
						// There should be just one sender with this pipeId since the ids are unique. We can stop
						// searching here.
						return;
					}
				}
			}
		}
	}

}