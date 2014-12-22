package de.uniol.inf.is.odysseus.peer.recovery.dynamic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
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
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryDynamicBackup;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;
import de.uniol.inf.is.odysseus.peer.recovery.util.AgreementHelper;
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

	private static IBackupInformationAccess backupInformationAccess;

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

	public static void bindBackupInformationAccess(IBackupInformationAccess infoAccess) {
		backupInformationAccess = infoAccess;
	}

	public static void unbindBackupInformationAccess(IBackupInformationAccess infoAccess) {
		if (backupInformationAccess == infoAccess) {
			backupInformationAccess = null;
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
	public void determineAndSendHoldOnMessages(int localPeerId, PeerID failedPeer, UUID recoveryStateIdentifier) {

		// Preconditions
		if (!cExecutor.isPresent()) {

			LOG.error("No executor bound!");
			return;

		}

		if (!cRecoveryCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		}

		// Test: Tell the peers which sent tuples to the failed peer that they have to hold on
		String backupPQL = backupInformationAccess.getBackupPQL(failedPeer.toString(), localPeerId);
		List<ILogicalQuery> logicalQueries = RecoveryHelper.convertToLogicalQueries(backupPQL);
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

	@Override
	public void initiateAgreement(PeerID failedPeer, int localQueryId, QueryState queryState, ID sharedQuery, PeerID newPeer,
			ILogicalQueryPart queryPart, UUID recoveryStateIdentifier, UUID subprocessID, boolean master) {
		AgreementHelper.waitForAndDoRecovery(failedPeer, localQueryId, queryState, sharedQuery, newPeer, queryPart,
				recoveryStateIdentifier, subprocessID, master);
	}

	@Override
	public RecoveryProcessState getRecoveryProcessState(UUID recoveryProcessStateId) {
		return cRecoveryCommunicator.get().getRecoveryProcessState(recoveryProcessStateId);
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocateToNewPeer(PeerID failedPeer, int localQueryId,
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

		// Get the logical query for the PQL statement and convert it to query parts for allocation
		String backupPQL = backupInformationAccess.getBackupPQL(failedPeer.toString(), localQueryId);
		List<ILogicalQuery> logicalQueries = RecoveryHelper.convertToLogicalQueries(backupPQL);
		// allocate the query
		for (ILogicalQuery query : logicalQueries) {
			ILogicalQueryPart queryPart = new LogicalQueryPart(LogicalQueryHelper.getAllOperators(query));
			ImmutableCollection<ILogicalQueryPart> queryParts = ImmutableList.copyOf(Lists.newArrayList(queryPart));
			return recoveryAllocator.allocate(queryParts, query, cPeerDictionary.get().getRemotePeerIDs(),
					cP2PNetworkManager.get().getLocalPeerID());
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
