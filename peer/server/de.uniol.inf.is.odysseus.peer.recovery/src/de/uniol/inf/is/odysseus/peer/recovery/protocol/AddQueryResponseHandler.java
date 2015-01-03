package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.recovery.IAddQueryResponseHandler;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAddQueryResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * A Handler for working with AddQueryResponse Messages. This messages could be an ack or fail message. Both types need
 * to be handled here.
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class AddQueryResponseHandler implements IAddQueryResponseHandler {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AddQueryResponseHandler.class);
	private static Optional<IRecoveryStrategyManager> recoveryStrategyManager = Optional.absent();
	private Optional<IPeerCommunicator> peerCommunicator = Optional.absent();
	private static IBackupInformationAccess backupInformationAccess;

	public void bindPeerCommunicator(IPeerCommunicator serv) {
		Preconditions.checkNotNull(serv);
		peerCommunicator = Optional.of(serv);
		LOG.debug("Bound {} as a peer communicator.", serv.getClass().getSimpleName());
	}

	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		Preconditions.checkNotNull(serv);
		if (peerCommunicator.isPresent() && peerCommunicator.get() == serv) {
			peerCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a peer communicator.", serv.getClass().getSimpleName());
		}
	}

	public static void bindRecoveryStrategyManager(IRecoveryStrategyManager serv) {
		Preconditions.checkNotNull(serv);
		recoveryStrategyManager = Optional.of(serv);
		LOG.debug("Bound {} as a recovery strategy manager.", serv.getClass().getSimpleName());
	}

	public static void unbindRecoveryStrategyManager(IRecoveryStrategyManager serv) {
		Preconditions.checkNotNull(serv);
		if (recoveryStrategyManager.isPresent() && recoveryStrategyManager.get() == serv) {
			recoveryStrategyManager = Optional.absent();
			LOG.debug("Unbound {} as a recovery strategy manager.", serv.getClass().getSimpleName());
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

	@Override
	public void handleAddQueryResponse(PeerID senderPeer, IRecoveryCommunicator communicator,
			RecoveryAddQueryResponseMessage responseMessage, UUID processStateId, UUID subprocessId, String pql,
			int localQueryId) {
		if (responseMessage.isPositive()) {
			handleAddQueryAck(senderPeer, communicator, processStateId, subprocessId, pql, localQueryId);
		} else {
			handleAddQueryFail(senderPeer, communicator, processStateId, subprocessId);
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
	private void handleAddQueryFail(PeerID senderPeer, IRecoveryCommunicator communicator, UUID processStateId,
			UUID subprocessId) {

		if (processStateId != null) {
			RecoveryProcessState state = communicator.getRecoveryProcessState(processStateId);
			if (state != null) {
				// senderPeer is peer that couldn't recover, we need to
				// reallocate another peer
				state.addInadequatePeer(senderPeer, subprocessId);
				recoveryStrategyManager.get().restartRecovery(state.getFailedPeerId(), state.getIdentifier(),
						subprocessId);
			} else {
				LOG.error("Could not find RecoveryProcessState with ID: {}", processStateId);
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
	private void handleAddQueryAck(PeerID senderPeer, IRecoveryCommunicator communicator, UUID processStateId,
			UUID subprocessId, String pql, int localQueryId) {

		if (processStateId != null) {
			RecoveryProcessState state = communicator.getRecoveryProcessState(processStateId);
			if (state != null) {

				state.subprocessIsDone(subprocessId);

				// Update our sender so it knows the new peerId
				List<JxtaSenderPO<?>> affectedSenders = getAffectedSenders(state.getFailedPeerId());
				for (int i = 0; i < affectedSenders.size(); i++) {
					affectedSenders.get(i).setPeerIDString(senderPeer.toString());
				}

				// Update the DDC -> This is recovered, remove the old entry from the DDC
				backupInformationAccess.removeBackupInformation(state.getFailedPeerId().toString(), localQueryId);

				// Remove recovery process state if all queryParts are processed
				if (state.allSubprocessesDone()) {
					communicator.removeRecoveryProcessState(processStateId);
				}

			} else {
				LOG.error("Could not find RecoveryProcessState with ID: {}", processStateId);
			}
		} else {
			LOG.error("ADD-Query-ACK has no RecoveryProcessStateId");
		}
	}

	/**
	 * The question is, which of our senders are affected by a (failed) peer
	 * 
	 * @param failedPeer
	 * @return
	 */
	private List<JxtaSenderPO<?>> getAffectedSenders(PeerID failedPeer) {

		List<JxtaSenderPO<?>> senders = RecoveryHelper.getJxtaSenders();
		List<JxtaSenderPO<?>> affectedSenders = new ArrayList<JxtaSenderPO<?>>();

		for (JxtaSenderPO<?> sender : senders) {
			if (sender.getPeerIDString() != null && sender.getPeerIDString().equals(failedPeer.toString())) {
				// We were a direct sender to the failed peer
				affectedSenders.add(sender);
			}
		}

		return affectedSenders;
	}
}
