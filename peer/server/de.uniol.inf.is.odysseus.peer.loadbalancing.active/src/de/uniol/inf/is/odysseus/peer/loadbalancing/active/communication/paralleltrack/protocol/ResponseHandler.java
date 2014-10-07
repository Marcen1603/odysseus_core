package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.protocol;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.LoadBalancingCommunicationListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.LoadBalancingMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages.LoadBalancingResponseMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.LoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.LoadBalancingMasterStatus.LB_PHASES;

/**
 * Handles Responses sent by Peers when they finish or fail instructions.
 */
public class ResponseHandler {
	

	/**
	 * The logger instance for this class.
	 */
	
	private static final Logger LOG = LoggerFactory
			.getLogger(ResponseHandler.class);
	
	
	
	/**
	 * Handles Responses sent by Peers when they finish or fail instructions.
	 * @param response
	 * @param senderPeer
	 */
	public static void handlePeerResonse(LoadBalancingResponseMessage response,
			PeerID senderPeer) {
		
		LOG.debug("Got response for lbProcess Id " + response.getLoadBalancingProcessId());
		
		int loadBalancingProcessId = response.getLoadBalancingProcessId();
		LoadBalancingMasterStatus status = LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(loadBalancingProcessId);
		
		LoadBalancingCommunicationListener communicationListener = LoadBalancingCommunicationListener.getInstance();

		// LoadBalancing Process is no longer active -> ignore!
		if (status == null) {
			return;
		}

		LoadBalancingMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		switch (response.getMsgType()) {
		case LoadBalancingResponseMessage.ACK_LOADBALANCING:
			LOG.debug("Got ACK_LOADBALANCING");
			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.INITIATING)) {
				
				status.setVolunteeringPeer(senderPeer);
				status.setPhase(LoadBalancingMasterStatus.LB_PHASES.COPYING);
				dispatcher.stopRunningJob();

				ILogicalQueryPart modifiedQueryPart = LoadBalancingHelper
						.getCopyOfQueryPart(status.getOriginalPart());
				LoadBalancingHelper
						.relinkQueryPart(modifiedQueryPart,status);

				String pqlFromQueryPart = LogicalQueryHelper
						.generatePQLStatementFromQueryPart(modifiedQueryPart);
				LOG.debug("Generated PQL Statement:" + pqlFromQueryPart);
				dispatcher.sendAddQuery(status.getVolunteeringPeer(),
						pqlFromQueryPart,communicationListener);
			}
			break;

		case LoadBalancingResponseMessage.SUCCESS_INSTALL_QUERY:
			LOG.debug("Got SUCCESS_INSTALL_QUERY");
			// When in Phase copying, the success Message says that Installing
			// the Query Part on the other Peer was successful.
			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.COPYING)) {
				dispatcher.sendMsgReceived(senderPeer);
				dispatcher.stopRunningJob();
				status.setPhase(LoadBalancingMasterStatus.LB_PHASES.RELINKING_SENDERS);
				LOG.debug("Relinking Senders.");
				LoadBalancingHelper.notifyOutgoingPeers(status);
			}
			break;

		case LoadBalancingResponseMessage.SUCCESS_DUPLICATE:
			
			
			
			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.RELINKING_SENDERS)) {
				LOG.debug("Got SUCCESS_DUPLICATE for RECEIVER");
				dispatcher.sendPipeSuccessReceivedMsg(senderPeer, response.getPipeID());
				dispatcher.stopRunningJob(response.getPipeID());
				LOG.debug("Stopped JOB " + response.getPipeID());
				LOG.debug("Jobs left:" + dispatcher.getNumberOfRunningJobs());
				
				if (dispatcher.getNumberOfRunningJobs() == 0) {
					// All success messages received. Yay!
					status.setPhase(LB_PHASES.RELINKING_RECEIVERS);
					LoadBalancingHelper.notifyIncomingPeers(status);
					LOG.debug("Status: Relinking Receivers.");
				}
			}
			
			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.RELINKING_RECEIVERS)) {
				LOG.debug("Got SUCCESS_DUPLICATE for SENDER");
				dispatcher.sendPipeSuccessReceivedMsg(senderPeer, response.getPipeID());
				dispatcher.stopRunningJob(response.getPipeID());
				LOG.debug("Stopped JOB " + response.getPipeID());
				LOG.debug("Jobs left:" + dispatcher.getNumberOfRunningJobs());
				
				if (dispatcher.getNumberOfRunningJobs() == 0) {
					// All success messages received. Yay!
					status.setPhase(LB_PHASES.SYNCHRONIZING);
					LOG.debug("WAITING FOR SYNC");
				}
			}
			
			break;

		case LoadBalancingResponseMessage.SYNC_FINISHED:
			LOG.debug("Got SYNC_FINISHED");
			if (status.getPhase().equals(LB_PHASES.SYNCHRONIZING)) {
				
				status.removePipeToSync(response.getPipeID());
				LOG.debug("Pipes left:" + status.getNumberOfPipesToSync());
				dispatcher.sendPipeSuccessReceivedMsg(senderPeer, response.getPipeID());
				if (status.getNumberOfPipesToSync() == 0) {
					LOG.debug("All outgoing pipes synced.");
					status.setPhase(LB_PHASES.DELETING);
					ILogicalQueryPart queryPart = status.getOriginalPart();
					if (!queryPart.getOperators().asList().isEmpty()) {

						// Tell Receivers and Senders to delete duplicate
						// Connections.
						for (ILogicalOperator operator : queryPart
								.getOperators()) {
							if (operator instanceof JxtaReceiverAO) {
								JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
								dispatcher.sendDeleteOperator(false,
										receiver.getPeerID(),
										receiver.getPipeID(),communicationListener);
							}
							if (operator instanceof JxtaSenderAO) {
								JxtaSenderAO sender = (JxtaSenderAO) operator;
								dispatcher.sendDeleteOperator(false,
										sender.getPeerID(), sender.getPipeID(),communicationListener);
							}
						}
					}
					int queryId = status.getLogicalQuery();
					LoadBalancingHelper.cutQuery(queryId);
					LoadBalancingHelper.deleteQuery(queryId);
				}
			}
			break;

		case LoadBalancingResponseMessage.FAILURE_INSTALL_QUERY:
			LOG.debug("Got FAILURE_INSTALL_QUERY");
			if(status.getPhase().equals(LB_PHASES.COPYING)) {
				dispatcher.sendMsgReceived(senderPeer);
				LOG.error("Installing Query on remote Peer failed. Aborting.");
				handleError(status,communicationListener);
			}
			break;
			

		case LoadBalancingResponseMessage.FAILURE_DUPLICATE_RECEIVER:
			LOG.debug("Got FAILURE_DUPLICATE_RECEIVER");
			if(status.getPhase().equals(LB_PHASES.RELINKING_RECEIVERS) || status.getPhase().equals(LB_PHASES.RELINKING_SENDERS) ) {
				dispatcher.sendMsgReceived(senderPeer);
				LOG.error("Duplicating connections failed. Aborting.");
				handleError(status,communicationListener);
			}
			break;
		
		case LoadBalancingResponseMessage.DELETE_FINISHED:
			if(status.getPhase().equals(LB_PHASES.DELETING)) {
				LOG.debug("Deleting finished for pipe " + response.getPipeID());
				dispatcher.stopRunningJob(response.getPipeID());
				if(dispatcher.getNumberOfRunningJobs()==0) {
					loadBalancingSuccessfullyFinished(status);
				}
			}
			break;
		}

	}
	
	/**
	 * Decides what to do when error occurs.
	 * @param status
	 */
	public static void handleError(LoadBalancingMasterStatus status,LoadBalancingCommunicationListener communicationListener) {
		LoadBalancingMessageDispatcher dispatcher = status.getMessageDispatcher();
		
		// Handle error depending on current LoadBalancing phase.
		switch (status.getPhase()) {
		case INITIATING:
		case COPYING:
			// Send abort only to volunteering Peer
			dispatcher.stopAllMessages();
			dispatcher.sendAbortInstruction(status.getVolunteeringPeer(),communicationListener);
			break;
		case RELINKING_RECEIVERS:
		case RELINKING_SENDERS:
			// Send Abort to all Peers involved
			dispatcher.stopAllMessages();
			LoadBalancingHelper.notifyInvolvedPeers(status);
			break;
		case SYNCHRONIZING:
		case DELETING:
			// New query is already running. 
			//TODO Go on as if nothing happened?
			break;
		default:
			break;

		}

	}
	
	/**
	 * Called after LoadBalancing was finished successfully.
	 * @param status LoadBalancing Status
	 */
	public static void loadBalancingSuccessfullyFinished(LoadBalancingMasterStatus status) {
		LOG.info("LoadBalancing successfully finished.");
		LoadBalancingStatusCache.getInstance().deleteLocalStatus(status.getProcessId());
		LoadBalancingCommunicationListener.getInstance().notifyFinished();
		
	}
	

}
