package de.uniol.inf.is.odysseus.peer.recovery.internal;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationAckMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;

/**
 * Entity to handle received backup information. <br />
 * Stores incoming backup information and sends acknowledge messages.
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static BackupInformationReceiver cInstance = new BackupInformationReceiver();

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link BackupInformationReceiver}.
	 */
	public static BackupInformationReceiver getInstance() {
		return cInstance;
	}

	/**
	 * Handling of a received backup information message.
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
	public void receivedMessage(BackupInformationMessage message,
			PeerID sender, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(sender);
		Preconditions.checkNotNull(communicator);

		switch (message.getMessageType()) {
		case BackupInformationMessage.NEW_INFO:
			handleNewInformation(message.getInfo());
			break;
		case BackupInformationMessage.UPDATE_INFO:
			handleUpdateInformation(sender, message.getInfo());
			break;
		default:
			LOG.error("Unknown backup information message type: {}",
					message.getMessageType());
			return;
		}

		try {
			communicator.send(sender,
					new BackupInformationAckMessage(message.getInfo()));
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send backup information ack message!", e);
		}

	}

	/**
	 * Handles new information -> Just saves it
	 * 
	 * @param info
	 *            The backup-information to handle
	 */
	private void handleNewInformation(IRecoveryBackupInformation info) {
		LocalBackupInformationAccess.getStore().add(info);
	}

	/**
	 * Handles updated information. Checks, if we already have information about
	 * this peer with this shared query id and if so, updates this information
	 * 
	 * @param sender
	 *            Sender of this message
	 * @param info
	 *            The informationMessage to handle
	 */
	private void handleUpdateInformation(PeerID sender,
			IRecoveryBackupInformation info) {
		// Just save the info, if we already have information about this
		if (LocalBackupInformationAccess.getStoredPQLStatements(
				info.getSharedQuery(), sender) != null) {

			LocalBackupInformationAccess.updateInfo(info.getSharedQuery(),
					info.getAboutPeer(), info.getPQL(), info.getLocationPeer(),
					info.getLocalPQL());
		}
	}
}