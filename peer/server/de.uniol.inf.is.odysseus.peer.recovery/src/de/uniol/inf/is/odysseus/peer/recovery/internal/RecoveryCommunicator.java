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

import com.google.common.base.Optional;
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

	// TODO unused
	public static final String JXTA_KEY_RECEIVER_PIPE_ID = "receiverPipeId";
	public static final String JXTA_KEY_SENDER_PIPE_ID = "senderPipeId";

	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cP2PNetworkManager = Optional.absent();
	
	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P network manager to bind. <br />
	 * Must be not null.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		
		Preconditions.checkNotNull(serv);
		cP2PNetworkManager = Optional.of(serv);
		LOG.debug("Bound {} as a P2P network manager.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a P2P network manager, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P network manager to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cP2PNetworkManager.isPresent() && cP2PNetworkManager.get() == serv) {
			
			cP2PNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	/**
	 * Gets the P2P network manager.
	 * @return The bound P2P network manager or {@link Optional#absent()}, if there is none bound.
	 */
	public static Optional<IP2PNetworkManager> getP2PNetworkManager() {
		return cP2PNetworkManager;
	}
	
	/**
	 * The P2P dictionary, if there is one bound.
	 */
	private static Optional<IP2PDictionary> cP2PDictionary = Optional.absent();
	
	/**
	 * Binds a P2P dictionary. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P dictionary to bind. <br />
	 * Must be not null.
	 */
	public static void bindP2PDictionary(IP2PDictionary serv) {
		
		Preconditions.checkNotNull(serv);
		cP2PDictionary = Optional.of(serv);
		LOG.debug("Bound {} as a P2P dictionary.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a P2P dictionary, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P dictionary to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cP2PDictionary.isPresent() && cP2PDictionary.get() == serv) {
			
			cP2PDictionary = Optional.absent();
			LOG.debug("Unbound {} as a P2P dictionary.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	/**
	 * Gets the P2P dictionary.
	 * @return The bound P2P dictionary or {@link Optional#absent()}, if there is none bound.
	 */
	public static Optional<IP2PDictionary> getP2PDictionary() {
		
		return cP2PDictionary;
		
	}
	
	/**
	 * The peer communicator, if there is one bound.
	 */
	private static Optional<IPeerCommunicator> cPeerCommunicator = Optional.absent();
	
	/**
	 * Registers messages and listeners at the peer communicator.
	 */
	private void registerMessagesAndAddListeners() {
		
		Preconditions.checkArgument(cPeerCommunicator.isPresent());
		cPeerCommunicator.get().registerMessageType(RecoveryInstructionMessage.class);
		cPeerCommunicator.get().addListener(this, RecoveryInstructionMessage.class);
		cPeerCommunicator.get().registerMessageType(BackupInformationMessage.class);
		cPeerCommunicator.get().addListener(this, BackupInformationMessage.class);
		cPeerCommunicator.get().registerMessageType(RecoveryAgreementMessage.class);
		cPeerCommunicator.get().addListener(this, RecoveryAgreementMessage.class);
		cPeerCommunicator.get().registerMessageType(RemoveQueryMessage.class);
		cPeerCommunicator.get().addListener(this, RemoveQueryMessage.class);
		
	}
	
	/**
	 * Unregisters messages and listeners at the peer communicator.
	 */
	private void unregisterMessagesAndAddListeners() {
		
		Preconditions.checkArgument(cPeerCommunicator.isPresent());
		cPeerCommunicator.get().removeListener(this, RecoveryInstructionMessage.class);
		cPeerCommunicator.get().unregisterMessageType(RecoveryInstructionMessage.class);
		cPeerCommunicator.get().removeListener(this, BackupInformationMessage.class);
		cPeerCommunicator.get().unregisterMessageType(BackupInformationMessage.class);
		cPeerCommunicator.get().removeListener(this, RecoveryAgreementMessage.class);
		cPeerCommunicator.get().unregisterMessageType(RecoveryAgreementMessage.class);
		cPeerCommunicator.get().removeListener(this, RemoveQueryMessage.class);
		
	}
	
	/**
	 * Binds a peer communicator. <br />
	 * Called by OSGi-DS.
	 * @param serv The peer communicator to bind. <br />
	 * Must be not null.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		
		Preconditions.checkNotNull(serv);
		cPeerCommunicator = Optional.of(serv);
		this.registerMessagesAndAddListeners();
		LOG.debug("Bound {} as a peer communicator.", serv
				.getClass().getSimpleName());		

	}

	/**
	 * Unbinds a peer communicator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The peer communicator to unbind. <br />
	 * Must be not null.
	 */
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cPeerCommunicator.isPresent() && cPeerCommunicator.get() == serv) {
			
			this.unregisterMessagesAndAddListeners();
			cPeerCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a peer communicator.", serv
					.getClass().getSimpleName());
			
		}
		
	}
		
	/**
	 * The recovery P2P listener, if there is one bound.
	 */
	private static Optional<IRecoveryP2PListener> cRecoveryP2PListener = Optional.absent();
	
	/**
	 * Binds a recovery P2P listener. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery P2P listener to bind. <br />
	 * Must be not null.
	 */
	public static void bindRecoveryP2PListener(IRecoveryP2PListener serv) {
		
		Preconditions.checkNotNull(serv);
		cRecoveryP2PListener = Optional.of(serv);
		LOG.debug("Bound {} as a recovery P2P listener.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a recovery P2P listener, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery P2P listener to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindRecoveryP2PListener(IRecoveryP2PListener serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cRecoveryP2PListener.isPresent() && cRecoveryP2PListener.get() == serv) {
			
			cRecoveryP2PListener = Optional.absent();
			LOG.debug("Unbound {} as a recovery P2P listener.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	// TODO The communicator should not bind an allocator. Single strategies and the console should do. M.B.
	
	/**
	 * The recovery allocator, if there is one bound.
	 */
	private static Optional<IRecoveryAllocator> cRecoveryAllocator = Optional.absent();
	
	/**
	 * Binds a recovery allocator. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery allocator to bind. <br />
	 * Must be not null.
	 */
	public static void bindRecoveryAllocator(IRecoveryAllocator serv) {
		
		Preconditions.checkNotNull(serv);
		cRecoveryAllocator = Optional.of(serv);
		LOG.debug("Bound {} as a recovery allocator.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a recovery allocator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery allocator to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindRecoveryAllocator(IRecoveryAllocator serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cRecoveryAllocator.isPresent() && cRecoveryAllocator.get() == serv) {
			
			cRecoveryAllocator = Optional.absent();
			LOG.debug("Unbound {} as a recovery allocator.", serv
					.getClass().getSimpleName());
			
		}
		
	}

	/**
	 * The executor, if there is one bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();
	
	/**
	 * Binds an executor. <br />
	 * Called by OSGi-DS.
	 * @param serv The executor to bind. <br />
	 * Must be not null.
	 */
	public static void bindExecutor(IExecutor serv) {
		
		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);
		cExecutor = Optional.of((IServerExecutor) serv);
		LOG.debug("Bound {} as an executor.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds an executor, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The executor to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindExecutor(IExecutor serv) {
		
		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);
		
		if (cExecutor.isPresent() && cExecutor.get() == (IServerExecutor) serv) {
			
			cExecutor = Optional.absent();
			LOG.debug("Unbound {} as an executor.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	/**
	 * Gets the executor.
	 * @return The bound executor or {@link Optional#absent()}, if there is none bound.
	 */
	public static Optional<IServerExecutor> getExecutor() {
		
		return cExecutor;
		
	}
	
	/**
	 * The active Session. <br />
	 * Only to be used by {@link #getActiveSession()}.
	 */
	private static ISession cActiveSession;
	
	/**
	 * Gets the currently active session.
	 */
	public static ISession getActiveSession() {
		
		if (cActiveSession == null || !cActiveSession.isValid()) {
			
			cActiveSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null,
					UserManagementProvider.getDefaultTenant().getName());
			
		}
		
		return cActiveSession;
		
	}

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {
		
		if (cRecoveryP2PListener.isPresent()) {
			
			cRecoveryP2PListener.get().addObserver(this);
			
			if (cRecoveryAllocator.isPresent()) {
				
				cRecoveryP2PListener.get().tryStartPeerFailureDetection();
				
			}
			
		}

	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
		
		if (cRecoveryP2PListener.isPresent() && cRecoveryAllocator.isPresent()) {
			
			cRecoveryP2PListener.get().stopPeerFailureDetection();
			
		}

	}

	// -----------------------------------------------------
	// Code with recovery logic
	// -----------------------------------------------------

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void recover(PeerID failedPeer) {
		
		if(!cP2PNetworkManager.isPresent()) {
			
			LOG.error("No P2P network manager bound!");
			return;
			
		} else if(!cP2PDictionary.isPresent()) {
			
			LOG.error("No P2P dictionary bound!");
			return;
			
		} else if(!cRecoveryAllocator.isPresent()) {
			
			LOG.error("No recovery allocator bound!");
			return;
			
		}

		LOG.debug("Startet recovery for {}", cP2PDictionary.get().getRemotePeerName(failedPeer));

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

			try {
				peer = cRecoveryAllocator.get().allocate(cP2PDictionary.get().getRemotePeerIDs(),
						cP2PNetworkManager.get().getLocalPeerID());
				LOG.debug("Peer ID for recovery allocation found.");
			} catch (QueryPartAllocationException e) {
				LOG.error("Peer ID search for recovery allocation failed.");
				e.printStackTrace();
			}

			// If the peer is null, we don't know any other peer so we have to
			// install it on ourself
			if (peer == null)
				peer = cP2PNetworkManager.get().getLocalPeerID();

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
		
		if(!cP2PNetworkManager.isPresent()) {
			
			LOG.error("No P2P network manager bound!");
			return;
			
		} else if(!cPeerCommunicator.isPresent()) {
			
			LOG.error("No peer communicator bound!");
			return;
			
		} else if(!cExecutor.isPresent()) {
			
			LOG.error("No executor bound!");
			return;
			
		}
		
		// Test: Tell the peers which sent tuples to the failed peer that
		// they have to hold on
		TransformationConfiguration trafoConfig = new TransformationConfiguration("relational");
		ImmutableSet<String> backupPQL = LocalBackupInformationAccess.getStoredPQLStatements(sharedQueryId, failedPeer);
		for (String pql : backupPQL) {
			List<IPhysicalQuery> physicalQueries = cExecutor.get().getCompiler().translateAndTransformQuery(pql, "PQL",
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
							if (!peerToHoldOn.equals(cP2PNetworkManager.get().getLocalPeerID())) {
								sendHoldOnMessage(peerToHoldOn, holdOnMessage);
							} else {
								// We are the peer
								receivedMessage(cPeerCommunicator.get(), peerToHoldOn, holdOnMessage);
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
		
		if(!cPeerCommunicator.isPresent()) {
			
			LOG.error("No peer communicator bound!");
			return;
			
		}

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
			cPeerCommunicator.get().send(newPeer, takeOverMessage);
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
		
		if(!cP2PDictionary.isPresent()) {
			
			LOG.error("No P2P dictionary bound!");
			return;
			
		}
		
		// Send this to all other peers we know
		RecoveryAgreementMessage message = RecoveryAgreementMessage.createRecoveryAgreementMessage(failedPeer,
				sharedQueryId);
		for (PeerID destinationPeer : cP2PDictionary.get().getRemotePeerIDs())
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
		
		if(!cP2PNetworkManager.isPresent()) {
			
			LOG.error("No P2P network manager bound!");
			return;
			
		} else if(!cP2PDictionary.isPresent()) {
			
			LOG.error("No P2P dictionary bound!");
			return;
			
		}
		
		// TODO Use a buddy-allocator? For now, we just choose the first peer we
		// know
		// 1. Choose buddy
		PeerID buddy = cP2PDictionary.get().getRemotePeerIDs().iterator().next();

		// 2. Get the necessary backup-information
		ImmutableSet<String> infos = LocalBackupInformationAccess.getStoredPQLStatements(sharedQueryId,
				cP2PNetworkManager.get().getLocalPeerID());
		List<String> pql = infos.asList();

		// 3. Send this to the buddy
		RecoveryInstructionMessage buddyMessage = RecoveryInstructionMessage.createBeBuddyMessage(sharedQueryId, pql);
		sendMessage(buddy, buddyMessage);
		
		// 4. TODO Save, that this is my buddy so that we can find a new buddy if that one fails
	}

	private void sendMessage(PeerID receiverPeer, IMessage message) {
		
		if(!cPeerCommunicator.isPresent()) {
			
			LOG.error("No peer communicator bound!");
			return;
			
		}
		
		try {
			cPeerCommunicator.get().send(receiverPeer, message);
		} catch (PeerCommunicationException e) {
			e.printStackTrace();
		}
	}

}