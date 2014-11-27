package de.uniol.inf.is.odysseus.peer.recovery.dynamic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryDynamicBackup;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;
import de.uniol.inf.is.odysseus.peer.recovery.internal.SharedQuery;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryAgreementHandler;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * This class implements the actions that are taken, when a new peer needs to be found and queries of failed peers need
 * to be allocated.
 * 
 * @author Simon Kuespert
 *
 */
public class RecoveryDynamicBackup implements IRecoveryDynamicBackup {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryDynamicBackup.class);

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cRecoveryCommunicator = Optional.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindRecoveryCommunicator(IRecoveryCommunicator serv) {

		Preconditions.checkNotNull(serv);
		cRecoveryCommunicator = Optional.of(serv);
		LOG.debug("Bound {} as a recovery communicator.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery communicator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery communicator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindRecoveryCommunicator(IRecoveryCommunicator serv) {

		Preconditions.checkNotNull(serv);

		if (cRecoveryCommunicator.isPresent() && cRecoveryCommunicator.get() == serv) {

			cRecoveryCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * Active Session. Use getActiveSession() to avoid null pointers.
	 */
	private static ISession activeSession;

	/**
	 * The executor, if there is one bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * Binds an executor. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to bind. <br />
	 *            Must be not null.
	 */
	public static void bindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);
		cExecutor = Optional.of((IServerExecutor) serv);
		LOG.debug("Bound {} as an executor.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds an executor, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);

		if (cExecutor.isPresent() && cExecutor.get() == (IServerExecutor) serv) {

			cExecutor = Optional.absent();
			LOG.debug("Unbound {} as an executor.", serv.getClass().getSimpleName());

		}

	}

	public static Optional<IServerExecutor> getcExecutor() {
		return cExecutor;
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
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cP2PNetworkManager = Optional.absent();

	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to bind. <br />
	 *            Must be not null.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);
		cP2PNetworkManager = Optional.of(serv);
		LOG.debug("Bound {} as a P2P network manager.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a P2P network manager, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);

		if (cP2PNetworkManager.isPresent() && cP2PNetworkManager.get() == serv) {

			cP2PNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * The Peer dictionary, if there is one bound.
	 */
	private static Optional<IPeerDictionary> cPeerDictionary = Optional.absent();

	/**
	 * Binds a Peer dictionary. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to bind. <br />
	 *            Must be not null.
	 */
	public static void bindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);
		cPeerDictionary = Optional.of(serv);
		LOG.debug("Bound {} as a Peer dictionary.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a Peer dictionary, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);

		if (cPeerDictionary.isPresent() && cPeerDictionary.get() == serv) {

			cPeerDictionary = Optional.absent();
			LOG.debug("Unbound {} as a Peer dictionary.", serv.getClass().getSimpleName());

		}

	}

	@Override
	public List<ID> getSharedQueryIdsForRecovery(PeerID failedPeer) {

		List<ID> sharedQueryIdsForRecovery = new ArrayList<ID>();

		// 1. Check, if we have backup information for the failed peer and for
		// which shared-query-ids
		// Return if there is no backup information stored for the given peer

		Collection<ID> sharedQueryIdsForPeer = LocalBackupInformationAccess.getStoredSharedQueryIdsForPeer(failedPeer);
		if (sharedQueryIdsForPeer == null || sharedQueryIdsForPeer.isEmpty()) {
			// We don't have any information about that failed peer
			return sharedQueryIdsForRecovery;
		}

		// 2. Check, if we were a direct sender to that failed peer

		// We maybe have backup-information about queries for that peer where we
		// are not the direct sender, so we have to save for which queries we
		// are the direct sender

		List<JxtaSenderPO<?>> senders = RecoveryHelper.getJxtaSenders();

		for (JxtaSenderPO<?> sender : senders) {
			if (sender.getPeerIDString() != null && sender.getPeerIDString().equals(failedPeer.toString())) {
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
		}
		return sharedQueryIdsForRecovery;
	}

	@Override
	public void determineAndSendHoldOnMessages(ID sharedQueryId, PeerID failedPeer, UUID recoveryStateIdentifier) {

		// Preconditions
		if (!cExecutor.isPresent()) {

			LOG.error("No executor bound!");
			return;

		}

		if (!cRecoveryCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		}

		// Test: Tell the peers which sent tuples to the failed peer that
		// they have to hold on
		ImmutableSet<String> backupPQL = LocalBackupInformationAccess.getStoredPQLStatements(sharedQueryId, failedPeer);
		for (String pql : backupPQL) {
			List<ILogicalQuery> logicalQueries = RecoveryHelper.convertToLogicalQueries(pql);
			// Search for the receiver
			for (ILogicalQuery query : logicalQueries) {
				for (ILogicalOperator op : LogicalQueryHelper.getAllOperators(query)) {
					if (op instanceof JxtaReceiverAO) {
						JxtaReceiverAO receiver = (JxtaReceiverAO) op;
						// This is the information about the peer from which
						// we get the data
						// This peer has to hold on
						String peerId = receiver.getPeerID();
						String pipeId = receiver.getPipeID();

						try {
							URI uri = new URI(pipeId);
							PipeID pipe = PipeID.create(uri);
							uri = new URI(peerId);
							PeerID peerToHoldOn = PeerID.create(uri);

							cRecoveryCommunicator.get().sendHoldOnMessage(peerToHoldOn, pipe);
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	@Override
	public void initiateAgreement(PeerID failedPeer, ID sharedQueryId, PeerID newPeer, ILogicalQueryPart queryPart,
			UUID recoveryStateIdentifier) {
		RecoveryAgreementHandler.waitForAndDoRecovery(failedPeer, sharedQueryId, newPeer, queryPart,
				recoveryStateIdentifier);
	}

	@Override
	public List<ID> removeMyBuddyAndSearchNew(PeerID failedPeer) {
		if (!cRecoveryCommunicator.isPresent()) {
			LOG.error("No recovery communicator bound!");
			return null;
		}

		if (LocalBackupInformationAccess.getMyBuddyList().containsKey(failedPeer)) {
			LOG.debug("My buddy failed, I have to search for new buddies.");
			List<ID> sharedQueryIds = LocalBackupInformationAccess.removeMyBuddy(failedPeer);
			for (ID sharedQueryId : sharedQueryIds) {
				cRecoveryCommunicator.get().chooseBuddyForQuery(sharedQueryId);
			}
		}
		return null;
	}

	@Override
	public RecoveryProcessState getRecoveryProcessState(UUID recoveryProcessStateId) {
		return cRecoveryCommunicator.get().getRecoveryProcessState(recoveryProcessStateId);
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocateToNewPeer(PeerID failedPeer, ID sharedQueryId,
			IRecoveryAllocator recoveryAllocator) throws QueryPartAllocationException {

		// Preconditions
		if (!cExecutor.isPresent()) {

			LOG.error("No executor bound!");
			return null;

		} else if (!cP2PNetworkManager.isPresent()) {

			LOG.error("No P2PNetworkManager bound!");
			return null;

		} else if (!cPeerDictionary.isPresent()) {

			LOG.error("No PeerDictionary bound!");
			return null;

		}

		// Get the logical query for the pql statement and convert it to query parts for allocation
		ImmutableSet<String> backupPQL = LocalBackupInformationAccess.getStoredPQLStatements(sharedQueryId, failedPeer);
		for (String pql : backupPQL) {
			List<ILogicalQuery> logicalQueries = RecoveryHelper.convertToLogicalQueries(pql);
			// allocate the query
			for (ILogicalQuery query : logicalQueries) {
				ILogicalQueryPart queryPart = new LogicalQueryPart(LogicalQueryHelper.getAllOperators(query));
				ImmutableCollection<ILogicalQueryPart> queryParts = ImmutableList.copyOf(Lists.newArrayList(queryPart));
				return recoveryAllocator.allocate(queryParts, query, cPeerDictionary.get().getRemotePeerIDs(),
						cP2PNetworkManager.get().getLocalPeerID());

			}
		}
		return null;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> reallocateToNewPeer(Map<ILogicalQueryPart, PeerID> previousAllocationMap,
			List<PeerID> inadequatePeers, IRecoveryAllocator recoveryAllocator) throws QueryPartAllocationException {
		return recoveryAllocator.reallocate(previousAllocationMap, inadequatePeers, cPeerDictionary.get()
				.getRemotePeerIDs(), cP2PNetworkManager.get().getLocalPeerID());
	}

}
