package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.protocol;

import java.util.ArrayList;
import java.util.Collections;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingStatusCache;

public class AbortHandler {

	/**
	 * Message Handler for Abort Messages. If AbortInstruction -> SlavePeer handles it, if AbortResponse -> Master Peer stops sending Messages to according slave Peer.
	 * @param abortMessage
	 * @param senderPeer
	 */
	public static void handleAbort(LoadBalancingAbortMessage abortMessage,
			PeerID senderPeer) {
		
		switch (abortMessage.getMsgType()) {
		case LoadBalancingAbortMessage.ABORT_INSTRUCTION:
			
			undoLoadBalancing(abortMessage, senderPeer);
			
		case LoadBalancingAbortMessage.ABORT_RESPONSE:
			
			stopSendingAbort(abortMessage, senderPeer);
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
		
		//Ignore if status no longer active.
		if(status==null) {
			return;
		}
		
		LoadBalancingMessageDispatcher dispatcher = status.getMessageDispatcher();
		
		//Ignore Message if already aborting.
		if(!(status.getPhase()==LoadBalancingSlaveStatus.LB_PHASES.ABORT)) {
			dispatcher.stopAllMessages();
			status.setPhase(LoadBalancingSlaveStatus.LB_PHASES.ABORT);
			switch (status.getInvolvementType()) {
	
			case PEER_WITH_SENDER_OR_RECEIVER:
	
				ArrayList<String> installedPipes = Collections.list(status
						.getReplacedPipes().keys());
				for (String pipe : installedPipes) {
					LoadBalancingHelper.removeDuplicateJxtaOperator(pipe);
				}
				// NO break, since Peer could in theory also be the volunteering
				// peer
	
			case VOLUNTEERING_PEER:
				Integer[] queriesToRemove = status.getInstalledQueries();
				if (queriesToRemove != null) {
					for (int query : queriesToRemove) {
						LoadBalancingHelper.deleteQuery(query);
					}
				}
				break;
	
			}
			dispatcher.sendAbortResponse(senderPeer);
		}
	}
	

	/**
	 * Re-Allocates and cleans up status after Abort has been completed.
	 * @param lbProcessId
	 */
	private static void finishAbort(int lbProcessId) {
		LoadBalancingMasterStatus status = LoadBalancingStatusCache.getInstance().getStatusForLocalProcess(lbProcessId);
		if(status!=null) {
			@SuppressWarnings("unused")
			int queryId = status.getLogicalQuery();
			//TODO ReAllocate.
			LoadBalancingStatusCache.getInstance().deleteLocalStatus(lbProcessId);
		}
	}
	

	public static void stopSendingAbort(LoadBalancingAbortMessage abortMessage,
			PeerID senderPeer) {
		LoadBalancingMessageDispatcher dispatcher = LoadBalancingStatusCache.getInstance().getStatusForLocalProcess(abortMessage.getLoadBalancingProcessId()).getMessageDispatcher();
		dispatcher.stopRunningJob(senderPeer.toString());
		if(dispatcher.getNumberOfRunningJobs()==0) {
			finishAbort(abortMessage.getLoadBalancingProcessId());
		}
	}
	

	
}
