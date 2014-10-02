package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryP2PListener;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupJxtaInfoMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryAgreementHandler;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryInstructionHandler;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;

/**
 * A recovery communicator handles the communication between peers for recovery
 * mechanisms.
 * 
 * Attention: Under construction
 * 
 * @author Tobias Brandt & Michael Brand & Simon Kuespert
 *
 */
public class RecoveryCommunicator implements IRecoveryCommunicator,
		IPeerCommunicatorListener, Observer {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryCommunicator.class);

	// @formatter:off
	/**
	 * Plan (try to find some ideas) 1. Someone (who?) tells us that a peer
	 * failed and which query this peer had (this should be part of LSOP-213) 2.
	 * We search for another peer who can install this query 3. A new peer is
	 * found, we just take the first for now 4. We tell this peer to install the
	 * query 5. We connect the peers so that the new peer is known by the right
	 * peers
	 * 
	 * When we have this we add the second part (right side of diagram) 1. Tell
	 * the peers which are before the failed peer that the peer failed 2. Save
	 * the tuples there 3. When the new peer is there -> direct saved tuples to
	 * the new peer
	 * 
	 * And when we have this we add a third part 1. Add an error-protocol to
	 * handle errors during the recovery? (maybe like loadBalancing?)
	 * 
	 */
	// @formatter:on

	public static final String JXTA_KEY_RECEIVER_PIPE_ID = "receiverPipeId";
	public static final String JXTA_KEY_SENDER_PIPE_ID = "senderPipeId";

	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	private static IPeerCommunicator peerCommunicator;
	private static IRecoveryP2PListener recoveryP2PListener;
	/**
	 * Executor to get queries
	 */
	private static IServerExecutor executor;
	/**
	 * Active Session. Use getActiveSession() to avoid null pointers.
	 */
	private static ISession activeSession;

	private static RecoveryCommunicator instance;

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {
		instance = this;
		if (recoveryP2PListener != null)
			recoveryP2PListener.addObserver(this);
	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
		instance = null;
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Peer Communicator (registers all Messages and
	 * adds Listeners)
	 * 
	 * @param serv
	 *            PeerCommunicator to bind.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		peerCommunicator.registerMessageType(RecoveryInstructionMessage.class);
		peerCommunicator.addListener(this, RecoveryInstructionMessage.class);
		peerCommunicator.registerMessageType(BackupInformationMessage.class);
		peerCommunicator.addListener(this, BackupInformationMessage.class);
		peerCommunicator.registerMessageType(BackupJxtaInfoMessage.class);
		peerCommunicator.addListener(this, BackupJxtaInfoMessage.class);
	}

	/**
	 * called by OSGi-DS to unbind Peer Communicator (unregisters all Messages
	 * and removes Listeners)
	 * 
	 * @param serv
	 *            Peer Communicator to unbind.
	 * 
	 */
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this,
					RecoveryInstructionMessage.class);
			peerCommunicator
					.unregisterMessageType(RecoveryInstructionMessage.class);
			peerCommunicator.removeListener(this,
					BackupInformationMessage.class);
			peerCommunicator.removeListener(this, BackupJxtaInfoMessage.class);
			peerCommunicator = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Executor
	 * 
	 * @param exe
	 *            Executor to bind.
	 */
	public static void bindExecutor(IExecutor exe) {
		executor = (IServerExecutor) exe;
	}

	/**
	 * called by OSGi-DS to unbind Executor
	 * 
	 * @param exe
	 *            Executor to unbind.
	 */
	public static void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public static void bindRecoveryP2PListener(IRecoveryP2PListener serv) {
		recoveryP2PListener = serv;
	}

	// called by OSGi-DS
	public static void unbindRecoveryP2PListener(IRecoveryP2PListener serv) {
		if (recoveryP2PListener == serv)
			recoveryP2PListener = null;
	}

	/**
	 * Gets currently active Session.
	 * 
	 * @return active Session
	 */
	public static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}

	/**
	 * Getter for Executor
	 * 
	 * @return
	 */
	public static IServerExecutor getExecutor() {
		return executor;
	}

	public static RecoveryCommunicator getInstance() {
		return instance;
	}

	public static IP2PDictionary getP2pDictionary() {
		return p2pDictionary;
	}

	public static void setP2pDictionary(IP2PDictionary p2pDictionary) {
		RecoveryCommunicator.p2pDictionary = p2pDictionary;
	}

	public static IP2PNetworkManager getP2pNetworkManager() {
		return p2pNetworkManager;
	}

	public static void setP2pNetworkManager(IP2PNetworkManager p2pNetworkManager) {
		RecoveryCommunicator.p2pNetworkManager = p2pNetworkManager;
	}

	// -----------------------------------------------------
	// Code with recovery logic
	// -----------------------------------------------------

	@Override
	public void recover(PeerID failedPeer) {

		// 1. Check, if there was something on the failed peer
		// Return if there is no backup information stored for the given peer

		if (LocalBackupInformationAccess
				.getStoredSharedQueryIdsForPeer(failedPeer) == null) {
			return;
		}

		// 2. Search for another peer who can take the parts from the failed
		// peer

		// For now, take a random peer. Here we have to create an interface for
		// allocation strategies

		int numOfPeers = p2pDictionary.getRemotePeerIDs().size();
		int peerWeTake = (int) (Math.random() * numOfPeers);

		Iterator<PeerID> peers = p2pDictionary.getRemotePeerIDs().iterator();
		PeerID peer = null;

		int counter = 0;
		while (peers.hasNext()) {

			if (counter == peerWeTake) {
				peer = peers.next();
				break;
			}
			counter++;
			peers.next();
		}

		// If the peer if null, we don't know any other peer so we have to
		// install it on ourself
		if (peer == null)
			peer = p2pNetworkManager.getLocalPeerID();

		// TODO Use RecoveryAgreementHandler

		// 3. Tell the new peer to install the parts from the failed peer

	}

	public void sendHoldOnMessages(List<PeerID> peers, List<ID> queryIds) {
		try {
			for (int i = 0; i < peers.size(); i++) {
				RecoveryInstructionMessage holdOnMessage = RecoveryInstructionMessage
						.createHoldOnMessage(queryIds.get(i));
				peerCommunicator.send(peers.get(i), holdOnMessage);
			}
		} catch (Exception e) {

		}
	}

	public void installQueriesOnNewPeer(PeerID failedPeer, PeerID newPeer) {

		List<SharedQuery> sharedQueries = LocalBackupInformationAccess
				.getStoredPQLStatements(failedPeer);

		for (SharedQuery query : sharedQueries) {
			try {
				String pql = "";
				for (String pqlPart : query.getPqlParts()) {
					pql += " " + pqlPart;
				}
				RecoveryInstructionMessage addQueryMessage = RecoveryInstructionMessage
						.createAddQueryMessage(pql, query.getSharedQueryID());
				peerCommunicator.send(newPeer, addQueryMessage);
			} catch (PeerCommunicationException e) {

			}
		}
	}

	/**
	 * 
	 * @param senderPeer
	 *            The peer which gets the message, so this is the peer who is
	 *            the sender (A -> B; after the message A -> C; senderPeer is A)
	 * @param newReceiverPeer
	 *            The peer to which the tuples should be send
	 * @param sharedQueryID
	 *            The query id for which the tuples should be send to a new
	 *            receiver
	 */
	public void sendNewReceiverMessage(PeerID senderPeer,
			PeerID newReceiverPeer, ID sharedQueryID) {
		RecoveryInstructionMessage message = RecoveryInstructionMessage
				.createNewReceiverMessage(newReceiverPeer, sharedQueryID);
		try {
			peerCommunicator.send(senderPeer, message);
		} catch (Exception e) {

		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		if (message instanceof RecoveryInstructionMessage) {
			RecoveryInstructionMessage instruction = (RecoveryInstructionMessage) message;
			RecoveryInstructionHandler.handleInstruction(instruction);
		} else if (message instanceof BackupInformationMessage) {

			BackupInformationMessage biMessage = (BackupInformationMessage) message;

			// Store the backup information
			LocalBackupInformationAccess.storeLocal(
					biMessage.getSharedQueryID(),
					biMessage.geBackupInformation());

		} else if (message instanceof BackupJxtaInfoMessage) {
			BackupJxtaInfoMessage jxtaMessage = (BackupJxtaInfoMessage) message;

			// Store the backup information
			LocalBackupInformationAccess.storeLocalJxtaInfo(
					jxtaMessage.getPeerId(), jxtaMessage.getSharedQueryId(),
					jxtaMessage.getKey(), jxtaMessage.getValue());
		} else if (message instanceof RecoveryAgreementMessage) {
			RecoveryAgreementMessage agreementMessage = (RecoveryAgreementMessage) message;
			RecoveryAgreementHandler.handleAgreementMessage(senderPeer,
					agreementMessage);
		}
	}

	@Override
	public void sendBackupInformation(PeerID peerId, ID sharedQueryId,
			Map<PeerID, Collection<String>> backupInformation) {

		BackupInformationMessage message = new BackupInformationMessage(
				sharedQueryId, backupInformation);

		try {

			peerCommunicator.send(peerId, message);

		} catch (Throwable e) {

			LOG.error(
					"Could not send backup information to peer "
							+ peerId.toString(), e);

		}

	}

	public void sendBackupJxtaInformation(PeerID peerId, ID sharedQueryId,
			String key, String value) {
		// Send info to all other peers
		BackupJxtaInfoMessage message = new BackupJxtaInfoMessage(peerId,
				sharedQueryId, key, value);
		for (PeerID destinationPeer : p2pDictionary.getRemotePeerIDs())
			try {
				peerCommunicator.send(destinationPeer, message);
			} catch (PeerCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void sendRecoveryAgreementMessage(PeerID failedPeer) {
		// Send this to all other peers we know
		RecoveryAgreementMessage message = RecoveryAgreementMessage
				.createRecoveryAgreementMessage(failedPeer);
		for (PeerID destinationPeer : p2pDictionary.getRemotePeerIDs())
			try {
				peerCommunicator.send(destinationPeer, message);
			} catch (PeerCommunicationException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void update(Observable observable, Object notification) {
		if (notification instanceof P2PNetworkNotification) {
			P2PNetworkNotification p2pNotification = (P2PNetworkNotification) notification;
			if (p2pNotification.getType().equals(
					P2PNetworkNotification.LOST_PEER)) {
				// Start recovery
				recover(p2pNotification.getPeer());
			}
		}

	}

}