package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

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
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.message.RemoveQueryMessage;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryP2PListener;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupJxtaInfoMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryAgreementHandler;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryInstructionHandler;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * A recovery communicator handles the communication between peers for recovery
 * mechanisms.
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

	public static final String JXTA_KEY_RECEIVER_PIPE_ID = "receiverPipeId";
	public static final String JXTA_KEY_SENDER_PIPE_ID = "senderPipeId";

	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	private static IPeerCommunicator peerCommunicator;
	private static IRecoveryP2PListener recoveryP2PListener;

	private IRecoveryAllocator recoveryAllocator;
	/**
	 * Executor to get queries
	 */
	private static IServerExecutor executor;
	/**
	 * Active Session. Use getActiveSession() to avoid null pointers.
	 */
	private static ISession activeSession;

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {
		if (recoveryP2PListener != null)
			recoveryP2PListener.addObserver(this);
	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
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
		peerCommunicator.registerMessageType(RecoveryAgreementMessage.class);
		peerCommunicator.addListener(this, RecoveryAgreementMessage.class);
		peerCommunicator.addListener(this, RemoveQueryMessage.class);
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
			peerCommunicator
					.unregisterMessageType(BackupInformationMessage.class);
			peerCommunicator.removeListener(this, BackupJxtaInfoMessage.class);
			peerCommunicator.unregisterMessageType(BackupJxtaInfoMessage.class);
			peerCommunicator.removeListener(this,
					RecoveryAgreementMessage.class);
			peerCommunicator
					.unregisterMessageType(RecoveryAgreementMessage.class);
			peerCommunicator.removeListener(this, RemoveQueryMessage.class);
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

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void recover(PeerID failedPeer) {

		// 1. Check, if we have backup information for the failed peer and for
		// which shared-query-ids
		// Return if there is no backup information stored for the given peer

		Collection<ID> sharedQueryIdsForPeer = LocalBackupInformationAccess
				.getStoredSharedQueryIdsForPeer(failedPeer);
		if (sharedQueryIdsForPeer == null || sharedQueryIdsForPeer.isEmpty()) {
			// We don't have any information about that failed peer
			return;
		}

		// 2. Check, if we were a direct sender to that failed peer or if we are
		// a buddy

		// We maybe have backup-information about queries for that peer where we
		// are not the direct sender, so we have to save for which queries we
		// are the direct sender
		List<ID> sharedQueryIdsForRecovery = new ArrayList<ID>();

		List<JxtaSenderPO> senders = RecoveryHelper.getJxtaSenders();
		List<JxtaSenderPO> affectedSenders = new ArrayList<JxtaSenderPO>();

		for (JxtaSenderPO sender : senders) {
			if (sender.getPeerIDString().equals(failedPeer.toString())) {
				// We were a direct sender to the failed peer

				// Determine for which shared query id we are the direct
				// sender: Search in the saved backup information for
				// that pipe id and look, which shared query id belongs
				// to the operator which has this pipeId
				Set<SharedQuery> pqls = LocalBackupInformationAccess
						.getStoredPQLStatements(failedPeer);
				for (SharedQuery sharedQuery : pqls) {
					List<String> pqlParts = sharedQuery.getPqlParts();
					for (String pql : pqlParts) {
						if (pql.contains(sender.getPipeIDString())) {
							// This is the shared query id we search for
							sharedQueryIdsForRecovery.add(sharedQuery
									.getSharedQueryID());

							// Save that this sender if affected
							affectedSenders.add(sender);
						}
					}
				}
			}
		}

		// 3. Check, if we are the buddy of that peer
		Map<PeerID, List<ID>> buddyMap = LocalBackupInformationAccess
				.getBuddyList();
		if (buddyMap.containsKey(failedPeer)) {
			// We are a buddy for that peer
			sharedQueryIdsForRecovery.addAll(buddyMap.get(failedPeer));

			// TODO What are the affected senders? Maybe no sender is affected
			// cause it's a totally different peer. Maybe we have to tell other
			// peers to install new receivers or sth. like that
		}

		// To update the affected senders
		int i = 0;

		// Reallocate each query to another peer
		for (ID sharedQueryId : sharedQueryIdsForRecovery) {
			// 4. Search for another peer who can take the parts from the failed
			// peer

			// TODO find a good place to reallocate if the peer doesn't accept
			// the query or is unable to install it

			PeerID peer = null;

			try {
				peer = recoveryAllocator.allocate(
						p2pDictionary.getRemotePeerIDs(),
						p2pNetworkManager.getLocalPeerID());
				LOG.debug("Peer ID for recovery allocation found.");
			} catch (QueryPartAllocationException e) {
				LOG.error("Peer ID search for recovery allocation failed.");
				e.printStackTrace();
			}

			// If the peer is null, we don't know any other peer so we have to
			// install it on ourself
			if (peer == null)
				peer = p2pNetworkManager.getLocalPeerID();

			// 5. Tell the new peer to install the parts from the failed peer
			RecoveryAgreementHandler.waitForAndDoRecovery(failedPeer,
					sharedQueryId, peer);

			// 6. Update our sender so it knows the new peerId
			if (i < affectedSenders.size()) {
				affectedSenders.get(i).setPeerIDString(peer.toString());
			}

		}

	}

	@Override
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

	// TODO Better way: allocate each single query part new. M.B.
	@Override
	public void installQueriesOnNewPeer(PeerID failedPeer, PeerID newPeer,
			ID sharedQueryId) {

		Preconditions.checkNotNull(failedPeer);
		Preconditions.checkNotNull(newPeer);
		Preconditions.checkNotNull(sharedQueryId);

		ImmutableCollection<String> pqlParts = LocalBackupInformationAccess
				.getStoredPQLStatements(sharedQueryId, failedPeer);

		String pql = "";
		for (String pqlPart : pqlParts) {
			pql += " " + pqlPart;
		}

		// Send the add query message
		RecoveryInstructionMessage takeOverMessage = RecoveryInstructionMessage
				.createAddQueryMessage(pql, sharedQueryId);
		try {
			peerCommunicator.send(newPeer, takeOverMessage);
		} catch (Throwable e) {
			LOG.error(
					"Could not send add query message to peer "
							+ newPeer.toString(), e);
		}

		// The affected backup information
		Set<IRecoveryBackupInformation> affectedInfos = Sets.newHashSet();
		for (String affectedPQL : pqlParts) {

			Optional<IRecoveryBackupInformation> affectedInfo = LocalBackupInformationAccess
					.getStore().get(affectedPQL);
			if (affectedInfo.isPresent()) {

				affectedInfos.add(affectedInfo.get());

			}

		}

		// Determine, which backup information have to be sent to the new peer
		Set<IRecoveryBackupInformation> subsequentInfos = determineSubsequentBackupInformation(affectedInfos);

		// Send these information
		for (IRecoveryBackupInformation info : subsequentInfos) {

			this.sendBackupInformation(newPeer, info);

		}

		// Update the local backup information due to the take over
		for (IRecoveryBackupInformation info : affectedInfos) {

			LocalBackupInformationAccess.getStore().remove(info);

		}

	}

	/**
	 * Determines all subsequent backup information of given information.
	 * 
	 * @param infos
	 *            The given info. <br />
	 *            Must be not null.
	 * @return All subsequent backup information of the given information.
	 */
	private ImmutableSet<IRecoveryBackupInformation> determineSubsequentBackupInformation(
			Set<IRecoveryBackupInformation> infos) {

		Preconditions.checkNotNull(infos);
		Set<IRecoveryBackupInformation> out = Sets.newHashSet();

		for (IRecoveryBackupInformation info : infos) {

			out.addAll(info.getSubsequentPartsInformation());

		}

		return ImmutableSet.copyOf(out);

	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		if (message instanceof RecoveryInstructionMessage) {
			RecoveryInstructionMessage instruction = (RecoveryInstructionMessage) message;
			RecoveryInstructionHandler.handleInstruction(senderPeer,
					instruction);
		} else if (message instanceof BackupInformationMessage) {

			// Store the backup information
			LocalBackupInformationAccess.getStore().add(
					((BackupInformationMessage) message).getInfo());

		} else if (message instanceof RemoveQueryMessage) {

			// Remove stored backup information
			LocalBackupInformationAccess.getStore().remove(
					((RemoveQueryMessage) message).getSharedQueryID());

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
	public void sendBackupInformation(PeerID destination,
			IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(info);
		BackupInformationMessage message = new BackupInformationMessage(info);

		try {

			peerCommunicator.send(destination, message);

		} catch (Throwable e) {

			LOG.error("Could not send backup information to peer "
					+ destination.toString(), e);

		}

	}

	/*
	 * TODO Jxta information. Not clear atm. if they are needed.
	 */
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

	public void sendRecoveryAgreementMessage(PeerID failedPeer, ID sharedQueryId) {
		// Send this to all other peers we know
		RecoveryAgreementMessage message = RecoveryAgreementMessage
				.createRecoveryAgreementMessage(failedPeer, sharedQueryId);
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

	@Override
	public void sendUpdateReceiverMessage(PeerID receiverPeer,
			PeerID newSenderPeer, PipeID pipeId) {
		RecoveryInstructionMessage message = RecoveryInstructionMessage
				.createUpdateReceiverMessage(newSenderPeer, pipeId);
		try {
			peerCommunicator.send(receiverPeer, message);
		} catch (PeerCommunicationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IRecoveryAllocator getRecoveryAllocator() {
		return recoveryAllocator;
	}

	@Override
	public void setRecoveryAllocator(IRecoveryAllocator recoveryAllocator) {
		this.recoveryAllocator = recoveryAllocator;
	}

}