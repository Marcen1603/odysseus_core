package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
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
	
	
	public static void sendState(String pipe, IStatefulPO operator) throws LoadBalancingException {
		
		MovingStateSender sender = MovingStateManager.getInstance().getSender(pipe);
		
		if(operator==null) {
			throw new LoadBalancingException("Stateful Operator cannot be null.");
		}
		
		if(sender==null) {
			throw new LoadBalancingException("No sender found for pipeID " + pipe);
		}
		
		LOG.debug("Sending state for " + operator.toString() + " on pipe " + pipe);
		Serializable state = operator.getState();
		sender.sendData(state);
		LOG.debug("Data sent.");
		
		
	}
	
	
	public static List<IStatefulPO> getStatefulOperatorList(int queryId) {
		
		IExecutor executor = ActiveLoadBalancingActivator.getExecutor();
		List<IPhysicalOperator> roots = executor.getPhysicalRoots(queryId, ActiveLoadBalancingActivator.getActiveSession());
		List<IStatefulPO> statefulPOs = new ArrayList<IStatefulPO>();
		for(IPhysicalOperator root : roots) {
			LOG.debug(("Found root for Query " + queryId +": " + root.getName()));
			List<IStatefulPO> knownOperators = new ArrayList<IStatefulPO>();
			statefulPOs.addAll(traverseGraphAndFindStatefulOperators(root, knownOperators));
		}
		return statefulPOs;
		
	}
	
	public static List<IStatefulPO> getStatefulOperatorList(Collection<Integer> installedQueries) {
		List<IStatefulPO> statefulList = new ArrayList<IStatefulPO>();
		for(int queryId : installedQueries) {
			statefulList.addAll(getStatefulOperatorList(queryId));
		}
		return statefulList;
	}
	
	public static void initiateStateCopy(MovingStateMasterStatus status) {
		
		MovingStateCommunicatorImpl communicator = MovingStateCommunicatorImpl.getInstance();
		MovingStateMessageDispatcher dispatcher = status.getMessageDispatcher();
		
		List<IStatefulPO> statefulOperators = getStatefulOperatorList(status.getLogicalQuery());
		LOG.debug("Got list of "+statefulOperators.size()+" Stateful Operators.");
		for(IStatefulPO statefulPO : statefulOperators) {
			
			//Installing a new State Sender
			String stateSenderPipe = MovingStateManager.getInstance().addSender(status.getVolunteeringPeer().toString());
			status.addSender(stateSenderPipe, statefulPO);
			
			dispatcher.sendInititateStateCopy(status.getVolunteeringPeer(), communicator, stateSenderPipe, statefulPO.getClass().toString(), statefulOperators.indexOf(statefulPO));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<IStatefulPO> traverseGraphAndFindStatefulOperators(IPhysicalOperator root, List<IStatefulPO> knownOperators) {
		
		if(root instanceof IStatefulPO && !knownOperators.contains(root)) {
			LOG.debug((" + Found stateful Op: " +  root.getName()));
			knownOperators.add((IStatefulPO)root);
		} else {
			LOG.debug(" - Op " + root.getName() + " not added.");
		}
		if(root instanceof IPipe) {
			LOG.debug("   recognized Operator as Pipe.");
			IPipe rootAsSource = (IPipe) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>)rootAsSource.getSubscribedToSource()) {
				knownOperators = traverseGraphAndFindStatefulOperators((IPhysicalOperator)subscription.getTarget(),knownOperators);
			}
			return knownOperators;
		}
		
		if(root instanceof ISource) {
			LOG.debug("   recognized Operator as Source.");
			return knownOperators;
		}
		if(root instanceof ISink) {
			LOG.debug("   recognized Operator as Sink.");
			ISink rootAsSink = (ISink) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>)rootAsSink.getSubscribedToSource()) {
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
	
	@SuppressWarnings("rawtypes")
	public static void openReceiver(JxtaReceiverPO receiver) {
	
		for(Object ownerObject : receiver.getOwner()) {
			if(ownerObject instanceof IOperatorOwner) {
				IOperatorOwner owner = (IOperatorOwner) ownerObject;
				receiver.open(owner);
			}
			
		}
	}
	
	
	/***
	 * Stops buffering of a jxta Sender
	 * @param pipeID PipeID of JxtaSender
	 * @throws LoadBalancingException 
	 */
	@SuppressWarnings("rawtypes")
	public static void stopBuffering(String pipeID) throws LoadBalancingException {
		LOG.debug("Resuming Sender with pipe " + pipeID);
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
		MovingStateMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		HashMap<String,PeerID> peersForPipe = new HashMap<String,PeerID>();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				
				String pipe = sender.getPipeID();
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(sender.getPeerID());
				peersForPipe.put(pipe,destinationPeer);
				
				dispatcher.sendReplaceReceiverMessage(destinationPeer, volunteeringPeer, pipe, deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);

	}
	
	public static void sendStopBufferingToUpstreamPeers(MovingStateMasterStatus status) {
		for(PeerID peer : status.getUpstreamPeers()) {
			status.getMessageDispatcher().sendStopBuffering(peer, MovingStateCommunicatorImpl.getInstance());
		}
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
		ArrayList<PeerID> upstreamPeers = new ArrayList<PeerID>();
		
		MovingStateMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		HashMap<String,PeerID> peersForPipe = status.getPeersForPipe();
		if(peersForPipe==null) {
			peersForPipe = new HashMap<String,PeerID>();
		}

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				
				String pipe = receiver.getPipeID();
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(receiver.getPeerID());
				if(!upstreamPeers.contains(destinationPeer))
					upstreamPeers.add(destinationPeer);
				peersForPipe.put(pipe,destinationPeer);
				
				dispatcher.sendInstallBufferAndReplaceSenderMessage(destinationPeer, volunteeringPeer, pipe, deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);
		status.setUpstreamPeers(upstreamPeers);

	}
	
	/**
	 * Relinks a logical Query Part to a new peer.
	 * @param modifiedPart
	 * @param status
	 * @return
	 */
	public static void relinkQueryPart(ILogicalQueryPart modifiedPart,MovingStateMasterStatus status) {
		
		LoadBalancingHelper.removeTopAOs(modifiedPart);
		
		Map<ILogicalOperator, Collection<IncomingConnection>> incomingConnections = LoadBalancingHelper
				.stripJxtaReceivers(modifiedPart);
		Map<ILogicalOperator, Collection<OutgoingConnection>> outgoingConnections = LoadBalancingHelper
				.stripJxtaSenders(modifiedPart);
		
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(modifiedPart);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(modifiedPart);

		ArrayList<String> senderPipes = new ArrayList<String>();

		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<IncomingConnection> connections = incomingConnections
						.get(relativeSource);
				for (IncomingConnection connection : connections) {
					JxtaReceiverAO receiver = LoadBalancingHelper.createReceiverAO(connection,
							connection.oldPipeID);
					modifiedPart.addOperator(receiver);
					
				}
			}
		}
		
		for (ILogicalOperator relativeSink : relativeSinks) {
			if (outgoingConnections.containsKey(relativeSink)) {
				Collection<OutgoingConnection> connections = outgoingConnections
						.get(relativeSink);
				for (OutgoingConnection connection : connections) {
					JxtaSenderAO sender = LoadBalancingHelper.createSenderAO(connection, connection.oldPipeID);
					
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
	}

	public static boolean compareStatefulOperator(int operatorIndex,
			Collection<Integer> installedQueries, String operatorType) {
		List<IStatefulPO> statefulList = getStatefulOperatorList(installedQueries);
		IStatefulPO operator = statefulList.get(operatorIndex);
		if(operator!=null) {
			if(operator.getClass().toString().equals(operatorType))
				return true;
		}
		return false;
	}

	public static IStatefulPO getStatefulPO(int operatorIndex,
			Collection<Integer> installedQueries) {
		List<IStatefulPO> statefulList = getStatefulOperatorList(installedQueries);
		return statefulList.get(operatorIndex);
	
	}

	public static void injectState(MovingStateSlaveStatus status, String pipeId) {
		try {
			IStatefulPO op = status.getStatefulPOforPipe(pipeId);
			MovingStateManager.getInstance().getReceiver(pipeId).injectState(op);
			status.getMessageDispatcher().sendCopyStateFinished(status.getMasterPeer(), pipeId);
		}
		catch(Exception e) {
			//TODO Error Handling
		}
		
		
	}
}
