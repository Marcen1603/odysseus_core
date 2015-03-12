package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol;

import java.util.Collection;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.ParallelTrackCommunicatorImpl;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.ParallelTrackHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.ParallelTrackMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.ParallelTrackInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.ParallelTrackSlaveStatus;

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
	public static void handleInstruction(ParallelTrackInstructionMessage instruction,
			PeerID senderPeer) {

		LOG.debug("Got Instruction for Process:" + instruction.getLoadBalancingProcessId());
		
		int lbProcessId = instruction.getLoadBalancingProcessId();
		ParallelTrackMessageDispatcher dispatcher = null;
		
		ParallelTrackSlaveStatus status = (ParallelTrackSlaveStatus)LoadBalancingStatusCache
				.getInstance().getSlaveStatus(senderPeer, lbProcessId);
		

		IPeerCommunicator peerCommunicator = ParallelTrackCommunicatorImpl.getPeerCommunicator();
		
		
		boolean isSender = true;

		
		//Decide which message we received.
		switch (instruction.getMsgType()) {

		case ParallelTrackInstructionMessage.INITIATE_LOADBALANCING:
			// Only react to first INITIATE_LOADBALANCING Message, even if sent
			// more often.
			
			LOG.debug("Got INITIATE_LOADBALANCING");
			
			if (status == null) {
				status = new ParallelTrackSlaveStatus(
						ParallelTrackSlaveStatus.INVOLVEMENT_TYPES.VOLUNTEERING_PEER,
						ParallelTrackSlaveStatus.LB_PHASES.WAITING_FOR_ADD,
						senderPeer, lbProcessId,
						new ParallelTrackMessageDispatcher(peerCommunicator, lbProcessId));
				
				
				
				if(!LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status)) {
					LOG.error("Adding Status to Cache failed.");
				}
				status.getMessageDispatcher().sendAckInit(senderPeer);
				
			}
			break;

		case ParallelTrackInstructionMessage.ADD_QUERY:
			// Only react if status is not set yet.
			
			LOG.debug("Got ADD_QUERY");
			
			if(status==null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			
			if (status.getPhase().equals(
					ParallelTrackSlaveStatus.LB_PHASES.WAITING_FOR_ADD)) {
				
				LOG.debug("PQL received:");
				LOG.debug(instruction.getPQLQuery());
				
				status.setPhase(ParallelTrackSlaveStatus.LB_PHASES.WAITING_FOR_SYNC);
				dispatcher = status.getMessageDispatcher();
				dispatcher.stopRunningJob();
				
				try {
					Collection<Integer> queryIDs = LoadBalancingHelper
							.installAndRunQueryPartFromPql(Context.empty(),
									instruction.getPQLQuery());
					status.setInstalledQueries(queryIDs);
					
					int installedQuery = (int)(queryIDs.toArray()[0]);
					ILogicalQuery query = OsgiServiceManager.getExecutor().getLogicalQueryById(installedQuery, OsgiServiceManager.getActiveSession());
					IQueryPartController queryPartController = OsgiServiceManager.getQueryPartController();
					
					//Register as new Master when Query is Master Query
					if(instruction.isMasterForQuery()) {
						
						LOG.debug("Received Query is Master Query");
						
						List<String> otherPeerIDStrings = instruction.getOtherPeerIDs();
						String sharedQueryIDString = instruction.getSharedQueryID();
						
						LOG.debug("Received {} other peer IDs.",otherPeerIDStrings.size());
						LOG.debug("Shared Query ID is {}",sharedQueryIDString);
						Collection<PeerID> otherPeers = LoadBalancingHelper.toPeerIDCollection(otherPeerIDStrings);
						ID sharedQueryID = LoadBalancingHelper.toID(sharedQueryIDString);
						
						
						queryPartController.registerAsMaster(query,installedQuery, sharedQueryID, otherPeers);
						status.setRegisteredAsMaster(sharedQueryID);
						OsgiServiceManager.getQueryManager().sendChangeMasterToAllSlaves(sharedQueryID);
						
					}
					else {
						LOG.debug("Received Query is Slave Query.");
						ID sharedQueryID = LoadBalancingHelper.toID(instruction.getSharedQueryID());
						PeerID masterPeerID = LoadBalancingHelper.toPeerID(instruction.getMasterPeerID());
						
						if(queryPartController.isSharedQueryKnown(sharedQueryID)) {
							//No need to inform Master as he already knows this Peer, just add local Query to sharedID...
							Collection<Integer> localQueriesForSharedQuery = queryPartController.getLocalIds(sharedQueryID);
							localQueriesForSharedQuery.addAll(queryIDs);
							queryPartController.registerAsSlave(localQueriesForSharedQuery, sharedQueryID, masterPeerID);
						}
						else {
							queryPartController.registerAsSlave(queryIDs,sharedQueryID,masterPeerID);
							status.setRegisteredAsNewSlave(masterPeerID, sharedQueryID);
							OsgiServiceManager.getQueryManager().sendRegisterAsSlave(masterPeerID, sharedQueryID);
							
						}
						
						
					}
					
					
					dispatcher.sendInstallSuccess(senderPeer);
				} catch (Exception e) {
					dispatcher.sendInstallFailure(senderPeer);
				}
			}
			break;

		case ParallelTrackInstructionMessage.COPY_RECEIVER:
			
			isSender=false;
			LOG.debug("Got COPY_RECEIVER");
			
			LOG.debug(instruction.getOldPipeId()+ " ----> " + instruction.getNewPipeId());
			
			// Create Status if none exist
			if (status == null) {
				status = new ParallelTrackSlaveStatus(
						ParallelTrackSlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER,
						ParallelTrackSlaveStatus.LB_PHASES.WAITING_FOR_SYNC,
						senderPeer, lbProcessId,
						new ParallelTrackMessageDispatcher(peerCommunicator,lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status);
			}
			
			// Process Pipe only if not already processed:
			
			if (status.getPhase().equals(
					ParallelTrackSlaveStatus.LB_PHASES.WAITING_FOR_SYNC)
					&& !status.isReceiverPipeKnown(instruction.getNewPipeId())) {
				
				
				LOG.debug("Installing pipe " + instruction.getNewPipeId());
				
				status.addReplacedReceiverPipe(instruction.getNewPipeId(),
						instruction.getOldPipeId());
				if(status.getVolunteeringPeer()==null) {
					status.setVolunteeringPeer(LoadBalancingHelper.toPeerID((instruction.getNewPeerId())));
				}
				
				
				dispatcher = status.getMessageDispatcher();
				try {
					ParallelTrackHelper.findAndCopyLocalJxtaOperator(status,isSender,
							instruction.getNewPeerId(),
							instruction.getOldPipeId(),
							instruction.getNewPipeId());
					dispatcher.sendDuplicateSuccess(senderPeer,
							instruction.getNewPipeId());
				} catch (Exception e) {
					LOG.error("Error while copying JxtaOperator:",e);
					dispatcher.sendDuplicateFailure(senderPeer);
				}
			}
			break;
			
		case ParallelTrackInstructionMessage.COPY_SENDER:
			isSender=true;
			LOG.debug("Got COPY_SENDER");
			
			LOG.debug(instruction.getOldPipeId()+ " ----> " + instruction.getNewPipeId());
			
			// Create Status if none exist
			if (status == null) {
				status = new ParallelTrackSlaveStatus(
						ParallelTrackSlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER,
						ParallelTrackSlaveStatus.LB_PHASES.WAITING_FOR_SYNC,
						senderPeer, lbProcessId,
						new ParallelTrackMessageDispatcher(peerCommunicator,lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status);
			}
			
			// Process Pipe only if not already processed:
			
			if (status.getPhase().equals(
					ParallelTrackSlaveStatus.LB_PHASES.WAITING_FOR_SYNC)
					&& !status.isSenderPipeKnown(instruction.getNewPipeId())) {
				
				
				LOG.debug("Installing pipe " + instruction.getNewPipeId());
				
				status.addReplacedSenderPipe(instruction.getNewPipeId(),
						instruction.getOldPipeId());
				if(status.getVolunteeringPeer()==null) {
					status.setVolunteeringPeer(LoadBalancingHelper.toPeerID((instruction.getNewPeerId())));
				}
				
				
				dispatcher = status.getMessageDispatcher();
				try {
					ParallelTrackHelper.findAndCopyLocalJxtaOperator(status,isSender,
							instruction.getNewPeerId(),
							instruction.getOldPipeId(),
							instruction.getNewPipeId());
					dispatcher.sendDuplicateSuccess(senderPeer,
							instruction.getNewPipeId());
				} catch (Exception e) {
					LOG.error("Error while copying JxtaOperator:",e);
					dispatcher.sendDuplicateFailure(senderPeer);
				}
			}
			break;

		case ParallelTrackInstructionMessage.PIPE_SUCCCESS_RECEIVED:
			LOG.debug("Got PIPE_SUCCESS");
			if(status==null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			
			status.getMessageDispatcher().stopRunningJob(instruction.getOldPipeId());
			break;
			
		case ParallelTrackInstructionMessage.DELETE_RECEIVER:
			LOG.debug("Got DELETE_RECEIVER");
			if(status==null) {
				LOG.debug("Stauts already null. Sending improvised Stop message.");
				ParallelTrackMessageDispatcher tmpMessageDispatcher = new ParallelTrackMessageDispatcher(ParallelTrackCommunicatorImpl.getPeerCommunicator(),instruction.getLoadBalancingProcessId());
				tmpMessageDispatcher.sendDeleteFinished(senderPeer, instruction.getOldPipeId());
				return;
			}
			//Sync is finished (or we would not be here). So we can stop spamming SYNC_FINISHED Messages now.
			status.getMessageDispatcher().stopAllMessages();
			
			status.getMessageDispatcher().sendDeleteFinished(senderPeer,instruction.getOldPipeId());
			
			if(status.getReplacedReceiverPipes().containsKey(instruction.getOldPipeId())) {
				
				ParallelTrackHelper.removeDuplicateJxtaReceiver(instruction.getOldPipeId());
				ParallelTrackHelper.updatePipeID(instruction.getOldPipeId(), status.getReplacedReceiverPipes().get(instruction.getOldPipeId()), status.getVolunteeringPeer().toString());
				status.getReplacedReceiverPipes().remove(instruction.getOldPipeId());
			}
			
			break;
			
			
		case ParallelTrackInstructionMessage.DELETE_SENDER:
			LOG.debug("Got DELETE_SENDER");
			if(status==null) {
				LOG.debug("Stauts already null. Sending improvised Stop message.");
				ParallelTrackMessageDispatcher tmpMessageDispatcher = new ParallelTrackMessageDispatcher(ParallelTrackCommunicatorImpl.getPeerCommunicator(),instruction.getLoadBalancingProcessId());
				tmpMessageDispatcher.sendDeleteFinished(senderPeer, instruction.getOldPipeId());
				return;
			}
			//Sync is finished (or we would not be here). So we can stop spamming SYNC_FINISHED Messages now.
			status.getMessageDispatcher().stopAllMessages();
			status.getMessageDispatcher().sendDeleteFinished(senderPeer,instruction.getOldPipeId());
			if(status.getReplacedSenderPipes().containsKey(instruction.getOldPipeId())) {
				
				ParallelTrackHelper.removeDuplicateJxtaSender(instruction.getOldPipeId(),status);
				ParallelTrackHelper.updatePipeID(instruction.getOldPipeId(), status.getReplacedSenderPipes().get(instruction.getOldPipeId()), status.getVolunteeringPeer().toString());
				status.getReplacedSenderPipes().remove(instruction.getOldPipeId());
			}
			
			break;

				
		case ParallelTrackInstructionMessage.MESSAGE_RECEIVED:
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
