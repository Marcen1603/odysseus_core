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
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingStatus.LB_PHASES;
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
	 * Constants
	 */
	public static final int LOOK_FOR_QUERY_FAILED = -1;

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

	/**
	 * Removes a query from current Peer.
	 * 
	 * @param queryId
	 */
	public void deleteQuery(int queryId) {
		executor.removeQuery(queryId, getActiveSession());
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

		/*
		 * 1. Step: Initiating Peer send LoadBalancingInitiateMessage to chosen
		 * Peer. (Happens in Allocator)
		 */

		/*
		 * 2. Step: Chosen Peer answers with LoadBalancingInitiateCopyMessage to
		 * signalize it's ready.
		 */

		/*
		 * 3. Step: Initiating receives LoadBalancingInitiateCopyMessage and
		 * copies and relinks the QueryPart. Send modified QueryPart to
		 * volunteering Peer.
		 */
		if (message instanceof LoadBalancingResponseMessage) {
			LoadBalancingResponseMessage response = (LoadBalancingResponseMessage) message;
			reactToPeerResponse(response, senderPeer);
		}

		if (message instanceof LoadBalancingInstructionMessage) {
			LoadBalancingInstructionMessage instruction = (LoadBalancingInstructionMessage) message;
			reactToInstruction(instruction, senderPeer);
		}
		
		//TODO react to Abort.

		/*
		 * 4. Step: Vounteering peer receives new QueryPart and try to install
		 * Send Success if ok, send Failure if not ok.
		 */

		/*
		 * 5. Step: Initiating Peer receives InstallingQueryPartSuccessful or
		 * LoadBalancingFailedMessage 6. Step: Initiating Peer send
		 * CopyConnectionMessages to all incoming and outgoing peers in
		 * QueryPart.
		 */

		/*
		 * 7. Step Incoming and outgoing Peers receive CopyConnectionMessage and
		 * act accordingly. They send an Ack or a Failure Message.
		 */

		/*
		 * Step 8. Initiating Peer received all Ack Messages? -> Ok If not...
		 * Try again and if that fails Abort. (Can probably not do anything
		 * here)
		 */

		/*
		 * Step 9. Receiving SyncComlete Message -> Delete old Connection.
		 */

	}

	private void reactToInstruction(
			LoadBalancingInstructionMessage instruction, PeerID senderPeer) {
		int lbProcessId = instruction.getLoadBalancingProcessId();
		LoadBalancingStatus status = LoadBalancingStatusCache.getInstance()
				.getStatus(lbProcessId);
		boolean isSender = true;
		switch (instruction.getMsgType()) {
		case LoadBalancingInstructionMessage.INITIATE_LOADBALANCING:
			LoadBalancingMessageDispatcher.sendAckInit(peerCommunicator,
					senderPeer, lbProcessId);
			break;
		case LoadBalancingInstructionMessage.ADD_QUERY:
			try {
				Collection<Integer> queryIDs = LoadBalancingHelper
						.installAndRunQueryPartFromPql(executor,
								getActiveSession(), Context.empty(),
								instruction.getPQLQuery());
				LoadBalancingMessageDispatcher.sendInstallSuccess(
						peerCommunicator, senderPeer, lbProcessId, queryIDs);
			} catch (Exception e) {
				LoadBalancingMessageDispatcher.sendInstallFailure(
						peerCommunicator, senderPeer, lbProcessId);
			}
			break;
		case LoadBalancingInstructionMessage.COPY_RECEIVER:
			isSender = false;
		case LoadBalancingInstructionMessage.COPY_SENDER:
			try {
				LoadBalancingHelper.findAndCopyLocalJxtaOperator(executor,
						peerCommunicator, getActiveSession(), isSender,
						instruction.getNewPeerId(), instruction.getOldPipeId(),
						instruction.getNewPipeId(), lbProcessId);
				LoadBalancingMessageDispatcher.sendDuplicateSuccess(
						peerCommunicator, senderPeer, lbProcessId,
						instruction.getNewPeerId());
			} catch (Exception e) {
				LoadBalancingMessageDispatcher.sendDuplicateFailure(
						peerCommunicator, senderPeer, lbProcessId);

			}
			break;
		case LoadBalancingInstructionMessage.DELETE_RECEIVER:
			LoadBalancingHelper.deleteDeprecatedReceiver(executor,
					instruction.getOldPipeId());
			break;
		case LoadBalancingInstructionMessage.DELETE_SENDER:
			deleteDeprecatedSender(instruction.getOldPipeId());
			break;
		case LoadBalancingInstructionMessage.DELETE_QUERY:
			ILogicalQueryPart queryPart = status.getOriginalPart();
			if (!queryPart.getOperators().asList().isEmpty()) {

				// Tell Receivers and Senders to delete duplicate Connections.
				for (ILogicalOperator operator : queryPart.getOperators()) {
					if (operator instanceof JxtaReceiverAO) {
						JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
						LoadBalancingMessageDispatcher.sendDeleteOperator(
								peerCommunicator, false, receiver.getPeerID(),
								receiver.getPipeID(), lbProcessId);
					}
					if (operator instanceof JxtaSenderAO) {
						JxtaSenderAO sender = (JxtaSenderAO) operator;
						LoadBalancingMessageDispatcher.sendDeleteOperator(
								peerCommunicator, false, sender.getPeerID(),
								sender.getPipeID(), lbProcessId);
					}
				}
			}
			int queryId = status.getLogicalQuery();
			deleteQuery(queryId);
			break;
		}

	}

	private void notifyOutgoingAndIncomingPeers(LoadBalancingStatus status) {

		HashMap<String, String> replacedPipes = status.getReplacedPipes();
		int lbProcessId = status.getProcessId();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		String volunteeringPeer = status.getVolunteeringPeer().toString();

		ArrayList<String> pipesToInstall = new ArrayList<String>();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				LoadBalancingMessageDispatcher.sendCopyOperator(
						peerCommunicator, true, lbProcessId,
						LoadBalancingHelper.toPeerID(sender.getPeerID()),
						replacedPipes.get(sender.getPipeID()),
						sender.getPipeID(), volunteeringPeer);
				pipesToInstall.add(sender.getPipeID());
			}
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				LoadBalancingMessageDispatcher.sendCopyOperator(
						peerCommunicator, true, lbProcessId,
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

	private ILogicalQueryPart copyQueryPart(int lbProcessId) {
		ILogicalQueryPart part = LoadBalancingStatusCache.getInstance()
				.getOriginalQueryPart(lbProcessId);
		ILogicalQueryPart copy = LoadBalancingHelper.getCopyOfQueryPart(part);
		return copy;
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
				.createNewProcess(partToCopy);
		LoadBalancingStatusCache.getInstance().getStatus(lbProcessIdentifier)
				.setLogicalQuery(queryId);
		LoadBalancingMessageDispatcher.sendInitiate(peerCommunicator,
				otherPeer, lbProcessIdentifier);
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

	private void reactToPeerResponse(LoadBalancingResponseMessage response,
			PeerID senderPeer) {

		int loadBalancingProcessId = response.getLoadBalancingProcessId();
		LoadBalancingStatus status = LoadBalancingStatusCache.getInstance()
				.getStatus(loadBalancingProcessId);

		switch (response.getMsgType()) {
		case LoadBalancingResponseMessage.ACK_LOADBALANCING:

			ILogicalQueryPart modifiedQueryPart = copyQueryPart(loadBalancingProcessId);
			HashMap<String, String> replacedPipes = LoadBalancingHelper
					.relinkQueryPart(p2pNetworkManager, modifiedQueryPart,
							senderPeer);

			status.setReplacedPipes(replacedPipes);
			status.setPhase(LoadBalancingStatus.LB_PHASES.copying);
			status.setModifiedPart(modifiedQueryPart);

			String pqlFromQueryPart = LogicalQueryHelper
					.generatePQLStatementFromQueryPart(modifiedQueryPart);
			LoadBalancingMessageDispatcher.sendAddQuery(peerCommunicator,
					loadBalancingProcessId, senderPeer, pqlFromQueryPart);
			break;

		case LoadBalancingResponseMessage.SUCCESS_INSTALL_QUERY:

			// When in Phase copying, the success Message says that Installing
			// the Query Part on the other Peer was successful.
			if (status.getPhase().equals(LoadBalancingStatus.LB_PHASES.copying)) {
				status.setPhase(LoadBalancingStatus.LB_PHASES.relinking);
				status.foreignQueryIDs = response.getInstalledQueries();
				notifyOutgoingAndIncomingPeers(status);
			}
			break;

		case LoadBalancingResponseMessage.SUCCESS_DUPLICATE:
			if (status.getPhase().equals(
					LoadBalancingStatus.LB_PHASES.relinking)) {
				status.markPipeInstalled(response.getPipeID());
			}
			// No Pipes Left to install -> Everything went well. Wait for sync.
			if (status.pipesLeft() == 0) {
				status.setPhase(LB_PHASES.synchronizing);
			}
			break;

		case LoadBalancingResponseMessage.FAILURE_INSTALL_QUERY:
			// Nothing happened yet ->
			// Kill status and restart Allocation
			LOG.debug("Failure in installing Query");
			handleError(status);
			break;

		case LoadBalancingResponseMessage.FAILURE_DUPLICATE_RECEIVER:
			LOG.debug("Failure in duplicating Connections");
			handleError(status);
			break;

		}

	}

	public void handleError(LoadBalancingStatus status) {
		// Handle error depending on current LoadBalancing phase.
		switch (status.getPhase()) {
		case initiating:
		case copying:
			// Nothing bad happened yet. Send abort DO_NOTHING to other peer,
			// kill Status and try re-Allocation.
			LoadBalancingMessageDispatcher.sendAbort(peerCommunicator,
					status.getVolunteeringPeer(), status.getProcessId());
			// TODO kill status and Re-Allocate.
			break;
		case relinking:
			// Query has already been installed on other Peer and maybe
			// connections have been duplicated. Send everyone an abort Message
			// with instructions how to undo damage.
			undoLoadBalancing(status);
			// TODO kill status and Re-Allocate.
			break;
		case synchronizing:
			// New query is already running. Do not abort.
			break;
		default:
			break;

		}
	}

	public void undoLoadBalancing(LoadBalancingStatus status) {
		status.setPhase(LoadBalancingStatus.LB_PHASES.failure);
		// First try to message every Peer that wanted to install a duplicate:
		for (String pipeID : status.getPipesToInstall()) {
			LoadBalancingMessageDispatcher.sendAbortToDuplicatingPeer(
					peerCommunicator, status.getProcessId(), status
							.getPeersForPipe().get(pipeID), pipeID);
		}

		for (String pipeID : status.getPipesInstalled()) {
			LoadBalancingMessageDispatcher.sendAbortToDuplicatingPeer(
					peerCommunicator, status.getProcessId(), status
							.getPeersForPipe().get(pipeID), pipeID);
		}
		// Now let volunteering Peer remove already installed querys:
		LoadBalancingMessageDispatcher.sendAbort(peerCommunicator,
				status.getVolunteeringPeer(), status.getProcessId(),
				status.getForeignQueryIDs());

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