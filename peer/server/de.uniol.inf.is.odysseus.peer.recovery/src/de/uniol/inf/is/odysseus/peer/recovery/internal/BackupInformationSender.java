package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Map;

import net.jxta.id.ID;
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
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationFailMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;

// TODO javaDoc
public class BackupInformationSender implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationSender.class);

	private static final String OK_RESULT = "OK";

	private static final Map<ID, RepeatingMessageSend> senderMap = Maps
			.newConcurrentMap();
	private static final Map<ID, PeerID> sendDestinationMap = Maps
			.newConcurrentMap();
	private static final Map<ID, String> sendResultMap = Maps
			.newConcurrentMap();

	/**
	 * The peer communicator, if there is one bound.
	 */
	private static Optional<IPeerCommunicator> cPeerCommunicator = Optional
			.absent();

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
		
		cPeerCommunicator.get().registerMessageType(
				BackupInformationFailMessage.class);
		cPeerCommunicator.get().addListener(this,
				BackupInformationFailMessage.class);		
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

		cPeerCommunicator.get().unregisterMessageType(
				BackupInformationFailMessage.class);
		cPeerCommunicator.get().removeListener(this,
				BackupInformationFailMessage.class);
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
		}
	}

	public static boolean sendNewBackupInfo(PeerID destination,
			IRecoveryBackupInformation info) {
		return sendBackupInfo(destination, info,
				BackupInformationMessage.NEW_INFO);
	}

	public static boolean sendBackupInfoUpdate(PeerID destination,
			IRecoveryBackupInformation info) {
		return sendBackupInfo(destination, info,
				BackupInformationMessage.UPDATE_INFO);
	}

	private static boolean sendBackupInfo(PeerID destination,
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

		synchronized (senderMap) {
			senderMap.put(info.getSharedQuery(), msgSender);
		}
		synchronized (sendDestinationMap) {
			sendDestinationMap.put(info.getSharedQuery(), destination);
		}

		msgSender.start();
		LOG.debug("Sent backup info {} to peerID {}", info, destination);

		LOG.debug("Waiting for response from peer...");
		while (senderIsActive(info.getSharedQuery())) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO
			}

		}

		String result = "";
		synchronized (sendResultMap) {
			if (!sendResultMap.containsKey(info.getSharedQuery())) {
				result = "Could not send backup information: Peer is not reachable!";
			} else {
				result = sendResultMap.get(info.getSharedQuery());
				sendResultMap.remove(info.getSharedQuery());
			}

		}

		if (result.equals(OK_RESULT)) {
			return true;
		}

		// else
		synchronized (senderMap) {
			senderMap.remove(info.getSharedQuery());
		}
		synchronized (sendDestinationMap) {
			sendDestinationMap.remove(info.getSharedQuery());
		}

		LOG.error("Could not send backup information: {}", result);
		return false;

	}

	private static boolean senderIsActive(ID sharedQuery) {
		synchronized (senderMap) {
			if (!senderMap.containsKey(sharedQuery)) {
				return false;
			}

			// else
			return senderMap.get(sharedQuery).isRunning();
		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		if (message instanceof BackupInformationAckMessage) {
			BackupInformationAckMessage ackMessage = (BackupInformationAckMessage) message;

			synchronized (senderMap) {
				RepeatingMessageSend sender = senderMap.get(ackMessage
						.getSharedQueryID());
				
				if (sender != null) {
					sender.stopRunning();
					senderMap.remove(ackMessage.getSharedQueryID());
				}
			}

			synchronized (sendResultMap) {
				sendResultMap.put(ackMessage.getSharedQueryID(), OK_RESULT);
			}

		} else if (message instanceof BackupInformationFailMessage) {
			BackupInformationFailMessage failMessage = (BackupInformationFailMessage) message;

			synchronized (senderMap) {
				RepeatingMessageSend sender = senderMap.get(failMessage
						.getSharedQueryID());

				if (sender != null) {
					sender.stopRunning();
					senderMap.remove(failMessage.getSharedQueryID());
				}
			}

			sendResultMap.put(failMessage.getSharedQueryID(),
					failMessage.getErrorMessage());
		}
	}
}