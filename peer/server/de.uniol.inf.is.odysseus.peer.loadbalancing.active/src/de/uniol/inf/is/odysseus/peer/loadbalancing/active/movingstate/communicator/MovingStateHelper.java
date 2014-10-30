package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

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
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ActiveLoadBalancingActivator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IncomingConnection;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.OutgoingConnection;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status.MovingStateSlaveStatus;

public class MovingStateHelper {
	

	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateHelper.class);
	
	
	public static List<IStatefulPO> getStatefulOperatorList(int queryId) {
		IExecutor executor = ActiveLoadBalancingActivator.getExecutor();
		List<IPhysicalOperator> roots = executor.getPhysicalRoots(queryId, ActiveLoadBalancingActivator.getActiveSession());
		List<IStatefulPO> statefulPOs = new ArrayList<IStatefulPO>();
		for(IPhysicalOperator root : roots) {
			LOG.debug(("Found root for Query " + queryId +": " + root.getName()));
			statefulPOs = traverseGraphAndFindStatefulOperators(root, statefulPOs);
		}
		return statefulPOs;
	}
	
	public static void initiateStateCopy(MovingStateMasterStatus status) {
		MovingStateCommunicatorImpl communicator = MovingStateCommunicatorImpl.getInstance();
		MovingStateMessageDispatcher dispatcher = status.getMessageDispatcher();
		
		List<IStatefulPO> statefulOperators = getStatefulOperatorList(status.getLogicalQuery());
		for(IStatefulPO statefulPO : statefulOperators) {
			//Installing a new State Sender
			String stateSenderPipe = MovingStateManager.getInstance().addSender(status.getVolunteeringPeer().toString());
			dispatcher.sendInititateStateCopy(status.getVolunteeringPeer(), communicator, stateSenderPipe, statefulPO.getClass().toString(), statefulOperators.indexOf(statefulPO));
		}
	}
	
	public static void installStateReceiver(MovingStateSlaveStatus status, String pipeID, String operatorType, int operatorIndex) {
		LOG.debug("Installing State Receiver for operator #"+operatorIndex+ " of type " + operatorType + " at pipe ID: " + pipeID);
		Collection<Integer> installedQueries = status.getInstalledQueries();
		ArrayList<IStatefulPO> statefulOperators = new ArrayList<IStatefulPO>();
		for(int queryID : installedQueries) {
			statefulOperators.addAll(getStatefulOperatorList(queryID));
		}
		//TODO Check for right operator type!
		MovingStateManager.getInstance().addReceiver(status.getMasterPeer().toString(), pipeID);
		status.getMessageDispatcher().sendInititiateStateCopyAck(status.getMasterPeer(), status.getLbProcessId(), pipeID);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<IStatefulPO> traverseGraphAndFindStatefulOperators(IPhysicalOperator root, List<IStatefulPO> knownOperators) {
		
		if(root instanceof IStatefulPO && !knownOperators.contains(root)) {
			LOG.debug((" + Found stateful Op: " +  root.getName()));
			knownOperators.add((IStatefulPO)root);
		}
		if(root instanceof ISink) {
			return knownOperators;
		}
		if(root instanceof ISource) {
			ISource rootAsSource = (ISource) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>)rootAsSource.getSubscriptions()) {
				knownOperators = traverseGraphAndFindStatefulOperators((IPhysicalOperator)subscription.getTarget(),knownOperators);
			}
			return knownOperators;
		}
		if(root instanceof IPipe) {
			IPipe rootAsSource = (IPipe) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>)rootAsSource.getSubscriptions()) {
				knownOperators = traverseGraphAndFindStatefulOperators((IPhysicalOperator)subscription.getTarget(),knownOperators);
			}
			return knownOperators;
		}
		return knownOperators;
	}
	
	/**
	 * Sends abort Message to all Peers involved in LoadBalancing Process
	 * 
	 * @param status
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
			status.getMessageDispatcher().sendAbortInstruction(peer,
					deliveryFailedListener);
		}
	}
	
	/***
	 * Starts buffering of a jxta Sender
	 * @param pipeID PipeID of JxtaSender
	 * @throws LoadBalancingException 
	 */
	@SuppressWarnings("rawtypes")
	public static void startBuffering(String pipeID) throws LoadBalancingException {
		IPhysicalOperator operator = LoadBalancingHelper.getPhysicalJxtaOperator(true, pipeID);
		if(operator==null) {
			throw new LoadBalancingException("No Sender with pipeID " + pipeID + " found.");
		}
		JxtaSenderPO sender = (JxtaSenderPO)operator;
		for(Object subscription : sender.getSubscribedToSource()) {
			if(subscription instanceof ControllablePhysicalSubscription) {
				ControllablePhysicalSubscription controllableSubscription = (ControllablePhysicalSubscription)subscription;
				controllableSubscription.suspend();
			}
			else {
				throw new LoadBalancingException("At least one subscription is not controllable.");
			}
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public static void setNewPeerId(String pipeID, String peerID, boolean isSender) throws LoadBalancingException {
		IPhysicalOperator operator = LoadBalancingHelper.getPhysicalJxtaOperator(isSender, pipeID);
		if(operator==null) {
			throw new LoadBalancingException("No Sender with pipeID " + pipeID + " found.");
		}
		if(operator instanceof JxtaSenderPO) {
			LoadBalancingHelper.setNewPeerID((JxtaSenderPO)operator, peerID);
		}
		if(operator instanceof JxtaReceiverPO) {
			LoadBalancingHelper.setNewPeerID((JxtaReceiverPO)operator, peerID);
		}
	}
	
	
	/***
	 * Stops buffering of a jxta Sender
	 * @param pipeID PipeID of JxtaSender
	 * @throws LoadBalancingException 
	 */
	@SuppressWarnings("rawtypes")
	public static void stopBuffering(String pipeID) throws LoadBalancingException {
		IPhysicalOperator operator = LoadBalancingHelper.getPhysicalJxtaOperator(true, pipeID);
		if(operator==null) {
			throw new LoadBalancingException("No Sender with pipeID " + pipeID + " found.");
		}
		JxtaSenderPO sender = (JxtaSenderPO)operator;
		for(Object subscription : sender.getSubscribedToSource()) {
			if(subscription instanceof ControllablePhysicalSubscription) {
				ControllablePhysicalSubscription controllableSubscription = (ControllablePhysicalSubscription)subscription;
				controllableSubscription.resume();
			}
			else {
				throw new LoadBalancingException("At least one subscription is not controllable.");
			}
		}
		
	}
	
	
	
	/**
	 * Send Message to all incoming Peers to add a duplicate
	 * connection
	 * 
	 * @param status
	 *            Status
	 */
	public static void notifyDownstreamPeers(MovingStateMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = MovingStateCommunicatorImpl.getInstance();
		
		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		HashMap<String, String> replacedPipes = status.getReplacedPipes();
		MovingStateMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		HashMap<String,PeerID> peersForPipe = new HashMap<String,PeerID>();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				
				String newPipe = sender.getPipeID();
				String oldPipe = replacedPipes.get(sender.getPipeID());
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(sender.getPeerID());
				peersForPipe.put(newPipe,destinationPeer);
				
				dispatcher.sendReplaceReceiverMessage(destinationPeer, volunteeringPeer, oldPipe, deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);

	}
	
	/**
	 * Send Message to all outing Peers to add a duplicate
	 * connection
	 * 
	 * @param status
	 *            Status
	 */
	public static void notifyUpstreamPeers(MovingStateMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = MovingStateCommunicatorImpl.getInstance();
		
		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		HashMap<String, String> replacedPipes = status.getReplacedPipes();
		MovingStateMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		HashMap<String,PeerID> peersForPipe = status.getPeersForPipe();
		if(peersForPipe==null) {
			peersForPipe = new HashMap<String,PeerID>();
		}

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				
				String newPipe = receiver.getPipeID();
				String oldPipe = replacedPipes.get(receiver.getPipeID());
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(receiver.getPeerID());
				peersForPipe.put(newPipe,destinationPeer);
				
				dispatcher.sendInstallBufferAndReplaceSenderMessage(destinationPeer, volunteeringPeer, oldPipe, deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);

	}
	
	/**
	 * Relinks a logical Query Part to a new peer.
	 * @param modifiedPart
	 * @param status
	 * @return
	 */
	public static HashMap<String, String> relinkQueryPart(ILogicalQueryPart modifiedPart,MovingStateMasterStatus status) {
		
		IP2PNetworkManager p2pNetworkManager = ActiveLoadBalancingActivator.getP2pNetworkManager();
		
		LoadBalancingHelper.removeTopAOs(modifiedPart);
		
		Map<ILogicalOperator, Collection<IncomingConnection>> incomingConnections = LoadBalancingHelper
				.stripJxtaReceivers(modifiedPart);
		Map<ILogicalOperator, Collection<OutgoingConnection>> outgoingConnections = LoadBalancingHelper
				.stripJxtaSenders(modifiedPart);
		
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(modifiedPart);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(modifiedPart);

		HashMap<String, String> replacedPipes = new HashMap<String, String>();
		ArrayList<String> senderPipes = new ArrayList<String>();

		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<IncomingConnection> connections = incomingConnections
						.get(relativeSource);
				for (IncomingConnection connection : connections) {

					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();
					
					JxtaReceiverAO receiver = LoadBalancingHelper.createReceiverAO(connection,
							newPipeID);

					replacedPipes.put(newPipeID, connection.oldPipeID);
					modifiedPart.addOperator(receiver);
					
				}
			}
		}
		
		for (ILogicalOperator relativeSink : relativeSinks) {
			if (outgoingConnections.containsKey(relativeSink)) {
				Collection<OutgoingConnection> connections = outgoingConnections
						.get(relativeSink);
				for (OutgoingConnection connection : connections) {
					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();

					replacedPipes.put(newPipeID, connection.oldPipeID);
					
					JxtaSenderAO sender = LoadBalancingHelper.createSenderAO(connection, newPipeID);
					modifiedPart.addOperator(sender);
					senderPipes.add(connection.oldPipeID);
				}
			}
		}
		status.setPipesToSync(senderPipes);
		LOG.debug("Pipes to Sync (later):" );
		for(String pipe : senderPipes) {
			LOG.debug(pipe);
		}
		status.setModifiedPart(modifiedPart);
		status.setReplacedPipes(replacedPipes);
		return replacedPipes;
	}
}
