package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementResponseMessage;

/**
 * Entity to send agreements. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class AgreementSender extends AbstractRepeatingMessageSender {

	/**
	 * The single instance of this class.
	 */
	private static AgreementSender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link AgreementSender}.
	 */
	public static AgreementSender getInstance() {
		return cInstance;
	}

	/**
	 * Sends given agreement to a given peer by using a repeating message send
	 * process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param failedPeer
	 *            The given failed peer. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The given shared query. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendAgreement(PeerID destination, PeerID failedPeer,
			ID sharedQuery, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(failedPeer);
		Preconditions.checkNotNull(sharedQuery);
		Preconditions.checkNotNull(communicator);

		RecoveryAgreementMessage message = new RecoveryAgreementMessage(
				failedPeer, sharedQuery);
		return repeatingSend(destination, message, message.getUUID(),
				communicator);

	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryAgreementResponseMessage.class);
		serv.addListener(this, RecoveryAgreementResponseMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryAgreementResponseMessage.class);
		serv.removeListener(this, RecoveryAgreementResponseMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(communicator);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(message);

		if (message instanceof RecoveryAgreementResponseMessage) {
			RecoveryAgreementResponseMessage response = (RecoveryAgreementResponseMessage) message;
			handleResponseMessage(response.getUUID(),
					response.getErrorMessage());
		}
	}

}