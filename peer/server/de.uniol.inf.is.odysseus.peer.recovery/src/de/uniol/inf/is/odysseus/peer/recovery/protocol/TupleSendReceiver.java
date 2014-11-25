package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryTupleSendMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryTupleSendResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * Entity to handle received tuple send instructions. <br />
 * Handles incoming instructions and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public class TupleSendReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(TupleSendReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static TupleSendReceiver cInstance = new TupleSendReceiver();

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link TupleSendReceiver}.
	 */
	public static TupleSendReceiver getInstance() {
		return cInstance;
	}

	/**
	 * Handling of a received tuple send message.
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
	public void receivedMessage(RecoveryTupleSendMessage message,
			PeerID sender, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(sender);
		Preconditions.checkNotNull(communicator);

		IMessage response = null;
		try {
			if (message.isHoldOnInstruction()) {
				this.holdOn(message.getPipeId());
			} else {
				this.goOn(message.getPipeId());
			}
			response = new RecoveryTupleSendResponseMessage(message.getUUID());
		} catch (Exception e) {
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

	private void holdOn(PipeID pipeId) throws Exception {
		// Here we want to store the tuples
		RecoveryHelper.startBuffering(pipeId.toString());
	}

	private void goOn(PipeID pipeId) throws Exception {
		// Here we want to empty the buffer and go on sending the tuples to the
		// next peer
		RecoveryHelper.resumeFromBuffering(pipeId.toString());
	}

}