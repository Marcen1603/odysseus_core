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
public class TupleSendReceiver extends AbtractRepeatingMessageReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(TupleSendReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static TupleSendReceiver cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link TupleSendReceiver}.
	 */
	public static TupleSendReceiver getInstance() {
		return cInstance;
	}
	
	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryTupleSendMessage.class);
		serv.addListener(this, RecoveryTupleSendMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryTupleSendMessage.class);
		serv.removeListener(this, RecoveryTupleSendMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);
		
		if (message instanceof RecoveryTupleSendMessage) {
			RecoveryTupleSendMessage tsMessage = (RecoveryTupleSendMessage) message;
			if(!mReceivedUUIDs.contains(tsMessage.getUUID())) {
				mReceivedUUIDs.add(tsMessage.getUUID());
			} else {
				return;
			}
			
			RecoveryTupleSendResponseMessage response = null;
			try {
				if (tsMessage.isHoldOnInstruction()) {
					this.holdOn(tsMessage.getPipeId());
				} else {
					this.goOn(tsMessage.getPipeId());
				}
				response = new RecoveryTupleSendResponseMessage(tsMessage.getUUID());
			} catch (Exception e) {
				response = new RecoveryTupleSendResponseMessage(tsMessage.getUUID(),
						e.getMessage());
			}

			try {
				communicator.send(senderPeer, response);
			} catch (PeerCommunicationException e) {
				LOG.error(
						"Could not send tuple send instruction response message!",
						e);
			}
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