package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryStrategyMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryStrategyResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.BackupInformationHelper;

/**
 * Entity to handle received recovery strategy information. <br />
 * Handles incoming instructions and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public class RecoveryStrategyReceiver extends AbstractRepeatingMessageReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryStrategyReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static RecoveryStrategyReceiver cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link RecoveryStrategyReceiver}.
	 */
	public static RecoveryStrategyReceiver getInstance() {
		return cInstance;
	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryStrategyMessage.class);
		serv.addListener(this, RecoveryStrategyMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryStrategyMessage.class);
		serv.removeListener(this, RecoveryStrategyMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);

		if (message instanceof RecoveryStrategyMessage) {
			RecoveryStrategyMessage stratMessage = (RecoveryStrategyMessage) message;
			if (!mReceivedUUIDs.contains(stratMessage.getUUID())) {
				mReceivedUUIDs.add(stratMessage.getUUID());
			} else {
				return;
			}

			RecoveryStrategyResponseMessage response = null;
			try {
				BackupInformationHelper.setRecoveryStrategy(
						stratMessage.getStrategy(), stratMessage.getPQLCode());
				response = new RecoveryStrategyResponseMessage(
						stratMessage.getUUID());
			} catch (Exception e) {
				response = new RecoveryStrategyResponseMessage(
						stratMessage.getUUID(), e.getMessage());
			}

			try {
				communicator.send(senderPeer, response);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send recovery strategy response message!",
						e);
			}
		}
	}

}