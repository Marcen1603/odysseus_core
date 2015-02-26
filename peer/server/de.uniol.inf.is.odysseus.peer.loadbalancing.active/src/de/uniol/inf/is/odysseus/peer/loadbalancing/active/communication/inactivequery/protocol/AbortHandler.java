package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.protocol;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryCommunicatorImpl;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages.InactiveQueryAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.InactiveQueryMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.InactiveQuerySlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.JxtaOperatorInformation;

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
	public static void handleAbort(InactiveQueryAbortMessage abortMessage,
			PeerID senderPeer) {

		switch (abortMessage.getMsgType()) {
		case InactiveQueryAbortMessage.ABORT_INSTRUCTION:
			LOG.debug("Received ABORT_INSTRUCTION");
			undoLoadBalancing(abortMessage, senderPeer);
			break;
		case InactiveQueryAbortMessage.ABORT_RESPONSE:
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
	private static void undoLoadBalancing(InactiveQueryAbortMessage abortMessage,
			PeerID senderPeer) {

		int lbProcessId = abortMessage.getLoadBalancingProcessId();
		InactiveQuerySlaveStatus status = (InactiveQuerySlaveStatus) LoadBalancingStatusCache
				.getInstance().getSlaveStatus(senderPeer, lbProcessId);

		// Only send Ack. if status no longer active.
		if (status == null) {
			LOG.debug("Status already null. Sending ABORT_RESPONSE on improvised Message Dispatcher.");
			IPeerCommunicator peerCommunicator = InactiveQueryCommunicatorImpl
					.getPeerCommunicator();
			InactiveQueryMessageDispatcher improvisedDispatcher = new InactiveQueryMessageDispatcher(
					peerCommunicator, abortMessage.getLoadBalancingProcessId());
			improvisedDispatcher.sendAbortResponse(senderPeer);
			return;
		}

		InactiveQueryMessageDispatcher dispatcher = status.getMessageDispatcher();

		// Ignore Message if already aborting.
		if (!(status.getPhase() == InactiveQuerySlaveStatus.LB_PHASES.ABORT)) {
			LOG.error("Received Abort.");
			dispatcher.stopAllMessages();
			status.setPhase(InactiveQuerySlaveStatus.LB_PHASES.ABORT);
			dispatcher.sendAbortResponse(senderPeer);
			switch (status.getInvolvementType()) {

			case PEER_WITH_SENDER_OR_RECEIVER:
					for (String pipeID : status.getPipeInformationMapping().keySet()) {
						try {
							
							InactiveQueryHelper.setNewPeerId(pipeID, status.getPipeInformationMapping().get(pipeID).getOldPeer(), status.getPipeInformationMapping().get(pipeID).isSender());
						} catch (LoadBalancingException e) {
							//do nothing as there is nothing we can do
						}
					}
					//Clear Information
					status.setPipeInformationMapping(new ConcurrentHashMap<String,JxtaOperatorInformation>());
				//No break.
				//$FALL-THROUGH$
			case VOLUNTEERING_PEER:
				Collection<Integer> queriesToRemove = status
				.getInstalledQueries();
				if (queriesToRemove != null) {
					IQueryPartController controller = OsgiServiceManager.getQueryPartController();
					//If new Peer registered itself as new Master->Undo.
					if(status.isRegisteredAsMaster()) {
						controller.unregisterAsMaster(status.sharedQueryID());
					}
					
					//If new Peer registered itself as new Slave->Undo.
					if(status.isRegisteredAsSlave()) {
						OsgiServiceManager.getQueryManager().sendUnregisterAsSlave(status.getSharedQueryMaster(), status.sharedQueryID());
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

		InactiveQueryMasterStatus status = (InactiveQueryMasterStatus) LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(lbProcessId);
		if (status != null) {
			LOG.info("LoadBalancing failed.");
			LoadBalancingStatusCache.getInstance().deleteLocalStatus(
					status.getProcessId());
			InactiveQueryCommunicatorImpl.getInstance().notifyFinished(false);
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
	public static void stopSendingAbort(InactiveQueryAbortMessage abortMessage,
			PeerID senderPeer) {
		InactiveQueryMasterStatus status = (InactiveQueryMasterStatus) LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(
						abortMessage.getLoadBalancingProcessId());

		LOG.debug("Stop Sending Abort called.");

		if (status != null) {
			LOG.debug("Stop Sending Abort to " + senderPeer);
			InactiveQueryMessageDispatcher dispatcher = status
					.getMessageDispatcher();
			dispatcher.stopRunningJob(senderPeer.toString());

			if (dispatcher.getNumberOfRunningJobs() == 0) {
				LOG.debug("No more Peers to notify. Finishing Abort.");
				finishAbort(abortMessage.getLoadBalancingProcessId());
			}
		}
	}

}
