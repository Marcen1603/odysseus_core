package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.protocol;

import java.util.Collection;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingCommunicationListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.LoadBalancingInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingStatusCache;

public class InstructionHandler {

	/**
	 * Handles Instruction messages, sent from initiation Master Peer to Slave Peers.
	 * @param instruction Instruction received by Peer.
	 * @param senderPeer Peer that sent Message (in this case: Master Peer).
	 */
	public static void handleInstruction(LoadBalancingInstructionMessage instruction,
			PeerID senderPeer) {

		int lbProcessId = instruction.getLoadBalancingProcessId();
		LoadBalancingMessageDispatcher dispatcher = null;
		LoadBalancingSlaveStatus status = LoadBalancingStatusCache
				.getInstance().getSlaveStatus(senderPeer, lbProcessId);
		

		IPeerCommunicator peerCommunicator = LoadBalancingCommunicationListener.getPeerCommunicator();
		ISession session = LoadBalancingCommunicationListener.getActiveSession();
		

		
		boolean isSender = true;

		
		//Decide which message we received.
		switch (instruction.getMsgType()) {

		case LoadBalancingInstructionMessage.INITIATE_LOADBALANCING:
			// Only react to first INITIATE_LOADBALANCING Message, even if sent
			// more often.
			
			if (status == null) {
				status = new LoadBalancingSlaveStatus(
						LoadBalancingSlaveStatus.INVOLVEMENT_TYPES.VOLUNTEERING_PEER,
						LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_ADD,
						senderPeer, lbProcessId,
						new LoadBalancingMessageDispatcher(peerCommunicator,
								session, lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status);
				status.getMessageDispatcher().sendAckInit(senderPeer);
			}
			break;

		case LoadBalancingInstructionMessage.ADD_QUERY:
			// Only react if status is not set yet.
			if (status.getPhase().equals(
					LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_ADD)) {
				status.setPhase(LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_SYNC);
				dispatcher = status.getMessageDispatcher();
				dispatcher.stopRunningJob();
				try {
					Collection<Integer> queryIDs = LoadBalancingHelper
							.installAndRunQueryPartFromPql(Context.empty(),
									instruction.getPQLQuery());
					dispatcher.sendInstallSuccess(senderPeer, queryIDs);
				} catch (Exception e) {
					status.setPhase(LoadBalancingSlaveStatus.LB_PHASES.ABORT);
					dispatcher.sendInstallFailure(senderPeer);
				}
			}
			break;

		case LoadBalancingInstructionMessage.COPY_RECEIVER:
		case LoadBalancingInstructionMessage.COPY_SENDER:
			// Create Status if none exist
			if (status == null) {
				status = new LoadBalancingSlaveStatus(
						LoadBalancingSlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER,
						LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_SYNC,
						senderPeer, lbProcessId,
						new LoadBalancingMessageDispatcher(peerCommunicator,
								session, lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status);
			}
			// Process Pipe only if not already processed:
			if (status.getPhase().equals(
					LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_SYNC)
					&& !status.isPipeKnown(instruction.getNewPipeId())) {
				status.addReplacedPipe(instruction.getOldPipeId(),
						instruction.getOldPipeId());
				dispatcher = status.getMessageDispatcher();
				try {
					LoadBalancingHelper.findAndCopyLocalJxtaOperator(status,isSender,
							instruction.getNewPeerId(),
							instruction.getOldPipeId(),
							instruction.getNewPipeId());
					dispatcher.sendDuplicateSuccess(senderPeer,
							instruction.getNewPipeId());
				} catch (Exception e) {
					dispatcher.sendDuplicateFailure(senderPeer);
				}
			}
			break;

		case LoadBalancingInstructionMessage.DELETE_RECEIVER:
		case LoadBalancingInstructionMessage.DELETE_SENDER:
			LoadBalancingHelper.removeDuplicateJxtaOperator(instruction.getOldPipeId());
			break;

		}

	}
}
