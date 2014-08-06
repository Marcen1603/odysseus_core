package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.protocol;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingCommunicationListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.LoadBalancingMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.LoadBalancingResponseMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingMasterStatus.LB_PHASES;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingStatusCache;

public class ResponseHandler {
	
	/**
	 * Handles Responses sent by Peers when they finish or fail instructions.
	 * @param response
	 * @param senderPeer
	 */
	public static void handlePeerResonse(LoadBalancingResponseMessage response,
			PeerID senderPeer) {

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
			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.INITIATING)) {
				status.setPhase(LoadBalancingMasterStatus.LB_PHASES.COPYING);
				dispatcher.stopRunningJob();

				ILogicalQueryPart modifiedQueryPart = LoadBalancingHelper
						.getCopyOfQueryPart(status.getOriginalPart());
				LoadBalancingHelper
						.relinkQueryPart(modifiedQueryPart,status);

				String pqlFromQueryPart = LogicalQueryHelper
						.generatePQLStatementFromQueryPart(modifiedQueryPart);
				dispatcher.sendAddQuery(status.getVolunteeringPeer(),
						pqlFromQueryPart,communicationListener);
			}
			break;

		case LoadBalancingResponseMessage.SUCCESS_INSTALL_QUERY:

			// When in Phase copying, the success Message says that Installing
			// the Query Part on the other Peer was successful.
			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.COPYING)) {
				dispatcher.stopRunningJob();
				status.setPhase(LoadBalancingMasterStatus.LB_PHASES.RELINKING);
				LoadBalancingHelper.notifyOutgoingAndIncomingPeers(status);
			}
			break;

		case LoadBalancingResponseMessage.SUCCESS_DUPLICATE:

			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.RELINKING)) {
				dispatcher.stopRunningJob(response.getPipeID());
				if (dispatcher.getNumberOfRunningJobs() == 0) {
					// All success messages received. Yay!
					status.setPhase(LB_PHASES.SYNCHRONIZING);
				}
			}
			break;

		case LoadBalancingResponseMessage.SYNC_FINISHED:
			if (status.getPhase().equals(LB_PHASES.SYNCHRONIZING)) {
				status.removePipeToSync(response.getPipeID());
				if (status.getNumberOfPipesToSync() == 0) {
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
					LoadBalancingHelper.deleteQuery(queryId);
				}
			}
			break;

		case LoadBalancingResponseMessage.FAILURE_INSTALL_QUERY:
			handleError(status,communicationListener);
			break;

		case LoadBalancingResponseMessage.FAILURE_DUPLICATE_RECEIVER:
			handleError(status,communicationListener);
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
		case RELINKING:
			// Send Abort to all Peers involved
			dispatcher.stopAllMessages();
			LoadBalancingHelper.notifyInvolvedPeers(status);
			break;
		case SYNCHRONIZING:
			// New query is already running. Do not abort.
			break;
		default:
			break;

		}

	}

}
