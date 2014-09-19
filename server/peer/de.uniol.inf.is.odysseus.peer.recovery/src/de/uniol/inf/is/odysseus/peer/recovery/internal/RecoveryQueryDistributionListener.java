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
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.SubsequentQueryPartsCalculator;

/**
 * The query distribution listener for recovery processes. <br />
 * It stores backup information about distributed queries within
 * {@link IRecoveryBackupInformationStore}.
 * 
 * @author Michael Brand
 */
public class RecoveryQueryDistributionListener extends
		AbstractQueryDistributionListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryQueryDistributionListener.class);

	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cNetworkManager = Optional
			.absent();

	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param manager
	 *            The P2P network manager to bind. <br />
	 *            Must be not null.
	 */
	public static void bindNetworkManager(IP2PNetworkManager manager) {

		Preconditions.checkNotNull(manager,
				"The P2P network manager to bind must be not null!");
		cNetworkManager = Optional.of(manager);
		LOG.debug("Bound {} as a P2P network manager for recovery.", manager
				.getClass().getSimpleName());

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

		Preconditions.checkNotNull(manager,
				"The P2P network manager to unbind must be not null!");
		if (cNetworkManager.isPresent()
				&& cNetworkManager.get().equals(manager)) {

			cNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager for recovery.",
					manager.getClass().getSimpleName());

		}

	}

	/**
	 * The helper instance to calculate subsequent query parts.
	 */
	private SubsequentQueryPartsCalculator mSubsequentPartsCalculator = new SubsequentQueryPartsCalculator();

	@Override
	public void afterPostProcessing(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap) {

		// Keep allocation map for that query in mind, because after
		// transmission, all query parts will be disconnected.
		this.mSubsequentPartsCalculator
				.calcQueryPartGraph(query, allocationMap);

	}

	@Override
	public void afterTransmission(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap, ID sharedQueryId) {

		// Calculate subsequent query parts
		Map<PeerID, Collection<ILogicalQueryPart>> subsequentPartsToPeer = this.mSubsequentPartsCalculator
				.calcSubsequentParts(query, allocationMap);

		if (!cNetworkManager.isPresent()) {

			LOG.error("No P2P network manager for recovery bound!");
			return;

		}

		// Distribute backup information
		for (PeerID peerID : subsequentPartsToPeer.keySet()) {

			Collection<ILogicalQueryPart> subsequentParts = subsequentPartsToPeer
					.get(peerID);

			if (subsequentParts.isEmpty()) {

				continue;

			} else if (peerID.equals(cNetworkManager.get().getLocalPeerID())) {

				LocalBackupInformationAccess.storeLocal(sharedQueryId,
						subsequentParts);

			} else {

				// TODO send a message with shared query ID and subsequent query
				// parts

			}

		}

	}

}