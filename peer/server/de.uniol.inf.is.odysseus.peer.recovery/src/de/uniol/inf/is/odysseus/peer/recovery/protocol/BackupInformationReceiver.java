package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;

/**
 * Entity to handle received backup information. <br />
 * Stores incoming backup information and sends acknowledge messages.
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationReceiver extends AbtractRepeatingMessageReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static BackupInformationReceiver cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link BackupInformationReceiver}.
	 */
	public static BackupInformationReceiver getInstance() {
		return cInstance;
	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(BackupInformationMessage.class);
		serv.addListener(this, BackupInformationMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(BackupInformationMessage.class);
		serv.removeListener(this, BackupInformationMessage.class);
	}
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);

		if (message instanceof BackupInformationMessage) {
			BackupInformationMessage biMessage = (BackupInformationMessage) message;
			if(!mReceivedUUIDs.contains(biMessage.getUUID())) {
				mReceivedUUIDs.add(biMessage.getUUID());
			} else {
				return;
			}
			
			if(biMessage.isUpdate()) {
				handleUpdateInformation(senderPeer, biMessage.getInfo());
			} else {
				handleNewInformation(biMessage.getInfo());
			}

			try {
				communicator.send(senderPeer, new BackupInformationResponseMessage(biMessage.getUUID()));
			} catch (PeerCommunicationException e) {
				LOG.error(
						"Could not send tuple send instruction response message!",
						e);
			}
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