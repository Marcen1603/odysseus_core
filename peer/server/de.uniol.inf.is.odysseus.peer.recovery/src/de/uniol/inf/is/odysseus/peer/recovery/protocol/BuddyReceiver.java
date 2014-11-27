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
public class BuddyReceiver extends AbtractRepeatingMessageReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BuddyReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static BuddyReceiver cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link BuddyReceiver}.
	 */
	public static BuddyReceiver getInstance() {
		return cInstance;
	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryBuddyMessage.class);
		serv.addListener(this, RecoveryBuddyMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryBuddyMessage.class);
		serv.removeListener(this, RecoveryBuddyMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);

		if (message instanceof RecoveryBuddyMessage) {
			RecoveryBuddyMessage buddyMessage = (RecoveryBuddyMessage) message;
			beBuddy(senderPeer, buddyMessage.getSharedQueryId(),
					buddyMessage.getPQLCodes());
			RecoveryBuddyResponseMessage response = new RecoveryBuddyResponseMessage(
					buddyMessage.getUUID());

			try {
				communicator.send(senderPeer, response);
			} catch (PeerCommunicationException e) {
				LOG.error(
						"Could not send tuple send instruction response message!",
						e);
			}
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