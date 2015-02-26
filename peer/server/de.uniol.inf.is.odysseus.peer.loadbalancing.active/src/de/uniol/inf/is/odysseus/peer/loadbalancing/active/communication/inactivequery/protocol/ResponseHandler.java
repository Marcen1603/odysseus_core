package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryCommunicatorImpl;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages.InactiveQueryResponseMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.InactiveQueryMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.InactiveQueryMasterStatus.LB_PHASES;

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
	 * 
	 * @param response
	 * @param senderPeer
	 */
	public static void handlePeerResonse(InactiveQueryResponseMessage response,
			PeerID senderPeer) {

		LOG.debug("Got response for lbProcess Id "
				+ response.getLoadBalancingProcessId());

		int loadBalancingProcessId = response.getLoadBalancingProcessId();
		InactiveQueryMasterStatus status = (InactiveQueryMasterStatus) LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(loadBalancingProcessId);

		InactiveQueryCommunicatorImpl communicationListener = InactiveQueryCommunicatorImpl
				.getInstance();

		// LoadBalancing Process is no longer active -> ignore!
		if (status == null) {
			return;
		}

		InactiveQueryMessageDispatcher dispatcher = status.getMessageDispatcher();

		switch (response.getMsgType()) {
		case InactiveQueryResponseMessage.ACK_LOADBALANCING:
			LOG.debug("Got ACK_LOADBALANCING");
			if (status.getPhase().equals(
					InactiveQueryMasterStatus.LB_PHASES.INITIATING)) {

				status.setVolunteeringPeer(senderPeer);
				status.setPhase(InactiveQueryMasterStatus.LB_PHASES.COPYING_QUERY);
				dispatcher.stopRunningJob();

				ILogicalQueryPart modifiedQueryPart = LoadBalancingHelper
						.getCopyOfQueryPart(status.getOriginalPart());
				InactiveQueryHelper.relinkQueryPart(modifiedQueryPart, status);

				String pqlFromQueryPart = LogicalQueryHelper
						.generatePQLStatementFromQueryPart(modifiedQueryPart);
				LOG.debug("Generated PQL Statement:" + pqlFromQueryPart);

				// Update Query part Controller.
				IQueryPartController queryPartController = OsgiServiceManager.getQueryPartController();
				
				ID sharedQueryID = queryPartController.getSharedQueryID(status.getLogicalQuery());
				boolean isMaster = queryPartController.isMasterForQuery(status.getLogicalQuery());
				Collection<Integer> localQueries = queryPartController.getLocalIds(sharedQueryID);
				
				Collection<Integer> toRemove = new ArrayList<Integer>();
				toRemove.add(status.getLogicalQuery());
				
				queryPartController.unregisterLocalQueriesFromSharedQuery(sharedQueryID, toRemove);
				Collection<Integer> remainingQueries = queryPartController.getLocalIds(sharedQueryID);
				
				LOG.debug("IsMaster: {}",isMaster);
				LOG.debug("Number of remaining Queries: {}",remainingQueries.size());
				
				
				//3 different Cases:
				// 1. Peer is Master and whole query is moved. -> New Master 
				// 2. Peer is Master and part of query remains -> Stay Master
				// 3. Peer is Slave
				//
				
				if(isMaster && remainingQueries.size()<=0) {
						
						// 1. Peer is Master and whole query is moved. -> New Master 
						
						LOG.debug("Registering {} as new Master Peer.",status.getVolunteeringPeer());
						//Deregister As Master, update other Peers List and send to new Master.
						
						Collection<PeerID> otherPeers = queryPartController.getOtherPeers(sharedQueryID);
						
						//If new Master is in otherPeers list-> Remove new Master.
						if(otherPeers.contains(status.getVolunteeringPeer())) {
							otherPeers.remove(status.getVolunteeringPeer());
						}
						
						List<String> otherPeerIDStrings = new ArrayList<String>();
						
						for (PeerID peer : otherPeers) {
							otherPeerIDStrings.add(peer.toString());
						}
						status.storeSharedQueryInformation(isMaster, sharedQueryID, localQueries, otherPeers);
						queryPartController.unregisterAsMaster(sharedQueryID);
						dispatcher.sendAddQueryForMasterQuery(status.getVolunteeringPeer(), pqlFromQueryPart, communicationListener, otherPeerIDStrings, sharedQueryID.toString());
						
						
				} else {
						PeerID masterPeerID;
						// 2. Peer is Master and part of query remains -> Stay Master
						if(isMaster) {
							masterPeerID = OsgiServiceManager.getP2pNetworkManager().getLocalPeerID();
							status.storeSharedQueryInformation(isMaster, sharedQueryID, localQueries, queryPartController.getOtherPeers(sharedQueryID));
						}
						else {
						// 3. Peer is Slave
							masterPeerID = queryPartController.getMasterForQuery(sharedQueryID);
							if(remainingQueries.size()<=0) {
								LOG.debug("No queries remaining for shared Query. Deregistering with master Peer.");
								status.storeSharedQueryInformation(isMaster, sharedQueryID, localQueries, null);
								status.setSharedQueryMasterPeer(masterPeerID);
								OsgiServiceManager.getQueryManager().sendUnregisterAsSlave(masterPeerID, sharedQueryID);
							}
						}
						
						dispatcher.sendAddQuery(status.getVolunteeringPeer(),
								pqlFromQueryPart, communicationListener,sharedQueryID.toString(),masterPeerID.toString());
						
				}
				
			}
			break;
			

		case InactiveQueryResponseMessage.SUCCESS_INSTALL_QUERY:
			LOG.debug("Got SUCCESS_INSTALL_QUERY");
			// When in Phase copying, the success Message says that Installing
			// the Query Part on the other Peer was successful.
			if (status.getPhase().equals(
					InactiveQueryMasterStatus.LB_PHASES.COPYING_QUERY) && !status.isLocked()) {
				status.lock();
				dispatcher.sendMsgReceived(senderPeer);
				dispatcher.stopRunningJob();
				status.setPhase(InactiveQueryMasterStatus.LB_PHASES.RELINKING_SENDERS);
				LOG.debug("Relinking Senders.");
				InactiveQueryHelper.notifyUpstreamPeers(status);
				status.unlock();
			}
			break;

		case InactiveQueryResponseMessage.SUCCESS_DUPLICATE_SENDER:

			if (status.getPhase().equals(
					InactiveQueryMasterStatus.LB_PHASES.RELINKING_SENDERS)  && !status.isLocked()) {
				LOG.debug("Got SUCCESS_DUPLICATE for SENDER");
				dispatcher.sendPipeSuccessReceivedMsg(senderPeer,
						response.getPipeID());
				dispatcher.stopRunningJob(response.getPipeID());
				LOG.debug("Stopped JOB " + response.getPipeID());
				LOG.debug("Jobs left:" + dispatcher.getNumberOfRunningJobs());

				if (dispatcher.getNumberOfRunningJobs() == 0) {
					status.lock();
					// All success messages received. Yay!
					status.setPhase(LB_PHASES.RELINKING_RECEIVERS);
					InactiveQueryHelper.notifyDownstreamPeers(status);
					LOG.debug("Status Relinking Receivers.");
					status.unlock();
				}
			}
			break;
		
		case InactiveQueryResponseMessage.SUCCESS_DUPLICATE_RECEIVER:
			if (status.getPhase().equals(LB_PHASES.RELINKING_RECEIVERS) && !status.isLocked()) {
				LOG.debug("Got SUCCESS_DUPLICATE for RECEIVER");
				dispatcher.sendPipeSuccessReceivedMsg(senderPeer,
						response.getPipeID());
				dispatcher.stopRunningJob(response.getPipeID());
				LOG.debug("Stopped JOB " + response.getPipeID());
				LOG.debug("Jobs left:" + dispatcher.getNumberOfRunningJobs());
				if (dispatcher.getNumberOfRunningJobs() == 0) {
					status.lock();
					// All success messages received. Yay!
					status.setPhase(LB_PHASES.FINISHED);
					status.unlock();
					LoadBalancingHelper.cutReceiversFromQuery(status
							.getLogicalQuery());
					LoadBalancingHelper.deleteQuery(status.getLogicalQuery());
					loadBalancingSuccessfullyFinished(status);
				}
			}
			break;

		case InactiveQueryResponseMessage.FAILURE_INSTALL_QUERY:
			LOG.debug("Got FAILURE_INSTALL_QUERY");
			if (status.getPhase().equals(LB_PHASES.COPYING_QUERY)) {
				dispatcher.sendMsgReceived(senderPeer);
				LOG.error("Installing Query on remote Peer failed. Aborting.");
				handleError(status, communicationListener);
			}
			break;

		case InactiveQueryResponseMessage.FAILURE_DUPLICATE_RECEIVER:
			LOG.debug("Got FAILURE_DUPLICATE_RECEIVER");
			if (status.getPhase().equals(LB_PHASES.RELINKING_RECEIVERS)
					|| status.getPhase().equals(LB_PHASES.RELINKING_SENDERS)) {
				dispatcher.sendMsgReceived(senderPeer);
				LOG.error("Duplicating connections failed. Aborting.");
				handleError(status, communicationListener);
			}
			break;
		}

	}

	/**
	 * Decides what to do when error occurs.
	 * 
	 * @param status
	 *            Status
	 * @param communicationListener
	 *            Communication Listener
	 */
	public static void handleError(InactiveQueryMasterStatus status,
			InactiveQueryCommunicatorImpl communicationListener) {
		InactiveQueryMessageDispatcher dispatcher = status.getMessageDispatcher();
		
		// Handle error depending on current LoadBalancing phase.
		switch (status.getPhase()) {
		case INITIATING:
		case COPYING_QUERY:
			// Send abort only to volunteering Peer
			dispatcher.stopAllMessages();
			dispatcher.sendAbortInstruction(status.getVolunteeringPeer(),
					communicationListener);
			resetQueryPartController(status);
			break;
		case RELINKING_RECEIVERS:
		case RELINKING_SENDERS:
			// Send Abort to all Peers involved
			dispatcher.stopAllMessages();
			InactiveQueryHelper.notifyInvolvedPeers(status);
			resetQueryPartController(status);
			break;
		
		default:
			break;

		}

	}

	/**
	 * Called after LoadBalancing was finished successfully.
	 * 
	 * @param status
	 *            LoadBalancing Status
	 */
	public static void loadBalancingSuccessfullyFinished(
			InactiveQueryMasterStatus status) {
		LOG.info("LoadBalancing successfully finished.");
		LoadBalancingStatusCache.getInstance().deleteLocalStatus(
				status.getProcessId());
		InactiveQueryCommunicatorImpl.getInstance().notifyFinished(true);

	}

	

	private static void resetQueryPartController(InactiveQueryMasterStatus status) {
		IQueryPartController controller = OsgiServiceManager.getQueryPartController();
		ID sharedQueryID = status.getSharedQueryID();
		if(sharedQueryID!=null) {
			if(status.isMaster()) {
				Collection<Integer> previousLocalQueries = controller.getLocalIds(sharedQueryID);
				//If Query was master->Re-Set Query in QueryPart Controller with old peer List and old Query parts...
				ILogicalQuery qry = OsgiServiceManager.getExecutor().getLogicalQueryById(status.getLogicalQuery(), OsgiServiceManager.getActiveSession());
				controller.registerAsMaster(qry, status.getLogicalQuery(), sharedQueryID, status.getOtherPeersForSharedQuery());
				OsgiServiceManager.getQueryManager().sendChangeMasterToAllSlaves(sharedQueryID);
				
				for (Integer local:previousLocalQueries) {
					if(controller.getLocalIds(sharedQueryID).contains(local))
						continue;
					
					controller.addLocalQueryToShared(sharedQueryID, local);
				}
			}
			else {
				//If Query was no master... (Re-)Set as Slave and tell master.
				Collection<Integer> localQueries = status.getLocalQueriesForSharedQuery();
				controller.registerAsSlave(localQueries, sharedQueryID, status.getSharedQueryMasterPeer());
			}
		}
	}

}
