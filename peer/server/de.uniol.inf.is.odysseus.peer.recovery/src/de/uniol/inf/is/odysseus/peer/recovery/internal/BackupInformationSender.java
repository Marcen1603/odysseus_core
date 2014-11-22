package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Map;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
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
public class BackupInformationSender implements IPeerCommunicatorListener {

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
	private static BackupInformationSender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link BackupInformationSender} or null, if no
	 *         {@link IPeerCommunicator} is bound.
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
	 * The peer communicator, if there is one bound.
	 */
	private Optional<IPeerCommunicator> cPeerCommunicator = Optional.absent();

	/**
	 * Registers messages and listeners at the peer communicator.
	 */
	private void registerMessagesAndAddListeners() {
		Preconditions.checkArgument(cPeerCommunicator.isPresent());

		cPeerCommunicator.get().registerMessageType(
				BackupInformationMessage.class);
		cPeerCommunicator.get().addListener(this,
				BackupInformationMessage.class);

		cPeerCommunicator.get().registerMessageType(
				BackupInformationAckMessage.class);
		cPeerCommunicator.get().addListener(this,
				BackupInformationAckMessage.class);
		
	}

	/**
	 * Unregisters messages and listeners at the peer communicator.
	 */
	private void unregisterMessagesAndAddListeners() {
		Preconditions.checkArgument(cPeerCommunicator.isPresent());

		cPeerCommunicator.get().unregisterMessageType(
				BackupInformationMessage.class);
		cPeerCommunicator.get().removeListener(this,
				BackupInformationMessage.class);

		cPeerCommunicator.get().unregisterMessageType(
				BackupInformationAckMessage.class);
		cPeerCommunicator.get().removeListener(this,
				BackupInformationAckMessage.class);
		
	}

	/**
	 * Binds a peer communicator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The peer communicator to bind. <br />
	 *            Must be not null.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		Preconditions.checkNotNull(serv);
		cPeerCommunicator = Optional.of(serv);
		this.registerMessagesAndAddListeners();
		LOG.debug("Bound {} as a peer communicator.", serv.getClass()
				.getSimpleName());
		cInstance = this;
	}

	/**
	 * Unbinds a peer communicator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The peer communicator to unbind. <br />
	 *            Must be not null.
	 */
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		Preconditions.checkNotNull(serv);

		if (cPeerCommunicator.isPresent() && cPeerCommunicator.get() == serv) {
			this.unregisterMessagesAndAddListeners();
			cPeerCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a peer communicator.", serv.getClass()
					.getSimpleName());
			cInstance = null;
		}
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
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendNewBackupInfo(PeerID destination,
			IRecoveryBackupInformation info) {
		return sendBackupInfo(destination, info,
				BackupInformationMessage.NEW_INFO);
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
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendBackupInfoUpdate(PeerID destination,
			IRecoveryBackupInformation info) {
		return sendBackupInfo(destination, info,
				BackupInformationMessage.UPDATE_INFO);
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
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	private boolean sendBackupInfo(PeerID destination,
			IRecoveryBackupInformation info, int messageType) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(info);
		if (!cPeerCommunicator.isPresent()) {
			LOG.error("No peer communicator bound!");
			return false;
		}

		BackupInformationMessage message = new BackupInformationMessage(info,
				messageType);
		RepeatingMessageSend msgSender = new RepeatingMessageSend(
				cPeerCommunicator.get(), message, destination);

		synchronized (cSenderMap) {
			cSenderMap.put(info.getUUID(), msgSender);
		}
		synchronized (cDestinationMap) {
			cDestinationMap.put(info.getUUID(), destination);
		}

		msgSender.start();
		LOG.debug("Sent backup info {} to peerID {}", info, destination);

		LOG.debug("Waiting for response from peer...");
		while (senderIsActive(info.getUUID())) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {

				LOG.error("...interrupted!", e);
				return false;

			}

		}

		String result = "";
		synchronized (cResultMap) {
			if (!cResultMap.containsKey(info.getUUID())) {
				result = "Could not send backup information: Peer is not reachable!";
			} else {
				result = OK_RESULT;
			}
			cResultMap.remove(info.getUUID());

		}
		synchronized (cSenderMap) {
			cSenderMap.remove(info.getUUID());
		}
		synchronized (cDestinationMap) {
			cDestinationMap.remove(info.getUUID());
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

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		if (message instanceof BackupInformationAckMessage) {
			BackupInformationAckMessage ackMessage = (BackupInformationAckMessage) message;
			
			synchronized (cSenderMap) {
				RepeatingMessageSend sender = cSenderMap.get(ackMessage
						.getUUID());

				if (sender != null) {
					sender.stopRunning();
					cSenderMap.remove(ackMessage.getUUID());
				}
			}

			synchronized (cResultMap) {
				cResultMap.put(ackMessage.getUUID(), true);
			}

		}
		
	}
}