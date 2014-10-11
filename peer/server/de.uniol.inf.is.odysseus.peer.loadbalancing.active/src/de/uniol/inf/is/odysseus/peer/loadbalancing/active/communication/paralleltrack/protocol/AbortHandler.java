package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.ParallelTrackCommunicatorImpl;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.ParallelTrackMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.LoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.LoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.LoadBalancingStatusCache;

/**
 * Message Handler for Abort Messages. If AbortInstruction -> SlavePeer handles it, if AbortResponse -> Master Peer stops sending Messages to according slave Peer.
 */
public class AbortHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbortHandler.class);

	
	/**
	 * Message Handler for Abort Messages. If AbortInstruction -> SlavePeer handles it, if AbortResponse -> Master Peer stops sending Messages to according slave Peer.
	 * @param abortMessage
	 * @param senderPeer
	 */
	public static void handleAbort(LoadBalancingAbortMessage abortMessage,
			PeerID senderPeer) {
		
		
		
		switch (abortMessage.getMsgType()) {
		case LoadBalancingAbortMessage.ABORT_INSTRUCTION:
			LOG.debug("Received ABORT_INSTRUCTION");
			undoLoadBalancing(abortMessage, senderPeer);
			break;
		case LoadBalancingAbortMessage.ABORT_RESPONSE:
			LOG.debug("Received ABORT_RESPONSE");
			stopSendingAbort(abortMessage, senderPeer);
			break;
		}
		
	}
	

	/**
	 * When Abort Message is received by slave Peer, this Method decides what to do.
	 * @param abortMessage
	 * @param senderPeer
	 */
	private static void undoLoadBalancing(LoadBalancingAbortMessage abortMessage,
			PeerID senderPeer) {
		
		
		
		int lbProcessId = abortMessage.getLoadBalancingProcessId();
		LoadBalancingSlaveStatus status = LoadBalancingStatusCache
				.getInstance().getSlaveStatus(senderPeer, lbProcessId);
		
		//Only send Ack. if status no longer active.
		if(status==null) {
			LOG.debug("Status already null. Sending ABORT_RESPONSE on improvised Message Dispatcher.");
			IPeerCommunicator peerCommunicator = ParallelTrackCommunicatorImpl.getPeerCommunicator();
			ParallelTrackMessageDispatcher improvisedDispatcher = new ParallelTrackMessageDispatcher(peerCommunicator,abortMessage.getLoadBalancingProcessId());
			improvisedDispatcher.sendAbortResponse(senderPeer);
			return;
		}
		
		ParallelTrackMessageDispatcher dispatcher = status.getMessageDispatcher();
		
		//Ignore Message if already aborting.
		if(!(status.getPhase()==LoadBalancingSlaveStatus.LB_PHASES.ABORT)) {
			LOG.error("Received Abort.");
			dispatcher.stopAllMessages();
			status.setPhase(LoadBalancingSlaveStatus.LB_PHASES.ABORT);
			dispatcher.sendAbortResponse(senderPeer);
			switch (status.getInvolvementType()) {
	
			case PEER_WITH_SENDER_OR_RECEIVER:
	
				if(status.getReplacedPipes()!=null) {
					ArrayList<String> installedPipes = Collections.list(status
							.getReplacedPipes().keys());
					for (String pipe : installedPipes) {
						LOG.error("Removing Operator with Pipe ID " + pipe);
						LoadBalancingHelper.removeDuplicateJxtaOperator(pipe);
					}
				} 
				// NO break, since Peer could in theory also be the volunteering
				// peer
				//$FALL-THROUGH$ 
			case VOLUNTEERING_PEER:
				Collection<Integer> queriesToRemove = status.getInstalledQueries();
				if (queriesToRemove != null) {
					
					for (int query : queriesToRemove) {
						LOG.error("Removing Query with ID " + query);
						LoadBalancingHelper.deleteQuery(query);
					}
				}
				break;
	
			}
		}
	}
	

	/**
	 * Re-Allocates and cleans up status after Abort has been completed.
	 * @param lbProcessId
	 */
	private static void finishAbort(int lbProcessId) {
		
		LoadBalancingMasterStatus status = LoadBalancingStatusCache.getInstance().getStatusForLocalProcess(lbProcessId);
		if(status!=null) {
			LOG.info("LoadBalancing failed.");
			LoadBalancingStatusCache.getInstance().deleteLocalStatus(status.getProcessId());
			ParallelTrackCommunicatorImpl.getInstance().notifyFinished();
		}
	}
	

	public static void stopSendingAbort(LoadBalancingAbortMessage abortMessage,
			PeerID senderPeer) {
		LoadBalancingMasterStatus status = LoadBalancingStatusCache.getInstance().getStatusForLocalProcess(abortMessage.getLoadBalancingProcessId());
		
		LOG.debug("Stop Sending Abort called.");
		
		
		if(status!=null) {
			LOG.debug("Stop Sending Abort to " + senderPeer);
			ParallelTrackMessageDispatcher dispatcher = status.getMessageDispatcher();
			dispatcher.stopRunningJob(senderPeer.toString());
			
			if(dispatcher.getNumberOfRunningJobs()==0) {
				LOG.debug("No more Peers to notify. Finishing Abort.");
				finishAbort(abortMessage.getLoadBalancingProcessId());
			}
		}
	}
	

	
}
