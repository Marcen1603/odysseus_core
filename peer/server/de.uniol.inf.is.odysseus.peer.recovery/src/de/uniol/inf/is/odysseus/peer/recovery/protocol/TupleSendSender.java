package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;
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
public class TupleSendSender extends AbstractRepeatingMessageSender<RecoveryTupleSendResponseMessage> {

	/**
	 * The single instance of this class.
	 */
	private static TupleSendSender cInstance = new TupleSendSender();

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
	public void receivedResponseMessage(RecoveryTupleSendResponseMessage message) {
		Preconditions.checkNotNull(message);

		synchronized (mSenderMap) {
			RepeatingMessageSend sender = mSenderMap.get(message.getUUID());

			if (sender != null) {
				sender.stopRunning();
				mSenderMap.remove(message.getUUID());
			}
		}

		String result = OK_RESULT;
		if (!message.isPositive()) {
			result = message.getErrorMessage().get();
		}
		synchronized (mResultMap) {
			mResultMap.put(message.getUUID(), result);
		}

	}

}