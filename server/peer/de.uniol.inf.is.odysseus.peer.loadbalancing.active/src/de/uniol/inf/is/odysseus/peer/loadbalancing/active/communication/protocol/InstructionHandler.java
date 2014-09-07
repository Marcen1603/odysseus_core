package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.protocol;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingCommunicationListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.LoadBalancingInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingStatusCache;

/**
 * Handles Instruction messages, sent from initiation Master Peer to Slave Peers.
 */
public class InstructionHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(InstructionHandler.class);
	
	
	/**
	 * Handles Instruction messages, sent from initiation Master Peer to Slave Peers.
	 * @param instruction Instruction received by Peer.
	 * @param senderPeer Peer that sent Message (in this case: Master Peer).
	 */
	public static void handleInstruction(LoadBalancingInstructionMessage instruction,
			PeerID senderPeer) {

		LOG.debug("Got Instruction for Process:" + instruction.getLoadBalancingProcessId());
		
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
			
			LOG.debug("Got INITIATE_LOADBALANCING");
			
			if (status == null) {
				status = new LoadBalancingSlaveStatus(
						LoadBalancingSlaveStatus.INVOLVEMENT_TYPES.VOLUNTEERING_PEER,
						LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_ADD,
						senderPeer, lbProcessId,
						new LoadBalancingMessageDispatcher(peerCommunicator,
								session, lbProcessId));
				
				
				
				if(!LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status)) {
					LOG.error("Adding Status to Cache failed.");
				}
				status.getMessageDispatcher().sendAckInit(senderPeer);
				
			}
			break;

		case LoadBalancingInstructionMessage.ADD_QUERY:
			// Only react if status is not set yet.
			
			LOG.debug("Got ADD_QUERY");
			
			if(status==null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			
			if (status.getPhase().equals(
					LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_ADD)) {
				
				LOG.debug("PQL received:");
				LOG.debug(instruction.getPQLQuery());
				
				status.setPhase(LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_SYNC);
				dispatcher = status.getMessageDispatcher();
				dispatcher.stopRunningJob();
				try {
					Collection<Integer> queryIDs = LoadBalancingHelper
							.installAndRunQueryPartFromPql(Context.empty(),
									instruction.getPQLQuery());
					status.setInstalledQueries(queryIDs);
					dispatcher.sendInstallSuccess(senderPeer);
				} catch (Exception e) {
					dispatcher.sendInstallFailure(senderPeer);
				}
			}
			break;

		case LoadBalancingInstructionMessage.COPY_RECEIVER:
			
			isSender=false;
			//NO BREAK!
		case LoadBalancingInstructionMessage.COPY_SENDER:
			LOG.debug("Got COPY_RECEIVER or COPY_SENDER");
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
				
				
				LOG.debug("Installing pipe " + instruction.getNewPipeId());
				
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
					LOG.error("Error while copying JxtaOperator:");
					LOG.error(e.getMessage());
					dispatcher.sendDuplicateFailure(senderPeer);
				}
			}
			break;

		case LoadBalancingInstructionMessage.PIPE_SUCCCESS_RECEIVED:
			LOG.debug("Got PIPE_SUCCESS");
			if(status==null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			status.getMessageDispatcher().stopRunningJob(instruction.getOldPipeId());
			break;
			
		case LoadBalancingInstructionMessage.DELETE_RECEIVER:
		case LoadBalancingInstructionMessage.DELETE_SENDER:
			LOG.debug("Got DELETE_SENDER or DELETE_RECEIVER");
			if(status==null) {
				return;
			}
			//Sync is finished (or we would not be here). So we can stop spamming SYNC_FINISHED Messages now.
			status.getMessageDispatcher().stopAllMessages();
			status.getMessageDispatcher().sendDeleteFinished(senderPeer,instruction.getOldPipeId());
			LoadBalancingHelper.removeDuplicateJxtaOperator(instruction.getOldPipeId());
			status.getReplacedPipes().remove(instruction.getOldPipeId());
			
			break;

				
		case LoadBalancingInstructionMessage.MESSAGE_RECEIVED:
			LOG.debug("Got MESSAGE_RECEIVED");
			if(status==null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			status.getMessageDispatcher().stopAllMessages();
			break;
		}
		
		
		
		
		
		

	}
}
