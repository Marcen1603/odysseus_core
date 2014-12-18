package de.uniol.inf.is.odysseus.peer.recovery.strategy.upstreambackup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryDynamicBackup;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoverySubProcessState;

/**
 * Implementation of the Upstream-Backup {@link IRecoveryStrategy}.
 * 
 * @author Simon Küspert
 * 
 */
public class RecoveryStrategyUpstreamBackup implements IRecoveryStrategy {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryStrategyUpstreamBackup.class);

	private static IBackupInformationAccess backupInformationAccess;

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Recovery strategy Upstream-Backup activated.");
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Recovery strategy Upstream-Backup deactivated.");
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

	/**
	 * The recovery allocator, if there is one bound.
	 */
	private static Optional<IRecoveryAllocator> cRecoveryAllocator = Optional.absent();

	/**
	 * Binds a recovery allocator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery allocator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindRecoveryAllocator(IRecoveryAllocator serv) {

		Preconditions.checkNotNull(serv);

		if (cRecoveryAllocator.isPresent() && cRecoveryAllocator.get().getName().equals("roundrobinwithlocal")) {
			// use local as default so do nothing here
		} else {
			cRecoveryAllocator = Optional.of(serv);
			LOG.debug("Bound {} as a recovery allocator.", serv.getClass().getSimpleName());
		}

	}

	/**
	 * Unbinds a recovery allocator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery allocator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindRecoveryAllocator(IRecoveryAllocator serv) {

		Preconditions.checkNotNull(serv);

		if (cRecoveryAllocator.isPresent() && cRecoveryAllocator.get() == serv) {

			cRecoveryAllocator = Optional.absent();
			LOG.debug("Unbound {} as a recovery allocator.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * The recovery dynamic backup, if there is one bound.
	 */
	private static Optional<IRecoveryDynamicBackup> cRecoveryDynamicBackup = Optional.absent();

	/**
	 * Binds a recovery dynamic backup. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery dynamic backup to bind. <br />
	 *            Must be not null.
	 */
	public static void bindRecoveryDynamicBackup(IRecoveryDynamicBackup serv) {

		Preconditions.checkNotNull(serv);
		cRecoveryDynamicBackup = Optional.of(serv);
		LOG.debug("Bound {} as a recovery dynamic backup.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery dynamic backup, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery dynamic backup to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindRecoveryDynamicBackup(IRecoveryDynamicBackup serv) {

		Preconditions.checkNotNull(serv);

		if (cRecoveryDynamicBackup.isPresent() && cRecoveryDynamicBackup.get() == serv) {

			cRecoveryDynamicBackup = Optional.absent();
			LOG.debug("Unbound {} as a recovery dynamic backup.", serv.getClass().getSimpleName());

		}

	}

	@Override
	public void recoverSingleQueryPart(PeerID failedPeer, UUID recoveryStateIdentifier, UUID recoverySubStateIdentifier) {
		// Preconditions
		if (!cRecoveryAllocator.isPresent()) {
			LOG.error("No recovery allocator bound!");
			return;
		}
		if (!cRecoveryDynamicBackup.isPresent()) {
			LOG.error("No recovery dynamic backup bound!");
			return;
		}

		Map<ILogicalQueryPart, PeerID> allocationMap = null;

		if (!cRecoveryAllocator.isPresent()) {
			LOG.error("No allocator for recovery allocation set.");
		} else {
			RecoveryProcessState state = cRecoveryDynamicBackup.get().getRecoveryProcessState(recoveryStateIdentifier);
			if (state == null) {
				LOG.error("Recovery Process State not found. Reallocation aborted");
				return;
			}

			RecoverySubProcessState subState = state.getRecoverySubprocess(recoverySubStateIdentifier);

			int localQueryId = subState.getLocalQueryId();
			QueryState queryState = subState.getQueryState();
			Map<ILogicalQueryPart, PeerID> previousAllocationMap = state.getAllocationMap(localQueryId);
			if (previousAllocationMap == null) {
				LOG.error("Recovery Process State has not enough information (SharedQueryID or "
						+ "previous AllocationMap is missing). Reallocation is aborted.");
			}

			List<PeerID> inadequatePeers = subState.getInadequatePeers();
			if (inadequatePeers == null) {
				inadequatePeers = new ArrayList<PeerID>();
				LOG.debug("Querypart has no inadequate peers. This could be a problem, "
						+ "because at minimum one peer could not process these query.");
			}

			try {
				allocationMap = cRecoveryDynamicBackup.get().reallocateToNewPeer(previousAllocationMap,
						inadequatePeers, cRecoveryAllocator.get());
				if (allocationMap != null && !allocationMap.isEmpty()) {
					for (ILogicalQueryPart queryPartForAllocation : allocationMap.keySet()) {
						sendRecoveryMessages(localQueryId, failedPeer, allocationMap.get(queryPartForAllocation),
								queryPartForAllocation, queryState, recoveryStateIdentifier, recoverySubStateIdentifier);
					}
				} else {
					LOG.debug("Unable to find Peer ID for reallocation.");
				}
			} catch (QueryPartAllocationException e) {
				LOG.error("Peer ID search for recovery reallocation failed.");
			}
		}
	}

	@Override
	public void recover(PeerID failedPeer, UUID recoveryStateIdentifier) {

		// Preconditions
		if (!cPeerDictionary.isPresent()) {
			LOG.error("No P2P dictionary bound!");
			return;
		} else if (!cRecoveryAllocator.isPresent()) {
			LOG.error("No recovery allocator bound!");
			return;
		} else if (!cRecoveryDynamicBackup.isPresent()) {
			LOG.error("No recovery dynamic backup bound!");
			return;
		} else if (!cP2PNetworkManager.isPresent()) {
			LOG.error("No P2PNetworkManager backup bound!");
			return;
		}

		LOG.debug("Want to start recovery for {}", cPeerDictionary.get().getRemotePeerName(failedPeer));

		// Let's see what we know about this peer
		HashMap<Integer, BackupInfo> infoMap = backupInformationAccess.getBackupInformation(failedPeer.toString());

		if (infoMap.isEmpty()) {
			LOG.debug("Can't do recovery for {}. Don't have the information.",
					cPeerDictionary.get().getRemotePeerName(failedPeer));
		}

		RecoveryProcessState state = cRecoveryDynamicBackup.get().getRecoveryProcessState(recoveryStateIdentifier);

		// Reallocate each query to another peer
		for (int localQueryId : infoMap.keySet()) {
			LOG.debug("Have info for {} -> I can do recovery.", cPeerDictionary.get().getRemotePeerName(failedPeer));
			// Search for another peer who can take the parts from the failed peer

			QueryState queryState = QueryState.valueOf(infoMap.get(localQueryId).state);
			Map<ILogicalQueryPart, PeerID> allocationMap = null;

			try {
				// get a PeerID for allocation
				allocationMap = cRecoveryDynamicBackup.get().allocateToNewPeer(failedPeer, localQueryId,
						cRecoveryAllocator.get());
				state.setAllocationMap(localQueryId, allocationMap);

				if (allocationMap != null && allocationMap.values() != null) {
					LOG.debug("Peer ID for recovery allocation found.");
					Iterator<ILogicalQueryPart> iterator = allocationMap.keySet().iterator();
					while (iterator.hasNext()) {
						ILogicalQueryPart queryPart = iterator.next();
						UUID subprocessID = state.createNewSubprocess(localQueryId, queryPart, queryState);

						sendRecoveryMessages(localQueryId, failedPeer, allocationMap.get(queryPart), queryPart,
								queryState, recoveryStateIdentifier, subprocessID);
					}
				} else {
					LOG.debug("Unable to find Peer ID for recovery allocation.");
				}

			} catch (QueryPartAllocationException e) {
				LOG.error("Peer ID search for recovery allocation failed.");
			}

		}

	}

	private void sendRecoveryMessages(int localQueryId, PeerID failedPeer, PeerID newPeer, ILogicalQueryPart queryPart,
			QueryState queryState, UUID recoveryStateIdentifier, UUID subprocessID) {
		cRecoveryDynamicBackup.get().determineAndSendHoldOnMessages(localQueryId, failedPeer, recoveryStateIdentifier);

		// Tell the new peer to install the parts from the failed peer
		cRecoveryDynamicBackup.get().initiateAgreement(failedPeer, localQueryId, queryState, newPeer, queryPart,
				recoveryStateIdentifier, subprocessID);
	}

	@Override
	public String getName() {
		return "upstreambackup";
	}

	@Override
	public void setAllocator(IRecoveryAllocator allocator) {
		cRecoveryAllocator = Optional.of(allocator);
	}

}
