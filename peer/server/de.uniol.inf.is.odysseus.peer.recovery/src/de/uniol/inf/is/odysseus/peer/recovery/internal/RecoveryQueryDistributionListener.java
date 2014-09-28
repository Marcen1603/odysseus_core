package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.listener.AbstractQueryDistributionListener;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
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
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cCommunicator = Optional
			.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to bind must be not null!");
		cCommunicator = Optional.of(communicator);
		LOG.debug("Bound {} as a recovery communicator.", communicator
				.getClass().getSimpleName());

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

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to unbind must be not null!");
		if (cCommunicator.isPresent()
				&& cCommunicator.get().equals(communicator)) {

			cCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", communicator
					.getClass().getSimpleName());

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

		} else if (!cCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		}

		// Distribute backup information
		for (PeerID peerID : subsequentPartsToPeer.keySet()) {

			Map<PeerID, Collection<String>> subsequentPQLStatements = Maps
					.newHashMap();
			for (ILogicalQueryPart subsequentPart : subsequentPartsToPeer
					.get(peerID)) {

				PeerID allocatedPeer = allocationMap.get(subsequentPart);
				String pqlStatement = LogicalQueryHelper
						.generatePQLStatementFromQueryPart(subsequentPart);

				if (subsequentPQLStatements.containsKey(allocatedPeer)) {

					subsequentPQLStatements.get(allocatedPeer)
							.add(pqlStatement);

				} else {

					Collection<String> pqlStatements = Lists
							.newArrayList(pqlStatement);
					subsequentPQLStatements.put(allocatedPeer, pqlStatements);

				}

				// Save information about JXTA
				// TODO Where can I access information about the JXTA-operators?

				List<ILogicalOperator> operators = Lists
						.newArrayList(subsequentPart.getOperators());
				List<ILogicalOperator> visitedOperators = Lists.newArrayList();
				while (!operators.isEmpty()) {
					ILogicalOperator operator = operators.remove(0);

					collectOperatorsWithSubscriptions(operator,
							visitedOperators);
				}

				// Hopefully, we have now more in visitedOperators (including
				// jxtaSender and receiver?)

				String key = "";
				String value = "";
				for (ILogicalOperator logicalOp : visitedOperators) {
					if (logicalOp instanceof JxtaSenderAO) {
						JxtaSenderAO sender = (JxtaSenderAO) logicalOp;
						value = sender.getPipeID();
						key = RecoveryCommunicator.JXTA_KEY_SENDER_PIPE_ID;
						cCommunicator.get().sendBackupJxtaInformation(peerID,
								sharedQueryId, key, value);
					} else if (logicalOp instanceof JxtaReceiverAO) {
						JxtaReceiverAO receiver = (JxtaReceiverAO) logicalOp;
						value = receiver.getPipeID();
						key = RecoveryCommunicator.JXTA_KEY_RECEIVER_PIPE_ID;
						cCommunicator.get().sendBackupJxtaInformation(peerID,
								sharedQueryId, key, value);
					}

				}

			}

			if (subsequentPQLStatements.isEmpty()) {

				continue;

			} else if (peerID.equals(cNetworkManager.get().getLocalPeerID())) {

				LocalBackupInformationAccess.storeLocal(sharedQueryId,
						subsequentPQLStatements);

			} else {

				cCommunicator.get().sendBackupInformation(peerID,
						sharedQueryId, subsequentPQLStatements);

			}

		}

	}

	private static void collectOperatorsWithSubscriptions(
			ILogicalOperator operator, List<ILogicalOperator> visitedOperators) {
		if (!visitedOperators.contains(operator)) {
			visitedOperators.add(operator);

			for (LogicalSubscription sub : operator.getSubscribedToSource()) {
				collectOperatorsWithSubscriptions(sub.getTarget(),
						visitedOperators);
			}

			for (LogicalSubscription sub : operator.getSubscriptions()) {
				collectOperatorsWithSubscriptions(sub.getTarget(),
						visitedOperators);
			}
		}
	}

}