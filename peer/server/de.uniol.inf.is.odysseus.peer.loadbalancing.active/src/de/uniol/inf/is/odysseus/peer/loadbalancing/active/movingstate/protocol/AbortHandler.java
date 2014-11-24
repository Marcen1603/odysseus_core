package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.protocol;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateCommunicatorImpl;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages.MovingStateAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateSlaveStatus;

/**
 * Message Handler for Abort Messages. If AbortInstruction -> SlavePeer handles
 * it, if AbortResponse -> Master Peer stops sending Messages to according slave
 * Peer.
 */
public class AbortHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbortHandler.class);

	/**
	 * Message Handler for Abort Messages. If AbortInstruction -> SlavePeer
	 * handles it, if AbortResponse -> Master Peer stops sending Messages to
	 * according slave Peer.
	 * 
	 * @param abortMessage
	 * @param senderPeer
	 */
	public static void handleAbort(MovingStateAbortMessage abortMessage,
			PeerID senderPeer) {

		switch (abortMessage.getMsgType()) {
		case MovingStateAbortMessage.ABORT_INSTRUCTION:
			LOG.debug("Received ABORT_INSTRUCTION");
			undoLoadBalancing(abortMessage, senderPeer);
			break;
		case MovingStateAbortMessage.ABORT_RESPONSE:
			LOG.debug("Received ABORT_RESPONSE");
			stopSendingAbort(abortMessage, senderPeer);
			break;
		}

	}

	/**
	 * When Abort Message is received by slave Peer, this Method decides what to
	 * do.
	 * 
	 * @param abortMessage
	 * @param senderPeer
	 */
	private static void undoLoadBalancing(MovingStateAbortMessage abortMessage,
			PeerID senderPeer) {

		int lbProcessId = abortMessage.getLoadBalancingProcessId();
		MovingStateSlaveStatus status = (MovingStateSlaveStatus) LoadBalancingStatusCache
				.getInstance().getSlaveStatus(senderPeer, lbProcessId);

		// Only send Ack. if status no longer active.
		if (status == null) {
			LOG.debug("Status already null. Sending ABORT_RESPONSE on improvised Message Dispatcher.");
			IPeerCommunicator peerCommunicator = MovingStateCommunicatorImpl
					.getPeerCommunicator();
			MovingStateMessageDispatcher improvisedDispatcher = new MovingStateMessageDispatcher(
					peerCommunicator, abortMessage.getLoadBalancingProcessId());
			improvisedDispatcher.sendAbortResponse(senderPeer);
			return;
		}

		MovingStateMessageDispatcher dispatcher = status.getMessageDispatcher();

		// Ignore Message if already aborting.
		if (!(status.getPhase() == MovingStateSlaveStatus.LB_PHASES.ABORT)) {
			LOG.error("Received Abort.");
			dispatcher.stopAllMessages();
			status.setPhase(MovingStateSlaveStatus.LB_PHASES.ABORT);
			dispatcher.sendAbortResponse(senderPeer);
			switch (status.getInvolvementType()) {

			case PEER_WITH_SENDER_OR_RECEIVER:
				boolean isSender = false;
				if(status.getBufferedPipes()!=null && status.getBufferedPipes().size()>0) {
					isSender = true;
				}
				
				if(status.getPipeOldPeerMapping()!=null) {
					for (String pipeID : status.getPipeOldPeerMapping().keySet()) {
						try {
							MovingStateHelper.setNewPeerId(pipeID, status.getPipeOldPeerMapping().get(pipeID), isSender);
						} catch (LoadBalancingException e) {
							//do nothing as there is nothing we can do
						}
					}
				}
				if(isSender) {
					for (String pipeID : status.getBufferedPipes()) {
						try {
							MovingStateHelper.stopBuffering(pipeID);
						} catch (LoadBalancingException e) {
							//do nothing as there is nothing we can do
						}
					}
				}
				//No break.
				//$FALL-THROUGH$
			case VOLUNTEERING_PEER:
				Collection<Integer> queriesToRemove = status
						.getInstalledQueries();
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
	 * 
	 * @param lbProcessId
	 */
	private static void finishAbort(int lbProcessId) {

		MovingStateMasterStatus status = (MovingStateMasterStatus) LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(lbProcessId);
		if (status != null) {
			LOG.info("LoadBalancing failed.");
			LoadBalancingStatusCache.getInstance().deleteLocalStatus(
					status.getProcessId());
			MovingStateCommunicatorImpl.getInstance().notifyFinished();
		}
	}

	/**
	 * Sends Stop Sending abort to a Peer
	 * 
	 * @param abortMessage
	 *            received abort Message
	 * @param senderPeer
	 *            Peer that sent Abort message.
	 */
	public static void stopSendingAbort(MovingStateAbortMessage abortMessage,
			PeerID senderPeer) {
		MovingStateMasterStatus status = (MovingStateMasterStatus) LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(
						abortMessage.getLoadBalancingProcessId());

		LOG.debug("Stop Sending Abort called.");

		if (status != null) {
			LOG.debug("Stop Sending Abort to " + senderPeer);
			MovingStateMessageDispatcher dispatcher = status
					.getMessageDispatcher();
			dispatcher.stopRunningJob(senderPeer.toString());

			if (dispatcher.getNumberOfRunningJobs() == 0) {
				LOG.debug("No more Peers to notify. Finishing Abort.");
				finishAbort(abortMessage.getLoadBalancingProcessId());
			}
		}
	}

}
