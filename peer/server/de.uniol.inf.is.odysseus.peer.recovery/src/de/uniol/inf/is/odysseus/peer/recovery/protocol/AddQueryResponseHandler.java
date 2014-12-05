package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.recovery.IAddQueryResponseHandler;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;
import de.uniol.inf.is.odysseus.peer.recovery.internal.SharedQuery;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAddQueryResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.BackupInformationHelper;
import de.uniol.inf.is.odysseus.peer.recovery.util.BuddyHelper;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * A Handler for working with AddQueryResponse Messages. This messages could be
 * an ack or fail message. Both types need to be handled here.
 * 
 * @author ChrisToenjesDeye
 * 
 */
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
			IRecoveryCommunicator communicator,
			RecoveryAddQueryResponseMessage responseMessage,
			UUID processStateId, UUID subprocessId, String pql, ID sharedQueryId) {
		if (responseMessage.isPositive()) {
			handleAddQueryAck(senderPeer, communicator, processStateId,
					subprocessId, pql, sharedQueryId);
		} else {
			handleAddQueryFail(senderPeer, communicator, processStateId,
					subprocessId);
		}
	}

	/**
	 * Handles an fail response. Starts reallocation of the queryPart
	 * 
	 * @param senderPeer
	 * @param communicator
	 * @param processStateId
	 * @param subprocessId
	 */
	private void handleAddQueryFail(PeerID senderPeer,
			IRecoveryCommunicator communicator, UUID processStateId,
			UUID subprocessId) {

		if (processStateId != null) {
			RecoveryProcessState state = communicator
					.getRecoveryProcessState(processStateId);
			if (state != null) {
				// senderPeer is peer that couldn't recover, we need to
				// reallocate another peer
				state.addInadequatePeer(senderPeer, subprocessId);
				recoveryStrategyManager.get().restartRecovery(
						state.getFailedPeerId(), state.getIdentifier(),
						subprocessId);
			} else {
				LOG.error("Could not found RecoveryProcessState with ID: {}",
						processStateId);
			}
		} else {
			LOG.error("ADD-Query-Fail has no RecoveryProcessStateId");
		}
	}

	/**
	 * Handles an ACK response. Removes recoverySubProcess.
	 * 
	 * @param senderPeer
	 * @param processStateId
	 * @param subprocessId
	 * @param pql
	 * @param sharedQueryId
	 */
	private void handleAddQueryAck(PeerID senderPeer,
			IRecoveryCommunicator communicator, UUID processStateId,
			UUID subprocessId, String pql, ID sharedQueryId) {

		if (processStateId != null) {
			RecoveryProcessState state = communicator
					.getRecoveryProcessState(processStateId);
			if (state != null) {

				state.subprocessIsDone(subprocessId);

				// If we have information about the backup-info of the failed
				// peer, give it to the new peer
				List<IRecoveryBackupInformation> infos = BuddyHelper
						.findBackupInfoForBuddy(state.getFailedPeerId());
				for (IRecoveryBackupInformation info : infos) {
					// Now this is located on the new peer, the failed peer does
					// not exist anymore
					info.setLocationPeer(senderPeer);

					BackupInformationSender.getInstance().sendNewBackupInfo(
							senderPeer, info, peerCommunicator.get());
				}

				// Update our sender so it knows the new peerId
				List<JxtaSenderPO<?>> affectedSenders = getAffectedSenders(state
						.getFailedPeerId());
				for (int i = 0; i < affectedSenders.size(); i++) {
					affectedSenders.get(i).setPeerIDString(
							senderPeer.toString());
				}

				// Update our info-store and the store of the new peer
				BackupInformationHelper
						.updateInfoStores(state.getFailedPeerId(), senderPeer,
								sharedQueryId, pql);

				// TODO Send to all peers, that this peer failed and was
				// recovered by the new peer
				// They have to remove their information about the failed peer
				// and can save information about the new peer, if they had
				// information about the failed peer with the
				// shared query id

				// Remove recovery process state if all queryParts are processed
				if (state.allSubprocessesDone()) {
					communicator.removeRecoveryProcessState(processStateId);
				}

			} else {
				LOG.error("Could not find RecoveryProcessState with ID: {}",
						processStateId);
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
			if (sender.getPeerIDString() != null
					&& sender.getPeerIDString().equals(failedPeer.toString())) {
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
							// Save that this sender is affected
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
