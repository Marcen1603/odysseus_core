package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingCommunicationListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingStatusCache;

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
			IPeerCommunicator peerCommunicator = LoadBalancingCommunicationListener.getPeerCommunicator();
			ISession session = LoadBalancingCommunicationListener.getActiveSession();
			LoadBalancingMessageDispatcher improvisedDispatcher = new LoadBalancingMessageDispatcher(peerCommunicator,session,abortMessage.getLoadBalancingProcessId());
			improvisedDispatcher.sendAbortResponse(senderPeer);
			return;
		}
		
		LoadBalancingMessageDispatcher dispatcher = status.getMessageDispatcher();
		
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
			@SuppressWarnings("unused")
			int queryId = status.getLogicalQuery();
			//TODO ReAllocate.
			LoadBalancingStatusCache.getInstance().deleteLocalStatus(lbProcessId);
		}
	}
	

	public static void stopSendingAbort(LoadBalancingAbortMessage abortMessage,
			PeerID senderPeer) {
		LoadBalancingMasterStatus status = LoadBalancingStatusCache.getInstance().getStatusForLocalProcess(abortMessage.getLoadBalancingProcessId());
		
		LOG.debug("Stop Sending Abort called.");
		
		
		if(status!=null) {
			LOG.debug("Stop Sending Abort to " + senderPeer);
			LoadBalancingMessageDispatcher dispatcher = status.getMessageDispatcher();
			dispatcher.stopRunningJob(senderPeer.toString());
			
			if(dispatcher.getNumberOfRunningJobs()==0) {
				LOG.debug("No more Peers to notify. Finishing Abort.");
				finishAbort(abortMessage.getLoadBalancingProcessId());
			}
		}
	}
	

	
}
