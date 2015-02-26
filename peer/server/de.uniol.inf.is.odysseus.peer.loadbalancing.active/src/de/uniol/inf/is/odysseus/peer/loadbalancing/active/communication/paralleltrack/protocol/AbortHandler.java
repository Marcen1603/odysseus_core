package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.ParallelTrackCommunicatorImpl;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.ParallelTrackMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.ParallelTrackAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.ParallelTrackMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.ParallelTrackSlaveStatus;

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
	public static void handleAbort(ParallelTrackAbortMessage abortMessage,
			PeerID senderPeer) {

		switch (abortMessage.getMsgType()) {
		case ParallelTrackAbortMessage.ABORT_INSTRUCTION:
			LOG.debug("Received ABORT_INSTRUCTION");
			undoLoadBalancing(abortMessage, senderPeer);
			break;
		case ParallelTrackAbortMessage.ABORT_RESPONSE:
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
	private static void undoLoadBalancing(
			ParallelTrackAbortMessage abortMessage, PeerID senderPeer) {

		int lbProcessId = abortMessage.getLoadBalancingProcessId();
		ParallelTrackSlaveStatus status = (ParallelTrackSlaveStatus) LoadBalancingStatusCache
				.getInstance().getSlaveStatus(senderPeer, lbProcessId);

		// Only send Ack. if status no longer active.
		if (status == null) {
			LOG.debug("Status already null. Sending ABORT_RESPONSE on improvised Message Dispatcher.");
			IPeerCommunicator peerCommunicator = ParallelTrackCommunicatorImpl
					.getPeerCommunicator();
			ParallelTrackMessageDispatcher improvisedDispatcher = new ParallelTrackMessageDispatcher(
					peerCommunicator, abortMessage.getLoadBalancingProcessId());
			improvisedDispatcher.sendAbortResponse(senderPeer);
			return;
		}

		ParallelTrackMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		// Ignore Message if already aborting.
		if (!(status.getPhase() == ParallelTrackSlaveStatus.LB_PHASES.ABORT)) {
			LOG.error("Received Abort.");
			dispatcher.stopAllMessages();
			status.setPhase(ParallelTrackSlaveStatus.LB_PHASES.ABORT);
			dispatcher.sendAbortResponse(senderPeer);
			switch (status.getInvolvementType()) {

			case PEER_WITH_SENDER_OR_RECEIVER:

				if (status.getReplacedPipes() != null) {
					Collection<String> installedPipes = status.getReplacedPipes().values();
					for (String pipe : installedPipes) {
						LOG.error("Removing Operator with Pipe ID " + pipe);
						LoadBalancingHelper.removeDuplicateJxtaOperator(pipe);
					}
				}
				// NO break, since Peer could in theory also be the volunteering
				// peer
				//$FALL-THROUGH$
			case VOLUNTEERING_PEER:
				Collection<Integer> queriesToRemove = status
						.getInstalledQueries();
				if (queriesToRemove != null) {
					IQueryPartController controller = OsgiServiceManager
							.getQueryPartController();
					// If new Peer registered itself as new Master->Undo.
					if (status.isRegisteredAsMaster()) {
						controller.unregisterAsMaster(status.sharedQueryID());
					}

					// If new Peer registered itself as new Slave->Undo.
					if (status.isRegisteredAsSlave()) {
						OsgiServiceManager.getQueryManager()
								.sendUnregisterAsSlave(
										status.getSharedQueryMaster(),
										status.sharedQueryID());
					}

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

		ParallelTrackMasterStatus status = (ParallelTrackMasterStatus) LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(lbProcessId);
		if (status != null) {
			LOG.info("LoadBalancing failed.");
			ParallelTrackCommunicatorImpl.getInstance().notifyFinished(false);

			LoadBalancingStatusCache.getInstance().deleteLocalStatus(
					status.getProcessId());
		}
	}

	public static void stopSendingAbort(ParallelTrackAbortMessage abortMessage,
			PeerID senderPeer) {
		ParallelTrackMasterStatus status = (ParallelTrackMasterStatus) LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(
						abortMessage.getLoadBalancingProcessId());

		LOG.debug("Stop Sending Abort called.");

		if (status != null) {
			LOG.debug("Stop Sending Abort to " + senderPeer);
			ParallelTrackMessageDispatcher dispatcher = status
					.getMessageDispatcher();
			dispatcher.stopRunningJob(senderPeer.toString());

			if (dispatcher.getNumberOfRunningJobs() == 0) {
				LOG.debug("No more Peers to notify. Finishing Abort.");
				finishAbort(abortMessage.getLoadBalancingProcessId());
			}
		}
	}

}
