package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryBuddyMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryBuddyResponseMessage;

/**
 * Entity to send buddy instructions. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class BuddySender extends AbstractRepeatingMessageSender {

	/**
	 * The single instance of this class.
	 */
	private static BuddySender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link BuddySender}.
	 */
	public static BuddySender getInstance() {
		return cInstance;
	}

	/**
	 * Sends given buddy instruction to a given peer by using a repeating
	 * message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pqls
	 *            The PQL codes to backup. <br />
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
	public boolean sendInstruction(PeerID destination, List<String> pqls,
			ID sharedQuery, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(pqls);
		Preconditions.checkNotNull(sharedQuery);
		Preconditions.checkNotNull(communicator);

		RecoveryBuddyMessage message = new RecoveryBuddyMessage(pqls,
				sharedQuery);
		return repeatingSend(destination, message, message.getUUID(),
				communicator);

	}
	
	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryBuddyResponseMessage.class);
		serv.addListener(this, RecoveryBuddyResponseMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryBuddyResponseMessage.class);
		serv.removeListener(this, RecoveryBuddyResponseMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(communicator);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(message);
		
		if(message instanceof RecoveryBuddyResponseMessage) {
			RecoveryBuddyResponseMessage response = (RecoveryBuddyResponseMessage) message;
			handleResponseMessage(response.getUUID(), response.getErrorMessage());
		}
	}

}