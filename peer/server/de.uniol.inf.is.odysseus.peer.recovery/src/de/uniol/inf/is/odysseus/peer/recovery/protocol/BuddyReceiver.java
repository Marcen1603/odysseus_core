package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryBuddyMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryBuddyResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;

/**
 * Entity to handle received buddy instructions. <br />
 * Handles incoming instructions and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public class BuddyReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BuddyReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static BuddyReceiver cInstance = new BuddyReceiver();

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link BuddyReceiver}.
	 */
	public static BuddyReceiver getInstance() {
		return cInstance;
	}

	/**
	 * Handling of a received buddy message.
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
	public void receivedMessage(RecoveryBuddyMessage message, PeerID sender,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(sender);
		Preconditions.checkNotNull(communicator);

		beBuddy(sender, message.getSharedQueryId(), message.getPQLCodes());
		IMessage response = new RecoveryBuddyResponseMessage(message.getUUID());

		try {
			communicator.send(sender, response);
		} catch (PeerCommunicationException e) {
			LOG.error(
					"Could not send tuple send instruction response message!",
					e);
		}

	}

	/**
	 * Saves, that this peer is a buddy for the given peer and saves the
	 * corresponding backup-information
	 * 
	 * @param sender
	 *            The sender of the message - this is the peer we will be the
	 *            buddy for
	 * @param sharedQueryId
	 *            The id of the shared query we ware the buddy for
	 * @param pqls
	 *            The PQL-Strings with the information about the peer we are the
	 *            buddy for
	 */
	private void beBuddy(PeerID sender, ID sharedQueryId, List<String> pqls) {
		LocalBackupInformationAccess.addBuddy(sender, sharedQueryId);
		IRecoveryBackupInformation info = new BackupInformation();
		info.setAboutPeer(sender);
		info.setSharedQuery(sharedQueryId);
		String totalPQL = "";
		for (String pql : pqls) {
			totalPQL += " " + pql;
		}
		info.setPQL(totalPQL);
		// This info is meant to be used in this peer
		info.setLocationPeer(RecoveryCommunicator.getP2PNetworkManager().get()
				.getLocalPeerID());
		LocalBackupInformationAccess.getStore().add(info);
		LOG.debug("I am now the buddy for {}", RecoveryCommunicator
				.getPeerDictionary().get().getRemotePeerName(sender));
	}

}