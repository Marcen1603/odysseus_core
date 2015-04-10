package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryUpdatePipeMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryUpdatePipeResponseMessage;

/**
 * Entity to send pipe update instructions. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class UpdatePipeSender extends AbstractRepeatingMessageSender {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UpdatePipeSender.class);

	/**
	 * The single instance of this class.
	 */
	private static UpdatePipeSender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link UpdatePipeSender}.
	 */
	public static UpdatePipeSender getInstance() {
		return cInstance;
	}

	/**
	 * Sends given update sender instruction to a given peer by using a
	 * repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param peer
	 *            The new sending or receiving peer. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The affected shared query. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendSenderUpdateInstruction(PeerID destination, PipeID pipe, PeerID peer, int localQuery,
			IPeerCommunicator communicator) {
		return sendInstruction(destination, pipe, peer, localQuery, true, communicator);
	}

	/**
	 * Sends given update receiver instruction to a given peer by using a
	 * repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param peer
	 *            The new sending or receiving peer. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The affected shared query. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendReceiverUpdateInstruction(PeerID destination, PipeID pipe, PeerID peer, int localQuery,
			IPeerCommunicator communicator) {
		return sendInstruction(destination, pipe, peer, localQuery, false, communicator);
	}

	/**
	 * Sends given update receiver instruction to a given peer by using a
	 * repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param peer
	 *            The new sending or receiving peer. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The affected shared query. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @param senderUpdate
	 *            True for a sender update message; false for a receiver update
	 *            message.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	private boolean sendInstruction(PeerID destination, PipeID pipe, PeerID peer, int localQuery, boolean senderUpdate,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(pipe);
		Preconditions.checkNotNull(peer);
		Preconditions.checkNotNull(communicator);

		RecoveryUpdatePipeMessage message = new RecoveryUpdatePipeMessage(pipe, peer, localQuery, senderUpdate);

		LOG.debug("Send update pipe message to " + peer + " for local query " + localQuery + " for pipe " + pipe
				+ " SenderUpdate: " + senderUpdate);

		return repeatingSend(destination, message, message.getUUID(), communicator);
	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryUpdatePipeResponseMessage.class);
		serv.addListener(this, RecoveryUpdatePipeResponseMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryUpdatePipeResponseMessage.class);
		serv.removeListener(this, RecoveryUpdatePipeResponseMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(communicator);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(message);

		if (message instanceof RecoveryUpdatePipeResponseMessage) {
			RecoveryUpdatePipeResponseMessage response = (RecoveryUpdatePipeResponseMessage) message;
			handleResponseMessage(response.getUUID(), response.getErrorMessage());
		}
	}

}