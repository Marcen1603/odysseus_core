package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.AgreementHelper;

/**
 * Entity to handle received agreements. <br />
 * Handles incoming agreements and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public class AgreementReceiver extends AbstractRepeatingMessageReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AgreementReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static AgreementReceiver cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link AgreementReceiver}.
	 */
	public static AgreementReceiver getInstance() {
		return cInstance;
	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryAgreementMessage.class);
		serv.addListener(this, RecoveryAgreementMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryAgreementMessage.class);
		serv.removeListener(this, RecoveryAgreementMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);

		if (message instanceof RecoveryAgreementMessage) {
			RecoveryAgreementMessage agMessage = (RecoveryAgreementMessage) message;
			if (!mReceivedUUIDs.contains(agMessage.getUUID())) {
				mReceivedUUIDs.add(agMessage.getUUID());
			} else {
				return;
			}

			// Check, if we want to do recovery (if not or if yes, the other
			// peer does not need to know)
			AgreementHelper.decideToRecover(agMessage.getFailedPeer(), agMessage.getLocalQueryId(), senderPeer);

			// Tell the other peer that we got this message
			RecoveryAgreementResponseMessage response = new RecoveryAgreementResponseMessage(agMessage.getUUID());

			//
			// RecoveryAgreementResponseMessage response = null;
			// if (wantToRecover) {
			// response = new
			// RecoveryAgreementResponseMessage(agMessage.getUUID());
			// } else {
			// response = new
			// RecoveryAgreementResponseMessage(agMessage.getUUID(),
			// "Don't want to do recovery");
			// }

			try {
				communicator.send(senderPeer, response);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send agreement response message!", e);
			}
		}
	}

}