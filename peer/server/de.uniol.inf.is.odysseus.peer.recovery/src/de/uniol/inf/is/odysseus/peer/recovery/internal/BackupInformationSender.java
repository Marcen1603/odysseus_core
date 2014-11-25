package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Map;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationAckMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;

/**
 * Entity to send backup information. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationSender {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationSender.class);

	/**
	 * The result code for successes.
	 */
	private static final String OK_RESULT = "OK";

	/**
	 * The single instance of this class.
	 */
	private static BackupInformationSender cInstance = new BackupInformationSender();

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link BackupInformationSender}.
	 */
	public static BackupInformationSender getInstance() {
		return cInstance;
	}

	/**
	 * Data structure to keep all currently running repeating message send
	 * processes in mind.
	 */
	private final Map<UUID, RepeatingMessageSend> cSenderMap = Maps
			.newConcurrentMap();

	/**
	 * Data structure to keep the destinations of all currently running
	 * repeating message send processes in mind.
	 */
	private final Map<UUID, PeerID> cDestinationMap = Maps.newConcurrentMap();

	/**
	 * Data structure to keep already received results of all currently running
	 * repeating message send processes in mind.
	 */
	private final Map<UUID, Boolean> cResultMap = Maps.newConcurrentMap();

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
				BackupInformationMessage.NEW_INFO, communicator);
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
				BackupInformationMessage.UPDATE_INFO, communicator);
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
	 * @param messageType
	 *            {@link BackupInformationMessage#NEW_INFO} or
	 *            {@link BackupInformationMessage#UPDATE_INFO}.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	private boolean sendBackupInfo(PeerID destination,
			IRecoveryBackupInformation info, int messageType,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(info);
		Preconditions.checkNotNull(communicator);

		BackupInformationMessage message = new BackupInformationMessage(info,
				messageType);
		RepeatingMessageSend msgSender = new RepeatingMessageSend(communicator,
				message, destination);

		synchronized (cSenderMap) {
			cSenderMap.put(message.getUUID(), msgSender);
		}
		synchronized (cDestinationMap) {
			cDestinationMap.put(message.getUUID(), destination);
		}

		msgSender.start();
		LOG.debug("Sent backup info {} to peerID {}", info, destination);

		LOG.debug("Waiting for response from peer...");
		while (senderIsActive(message.getUUID())) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {

				LOG.error("...interrupted!", e);
				return false;

			}

		}

		String result = "";
		synchronized (cResultMap) {
			if (!cResultMap.containsKey(message.getUUID())) {
				result = "Could not send backup information: Peer is not reachable!";
			} else {
				result = OK_RESULT;
			}
			cResultMap.remove(message.getUUID());

		}
		synchronized (cSenderMap) {
			cSenderMap.remove(message.getUUID());
		}
		synchronized (cDestinationMap) {
			cDestinationMap.remove(message.getUUID());
		}

		if (result.equals(OK_RESULT)) {
			return true;
		}

		// else
		LOG.error("Could not send backup information: {}", result);
		return false;

	}

	/**
	 * Checks, if a given repeating message send process is still active.
	 * 
	 * @param id
	 *            The UUID of the backup information identifying the repeating
	 *            message send process. <br />
	 *            Must be not null.
	 * @return True, if the given repeating message send process is still
	 *         active; false, else.
	 */
	private boolean senderIsActive(UUID id) {
		Preconditions.checkNotNull(id);

		synchronized (cSenderMap) {
			if (!cSenderMap.containsKey(id)) {
				return false;
			}

			// else
			return cSenderMap.get(id).isRunning();
		}
	}

	/**
	 * Handling of a received acknowledge message.
	 * 
	 * @param message
	 *            The received message. <br />
	 *            Must be not null.
	 */
	public void receivedAckMessage(BackupInformationAckMessage message) {
		Preconditions.checkNotNull(message);

		synchronized (cSenderMap) {
			RepeatingMessageSend sender = cSenderMap.get(message.getUUID());

			if (sender != null) {
				sender.stopRunning();
				cSenderMap.remove(message.getUUID());
			}
		}

		synchronized (cResultMap) {
			cResultMap.put(message.getUUID(), true);
		}

	}
}