package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryTupleSendMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryTupleSendResponseMessage;

/**
 * Entity to send tuple send instructions. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class TupleSendSender extends AbstractRepeatingMessageSender {

	/**
	 * The single instance of this class.
	 */
	private static TupleSendSender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link TupleSendSender}.
	 */
	public static TupleSendSender getInstance() {
		return cInstance;
	}

	/**
	 * Sends given hold on instruction to a given peer by using a repeating
	 * message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendHoldOnInstruction(PeerID destination, PipeID pipe,
			IPeerCommunicator communicator) {
		return sendInstruction(destination, pipe, true, communicator);
	}

	/**
	 * Sends given go on instruction to a given peer by using a repeating
	 * message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendGoOnInstruction(PeerID destination, PipeID pipe,
			IPeerCommunicator communicator) {
		return sendInstruction(destination, pipe, false, communicator);
	}

	/**
	 * Sends given go on or hold on instruction to a given peer by using a
	 * repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param holdOn
	 *            True for a hold on message; false for a go on message.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	private boolean sendInstruction(PeerID destination, PipeID pipe,
			boolean holdOn, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(pipe);
		Preconditions.checkNotNull(communicator);

		RecoveryTupleSendMessage message = new RecoveryTupleSendMessage(pipe,
				holdOn);
		return repeatingSend(destination, message, message.getUUID(),
				communicator);

	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryTupleSendResponseMessage.class);
		serv.addListener(this, RecoveryTupleSendResponseMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryTupleSendResponseMessage.class);
		serv.removeListener(this, RecoveryTupleSendResponseMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(communicator);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(message);

		if (message instanceof RecoveryTupleSendResponseMessage) {
			RecoveryTupleSendResponseMessage response = (RecoveryTupleSendResponseMessage) message;
			handleResponseMessage(response.getUUID(),
					response.getErrorMessage());
		}
	}
}