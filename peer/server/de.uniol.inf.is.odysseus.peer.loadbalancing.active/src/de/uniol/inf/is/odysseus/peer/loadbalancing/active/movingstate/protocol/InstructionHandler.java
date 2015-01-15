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
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ActiveLoadBalancingActivator;
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

	private static final Logger LOG = LoggerFactory
			.getLogger(InstructionHandler.class);

	/**
	 * Handles Instruction messages, sent from initiation Master Peer to Slave
	 * Peers.
	 * 
	 * @param instruction
	 *            Instruction received by Peer.
	 * @param senderPeer
	 *            Peer that sent Message (in this case: Master Peer).
	 */
	public static void handleInstruction(
			MovingStateInstructionMessage instruction, PeerID senderPeer) {

		int lbProcessId = instruction.getLoadBalancingProcessId();
		MovingStateMessageDispatcher dispatcher = null;

		MovingStateSlaveStatus status = (MovingStateSlaveStatus) LoadBalancingStatusCache
				.getInstance().getSlaveStatus(senderPeer, lbProcessId);

		IPeerCommunicator peerCommunicator = MovingStateCommunicatorImpl
				.getPeerCommunicator();

		// Decide which message we received.
		switch (instruction.getMsgType()) {

		case MovingStateInstructionMessage.INITIATE_LOADBALANCING:
			// Only react to first INITIATE_LOADBALANCING Message, even if sent
			// more often.

			LOG.debug("Got INITIATE_LOADBALANCING");

			if (status == null) {
				status = new MovingStateSlaveStatus(
						MovingStateSlaveStatus.INVOLVEMENT_TYPES.VOLUNTEERING_PEER,
						MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_ADD,
						senderPeer, lbProcessId,
						new MovingStateMessageDispatcher(peerCommunicator,
								lbProcessId));

				if (!LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status)) {
					LOG.error("Adding Status to Cache failed.");
				}
				status.getMessageDispatcher().sendAckInit(senderPeer);

			}
			break;

		case MovingStateInstructionMessage.ADD_QUERY:
			// Only react if status is not set yet.

			LOG.debug("Got ADD_QUERY");

			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}

			if (status.getPhase().equals(
					MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_ADD)) {

				LOG.debug("PQL received:");
				LOG.debug(instruction.getPQLQuery());

				status.setPhase(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY);
				dispatcher = status.getMessageDispatcher();
				dispatcher.stopRunningJob();
				try {
					Collection<Integer> queryIDs = LoadBalancingHelper
							.installAndRunQueryPartFromPql(Context.empty(),
									instruction.getPQLQuery());
					status.setInstalledQueries(queryIDs);
					
					int installedQuery = (int)(queryIDs.toArray()[0]);
					ILogicalQuery query = ActiveLoadBalancingActivator.getExecutor().getLogicalQueryById(installedQuery, ActiveLoadBalancingActivator.getActiveSession());
					
					//Register as new Master when Query is Master Query
					if(instruction.isMasterForQuery()) {
						LOG.debug("Received Query is Master Query");
						List<String> otherPeerIDStrings = instruction.getOtherPeerIDs();
						String sharedQueryIDString = instruction.getSharedQueryID();
						LOG.debug("Received {} other peer IDs.",otherPeerIDStrings.size());
						LOG.debug("Shared Query ID is {}",sharedQueryIDString);
						Collection<PeerID> otherPeers = LoadBalancingHelper.toPeerIDCollection(otherPeerIDStrings);
						ID sharedQueryID = LoadBalancingHelper.toID(sharedQueryIDString);
						
						IQueryPartController queryPartController = ActiveLoadBalancingActivator.getQueryPartController();
						
						
						queryPartController.registerAsMaster(query,installedQuery, sharedQueryID, otherPeers);
						
					}
					
					
					dispatcher.sendInstallSuccess(senderPeer);
				} catch (Exception e) {
					dispatcher.sendInstallFailure(senderPeer);
				}
			}
			
			
			break;

		case MovingStateInstructionMessage.INSTALL_BUFFER_AND_REPLACE_SENDER:
			LOG.debug("Got INSTALL_BUFFER_AND_REPLACE_SENDER.");
			// Create Status if none exist
			if (status == null) {
				status = new MovingStateSlaveStatus(
						MovingStateSlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER,
						MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH,
						senderPeer, lbProcessId,
						new MovingStateMessageDispatcher(peerCommunicator,
								lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status);
			}

			// Process Pipe only if not already processed:
			if ((status.getPhase()
					.equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH))
					|| (status.getPhase()
							.equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY))
					&& !status.isPipeKnown(instruction.getPipeId())) {
				String pipe = instruction.getPipeId();
				String peer = instruction.getPeerId();

				status.addKnownPipe(pipe);
				status.addBufferedPipe(pipe);

				dispatcher = status.getMessageDispatcher();

				try {
					MovingStateHelper.addChangeInformation(pipe, status, true);
					MovingStateHelper.startBuffering(pipe);
					MovingStateHelper.setNewPeerId(pipe, peer, true);
					dispatcher.sendDuplicateSenderSuccess(status.getMasterPeer(),
							pipe);
				} catch (Exception e) {
					LOG.error("Error while copying JxtaSender:");
					LOG.error(e.getMessage());
					dispatcher.sendDuplicateFailure(senderPeer);
				}
			}
			break;

		case MovingStateInstructionMessage.REPLACE_RECEIVER:
			LOG.debug("Got REPLACE_RECEIVER");
			// Create Status if none exist
			if (status == null) {
				status = new MovingStateSlaveStatus(
						MovingStateSlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER,
						MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH,
						senderPeer, lbProcessId,
						new MovingStateMessageDispatcher(peerCommunicator,
								lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status);
			}
			// Process Pipe only if not already processed:
			if ((status.getPhase()
					.equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH))
					|| (status.getPhase()
							.equals(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY))
					&& !status.isPipeKnown(instruction.getPipeId())) {
				String pipe = instruction.getPipeId();
				String peer = instruction.getPeerId();

				status.addKnownPipe(pipe);
				dispatcher = status.getMessageDispatcher();
				try {
					MovingStateHelper.addChangeInformation(pipe, status, false);
					MovingStateHelper.setNewPeerId(pipe, peer, false);
					dispatcher.sendDuplicateReceiverSuccess(status.getMasterPeer(),
							pipe);
				} catch (Exception e) {
					LOG.error("Error while replacing JxtaReceiver:");
					LOG.error(e.getMessage());
					dispatcher.sendDuplicateFailure(senderPeer);
				}
			}
			break;

		case MovingStateInstructionMessage.PIPE_SUCCCESS_RECEIVED:
			LOG.debug("Got PIPE_SUCCESS_RECEIVED");
			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			status.getMessageDispatcher().stopRunningJob(
					instruction.getPipeId());
			break;

		case MovingStateInstructionMessage.INITIATE_STATE_COPY:
			LOG.debug("Got INITITATE_STATE_COPY");
			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			MovingStateManager manager = MovingStateManager.getInstance();
			try {
			if (status.getPhase().equals(
					MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY)
					&& !manager.isReceiverPipeKnown(instruction.getPipeId())) {
				List<IStatefulPO> statefulOperatorList = status.getStatefulOperatorList();
				if(statefulOperatorList==null) {
					statefulOperatorList = MovingStateHelper.getStatefulOperatorList(status.getInstalledQueries());
					status.setStatefulOperatorList(statefulOperatorList);
				}
				
				if (MovingStateHelper.compareStatefulOperator(statefulOperatorList,
						instruction.getOperatorIndex(),
						instruction.getOperatorType())) {
					IStatefulPO operator = statefulOperatorList.get(instruction.getOperatorIndex());
					manager.addReceiver(senderPeer.toString(),
							instruction.getPipeId());
					status.addReceiver(instruction.getPipeId(), operator);
					manager.getReceiver(instruction.getPipeId()).addListener(
							status);
					status.getMessageDispatcher().sendInititiateStateCopyAck(
							status.getMasterPeer(), status.getLbProcessId(),
							instruction.getPipeId());
				} else {
						throw new LoadBalancingException("Operators do not match on both peers.");
				}
			}
			} catch (Exception e) {
				status.getMessageDispatcher().sendInitiateStateCopyFail(senderPeer, instruction.getPeerId());
			}
			break;

		case MovingStateInstructionMessage.FINISHED_COPYING_STATES:
			LOG.debug("Got FINISHED_COPYING_STATES");
			if (status.getPhase().equals(
					MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_COPY)) {
				status.setPhase(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH);
				status.getMessageDispatcher()
						.sendAckCopyingFinished(senderPeer);
			}
			break;

		case MovingStateInstructionMessage.STOP_BUFFERING:
			LOG.debug("Got STOP_BUFFERING");
			if (status == null) {
				return;
			}
			boolean successful = true;
			if (status.getPhase().equals(
					MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_FINISH)) {
				for (String pipe : status.getBufferedPipes()) {
					try {
						MovingStateHelper.stopBuffering(pipe);
					} catch (LoadBalancingException e) {
						LOG.error("Error while stopping buffering.");
						e.printStackTrace();
						successful = false;
					}
				}
				if (successful) {
					status.setPhase(MovingStateSlaveStatus.LB_PHASES.WAITING_FOR_MSG_RECEIVED);
					status.getMessageDispatcher().sendStopBufferingFinished(
							status.getMasterPeer());
				} else {
					LOG.error("STOP_BUFFERING failed. Ignoring error.");
				}
			}

			break;

		case MovingStateInstructionMessage.MESSAGE_RECEIVED:
			LOG.debug("Got MESSAGE_RECEIVED");
			if (status == null) {
				LOG.error("Status on Slave Peer is null.");
				return;
			}
			status.getMessageDispatcher().stopAllMessages();
			break;
		}

	}
}
