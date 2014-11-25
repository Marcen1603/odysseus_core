package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IAddQueryResponseHandler;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;
import de.uniol.inf.is.odysseus.peer.recovery.internal.SharedQuery;
import de.uniol.inf.is.odysseus.peer.recovery.messages.AddQueryResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.BackupInformationHelper;
import de.uniol.inf.is.odysseus.peer.recovery.util.BuddyHelper;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

public class AddQueryResponseHandler implements IAddQueryResponseHandler {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AddQueryResponseHandler.class);
	private static Optional<IRecoveryStrategyManager> recoveryStrategyManager = Optional
			.absent();
	private Optional<IPeerCommunicator> peerCommunicator = Optional.absent();

	public void bindPeerCommunicator(IPeerCommunicator serv) {
		Preconditions.checkNotNull(serv);
		peerCommunicator = Optional.of(serv);
		LOG.debug("Bound {} as a peer communicator.", serv.getClass()
				.getSimpleName());
	}

	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		Preconditions.checkNotNull(serv);
		if (peerCommunicator.isPresent() && peerCommunicator.get() == serv) {
			peerCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a peer communicator.", serv.getClass()
					.getSimpleName());
		}
	}

	public static void bindRecoveryStrategyManager(IRecoveryStrategyManager serv) {
		Preconditions.checkNotNull(serv);
		recoveryStrategyManager = Optional.of(serv);
		LOG.debug("Bound {} as a recovery strategy manager.", serv.getClass()
				.getSimpleName());
	}

	public static void unbindRecoveryStrategyManager(
			IRecoveryStrategyManager serv) {
		Preconditions.checkNotNull(serv);
		if (recoveryStrategyManager.isPresent()
				&& recoveryStrategyManager.get() == serv) {
			recoveryStrategyManager = Optional.absent();
			LOG.debug("Unbound {} as a recovery strategy manager.", serv
					.getClass().getSimpleName());
		}
	}

	@Override
	public void handleAddQueryResponse(PeerID senderPeer,
			AddQueryResponseMessage responseMessage,
			IRecoveryCommunicator recoveryCommunicator) {
		if (responseMessage.getMessageType() == AddQueryResponseMessage.ACK) {
			handleAddQueryAck(senderPeer, responseMessage, recoveryCommunicator);
		} else if (responseMessage.getMessageType() == AddQueryResponseMessage.FAIL) {
			handleAddQueryFail(senderPeer, responseMessage,
					recoveryCommunicator);
		}
	}

	private void handleAddQueryFail(PeerID senderPeer,
			AddQueryResponseMessage addQueryFailResponse,
			IRecoveryCommunicator recoveryCommunicator) {

		if (addQueryFailResponse.getRecoveryProcessStateId() != null) {
			RecoveryProcessState state = recoveryCommunicator
					.getRecoveryProcessState(addQueryFailResponse
							.getRecoveryProcessStateId());
			if (state != null) {
				// senderPeer is peer that couldn't recover, we need to
				// reallocate another peer

				List<ILogicalQuery> logicalQueries = RecoveryHelper
						.convertToLogicalQueries(addQueryFailResponse
								.getPqlQueryPart());
				// allocate the query
				if (!logicalQueries.isEmpty()) {
					ILogicalQueryPart queryPart = new LogicalQueryPart(
							LogicalQueryHelper.getAllOperators(logicalQueries
									.get(0)));

					state.addInadequatePeer(senderPeer,
							addQueryFailResponse.getSharedQueryId(), queryPart);
					recoveryStrategyManager.get().restartRecovery(
							state.getFailedPeerId(), state.getIdentifier(),
							queryPart);
				}
			} else {
				LOG.error("Could not found RecoveryProcessState with ID: {}",
						addQueryFailResponse.getRecoveryProcessStateId());
			}
		} else {
			LOG.error("ADD-Query-Fail has no RecoveryProcessStateId");
		}
	}

	private void handleAddQueryAck(PeerID senderPeer,
			AddQueryResponseMessage addQueryAckResponse,
			IRecoveryCommunicator recoveryCommunicator) {
		if (addQueryAckResponse.getRecoveryProcessStateId() != null) {
			RecoveryProcessState state = recoveryCommunicator
					.getRecoveryProcessState(addQueryAckResponse
							.getRecoveryProcessStateId());
			if (state != null) {
				// Give this peer the backup-info from the peer which he
				// recovers
				List<ILogicalQuery> logicalQueries = RecoveryHelper
						.convertToLogicalQueries(addQueryAckResponse
								.getPqlQueryPart());
				if (!logicalQueries.isEmpty()) {
					ILogicalQueryPart queryPart = new LogicalQueryPart(
							LogicalQueryHelper.getAllOperators(logicalQueries
									.get(0)));
					state.queryPartIsProcessed(queryPart);
				}

				List<IRecoveryBackupInformation> infos = BuddyHelper
						.findBackupInfoForBuddy(state.getFailedPeerId());
				for (IRecoveryBackupInformation info : infos) {
					// Now this is located on the new peer, the failed
					// peer does not exist anymore
					info.setLocationPeer(senderPeer);

					BackupInformationSender.getInstance().sendNewBackupInfo(
							senderPeer, info, peerCommunicator.get());
				}

				// 6. Update our sender so it knows the new peerId
				List<JxtaSenderPO<?>> affectedSenders = getAffectedSenders(state
						.getFailedPeerId());
				for (int i = 0; i < affectedSenders.size(); i++) {
					affectedSenders.get(i).setPeerIDString(
							senderPeer.toString());
				}

				// Remove recovery process state if all queryParts are processed
				BackupInformationHelper.updateInfoStores(
						state.getFailedPeerId(), senderPeer,
						addQueryAckResponse.getSharedQueryId(),
						addQueryAckResponse.getPqlQueryPart());

				if (state.allQueryPartsRecovered()) {
					recoveryCommunicator
							.removeRecoveryProcessState(addQueryAckResponse
									.getRecoveryProcessStateId());
				}

			} else {
				LOG.error("Could not found RecoveryProcessState with ID: {}",
						addQueryAckResponse.getRecoveryProcessStateId());
			}
		} else {
			LOG.error("ADD-Query-ACK has no RecoveryProcessStateId");
		}
	}

	private List<JxtaSenderPO<?>> getAffectedSenders(PeerID failedPeer) {

		// 1. Check, if we have backup information for the failed peer and for
		// which shared-query-ids
		// Return if there is no backup information stored for the given peer

		Collection<ID> sharedQueryIdsForPeer = LocalBackupInformationAccess
				.getStoredSharedQueryIdsForPeer(failedPeer);
		if (sharedQueryIdsForPeer == null || sharedQueryIdsForPeer.isEmpty()) {
			// We don't have any information about that failed peer
			return null;
		}

		// 2. Check, if we were a direct sender to that failed peer

		// We maybe have backup-information about queries for that peer where we
		// are not the direct sender, so we have to save for which queries we
		// are the direct sender

		List<JxtaSenderPO<?>> senders = RecoveryHelper.getJxtaSenders();
		List<JxtaSenderPO<?>> affectedSenders = new ArrayList<JxtaSenderPO<?>>();

		for (JxtaSenderPO<?> sender : senders) {
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

			// TODO What are the affected senders? Maybe no sender is affected
			// cause it's a totally different peer. Maybe we have to tell other
			// peers to install new receivers or sth. like that
		}
		return affectedSenders;
	}
}
