package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.DownstreamConnection;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.UpstreamConnection;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateMasterStatus.LB_PHASES;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateSlaveStatus;

/***
 * Helper class for MovingState Strategy
 * 
 * @author Carsten Cordes
 *
 */
public class MovingStateHelper {

	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateHelper.class);

	/****
	 * Sends a state to another Peer
	 * 
	 * @param pipe
	 *            PipeID to use to send status.
	 * @param operator
	 *            Operator which has state that should be sent.
	 * @throws LoadBalancingException
	 */
	public static void sendState(String pipe, IStatefulPO operator)
			throws LoadBalancingException {

		MovingStateSender sender = MovingStateManager.getInstance().getSender(
				pipe);

		if (operator == null) {
			throw new LoadBalancingException(
					"Stateful Operator cannot be null.");
		}

		if (sender == null) {
			throw new LoadBalancingException("No sender found for pipeID "
					+ pipe);
		}

		LOG.debug("Sending state for " + operator.toString() + " on pipe "
				+ pipe);
		Serializable state = operator.getState();
		LOG.debug("Got state.");
		sender.sendData(state);
		LOG.debug("Data sent.");

	}
	
	

	/***
	 * Gets a list of Stateful Operators from Query
	 * 
	 * @param queryId
	 *            query that should be traversed to get StatefulPOs.
	 * @return List of Stateful POs in Query.
	 */
	public static List<IStatefulPO> getStatefulOperatorList(int queryId) {

		IExecutor executor = OsgiServiceManager.getExecutor();
		List<IPhysicalOperator> roots = executor.getPhysicalRoots(queryId,
				OsgiServiceManager.getActiveSession());
		List<IStatefulPO> statefulPOs = new ArrayList<IStatefulPO>();
		for (IPhysicalOperator root : roots) {
			LOG.debug(("Found root for Query " + queryId + ": " + root
					.getName()));
			List<IStatefulPO> knownOperators = new ArrayList<IStatefulPO>();
			statefulPOs.addAll(traverseGraphAndFindStatefulOperators(root,
					knownOperators));
		}
		return statefulPOs;

	}

	/***
	 * Gets a list of Stateful Operators from multiple Queries
	 * 
	 * @param installedQueries
	 *            List of Queries that should be traversed to find StatefulPOs.
	 * @return List of statefulPOs
	 */
	public static List<IStatefulPO> getStatefulOperatorList(
			Collection<Integer> installedQueries) {
		List<IStatefulPO> statefulList = new ArrayList<IStatefulPO>();
		for (int queryId : installedQueries) {
			statefulList.addAll(getStatefulOperatorList(queryId));
		}
		return statefulList;
	}

	/***
	 * Prepares master Peer for sending states of relevant Operators and sends
	 * message to Slave Peer.
	 * 
	 * @param status
	 *            MasterStatus
	 */
	public static void initiateStateCopy(MovingStateMasterStatus status) {
		LOG.debug("Inititate State copy called.");
		MovingStateCommunicatorImpl communicator = MovingStateCommunicatorImpl
				.getInstance();
		MovingStateMessageDispatcher dispatcher = status.getMessageDispatcher();

		List<IStatefulPO> statefulOperators = getStatefulOperatorList(status
				.getLogicalQuery());
		LOG.debug("Got list of " + statefulOperators.size()
				+ " Stateful Operators.");
		
		if (statefulOperators.size()==0) {
			LOG.info("No stateful Operators found in Query. Skipping State-Copy-Phase.");
			status.setPhase(LB_PHASES.COPYING_FINISHED);
			status.getMessageDispatcher()
					.sendFinishedCopyingStates(
							status.getVolunteeringPeer());
		}
		
		for (IStatefulPO statefulPO : statefulOperators) {

			// Installing a new State Sender
			String stateSenderPipe = MovingStateManager.getInstance()
					.addSender(status.getVolunteeringPeer().toString());
			status.addSender(stateSenderPipe, statefulPO);

			dispatcher.sendInititateStateCopy(status.getVolunteeringPeer(),
					communicator, stateSenderPipe, statefulPO.getClass()
							.toString(), statefulOperators.indexOf(statefulPO));
		}
	}

	/***
	 * Recursive Function that traverses Graph to find StatefulPOs
	 * 
	 * @param root
	 *            Where to start
	 * @param knownOperators
	 *            Already known statefulPOs
	 * @return List of Stateful Operators in Graph.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<IStatefulPO> traverseGraphAndFindStatefulOperators(
			IPhysicalOperator root, List<IStatefulPO> knownOperators) {

		if (root instanceof IStatefulPO && !knownOperators.contains(root)) {
			LOG.debug((" + Found stateful Op: " + root.getName()));
			knownOperators.add((IStatefulPO) root);
		} else {
			LOG.debug(" - Op " + root.getName() + " not added.");
		}
		if (root instanceof IPipe) {
			LOG.debug("   recognized Operator as Pipe.");
			IPipe rootAsSource = (IPipe) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>) rootAsSource
					.getSubscribedToSource()) {
				knownOperators = traverseGraphAndFindStatefulOperators(
						(IPhysicalOperator) subscription.getTarget(),
						knownOperators);
			}
			return knownOperators;
		}

		if (root instanceof ISource) {
			LOG.debug("   recognized Operator as Source.");
			return knownOperators;
		}
		if (root instanceof ISink) {
			LOG.debug("   recognized Operator as Sink.");
			ISink rootAsSink = (ISink) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>) rootAsSink
					.getSubscribedToSource()) {
				knownOperators = traverseGraphAndFindStatefulOperators(
						(IPhysicalOperator) subscription.getTarget(),
						knownOperators);
			}
			return knownOperators;
		}
		return knownOperators;
	}

	/**
	 * Sends abort Message to all Peers involved in LoadBalancing Process
	 * 
	 * @param status
	 *            Master Status
	 */
	public static void notifyInvolvedPeers(MovingStateMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = MovingStateCommunicatorImpl
				.getInstance();

		status.setPhase(MovingStateMasterStatus.LB_PHASES.FAILURE);
		// Get Distinct List of involved Peers:
		ArrayList<PeerID> involvedPeers = new ArrayList<PeerID>(
				new HashSet<PeerID>(status.getPeersForPipe().values()));
		involvedPeers.add(status.getVolunteeringPeer());
		for (PeerID peer : involvedPeers) {
			LOG.debug("Asking Peer " + peer, " to abort.");
			status.getMessageDispatcher().sendAbortInstruction(peer,
					deliveryFailedListener);
		}
	}

	/***
	 * Starts buffering of a jxta Sender by suspending all subscriptions.
	 * 
	 * @param pipeID
	 *            PipeID of JxtaSender
	 * @throws LoadBalancingException
	 */
	public static void startBuffering(String pipeID)
			throws LoadBalancingException {
		IPhysicalOperator operator = LoadBalancingHelper
				.getPhysicalJxtaOperator(true, pipeID);
		if (operator == null) {
			throw new LoadBalancingException("No Sender with pipeID " + pipeID
					+ " found.");
		}
		startBuffering(operator);

	}
	
	
	
	@SuppressWarnings("rawtypes")
	public static void addChangeInformation(String pipeID, MovingStateSlaveStatus status, boolean isSender) {
		IPhysicalOperator operator = LoadBalancingHelper
				.getPhysicalJxtaOperator(isSender, pipeID);
		if(operator!=null) {
			if(isSender) {
				status.addChangedPipe(pipeID,((JxtaSenderPO)operator).getPeerIDString());
			}
			else {
				status.addChangedPipe(pipeID,((JxtaReceiverPO)operator).getPeerIDString());
			}
		}
	}
	
	
	/***
	 * Starts Buffering before a given Operator
	 * @param operator Operator to start buffering
	 * @throws LoadBalancingException
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void startBuffering(IPhysicalOperator operator) throws LoadBalancingException {
		if(operator instanceof ISink) {
			for(Object subscriptionFrom : ((ISink) operator).getSubscribedToSource()) {
				AbstractPhysicalSubscription sub = (AbstractPhysicalSubscription)subscriptionFrom;
				ISource operatorBefore = (ISource)sub.getTarget();
				
				for(Object subscriptionTo : operatorBefore.getSubscriptions()) {
					AbstractPhysicalSubscription subTo = (AbstractPhysicalSubscription)subscriptionTo;
					
					if(subTo.getTarget()!=operator)
						continue;
					
					if(subTo instanceof ControllablePhysicalSubscription) {
						ControllablePhysicalSubscription cSub = (ControllablePhysicalSubscription)subTo;
						LOG.debug("Calling suspend on Subscription:"+ cSub);
						cSub.suspend();
						LOG.debug("Open calls is now " +cSub.getOpenCalls());
					}
					else {
						LOG.error("At least one subscription to Operator is not suspendable.");
					}
				}
				
				
			}
		}
		else {
			throw new LoadBalancingException("Operator has no incoming connections.");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void stopBuffering(IPhysicalOperator operator) throws LoadBalancingException {
		if(operator instanceof ISink) {
			for(Object subscriptionFrom : ((ISink) operator).getSubscribedToSource()) {
				AbstractPhysicalSubscription sub = (AbstractPhysicalSubscription)subscriptionFrom;
				ISource operatorBefore = (ISource)sub.getTarget();
				
				for(Object subscriptionTo : operatorBefore.getSubscriptions()) {
					AbstractPhysicalSubscription subTo = (AbstractPhysicalSubscription)subscriptionTo;
					
					if(subTo.getTarget()!=operator)
						continue;
					
					if(subTo instanceof ControllablePhysicalSubscription) {
						ControllablePhysicalSubscription cSub = (ControllablePhysicalSubscription)subTo;
						LOG.debug("Calling resume on Subscription:"+ cSub);
						cSub.resume();
						LOG.debug("Open calls is now " +cSub.getOpenCalls());
					}
					else {
						LOG.error("At least one subscription to Operator is not suspendable.");
					}
				}
				
				
			}
		}
		else {
			throw new LoadBalancingException("Operator has no incoming connections.");
		}
	}
	
	

	/***
	 * Sets Sender or Receiver to new PeerID.
	 * 
	 * @param pipeID
	 *            PipeID of JxtaOperator
	 * @param peerID
	 *            new Peer ID.
	 * @param isSender
	 *            is Operator sender?
	 * @throws LoadBalancingException
	 */
	@SuppressWarnings("rawtypes")
	public static void setNewPeerId(String pipeID, String peerID,
			boolean isSender) throws LoadBalancingException {
		IPhysicalOperator operator = LoadBalancingHelper
				.getPhysicalJxtaOperator(isSender, pipeID);
		if (operator == null) {
			throw new LoadBalancingException("No Sender with pipeID " + pipeID
					+ " found.");
		}
		if (operator instanceof JxtaSenderPO) {
			LoadBalancingHelper.setNewPeerID((JxtaSenderPO) operator, peerID);
		}
		if (operator instanceof JxtaReceiverPO) {
			LoadBalancingHelper.setNewPeerID((JxtaReceiverPO) operator, peerID);
		}
	}

	/***
	 * Opens a receiver
	 * 
	 * @param receiver
	 *            receiver to open.
	 */
	@SuppressWarnings("rawtypes")
	public static void openReceiver(JxtaReceiverPO receiver) {

		for (Object ownerObject : receiver.getOwner()) {
			if (ownerObject instanceof IOperatorOwner) {
				IOperatorOwner owner = (IOperatorOwner) ownerObject;
				receiver.open(owner);
			}

		}
	}

	/***
	 * Stops buffering of a jxta Sender
	 * 
	 * @param pipeID
	 *            PipeID of JxtaSender
	 * @throws LoadBalancingException
	 */
	public static void stopBuffering(String pipeID)
			throws LoadBalancingException {
		LOG.debug("Resuming Sender with pipe " + pipeID);
		IPhysicalOperator operator = LoadBalancingHelper
				.getPhysicalJxtaOperator(true, pipeID);
		if (operator == null) {
			throw new LoadBalancingException("No Sender with pipeID " + pipeID
					+ " found.");
		}
		stopBuffering(operator);

	}

	/**
	 * Send Message to all incoming Peers to add a duplicate connection
	 * 
	 * @param status
	 *            Status
	 */
	public static void notifyDownstreamPeers(MovingStateMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = MovingStateCommunicatorImpl
				.getInstance();

		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		MovingStateMessageDispatcher dispatcher = status.getMessageDispatcher();

		HashMap<String, PeerID> peersForPipe = new HashMap<String, PeerID>();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;

				String pipe = sender.getPipeID();
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(sender
						.getPeerID());
				peersForPipe.put(pipe, destinationPeer);

				dispatcher.sendReplaceReceiverMessage(destinationPeer,
						volunteeringPeer, pipe, deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);

	}

	/***
	 * Sends a stop Buffering Message to all upstream Peers.
	 * 
	 * @param status
	 *            current MasterStatus
	 */
	public static void sendStopBufferingToUpstreamPeers(
			MovingStateMasterStatus status) {
		for (PeerID peer : status.getUpstreamPeers()) {
			status.getMessageDispatcher().sendStopBuffering(peer,
					MovingStateCommunicatorImpl.getInstance());
		}
	}

	/**
	 * Send Message to all upstream Peers to add a duplicate connection
	 * 
	 * @param status
	 *            Status
	 */
	public static void notifyUpstreamPeers(MovingStateMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = MovingStateCommunicatorImpl
				.getInstance();

		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		ArrayList<PeerID> upstreamPeers = new ArrayList<PeerID>();

		MovingStateMessageDispatcher dispatcher = status.getMessageDispatcher();

		HashMap<String, PeerID> peersForPipe = status.getPeersForPipe();
		if (peersForPipe == null) {
			peersForPipe = new HashMap<String, PeerID>();
		}

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;

				String pipe = receiver.getPipeID();
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(receiver
						.getPeerID());
				if (!upstreamPeers.contains(destinationPeer))
					upstreamPeers.add(destinationPeer);
				peersForPipe.put(pipe, destinationPeer);

				dispatcher.sendInstallBufferAndReplaceSenderMessage(
						destinationPeer, volunteeringPeer, pipe,
						deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);
		status.setUpstreamPeers(upstreamPeers);

	}

	/**
	 * Relinks a logical Query Part to a new peer.
	 * 
	 * @param modifiedPart
	 *            Query part where receivers/senders should be added
	 * @param status
	 *            current master Status.
	 * @return
	 */
	public static void relinkQueryPart(ILogicalQueryPart modifiedPart,
			MovingStateMasterStatus status) {

		LoadBalancingHelper.removeTopAOs(modifiedPart);

		Map<ILogicalOperator, Collection<UpstreamConnection>> incomingConnections = LoadBalancingHelper
				.stripJxtaReceivers(modifiedPart);
		Map<ILogicalOperator, Collection<DownstreamConnection>> outgoingConnections = LoadBalancingHelper
				.stripJxtaSenders(modifiedPart);

		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(modifiedPart);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(modifiedPart);

		ArrayList<String> senderPipes = new ArrayList<String>();

		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<UpstreamConnection> connections = incomingConnections
						.get(relativeSource);
				for (UpstreamConnection connection : connections) {
					JxtaReceiverAO receiver = LoadBalancingHelper
							.createReceiverAO(connection, connection.oldPipeID);
					modifiedPart.addOperator(receiver);

				}
			} else {
				LOG.debug("Found real source " + relativeSource.getName());
			}
		}

		for (ILogicalOperator relativeSink : relativeSinks) {
			if (outgoingConnections.containsKey(relativeSink)) {
				Collection<DownstreamConnection> connections = outgoingConnections
						.get(relativeSink);
				for (DownstreamConnection connection : connections) {
					JxtaSenderAO sender = LoadBalancingHelper.createSenderAO(
							connection, connection.oldPipeID);

					modifiedPart.addOperator(sender);
					senderPipes.add(connection.oldPipeID);
				}
			}
		}
		status.setPipesToSync(senderPipes);
		LOG.debug("Pipes to Sync (later):");
		for (String pipe : senderPipes) {
			LOG.debug(pipe);
		}
		status.setModifiedPart(modifiedPart);
	}

	/**
	 * Compares if stateful Operator is of a particular type
	 * 
	 * @param operatorIndex
	 *            List index of Operator in Operator List
	 * @param installedQueries
	 *            List of installed Queries to get Operators from
	 * @param operatorType
	 *            Operator Type to comapre
	 * @return true if Operator is of Type operatorType, false if not.
	 */
	public static boolean compareStatefulOperator(List<IStatefulPO> statefulOperatorList,int operatorIndex, String operatorType) {
		IStatefulPO operator = statefulOperatorList.get(operatorIndex);
		if (operator != null) {
			if (operator.getClass().toString().equals(operatorType))
				return true;
		}
		return false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String installNewPhysicalJxtaSender(ISource operatorBefore, String peerID, int port) throws DataTransmissionException {
		String pipeID = IDFactory.newPipeID(
				OsgiServiceManager.getP2pNetworkManager().getLocalPeerGroupID()).toString();
		JxtaSenderAO sender = new JxtaSenderAO();
		sender.setPeerID(peerID);
		sender.setPipeID(pipeID);
		sender.setOutputSchema(operatorBefore.getOutputSchema());
		JxtaSenderPO senderPO;
		senderPO = new JxtaSenderPO(sender);
		operatorBefore.subscribeSink(senderPO, 0, port, operatorBefore.getOutputSchema());
		LOG.debug("Created SENDER with Pipe " + pipeID);
		return pipeID;
	}
	
	
	public static void replaceSourceWithLogicalReceiver(ILogicalOperator sourceOperator, String pipeID, String peerID) {
		JxtaReceiverAO receiver = new JxtaReceiverAO();
		receiver.setPeerID(peerID);
		receiver.setPipeID(pipeID);
		receiver.setSchema(sourceOperator.getOutputSchema().getAttributes());
		
		for (LogicalSubscription subscription : sourceOperator.getSubscriptions()) {
			sourceOperator.unsubscribeSink(subscription);
			receiver.subscribeSink(subscription.getTarget(), subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
		}
	}

	/**
	 * Gets Stateful PO from Queries
	 * 
	 * @param operatorIndex
	 *            Index of Operator in List
	 * @param installedQueries
	 *            List of installed Queries.
	 * @return StatefulPO
	 */
	public static IStatefulPO getStatefulPO(int operatorIndex,
			Collection<Integer> installedQueries) {
		List<IStatefulPO> statefulList = getStatefulOperatorList(installedQueries);
		return statefulList.get(operatorIndex);

	}

	/***
	 * Triggers injection of received status in Operator
	 * 
	 * @param status
	 *            SlaveStatus
	 * @param pipeId
	 *            PipeID on which state was sent.
	 */
	public static void injectState(MovingStateSlaveStatus status, String pipeId) {
		try {
			IStatefulPO op = status.getStatefulPOforPipe(pipeId);
			MovingStateManager.getInstance().getReceiver(pipeId)
					.injectState(op);
			status.getMessageDispatcher().sendCopyStateFinished(
					status.getMasterPeer(), pipeId);
		} catch (Exception e) {
			// TODO Error Handling
		}

	}



	public static void startBufferingLocalSenders(MovingStateMasterStatus status) {
		status.getAllSenderPipes();
		
	}
}
