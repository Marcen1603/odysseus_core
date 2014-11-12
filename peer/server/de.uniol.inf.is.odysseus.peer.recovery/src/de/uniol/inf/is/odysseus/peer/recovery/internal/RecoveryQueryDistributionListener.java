package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.listener.AbstractQueryDistributionListener;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.util.BackupInformationCalculator;
import de.uniol.inf.is.odysseus.peer.recovery.util.BuddyHelper;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;

/**
 * The query distribution listener for recovery processes. <br />
 * It stores backup information about distributed queries within
 * {@link IRecoveryBackupInformationStore}.
 * 
 * @author Michael Brand
 */
public class RecoveryQueryDistributionListener extends AbstractQueryDistributionListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryQueryDistributionListener.class);

	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cNetworkManager = Optional.absent();

	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param manager
	 *            The P2P network manager to bind. <br />
	 *            Must be not null.
	 */
	public static void bindNetworkManager(IP2PNetworkManager manager) {

		Preconditions.checkNotNull(manager, "The P2P network manager to bind must be not null!");
		cNetworkManager = Optional.of(manager);
		LOG.debug("Bound {} as a P2P network manager for recovery.", manager.getClass().getSimpleName());

	}

	/**
	 * Unbinds a P2P network manager. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param manager
	 *            The P2P network manager to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindNetworkManager(IP2PNetworkManager manager) {

		Preconditions.checkNotNull(manager, "The P2P network manager to unbind must be not null!");
		if (cNetworkManager.isPresent() && cNetworkManager.get().equals(manager)) {

			cNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager for recovery.", manager.getClass().getSimpleName());

		}

	}

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cCommunicator = Optional.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator, "The recovery communicator to bind must be not null!");
		cCommunicator = Optional.of(communicator);
		LOG.debug("Bound {} as a recovery communicator.", communicator.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator, "The recovery communicator to unbind must be not null!");
		if (cCommunicator.isPresent() && cCommunicator.get().equals(communicator)) {

			cCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", communicator.getClass().getSimpleName());
		}
	}

	/**
	 * The helper instance to calculate backup information.
	 */
	private BackupInformationCalculator mBackupInfoCalculator = new BackupInformationCalculator();

	@Override
	public void afterPostProcessing(ILogicalQuery query, Map<ILogicalQueryPart, PeerID> allocationMap) {

		// Keep allocation map for that query in mind, because after
		// transmission, all query parts will be disconnected.
		this.mBackupInfoCalculator.calcQueryPartGraph(query, allocationMap.keySet());

	}

	@Override
	public void afterTransmission(ILogicalQuery query, Map<ILogicalQueryPart, PeerID> allocationMap, ID sharedQueryId) {

		// Calculate backup information
		Map<PeerID, Collection<IRecoveryBackupInformation>> infoMap = this.mBackupInfoCalculator.calcBackupInformation(
				sharedQueryId, query, allocationMap);

		if (!cNetworkManager.isPresent()) {

			LOG.error("No P2P network manager for recovery bound!");
			return;

		} else if (!cCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		}

		// Distribute backup information
		for (PeerID peer : infoMap.keySet()) {
			for (IRecoveryBackupInformation info : infoMap.get(peer)) {

				if (peer.equals(cNetworkManager.get().getLocalPeerID())) {
					LocalBackupInformationAccess.getStore().add(info);
					checkForBuddy(info.getLocalPQL(), info.getSharedQuery());

				} else {
					cCommunicator.get().sendBackupInformation(peer, info);
				}
			}
		}
	}

	/**
	 * Checks, if this peer is the first peer in the chain of peers for a query.
	 * (This peer has no JxtaReceiverPO for the specific shared query.) If so,
	 * this peer needs a buddy.
	 */
	private void checkForBuddy(String pql, ID sharedQueryId) {

		if (BuddyHelper.needBuddy(pql))
			cCommunicator.get().chooseBuddyForQuery(sharedQueryId);

		/*
		 * Note: Switched from using physical operators to using logical
		 * operators. RecoveryHelper.convertToPhysicalPlan still exists but is
		 * deprecated. M.B.
		 */
		// List<IPhysicalQuery> plans =
		// RecoveryHelper.convertToPhysicalPlan(pql);
		// for (IPhysicalQuery plan : plans) {
		// Set<IPhysicalOperator> ops = plan.getAllOperators();
		// for (IPhysicalOperator op : ops) {
		// if (op instanceof JxtaReceiverPO) {
		// foundReceiver = true;
		// break;
		// }
		// }
		// if (!foundReceiver) {
		// // We need a buddy
		// cCommunicator.get().chooseBuddyForQuery(sharedQueryId);
		// }
		// foundReceiver = false;
		// }

	}
}