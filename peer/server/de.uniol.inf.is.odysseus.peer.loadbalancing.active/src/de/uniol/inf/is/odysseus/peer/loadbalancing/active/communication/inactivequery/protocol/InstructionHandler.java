package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.protocol;

import java.util.Collection;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryCommunicatorImpl;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages.InactiveQueryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status.InactiveQuerySlaveStatus;

/**
 * Handles Instruction messages, sent from initiation Master Peer to Slave
 * Peers.
 */
public class InstructionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(InstructionHandler.class);

	/**
	 * Handles Instruction messages, sent from initiation Master Peer to Slave
	 * Peers.
	 * 
	 * @param instruction
	 *            Instruction received by Peer.
	 * @param senderPeer
	 *            Peer that sent Message (in this case: Master Peer).
	 */
	public static void handleInstruction(InactiveQueryInstructionMessage instruction, PeerID senderPeer) {

		int lbProcessId = instruction.getLoadBalancingProcessId();
		InactiveQueryMessageDispatcher dispatcher = null;

		InactiveQuerySlaveStatus status = (InactiveQuerySlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);

		IPeerCommunicator peerCommunicator = InactiveQueryCommunicatorImpl.getPeerCommunicator();

		// Decide which message we received.
		switch (instruction.getMsgType()) {

		case InactiveQueryInstructionMessage.INITIATE_LOADBALANCING:
			// Only react to first INITIATE_LOADBALANCING Message, even if sent
			// more often.

			LOG.debug("Got INITIATE_LOADBALANCING");

			if (status == null) {
				status = new InactiveQuerySlaveStatus(InactiveQuerySlaveStatus.INVOLVEMENT_TYPES.VOLUNTEERING_PEER, InactiveQuerySlaveStatus.LB_PHASES.WAITING_FOR_ADD, senderPeer, lbProcessId, new InactiveQueryMessageDispatcher(peerCommunicator, lbProcessId));

				if (!LoadBalancingStatusCache.getInstance().storeSlaveStatus(senderPeer, lbProcessId, status)) {
					LOG.error("Adding Status to Cache failed.");
				}
				status.getMessageDispatcher().sendAckInit(senderPeer);

			}
			break;

		case InactiveQueryInstructionMessage.ADD_QUERY:
			// Only react if status is not set yet.

			LOG.debug("Got ADD_QUERY");

			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}

			if (status.getPhase().equals(InactiveQuerySlaveStatus.LB_PHASES.WAITING_FOR_ADD)) {

				LOG.debug("PQL received:");
				LOG.debug(instruction.getPQLQuery());

				status.setPhase(InactiveQuerySlaveStatus.LB_PHASES.WAITING_FOR_FINISH);
				dispatcher = status.getMessageDispatcher();
				dispatcher.stopRunningJob();

				try {
					Collection<Integer> queryIDs = LoadBalancingHelper.installQueryPartFromPql(Context.empty(), instruction.getPQLQuery());
					status.setInstalledQueries(queryIDs);

					int installedQuery = (int) (queryIDs.toArray()[0]);
					ILogicalQuery query = OsgiServiceManager.getExecutor().getLogicalQueryById(installedQuery, OsgiServiceManager.getActiveSession());
					IQueryPartController queryPartController = OsgiServiceManager.getQueryPartController();

					// Register as new Master when Query is Master Query
					if (instruction.isMasterForQuery()) {

						LOG.debug("Received Query is Master Query");

						List<String> otherPeerIDStrings = instruction.getOtherPeerIDs();
						String sharedQueryIDString = instruction.getSharedQueryID();

						LOG.debug("Received {} other peer IDs.", otherPeerIDStrings.size());
						LOG.debug("Shared Query ID is {}", sharedQueryIDString);
						Collection<PeerID> otherPeers = LoadBalancingHelper.toPeerIDCollection(otherPeerIDStrings);
						ID sharedQueryID = LoadBalancingHelper.toID(sharedQueryIDString);

						queryPartController.registerAsMaster(query, installedQuery, sharedQueryID, otherPeers);
						status.setRegisteredAsMaster(sharedQueryID);
						OsgiServiceManager.getQueryManager().sendChangeMasterToAllSlaves(sharedQueryID);

					} else {
						LOG.debug("Received Query is Slave Query.");
						ID sharedQueryID = LoadBalancingHelper.toID(instruction.getSharedQueryID());
						PeerID masterPeerID = LoadBalancingHelper.toPeerID(instruction.getMasterPeerID());

						if (queryPartController.isSharedQueryKnown(sharedQueryID)) {
							// No need to inform Master as he already knows this
							// Peer, just add local Query to sharedID...
							Collection<Integer> localQueriesForSharedQuery = queryPartController.getLocalIds(sharedQueryID);
							localQueriesForSharedQuery.addAll(queryIDs);
							queryPartController.registerAsSlave(localQueriesForSharedQuery, sharedQueryID, masterPeerID);
						} else {
							queryPartController.registerAsSlave(queryIDs, sharedQueryID, masterPeerID);
							status.setRegisteredAsNewSlave(masterPeerID, sharedQueryID);
							OsgiServiceManager.getQueryManager().sendRegisterAsSlave(masterPeerID, sharedQueryID);

						}

					}

					dispatcher.sendInstallSuccess(senderPeer);
				} catch (Exception e) {
					dispatcher.sendInstallFailure(senderPeer);
				}
			}
			break;

		case InactiveQueryInstructionMessage.REPLACE_SENDER:
			LOG.debug("Got REPLACE_SENDER.");
			// Create Status if none exist
			if (status == null) {
				status = new InactiveQuerySlaveStatus(InactiveQuerySlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER, InactiveQuerySlaveStatus.LB_PHASES.WAITING_FOR_FINISH, senderPeer, lbProcessId, new InactiveQueryMessageDispatcher(peerCommunicator, lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(senderPeer, lbProcessId, status);
			}

			// Process Pipe only if not already processed:
			if (status.getPhase().equals(InactiveQuerySlaveStatus.LB_PHASES.WAITING_FOR_FINISH) && !status.isPipeKnown(instruction.getPipeId())) {
				String pipe = instruction.getPipeId();
				String peer = instruction.getPeerId();

				status.addKnownPipe(pipe);

				dispatcher = status.getMessageDispatcher();

				try {
					InactiveQueryHelper.addChangeInformation(pipe, status, true);
					InactiveQueryHelper.setNewPeerId(pipe, peer, true);
					dispatcher.sendDuplicateSenderSuccess(status.getMasterPeer(), pipe);
				} catch (Exception e) {
					LOG.error("Error while copying JxtaSender:");
					LOG.error(e.getMessage());
					dispatcher.sendDuplicateFailure(senderPeer);
				}
			}
			break;

		case InactiveQueryInstructionMessage.REPLACE_RECEIVER:
			LOG.debug("Got REPLACE_RECEIVER");
			// Create Status if none exist
			if (status == null) {
				status = new InactiveQuerySlaveStatus(InactiveQuerySlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER, InactiveQuerySlaveStatus.LB_PHASES.WAITING_FOR_FINISH, senderPeer, lbProcessId, new InactiveQueryMessageDispatcher(peerCommunicator, lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(senderPeer, lbProcessId, status);
			}
			// Process Pipe only if not already processed:
			if (status.getPhase().equals(InactiveQuerySlaveStatus.LB_PHASES.WAITING_FOR_FINISH) && !status.isPipeKnown(instruction.getPipeId())) {
				String pipe = instruction.getPipeId();
				String peer = instruction.getPeerId();

				status.addKnownPipe(pipe);
				dispatcher = status.getMessageDispatcher();
				try {
					InactiveQueryHelper.addChangeInformation(pipe, status, false);
					InactiveQueryHelper.setNewPeerId(pipe, peer, false);
					dispatcher.sendDuplicateReceiverSuccess(status.getMasterPeer(), pipe);
				} catch (Exception e) {
					LOG.error("Error while replacing JxtaReceiver:");
					LOG.error(e.getMessage());
					dispatcher.sendDuplicateFailure(senderPeer);
				}
			}
			break;

		case InactiveQueryInstructionMessage.PIPE_SUCCCESS_RECEIVED:
			LOG.debug("Got PIPE_SUCCESS_RECEIVED");
			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			status.getMessageDispatcher().stopRunningJob(instruction.getPipeId());
			break;

		case InactiveQueryInstructionMessage.MESSAGE_RECEIVED:
			LOG.debug("Got MESSAGE_RECEIVED");
			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			status.getMessageDispatcher().stopAllMessages();
			break;

		default:
			break;
		}

	}

}
