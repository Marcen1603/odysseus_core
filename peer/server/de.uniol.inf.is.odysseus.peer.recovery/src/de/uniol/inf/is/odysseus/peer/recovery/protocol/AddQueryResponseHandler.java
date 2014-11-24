package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.List;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;
import de.uniol.inf.is.odysseus.peer.recovery.messages.AddQueryResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.BuddyHelper;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

public class AddQueryResponseHandler {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AddQueryResponseHandler.class);

	private static AddQueryResponseHandler instance = new AddQueryResponseHandler();

	public static AddQueryResponseHandler getInstance() {
		return instance;
	}

	public void handleAddQueryResponse(PeerID senderPeer,
			AddQueryResponseMessage responseMessage,
			RecoveryCommunicator recoveryCommunicator,
			IRecoveryStrategyManager iRecoveryStrategyManager) {
		if (responseMessage.getMessageType() == AddQueryResponseMessage.ACK) {
			handleAddQueryAck(senderPeer, responseMessage, recoveryCommunicator);
		} else if (responseMessage.getMessageType() == AddQueryResponseMessage.FAIL) {
			handleAddQueryFail(senderPeer, responseMessage,
					recoveryCommunicator, iRecoveryStrategyManager);
		}
	}

	public void handleAddQueryFail(PeerID senderPeer,
			AddQueryResponseMessage addQueryFailResponse,
			RecoveryCommunicator communicator,
			IRecoveryStrategyManager cRecoveryStrategyManager) {

		if (addQueryFailResponse.getRecoveryProcessStateId() != null) {
			RecoveryProcessState state = communicator
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
					cRecoveryStrategyManager.restartRecovery(
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

	public void handleAddQueryAck(PeerID senderPeer,
			AddQueryResponseMessage addQueryAckResponse,
			RecoveryCommunicator communicator) {
		if (addQueryAckResponse.getRecoveryProcessStateId() != null) {
			RecoveryProcessState state = communicator
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
				// Remove recovery process state if all queryParts are processed
				if (state.allQueryPartsRecovered()){
					communicator.removeRecoveryProcessState(addQueryAckResponse
							.getRecoveryProcessStateId());
				}

				List<IRecoveryBackupInformation> infos = BuddyHelper
						.findBackupInfoForBuddy(state.getFailedPeerId());
				for (IRecoveryBackupInformation info : infos) {
					// Now this is located on the new peer, the failed
					// peer does not exist anymore
					info.setLocationPeer(senderPeer);
					BackupInformationMessage backupMessage = new BackupInformationMessage(
							info, BackupInformationMessage.NEW_INFO);
					communicator.sendMessage(senderPeer, backupMessage);
				}
			} else {
				LOG.error("Could not found RecoveryProcessState with ID: {}",
						addQueryAckResponse.getRecoveryProcessStateId());
			}
		} else {
			LOG.error("ADD-Query-ACK has no RecoveryProcessStateId");
		}
	}
}
