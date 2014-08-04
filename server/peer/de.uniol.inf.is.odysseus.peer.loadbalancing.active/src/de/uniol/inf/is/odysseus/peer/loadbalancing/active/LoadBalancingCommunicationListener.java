package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingMasterStatus.LB_PHASES;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInstructionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingResponseMessage;

public class LoadBalancingCommunicationListener implements
		IPeerCommunicatorListener, ILoadBalancingCommunicator {

	/**
	 * 
	 * @author Carsten Cordes Class to encapsulate a Connection to an operator
	 *         on another Peer.
	 * 
	 */

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingCommunicationListener.class);

	/**
	 * List of registered Listeners.
	 */
	private ArrayList<ILoadBalancingListener> listeners = new ArrayList<ILoadBalancingListener>();

	/**
	 * Executor to get queries
	 */
	private static IServerExecutor executor;

	/**
	 * Peer Communicator
	 */
	private static IPeerCommunicator peerCommunicator;

	/**
	 * Network Manager (needed to get GroupID, etc.)
	 */
	private static IP2PNetworkManager p2pNetworkManager;

	/**
	 * Instance of Communication Listener.
	 */
	private static LoadBalancingCommunicationListener instance;

	/**
	 * Active Session. Use getActiveSession() to avoid null pointers.
	 */
	private static ISession activeSession;

	/**
	 * Get Instance of Communication Listener
	 * 
	 * @return this.
	 */
	public static LoadBalancingCommunicationListener getInstance() {
		return instance;
	}

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {
		instance = this;
	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
		instance = null;
	}

	/**
	 * called by OSGi-DS to bind Executor
	 * 
	 * @param exe
	 *            Executor to bind.
	 */
	public static void bindExecutor(IExecutor exe) {
		LOG.debug("Bound Executor.");
		executor = (IServerExecutor) exe;
	}

	/**
	 * called by OSGi-DS to unbind Executor
	 * 
	 * @param exe
	 *            Executor to unbind.
	 */
	public static void unbindExecutor(IExecutor exe) {
		LOG.debug("Unbound Executor.");
		if (executor == exe) {
			executor = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Network Manager
	 * 
	 * @param serv
	 *            Networkmanager to bind.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		LOG.debug("Bound network Manager");
		p2pNetworkManager = serv;
	}

	/**
	 * called by OSGi-DS to unbind Network Manager
	 * 
	 * @param serv
	 *            Networkmanager to unbind.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		LOG.debug("Unbound NetworkMananger");
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Peer Communicator (registers all Messages and
	 * adds Listeners)
	 * 
	 * @param serv
	 *            PeerCommunicator to bind.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Bound Peer Communicator.");
		peerCommunicator = serv;
		peerCommunicator.registerMessageType(LoadBalancingAbortMessage.class);
		peerCommunicator.addListener(this, LoadBalancingAbortMessage.class);

		peerCommunicator
				.registerMessageType(LoadBalancingInstructionMessage.class);
		peerCommunicator.addListener(this,
				LoadBalancingInstructionMessage.class);

		peerCommunicator
				.registerMessageType(LoadBalancingResponseMessage.class);
		peerCommunicator.addListener(this, LoadBalancingResponseMessage.class);

	}

	/**
	 * called by OSGi-DS to unbind Peer Communicator (unregisters all Messages
	 * and removes Listeners)
	 * 
	 * @param serv
	 *            Peer Communicator to unbind.
	 * 
	 */
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Unbound Peer Communicator.");
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this,
					LoadBalancingAbortMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingInstructionMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingResponseMessage.class);

			peerCommunicator
					.unregisterMessageType(LoadBalancingAbortMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingInstructionMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingResponseMessage.class);

			peerCommunicator = null;
		}
	}

	@Override
	/**
	 * called when a registered Message is received.
	 * Implements basic LoadBalancing Protokoll.
	 * @param communicator Peer Communicator
	 * @param senderPeer Peer sending the Message
	 * @param message Received Message.
	 */
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {

		if (message instanceof LoadBalancingResponseMessage) {
			LoadBalancingResponseMessage response = (LoadBalancingResponseMessage) message;
			handlePeerResonse(response, senderPeer);
		}

		if (message instanceof LoadBalancingInstructionMessage) {
			LoadBalancingInstructionMessage instruction = (LoadBalancingInstructionMessage) message;
			handleInstruction(instruction, senderPeer);
		}

		if (message instanceof LoadBalancingAbortMessage) {
			LoadBalancingAbortMessage abortMessage = (LoadBalancingAbortMessage) message;
			handleAbort(abortMessage);
		}

	}

	private void handleInstruction(LoadBalancingInstructionMessage instruction,
			PeerID senderPeer) {

		int lbProcessId = instruction.getLoadBalancingProcessId();
		LoadBalancingMessageDispatcher dispatcher = null;
		LoadBalancingSlaveStatus status = LoadBalancingStatusCache
				.getInstance().getSlaveStatus(senderPeer, lbProcessId);

		boolean isSender = true;

		switch (instruction.getMsgType()) {

		case LoadBalancingInstructionMessage.INITIATE_LOADBALANCING:
			// Only react to first INITIATE_LOADBALANCING Message, even if sent
			// more often.

			if (status == null) {
				status = new LoadBalancingSlaveStatus(
						LoadBalancingSlaveStatus.INVOLVEMENT_TYPES.VOLUNTEERING_PEER,
						LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_ADD,
						senderPeer, lbProcessId,
						new LoadBalancingMessageDispatcher(peerCommunicator,
								getActiveSession(), lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status);
				status.getMessageDispatcher().sendAckInit(senderPeer);
			}
			break;

		case LoadBalancingInstructionMessage.ADD_QUERY:
			// Only react if status is not set yet.
			if (status.getPhase().equals(
					LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_ADD)) {
				status.setPhase(LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_SYNC);
				dispatcher = status.getMessageDispatcher();
				dispatcher.stopRunningJob();
				try {
					Collection<Integer> queryIDs = LoadBalancingHelper
							.installAndRunQueryPartFromPql(executor,
									getActiveSession(), Context.empty(),
									instruction.getPQLQuery());
					dispatcher.sendInstallSuccess(senderPeer, queryIDs);
				} catch (Exception e) {
					status.setPhase(LoadBalancingSlaveStatus.LB_PHASES.ABORT);
					dispatcher.sendInstallFailure(senderPeer);
					// TODO Abort
				}
			}
			break;

		case LoadBalancingInstructionMessage.COPY_RECEIVER:
			isSender = false;
		case LoadBalancingInstructionMessage.COPY_SENDER:
			// Create Status if none exist
			if (status == null) {
				status = new LoadBalancingSlaveStatus(
						LoadBalancingSlaveStatus.INVOLVEMENT_TYPES.PEER_WITH_SENDER_OR_RECEIVER,
						LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_SYNC,
						senderPeer, lbProcessId,
						new LoadBalancingMessageDispatcher(peerCommunicator,
								getActiveSession(), lbProcessId));
				LoadBalancingStatusCache.getInstance().storeSlaveStatus(
						senderPeer, lbProcessId, status);
			}
			// Process Pipe only if not already processed:
			if (status.getPhase().equals(
					LoadBalancingSlaveStatus.LB_PHASES.WAITING_FOR_SYNC)
					&& !status.isPipeKnown(instruction.getNewPipeId())) {
				status.addReplacedPipe(instruction.getOldPipeId(),
						instruction.getOldPipeId());
				dispatcher = status.getMessageDispatcher();
				try {
					LoadBalancingHelper.findAndCopyLocalJxtaOperator(executor,
							dispatcher, getActiveSession(), isSender,
							instruction.getNewPeerId(),
							instruction.getOldPipeId(),
							instruction.getNewPipeId(), lbProcessId);
					dispatcher.sendDuplicateSuccess(senderPeer,
							instruction.getNewPipeId());
				} catch (Exception e) {
					dispatcher.sendDuplicateFailure(senderPeer);
				}
			}
			break;

		case LoadBalancingInstructionMessage.DELETE_RECEIVER:
			LoadBalancingHelper.deleteDeprecatedReceiver(executor,
					instruction.getOldPipeId());
			break;
		case LoadBalancingInstructionMessage.DELETE_SENDER:
			deleteDeprecatedSender(instruction.getOldPipeId());
			break;

		}

	}

	private void notifyOutgoingAndIncomingPeers(LoadBalancingMasterStatus status) {

		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		HashMap<String, String> replacedPipes = status.getReplacedPipes();
		LoadBalancingMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		ArrayList<String> pipesToInstall = new ArrayList<String>();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				dispatcher.sendCopyOperator(true,
						LoadBalancingHelper.toPeerID(sender.getPeerID()),
						replacedPipes.get(sender.getPipeID()),
						sender.getPipeID(), volunteeringPeer);
				pipesToInstall.add(sender.getPipeID());
			}
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				dispatcher.sendCopyOperator(true,
						LoadBalancingHelper.toPeerID(receiver.getPeerID()),
						replacedPipes.get(receiver.getPipeID()),
						receiver.getPipeID(), volunteeringPeer);
				pipesToInstall.add(receiver.getPipeID());
			}
		}
		status.setPipesToInstall(pipesToInstall);

	}

	/**
	 * Deletes deprecated JxtaSenderPO.
	 * 
	 * @param oldPipeId
	 */
	private void deleteDeprecatedSender(String oldPipeId) {
		JxtaSenderPO<?> physicalJxtaOperator = (JxtaSenderPO<?>) LoadBalancingHelper
				.getPhysicalJxtaOperator(executor, true, oldPipeId);
		if (physicalJxtaOperator != null) {
			// Sender always have one input port
			physicalJxtaOperator.done(0);
			physicalJxtaOperator.unsubscribeFromAllSources();
		}
	}

	/**
	 * Initiates the copy Process between a Peer and another Peer (after
	 * Allocation)
	 * 
	 * @param otherPeer
	 *            Peer which should receive a QueryPart
	 * @param queryPartID
	 *            queryId of QueryPart to copy.
	 */
	@Override
	public void initiateLoadBalancing(PeerID otherPeer, int queryId) {
		ILogicalQueryPart partToCopy = LoadBalancingHelper
				.getInstalledQueryPart(executor, getActiveSession(), queryId);
		int lbProcessIdentifier = LoadBalancingStatusCache.getInstance()
				.createNewLocalProcess(partToCopy);
		LoadBalancingMasterStatus status = LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(lbProcessIdentifier);
		status.setLogicalQuery(queryId);
		status.setMessageDispatcher(new LoadBalancingMessageDispatcher(
				peerCommunicator, getActiveSession(), lbProcessIdentifier));
		status.getMessageDispatcher().sendInitiate(otherPeer);
	}

	/**
	 * Gets currently active Session.
	 * 
	 * @return active Session
	 */
	public static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}

	private void handlePeerResonse(LoadBalancingResponseMessage response,
			PeerID senderPeer) {
		
		int loadBalancingProcessId = response.getLoadBalancingProcessId();
		LoadBalancingMasterStatus status = LoadBalancingStatusCache
				.getInstance().getStatusForLocalProcess(loadBalancingProcessId);
		
		//LoadBalancing Process is no longer active -> ignore!
		if(status==null) {
			return;
		}
		
		LoadBalancingMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		switch (response.getMsgType()) {
		case LoadBalancingResponseMessage.ACK_LOADBALANCING:
			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.initiating)) {
				status.setPhase(LoadBalancingMasterStatus.LB_PHASES.copying);
				dispatcher.stopRunningJob();

				ILogicalQueryPart modifiedQueryPart = LoadBalancingHelper
						.getCopyOfQueryPart(status.getOriginalPart());
				HashMap<String, String> replacedPipes = LoadBalancingHelper
						.relinkQueryPart(p2pNetworkManager, modifiedQueryPart,
								senderPeer,status);

				status.setReplacedPipes(replacedPipes);
				status.setModifiedPart(modifiedQueryPart);

				String pqlFromQueryPart = LogicalQueryHelper
						.generatePQLStatementFromQueryPart(modifiedQueryPart);
				dispatcher.sendAddQuery(status.getVolunteeringPeer(),
						pqlFromQueryPart);
			}
			break;

		case LoadBalancingResponseMessage.SUCCESS_INSTALL_QUERY:

			// When in Phase copying, the success Message says that Installing
			// the Query Part on the other Peer was successful.
			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.copying)) {
				dispatcher.stopRunningJob();
				status.setPhase(LoadBalancingMasterStatus.LB_PHASES.relinking);
				notifyOutgoingAndIncomingPeers(status);
			}
			break;

		case LoadBalancingResponseMessage.SUCCESS_DUPLICATE:

			if (status.getPhase().equals(
					LoadBalancingMasterStatus.LB_PHASES.relinking)) {
				dispatcher.stopRunningJob(response.getPipeID());
				if (dispatcher.getNumberOfRunningJobs() == 0) {
					// All success messages received. Yay!
					status.setPhase(LB_PHASES.synchronizing);
				}
			}
			break;

		case LoadBalancingResponseMessage.SYNC_FINISHED:
				if(status.getPhase().equals(LB_PHASES.synchronizing)) {
					status.removePipeToSync(response.getPipeID());
					if(status.getNumberOfPipesToSync()==0) {
						ILogicalQueryPart queryPart = status.getOriginalPart();
						if (!queryPart.getOperators().asList().isEmpty()) {
			
							// Tell Receivers and Senders to delete duplicate Connections.
							for (ILogicalOperator operator : queryPart.getOperators()) {
								if (operator instanceof JxtaReceiverAO) {
									JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
									dispatcher.sendDeleteOperator(false,
											receiver.getPeerID(), receiver.getPipeID());
								}
								if (operator instanceof JxtaSenderAO) {
									JxtaSenderAO sender = (JxtaSenderAO) operator;
									dispatcher.sendDeleteOperator(false,
											sender.getPeerID(), sender.getPipeID());
								}
							}
						}
						int queryId = status.getLogicalQuery();
						LoadBalancingHelper.deleteQuery(executor, getActiveSession(),
								queryId);
					}
				}
			break;

		case LoadBalancingResponseMessage.FAILURE_INSTALL_QUERY:
			LOG.debug("Failure in installing Query");
			handleError(status);
			break;

		case LoadBalancingResponseMessage.FAILURE_DUPLICATE_RECEIVER:
			LOG.debug("Failure in duplicating Connections");
			handleError(status);
			break;

		}

	}

	public void handleAbort(LoadBalancingAbortMessage abortMessage) {
		switch (abortMessage.getMsgType()) {
		case LoadBalancingAbortMessage.ABORT_REMOVE_QUERY:
			Integer[] queriesToRemove = abortMessage.getQueriesToRemove();
			for (int query : queriesToRemove) {
				LoadBalancingHelper.deleteQuery(executor, getActiveSession(),
						query);
			}
			break;

		case LoadBalancingAbortMessage.ABORT_REMOVE_DUPLICATE_CONNECTION:
			String pipeToRemove = abortMessage.getPipeToRemove();
			LoadBalancingHelper
					.deleteDeprecatedReceiver(executor, pipeToRemove);
			break;

		case LoadBalancingAbortMessage.ABORT_DO_NOTHING:
			// Do nothing. Maybe needed in later versions of this protocol.

		}
	}

	public void handleError(LoadBalancingMasterStatus status) {
		LoadBalancingMessageDispatcher dispatcher = status
				.getMessageDispatcher();
		// Handle error depending on current LoadBalancing phase.
		switch (status.getPhase()) {
		case initiating:
		case copying:
			// Send abort only to volunteering Peer
			dispatcher.sendAbort(status.getVolunteeringPeer());
			break;
		case relinking:
			// Send Abort to all Peers involved
			undoLoadBalancing(status);
			break;
		case synchronizing:
			// New query is already running. Do not abort.
			break;
		default:
			break;

		}
		//TODO Kill status and re-Allocate.
		
	}

	public void undoLoadBalancing(LoadBalancingMasterStatus status) {
		status.setPhase(LoadBalancingMasterStatus.LB_PHASES.failure);
			//TODO send Abort to all involved Peers.

	}

	/**
	 * Adds a LoadBalancing Listener
	 */
	@Override
	public void registerLoadBalancingListener(ILoadBalancingListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a Load Balancing Listener
	 */
	@Override
	public void removeLoadBalancingListener(ILoadBalancingListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}
	

}