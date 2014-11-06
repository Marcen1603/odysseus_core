package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.protocol;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateCommunicatorImpl;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages.MovingStateResponseMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateMasterStatus.LB_PHASES;

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
	public static void handlePeerResonse(MovingStateResponseMessage response,
			PeerID senderPeer) {
		
		LOG.debug("Got response for lbProcess Id " + response.getLoadBalancingProcessId());
		
		int loadBalancingProcessId = response.getLoadBalancingProcessId();
		MovingStateMasterStatus status = (MovingStateMasterStatus)LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(loadBalancingProcessId);
		
		MovingStateCommunicatorImpl communicationListener = MovingStateCommunicatorImpl.getInstance();

		// LoadBalancing Process is no longer active -> ignore!
		if (status == null) {
			return;
		}

		MovingStateMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		switch (response.getMsgType()) {
		case MovingStateResponseMessage.ACK_LOADBALANCING:
			LOG.debug("Got ACK_LOADBALANCING");
			if (status.getPhase().equals(
					MovingStateMasterStatus.LB_PHASES.INITIATING)) {
				
				status.setVolunteeringPeer(senderPeer);
				status.setPhase(MovingStateMasterStatus.LB_PHASES.COPYING_QUERY);
				dispatcher.stopRunningJob();

				ILogicalQueryPart modifiedQueryPart = LoadBalancingHelper
						.getCopyOfQueryPart(status.getOriginalPart());
				MovingStateHelper
						.relinkQueryPart(modifiedQueryPart,status);

				String pqlFromQueryPart = LogicalQueryHelper
						.generatePQLStatementFromQueryPart(modifiedQueryPart);
				LOG.debug("Generated PQL Statement:" + pqlFromQueryPart);
				dispatcher.sendAddQuery(status.getVolunteeringPeer(),
						pqlFromQueryPart,communicationListener);
			}
			break;

		case MovingStateResponseMessage.SUCCESS_INSTALL_QUERY:
			LOG.debug("Got SUCCESS_INSTALL_QUERY");
			// When in Phase copying, the success Message says that Installing
			// the Query Part on the other Peer was successful.
			if (status.getPhase().equals(
					MovingStateMasterStatus.LB_PHASES.COPYING_QUERY)) {
				dispatcher.sendMsgReceived(senderPeer);
				dispatcher.stopRunningJob();
				status.setPhase(MovingStateMasterStatus.LB_PHASES.RELINKING_SENDERS);
				LOG.debug("Relinking Senders.");
				MovingStateHelper.notifyUpstreamPeers(status);
			}
			break;

		case MovingStateResponseMessage.SUCCESS_DUPLICATE:
			
			if (status.getPhase().equals(
					MovingStateMasterStatus.LB_PHASES.RELINKING_SENDERS)) {
				LOG.debug("Got SUCCESS_DUPLICATE for SENDER");
				dispatcher.sendPipeSuccessReceivedMsg(senderPeer, response.getPipeID());
				dispatcher.stopRunningJob(response.getPipeID());
				LOG.debug("Stopped JOB " + response.getPipeID());
				LOG.debug("Jobs left:" + dispatcher.getNumberOfRunningJobs());
				
				if (dispatcher.getNumberOfRunningJobs() == 0) {
					// All success messages received. Yay!
					status.setPhase(LB_PHASES.RELINKING_RECEIVERS);
					MovingStateHelper.notifyDownstreamPeers(status);
					LOG.debug("Status: Relinking Receivers.");
				}
			}
			
			if (status.getPhase().equals(
					LB_PHASES.RELINKING_RECEIVERS)) {
				LOG.debug("Got SUCCESS_DUPLICATE for RECEIVER");
				dispatcher.sendPipeSuccessReceivedMsg(senderPeer, response.getPipeID());
				dispatcher.stopRunningJob(response.getPipeID());
				LOG.debug("Stopped JOB " + response.getPipeID());
				LOG.debug("Jobs left:" + dispatcher.getNumberOfRunningJobs());
				if (dispatcher.getNumberOfRunningJobs() == 0) {
					// All success messages received. Yay!
					status.setPhase(LB_PHASES.COPYING_STATES);
					LOG.debug("INITIATING COPYING STATES");
					MovingStateHelper.initiateStateCopy(status);
					//TODO what if no stateful Ops?
				}
			}
			
			break;
			
		case MovingStateResponseMessage.ACK_INIT_STATE_COPY:
			LOG.debug("Got ACK_INIT_STATE_COPY");
			if(status.getPhase().equals(LB_PHASES.COPYING_STATES)) {
				dispatcher.stopRunningJob(response.getPipeID());
			}
			if(dispatcher.getNumberOfRunningJobs()==0) {
				LOG.debug("Sending states.");
				for(String pipe : status.getAllSenderPipes()) {
					IStatefulPO operator = status.getOperatorForSender(pipe);
					try {
						MovingStateHelper.sendState(pipe, operator);
					} catch (LoadBalancingException e) {
						LOG.error("Sending state failed.");
						//TODO Error Handling
						e.printStackTrace();
					}
				}
				
			}
			break;
		
		case MovingStateResponseMessage.FAILURE_INSTALL_QUERY:
			LOG.debug("Got FAILURE_INSTALL_QUERY");
			if(status.getPhase().equals(LB_PHASES.COPYING_QUERY)) {
				dispatcher.sendMsgReceived(senderPeer);
				LOG.error("Installing Query on remote Peer failed. Aborting.");
				handleError(status,communicationListener);
			}
			break;
			
		case MovingStateResponseMessage.STATE_COPY_FINISHED:
			LOG.debug("Got STATE_COPY_FINISHED");
			if(status.getPhase().equals((LB_PHASES.COPYING_STATES))) {
				String pipe = response.getPipeID();
				MovingStateManager.getInstance().getSender(pipe).setSuccessfullyTransmitted();
				//TODO What about missing messages?
				LOG.debug("Unfinished Transmissions:" +status.getNumberOfUnfinishedTransmissions());
				if(status.getNumberOfUnfinishedTransmissions()==0) {
					
					status.setPhase(LB_PHASES.STOP_BUFFERING);
					
				}
			}
		break;

		case MovingStateResponseMessage.FAILURE_DUPLICATE_RECEIVER:
			LOG.debug("Got FAILURE_DUPLICATE_RECEIVER");
			if(status.getPhase().equals(LB_PHASES.RELINKING_RECEIVERS) || status.getPhase().equals(LB_PHASES.RELINKING_SENDERS) ) {
				dispatcher.sendMsgReceived(senderPeer);
				LOG.error("Duplicating connections failed. Aborting.");
				handleError(status,communicationListener);
			}
			break;
		
		case MovingStateResponseMessage.DELETE_FINISHED:
			if(status.getPhase().equals(LB_PHASES.STOP_BUFFERING)) {
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
	public static void handleError(MovingStateMasterStatus status,MovingStateCommunicatorImpl communicationListener) {
		MovingStateMessageDispatcher dispatcher = status.getMessageDispatcher();
		
		// Handle error depending on current LoadBalancing phase.
		switch (status.getPhase()) {
		case INITIATING:
		case COPYING_QUERY:
			// Send abort only to volunteering Peer
			dispatcher.stopAllMessages();
			dispatcher.sendAbortInstruction(status.getVolunteeringPeer(),communicationListener);
			break;
		case RELINKING_RECEIVERS:
		case RELINKING_SENDERS:
			// Send Abort to all Peers involved
			dispatcher.stopAllMessages();
			MovingStateHelper.notifyInvolvedPeers(status);
			break;
		case COPYING_STATES:
		case STOP_BUFFERING:
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
	public static void loadBalancingSuccessfullyFinished(MovingStateMasterStatus status) {
		LOG.info("LoadBalancing successfully finished.");
		LoadBalancingStatusCache.getInstance().deleteLocalStatus(status.getProcessId());
		MovingStateCommunicatorImpl.getInstance().notifyFinished();
		
	}
	

}
