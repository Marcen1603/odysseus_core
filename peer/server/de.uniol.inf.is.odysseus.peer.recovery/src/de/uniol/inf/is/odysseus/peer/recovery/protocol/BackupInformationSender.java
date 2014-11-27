package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationResponseMessage;

/**
 * Entity to send backup information. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationSender extends AbstractRepeatingMessageSender {

	/**
	 * The single instance of this class.
	 */
	private static BackupInformationSender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link BackupInformationSender}.
	 */
	public static BackupInformationSender getInstance() {
		return cInstance;
	}

	/**
	 * Sends given backup information as a new information to a given peer by
	 * using a repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param info
	 *            The given backup information. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendNewBackupInfo(PeerID destination,
			IRecoveryBackupInformation info, IPeerCommunicator communicator) {
		return sendBackupInfo(destination, info,
				false, communicator);
	}

	/**
	 * Sends given backup information as an update to a given peer by using a
	 * repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param info
	 *            The given backup information. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendBackupInfoUpdate(PeerID destination,
			IRecoveryBackupInformation info, IPeerCommunicator communicator) {
		return sendBackupInfo(destination, info,
				true, communicator);
	}

	/**
	 * Sends given backup information to a given peer by using a repeating
	 * message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param info
	 *            The given backup information. <br />
	 *            Must be not null.
	 * @param update
	 *            True for a update of backup information; false for new backup
	 *            information.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	private boolean sendBackupInfo(PeerID destination,
			IRecoveryBackupInformation info, boolean update,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(info);
		Preconditions.checkNotNull(communicator);

		BackupInformationMessage message = new BackupInformationMessage(info,
				update);
		return repeatingSend(destination, message, message.getUUID(),
				communicator);

	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(BackupInformationResponseMessage.class);
		serv.addListener(this, BackupInformationResponseMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(BackupInformationResponseMessage.class);
		serv.removeListener(this, BackupInformationResponseMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(communicator);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(message);

		if (message instanceof BackupInformationResponseMessage) {
			BackupInformationResponseMessage response = (BackupInformationResponseMessage) message;
			handleResponseMessage(response.getUUID(),
					response.getErrorMessage());
		}
	}
}