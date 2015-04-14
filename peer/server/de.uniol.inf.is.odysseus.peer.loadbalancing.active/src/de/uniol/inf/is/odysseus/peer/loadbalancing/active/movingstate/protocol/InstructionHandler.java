package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.protocol;

import java.util.Collection;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingStatusCache;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateCommunicatorImpl;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages.MovingStateInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateSlaveStatus;

/**
 * Handles Instruction messages, sent from initiation Master Peer to Slave
 * Peers.
 */
public class InstructionHandler {

	private static Object semaphore = new Object();

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
	public static void handleInstruction(MovingStateInstructionMessage instruction, PeerID senderPeer) {

		MovingStateSlaveStatus status;
		int lbProcessId = instruction.getLoadBalancingProcessId();
		MovingStateMessageDispatcher dispatcher = null;

		IPeerCommunicator peerCommunicator = MovingStateCommunicatorImpl.getPeerCommunicator();

		// Decide which message we received.
		switch (instruction.getMsgType()) {

		case MovingStateInstructionMessage.INITIATE_LOADBALANCING:
			// Only react to first INITIATE_LOADBALANCING Message, even if sent
			// more often.

			LOG.debug("Got INITIATE_LOADBALANCING");
			LOG.info("Load Balancing requested from Peer {}", senderPeer);

			synchronized (semaphore) {
				status = (MovingStateSlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);

				if (status == null) {
					status = new MovingStateSlaveStatus(MovingStateSlaveStatus.INVOLVEMENT_TYPES.VOLUNTEERING_PEER, MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_ADD, senderPeer, lbProcessId, new MovingStateMessageDispatcher(peerCommunicator, lbProcessId));

					if (!LoadBalancingStatusCache.getInstance().storeSlaveStatus(senderPeer, lbProcessId, status)) {
						LOG.error("Adding Status to Cache failed.");
					}
					status.getMessageDispatcher().sendAckInit(senderPeer);

				}
			}
			break;

		case MovingStateInstructionMessage.ADD_QUERY:
			// Only react if status is not set yet.

			LOG.debug("Got ADD_QUERY");

			status = (MovingStateSlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);

			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}

			if (status.getPhase().equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_ADD)) {

				LOG.debug("PQL received:");
				LOG.debug(instruction.getPQLQuery());

				status.setPhase(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY);
				dispatcher = status.getMessageDispatcher();
				dispatcher.stopRunningJob();
				try {
					Collection<Integer> queryIDs = LoadBalancingHelper.installAndRunQueryPartFromPql(Context.empty(), instruction.getPQLQuery());
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

		case MovingStateInstructionMessage.INSTALL_BUFFER_AND_REPLACE_SENDER:

			synchronized (semaphore) {
				status = (MovingStateSlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);

				// Create Status if none exist
				if (status == null) {
					status = new MovingStateSlaveStatus(MovingStateSlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER, MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH, senderPeer, lbProcessId, new MovingStateMessageDispatcher(peerCommunicator, lbProcessId));
					LoadBalancingStatusCache.getInstance().storeSlaveStatus(senderPeer, lbProcessId, status);
				}
			}

			synchronized (status) {
				if (status.isSenderPipeKnown(instruction.getPipeId())) {
					return;
				}
				status.addKnownSenderPipe(instruction.getPipeId());
			}

			if (status.getPhase().equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH) || status.getPhase().equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY))

			{
				LOG.debug("Got INSTALL_BUFFER_AND_REPLACE_SENDER.");
				LOG.debug("Trying to duplicate Sender with pipe {}->{}", instruction.getPipeId(), instruction.getNewPipe());

				String pipe = instruction.getPipeId();
				String newPipe = instruction.getNewPipe();
				String peer = instruction.getPeerId();

				status.addBufferedPipe(pipe);

				dispatcher = status.getMessageDispatcher();

				try {
					MovingStateHelper.addChangeInformation(pipe, status, true);
					MovingStateHelper.startBuffering(pipe);

					Thread.sleep(1000);

					MovingStateHelper.setNewPipe(status, pipe, newPipe, peer, true);
					status.addPipeMapping(pipe, newPipe);
					dispatcher.sendDuplicateSenderSuccess(status.getMasterPeer(), pipe);
					return;

				} catch (Exception e) {
					LOG.error("Error while copying JxtaSender:");
					LOG.error(e.getMessage());
					dispatcher.sendDuplicateFailure(senderPeer);
					return;
				}
			}
			break;

		case MovingStateInstructionMessage.REPLACE_RECEIVER:

			// Create Status if none exist
			synchronized (semaphore) {
				status = (MovingStateSlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);
				if (status == null) {
					status = new MovingStateSlaveStatus(MovingStateSlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER, MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH, senderPeer, lbProcessId, new MovingStateMessageDispatcher(peerCommunicator, lbProcessId));
					LoadBalancingStatusCache.getInstance().storeSlaveStatus(senderPeer, lbProcessId, status);
				}
			}

			synchronized (status) {
				if (status.isReceiverPipeKnown(instruction.getPipeId())) {
					return;
				}
				status.addKnownReceiverPipe(instruction.getPipeId());
			}

			// Process Pipe only if not already processed:
			if ((status.getPhase().equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH)) || (status.getPhase().equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY))) {

				LOG.debug("Got REPLACE_RECEIVER");
				LOG.debug("Trying to duplicate Receiver with old pipe {}", instruction.getPipeId());

				String oldPipe = instruction.getPipeId();
				String newPipe = instruction.getNewPipe();
				String peer = instruction.getPeerId();

				status.addPipeMapping(oldPipe, instruction.getNewPipe());
				dispatcher = status.getMessageDispatcher();
				try {
					MovingStateHelper.addChangeInformation(oldPipe, status, false);
					MovingStateHelper.setNewPipe(status, oldPipe, newPipe, peer, false);
					dispatcher.sendDuplicateReceiverSuccess(status.getMasterPeer(), oldPipe);
				} catch (Exception e) {
					if (!status.isReceiverPipeKnown(oldPipe)) {
						LOG.error("Error while replacing JxtaReceiver:");
						LOG.error(e.getMessage());
						dispatcher.sendDuplicateFailure(senderPeer);
					}

				}
			}
			break;

		case MovingStateInstructionMessage.PIPE_SUCCCESS_RECEIVED:
			LOG.debug("Got PIPE_SUCCESS_RECEIVED");
			status = (MovingStateSlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);
			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			status.getMessageDispatcher().stopRunningJob(instruction.getPipeId());
			break;

		case MovingStateInstructionMessage.INITIATE_STATE_COPY:
			LOG.debug("Got INITITATE_STATE_COPY");
			status = (MovingStateSlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);
			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}

			MovingStateManager manager = MovingStateManager.getInstance();

			synchronized (status) {
				if (status.isReceiverPipeKnown(instruction.getPipeId())) {
					return;
				}
				status.addKnownReceiverPipe(instruction.getPipeId());
			}

			try {
				if (status.getPhase().equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY)) {
					List<IStatefulPO> statefulOperatorList = status.getStatefulOperatorList();
					if (statefulOperatorList == null) {
						statefulOperatorList = MovingStateHelper.getStatefulOperatorList(status.getInstalledQueries());
						status.setStatefulOperatorList(statefulOperatorList);
					}

					if (MovingStateHelper.compareStatefulOperator(statefulOperatorList, instruction.getOperatorIndex(), instruction.getOperatorType())) {
						IStatefulPO operator = statefulOperatorList.get(instruction.getOperatorIndex());
						if (operator == null) {
							LOG.error("Operator with index {} is null", instruction.getOperatorIndex());
						}

						LOG.debug("Adding State Receiver with pipe {}", instruction.getPipeId());

						status.addReceiver(instruction.getPipeId(), operator);
						manager.addReceiver(senderPeer.toString(), instruction.getPipeId());
						manager.getReceiver(instruction.getPipeId()).addListener(status);
						status.getMessageDispatcher().sendInititiateStateCopyAck(status.getMasterPeer(), status.getLbProcessId(), instruction.getPipeId());
					} else {
						throw new LoadBalancingException("Operators do not match on both peers.");
					}
				}
			} catch (Exception e) {
				status.getMessageDispatcher().sendInitiateStateCopyFail(senderPeer, instruction.getPeerId());
			}
			break;

		case MovingStateInstructionMessage.FINISHED_COPYING_STATES:
			status = (MovingStateSlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);
			if (status.getPhase().equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY)) {
				LOG.debug("Got FINISHED_COPYING_STATES");

				status.setPhase(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH);
				status.getMessageDispatcher().sendAckCopyingFinished(senderPeer);
			}
			break;

		case MovingStateInstructionMessage.STOP_BUFFERING:
			status = (MovingStateSlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);
			if (status == null) {
				return;
			}

			synchronized (status) {
				if (!status.getPhase().equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH)) {
					return;
				}
				status.setPhase(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_MSG_RECEIVED);
			}

			boolean successful = true;

			LOG.debug("Got STOP_BUFFERING");
			for (String pipe : status.getBufferedPipes()) {
				try {
					String newPipe = status.getNewPipeForOldPipe(pipe);
					if (newPipe != null) {
						MovingStateHelper.stopBuffering(newPipe);
					} else {
						LOG.warn("New Pipe is null for old Pipe {}", pipe);
					}
				} catch (LoadBalancingException e) {
					LOG.error("Error while stopping buffering.");
					e.printStackTrace();
					successful = false;
				}
			}
			if (successful) {
				status.getMessageDispatcher().sendStopBufferingFinished(status.getMasterPeer());
			} else {
				LOG.error("STOP_BUFFERING failed. Ignoring error.");
			}

			break;

		case MovingStateInstructionMessage.MESSAGE_RECEIVED:
			status = (MovingStateSlaveStatus) LoadBalancingStatusCache.getInstance().getSlaveStatus(senderPeer, lbProcessId);
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
