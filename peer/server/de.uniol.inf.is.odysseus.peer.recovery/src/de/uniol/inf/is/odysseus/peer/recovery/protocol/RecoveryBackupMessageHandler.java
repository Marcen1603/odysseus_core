package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;

/**
 * This class handles incoming BackupInformationMessages. It can save new information or update existing information.
 * 
 * @author Tobias Brandt
 *
 */
public class RecoveryBackupMessageHandler {

	/**
	 * Handles an incoming BackupInformationMessage, e.g. to save new information.
	 * 
	 * @param sender
	 *            Sender of this message
	 * @param backupInformationMessage
	 *            The informationMessage to handle
	 */
	public static void handleBackupMessage(PeerID sender, BackupInformationMessage backupInformationMessage) {
		int messageType = backupInformationMessage.getMessageType();

		switch (messageType) {
		case BackupInformationMessage.NEW_INFO:
			handleNewInformationMessage(backupInformationMessage);
			break;
		case BackupInformationMessage.UPDATE_INFO:
			handleUpdateInformationMessage(sender, backupInformationMessage);
			break;
		}
	}

	/**
	 * Handles new information -> Just saves it
	 * 
	 * @param backupInformationMessage
	 *            The backup-information to handle
	 */
	private static void handleNewInformationMessage(BackupInformationMessage backupInformationMessage) {
		LocalBackupInformationAccess.getStore().add(backupInformationMessage.getInfo());
	}

	/**
	 * Handles updated information. Checks, if we already have information about this peer with this shared query id and
	 * if so, updates this information
	 * 
	 * @param sender
	 *            Sender of this message
	 * @param backupInformationMessage
	 *            The informationMessage to handle
	 */
	private static void handleUpdateInformationMessage(PeerID sender, BackupInformationMessage backupInformationMessage) {
		// Just save the info, if we already have information about this
		if (LocalBackupInformationAccess.getStoredPQLStatements(backupInformationMessage.getInfo().getSharedQuery(),
				sender) != null) {

			LocalBackupInformationAccess.updateInfo(backupInformationMessage.getInfo().getSharedQuery(),
					backupInformationMessage.getInfo().getAboutPeer(), backupInformationMessage.getInfo().getPQL(),
					backupInformationMessage.getInfo().getLocationPeer(), backupInformationMessage.getInfo()
							.getLocalPQL());
		}
	}
}
