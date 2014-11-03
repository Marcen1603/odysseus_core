package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.net.URI;
import java.net.URISyntaxException;
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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.message.RemoveQueryMessage;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryP2PListener;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryAgreementHandler;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryInstructionHandler;
import de.uniol.inf.is.odysseus.peer.recovery.util.BackupInformationHelper;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * A recovery communicator handles the communication between peers for recovery
 * mechanisms.
 * 
 * @author Tobias Brandt & Michael Brand & Simon Kuespert
 *
 */
public class RecoveryCommunicator implements IRecoveryCommunicator, IPeerCommunicatorListener, Observer {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryCommunicator.class);

	public static final String JXTA_KEY_RECEIVER_PIPE_ID = "receiverPipeId";
	public static final String JXTA_KEY_SENDER_PIPE_ID = "senderPipeId";

	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	private static IPeerCommunicator peerCommunicator;
	private static IRecoveryP2PListener recoveryP2PListener;
	private static IRecoveryAllocator recoveryAllocator;

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
		if (recoveryP2PListener != null) {
			recoveryP2PListener.addObserver(this);
			if (recoveryAllocator != null) {
				recoveryP2PListener.tryStartPeerFailureDetection();
			}
		}

	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
		if (recoveryAllocator != null) {
			recoveryP2PListener.stopPeerFailureDetection();
		}

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
		peerCommunicator.registerMessageType(RecoveryAgreementMessage.class);
		peerCommunicator.addListener(this, RecoveryAgreementMessage.class);
		peerCommunicator.registerMessageType(RemoveQueryMessage.class);
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
			peerCommunicator.removeListener(this, RecoveryInstructionMessage.class);
			peerCommunicator.unregisterMessageType(RecoveryInstructionMessage.class);
			peerCommunicator.removeListener(this, BackupInformationMessage.class);
			peerCommunicator.unregisterMessageType(BackupInformationMessage.class);
			peerCommunicator.removeListener(this, RecoveryAgreementMessage.class);
			peerCommunicator.unregisterMessageType(RecoveryAgreementMessage.class);
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

	// called by OSGi-DS
	public static void bindRecoveryAllocator(IRecoveryAllocator allocator) {
		recoveryAllocator = allocator;
	}

	// called by OSGi-DS
	public static void unbindRecoveryAllocator(IRecoveryAllocator allocator) {
		if (recoveryAllocator == allocator)
			recoveryAllocator = null;
	}

	/**
	 * Gets currently active Session.
	 * 
	 * @return active Session
	 */
	public static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null,
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

		LOG.debug("Startet recovery for {}", p2pDictionary.getRemotePeerName(failedPeer));

		// 1. Check, if we have backup information for the failed peer and for
		// which shared-query-ids
		// Return if there is no backup information stored for the given peer

		Collection<ID> sharedQueryIdsForPeer = LocalBackupInformationAccess.getStoredSharedQueryIdsForPeer(failedPeer);
		if (sharedQueryIdsForPeer == null || sharedQueryIdsForPeer.isEmpty()) {
			// We don't have any information about that failed peer
			return;
		}

		// 2. Check, if we were a direct sender to that failed peer

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
				Set<SharedQuery> pqls = LocalBackupInformationAccess.getStoredPQLStatements(failedPeer);
				for (SharedQuery sharedQuery : pqls) {
					List<String> pqlParts = sharedQuery.getPqlParts();
					for (String pql : pqlParts) {
						if (pql.contains(sender.getPipeIDString())) {
							// This is the shared query id we search for
							sharedQueryIdsForRecovery.add(sharedQuery.getSharedQueryID());

							// Save that this sender if affected
							affectedSenders.add(sender);
						}
					}
				}
			}
		}

		// 3. Check, if we are the buddy of that peer
		Map<PeerID, List<ID>> buddyMap = LocalBackupInformationAccess.getBuddyList();
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

			if (recoveryAllocator == null) {
				LOG.error("No allocator for recovery allocation set.");
			} else {
				try {
					peer = recoveryAllocator.allocate(p2pDictionary.getRemotePeerIDs(),
							p2pNetworkManager.getLocalPeerID());
					LOG.debug("Peer ID for recovery allocation found.");
				} catch (QueryPartAllocationException e) {
					LOG.error("Peer ID search for recovery allocation failed.");
					e.printStackTrace();
				}
			}

			// If the peer is null, we don't know any other peer so we have to
			// install it on ourself
			if (peer == null)
				peer = p2pNetworkManager.getLocalPeerID();

			determineAndSendHoldOnMessages(sharedQueryId, failedPeer);

			// 5. Tell the new peer to install the parts from the failed peer
			RecoveryAgreementHandler.waitForAndDoRecovery(failedPeer, sharedQueryId, peer);

			// 6. Update our sender so it knows the new peerId
			if (i < affectedSenders.size()) {
				affectedSenders.get(i).setPeerIDString(peer.toString());
			}

		}

	}

	@SuppressWarnings("rawtypes")
	private void determineAndSendHoldOnMessages(ID sharedQueryId, PeerID failedPeer) {
		// Test: Tell the peers which sent tuples to the failed peer that
		// they have to hold on
		TransformationConfiguration trafoConfig = new TransformationConfiguration("relational");
		ImmutableSet<String> backupPQL = LocalBackupInformationAccess.getStoredPQLStatements(sharedQueryId, failedPeer);
		for (String pql : backupPQL) {
			List<IPhysicalQuery> physicalQueries = executor.getCompiler().translateAndTransformQuery(pql, "PQL",
					getActiveSession(), DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant()),
					trafoConfig, Context.empty());
			// Search for the receiver
			for (IPhysicalQuery query : physicalQueries) {
				for (IPhysicalOperator op : query.getAllOperators()) {
					if (op instanceof JxtaReceiverPO) {
						JxtaReceiverPO receiver = (JxtaReceiverPO) op;
						// This is the information about the peer from which
						// we get the data
						// This peer has to hold on
						String peerId = receiver.getPeerIDString();
						String pipeId = receiver.getPipeIDString();

						try {
							URI uri = new URI(pipeId);
							PipeID pipe = PipeID.create(uri);
							RecoveryInstructionMessage holdOnMessage = RecoveryInstructionMessage
									.createHoldOnMessage(pipe);
							uri = new URI(peerId);
							PeerID peerToHoldOn = PeerID.create(uri);
							if (!peerToHoldOn.equals(p2pNetworkManager.getLocalPeerID())) {
								sendHoldOnMessage(peerToHoldOn, holdOnMessage);
							} else {
								// We are the peer
								receivedMessage(peerCommunicator, peerToHoldOn, holdOnMessage);
							}
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	@Override
	public void sendHoldOnMessage(PeerID peerToHoldOn, RecoveryInstructionMessage holdOnMessage) {
		sendMessage(peerToHoldOn, holdOnMessage);
	}

	// TODO Better way: allocate each single query part new. M.B.
	@Override
	public void installQueriesOnNewPeer(PeerID failedPeer, PeerID newPeer, ID sharedQueryId) {

		Preconditions.checkNotNull(failedPeer);
		Preconditions.checkNotNull(newPeer);
		Preconditions.checkNotNull(sharedQueryId);

		ImmutableCollection<String> pqlParts = LocalBackupInformationAccess.getStoredPQLStatements(sharedQueryId,
				failedPeer);

		String pql = "";
		for (String pqlPart : pqlParts) {
			pql += " " + pqlPart;
		}

		// Send the add query message
		RecoveryInstructionMessage takeOverMessage = RecoveryInstructionMessage.createAddQueryMessage(pql,
				sharedQueryId);
		try {
			peerCommunicator.send(newPeer, takeOverMessage);
		} catch (Throwable e) {
			LOG.error("Could not send add query message to peer " + newPeer.toString(), e);
		}

		for (String pqlCode : pqlParts) {

			BackupInformationHelper.updateInfoStores(failedPeer, newPeer, sharedQueryId, pqlCode);

		}

	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof RecoveryInstructionMessage) {
			RecoveryInstructionMessage instruction = (RecoveryInstructionMessage) message;
			RecoveryInstructionHandler.handleInstruction(senderPeer, instruction);

		} else if (message instanceof BackupInformationMessage) {

			// Store the backup information
			LocalBackupInformationAccess.getStore().add(((BackupInformationMessage) message).getInfo());

		} else if (message instanceof RemoveQueryMessage) {

			// Remove stored backup information
			LocalBackupInformationAccess.getStore().remove(((RemoveQueryMessage) message).getSharedQueryID());

		} else if (message instanceof RecoveryAgreementMessage) {
			RecoveryAgreementMessage agreementMessage = (RecoveryAgreementMessage) message;
			RecoveryAgreementHandler.handleAgreementMessage(senderPeer, agreementMessage);

		}
	}

	@Override
	public void sendBackupInformation(PeerID destination, IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(info);
		BackupInformationMessage message = new BackupInformationMessage(info);

		sendMessage(destination, message);

	}

	public void sendRecoveryAgreementMessage(PeerID failedPeer, ID sharedQueryId) {
		// Send this to all other peers we know
		RecoveryAgreementMessage message = RecoveryAgreementMessage.createRecoveryAgreementMessage(failedPeer,
				sharedQueryId);
		for (PeerID destinationPeer : p2pDictionary.getRemotePeerIDs())
			sendMessage(destinationPeer, message);
	}

	@Override
	public void update(Observable observable, Object notification) {
		if (notification instanceof P2PNetworkNotification) {
			P2PNetworkNotification p2pNotification = (P2PNetworkNotification) notification;
			if (p2pNotification.getType().equals(P2PNetworkNotification.LOST_PEER)) {
				// Start recovery
				recover(p2pNotification.getPeer());
			}
		}
	}

	@Override
	public void sendUpdateReceiverMessage(PeerID receiverPeer, PeerID newSenderPeer, PipeID pipeId) {
		RecoveryInstructionMessage message = RecoveryInstructionMessage.createUpdateReceiverMessage(newSenderPeer,
				pipeId);
		sendMessage(receiverPeer, message);
	}

	public void sendGoOnMessage(PeerID receiverPeer, PipeID pipeId) {
		RecoveryInstructionMessage message = RecoveryInstructionMessage.createGoOnMessage(pipeId);
		sendMessage(receiverPeer, message);
	}

	@Override
	public void chooseBuddyForQuery(ID sharedQueryId) {
		// TODO Use a buddy-allocator? For now, we just choose the first peer we
		// know
		// 1. Choose buddy
		PeerID buddy = p2pDictionary.getRemotePeerIDs().iterator().next();

		// 2. Get the necessary backup-information
		ImmutableSet<String> infos = LocalBackupInformationAccess.getStoredPQLStatements(sharedQueryId,
				p2pNetworkManager.getLocalPeerID());
		List<String> pql = infos.asList();

		// 3. Send this to the buddy
		RecoveryInstructionMessage buddyMessage = RecoveryInstructionMessage.createBeBuddyMessage(sharedQueryId, pql);
		sendMessage(buddy, buddyMessage);
		
		// 4. TODO Save, that this is my buddy so that we can find a new buddy if that one fails
	}

	private void sendMessage(PeerID receiverPeer, IMessage message) {
		try {
			peerCommunicator.send(receiverPeer, message);
		} catch (PeerCommunicationException e) {
			e.printStackTrace();
		}
	}

	public static IRecoveryAllocator getRecoveryAllocator() {
		return recoveryAllocator;
	}

	public static void setRecoveryAllocator(IRecoveryAllocator recoveryAllocator) {
		RecoveryCommunicator.recoveryAllocator = recoveryAllocator;
	}
}