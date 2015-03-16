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

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
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
	
	
	/**
	 * Relinks a logical Query Part to a new peer.
	 * @param modifiedPart
	 * @param status
	 * @return
	 */
	public static HashMap<String, String> relinkQueryPartWithNewPipes(ILogicalQueryPart modifiedPart,MovingStateMasterStatus status) {
		
		IP2PNetworkManager p2pNetworkManager = OsgiServiceManager.getP2pNetworkManager();
		
		LoadBalancingHelper.removeTopAOs(modifiedPart);
		
		Map<ILogicalOperator, Collection<UpstreamConnection>> incomingConnections = LoadBalancingHelper
				.stripJxtaReceivers(modifiedPart);
		Map<ILogicalOperator, Collection<DownstreamConnection>> outgoingConnections = LoadBalancingHelper
				.stripJxtaSenders(modifiedPart);
		
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(modifiedPart);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(modifiedPart);

		HashMap<String, String> replacedPipes = new HashMap<String, String>();
		ArrayList<String> senderPipes = new ArrayList<String>();

		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<UpstreamConnection> connections = incomingConnections
						.get(relativeSource);
				for (UpstreamConnection connection : connections) {

					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();
					
					LOG.debug("Replacing PIPE {} with {}",connection.oldPipeID,newPipeID);
					
					JxtaReceiverAO receiver = LoadBalancingHelper.createReceiverAO(connection,
							newPipeID);

					replacedPipes.put(newPipeID, connection.oldPipeID);
					modifiedPart.addOperator(receiver);
					
				}
			}
		}
		
		for (ILogicalOperator relativeSink : relativeSinks) {
			if (outgoingConnections.containsKey(relativeSink)) {
				Collection<DownstreamConnection> connections = outgoingConnections
						.get(relativeSink);
				for (DownstreamConnection connection : connections) {
					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();

					replacedPipes.put(newPipeID, connection.oldPipeID);
					
					LOG.debug("Replacing PIPE {} with {}",connection.oldPipeID,newPipeID);
					
					
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
			List<IStatefulPO> foundOps = traverseGraphAndFindStatefulOperators(root,knownOperators);
			for(IStatefulPO op : foundOps) {
				if(!statefulPOs.contains(op)) {
					statefulPOs.add(op);
				}
			}
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
			List<IStatefulPO> statefulOps = getStatefulOperatorList(queryId);
			for(IStatefulPO op : statefulOps) {
				if(!statefulList.contains(op)) {
					statefulList.add(op);
				}
			}
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
	public static synchronized void initiateStateCopy(MovingStateMasterStatus status) {
		
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
	
	
	/**
	 * Adds Information about changed Operators to status
	 * @param pipeID Pipe ID 
	 * @param status status that stores information
	 * @param isSender is Operator JxtaSender? 
	 */
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
						cSub.suspend();
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
	
	/**
	 * Stops Buffering of a pipe before operator
	 * @param operator operator which follows pipe-to-resume
	 * @throws LoadBalancingException
	 */
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
						cSub.resume();
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
	 * Set Receiver or Sender to new PeerID and PipeID by cloning it.
	 * @param status
	 * @param oldPipeID
	 * @param newPipeId
	 * @param newPeerID
	 * @param isSender
	 * @throws LoadBalancingException
	 * @throws DataTransmissionException
	 */
	public static void setNewPipe(MovingStateSlaveStatus status,String oldPipeID, String newPipeId,String newPeerID,
			boolean isSender) throws LoadBalancingException, DataTransmissionException {
		IPhysicalOperator operator = LoadBalancingHelper
				.getPhysicalJxtaOperator(isSender, oldPipeID);
		
		
		ILogicalOperator logicalOp = LoadBalancingHelper.getLogicalJxtaOperator(isSender, oldPipeID);
		if (operator == null) {
			throw new LoadBalancingException("No Sender with pipeID found:"+ oldPipeID);
		}
		if (operator instanceof JxtaSenderPO) {
			findAndCopyLocalJxtaOperator(status,true,newPeerID,oldPipeID,newPipeId);
			if(logicalOp!=null) {
				JxtaSenderAO senderAO = (JxtaSenderAO) logicalOp;
				senderAO.setPeerID(newPeerID);
				senderAO.setPipeID(newPipeId);
			}
			else {
				LOG.warn("No according logical operator to existing pyhsical Operator with pipe ID " + oldPipeID);
			}
		}
		
		if (operator instanceof JxtaReceiverPO) {

			findAndCopyLocalJxtaOperator(status,false,newPeerID,oldPipeID,newPipeId);
			if(logicalOp!=null) {
				JxtaReceiverAO receiverAO = (JxtaReceiverAO) logicalOp;
				receiverAO.setPeerID(newPeerID);
				receiverAO.setPipeID(newPipeId);
			}
		}
	}
	
	
	public static void findAndCopyLocalJxtaOperator(MovingStateSlaveStatus status,
			boolean isSender, String newPeerId, String oldPipeId,
			String newPipeId) throws DataTransmissionException, LoadBalancingException{
		
			ILogicalOperator operator = LoadBalancingHelper.getLogicalJxtaOperator(isSender, oldPipeId);
			
			
			if(operator==null) {
				LOG.error("No operator with pipe ID " + oldPipeId + " found.");
			}
			
			if (isSender) {
					replaceSender(newPeerId, oldPipeId, newPipeId, operator,status);
	
			} 
			else {
					replaceReceiver(newPeerId, oldPipeId, newPipeId, operator,status);
				}
			

			
		}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void replaceReceiver(String newPeerId, String oldPipeId,
			String newPipeId, ILogicalOperator logicalOriginal,MovingStateSlaveStatus status)
			throws DataTransmissionException, LoadBalancingException {
		JxtaReceiverAO logicalReceiver = (JxtaReceiverAO) logicalOriginal;
		JxtaReceiverAO copy = (JxtaReceiverAO) logicalReceiver.clone();

		copy.setPipeID(newPipeId);
		copy.setPeerID(newPeerId);
		copy.setSchema(logicalReceiver.getSchema());


		JxtaReceiverPO physicalOriginal = (JxtaReceiverPO) LoadBalancingHelper.getPhysicalJxtaOperator(false, oldPipeId);
		if(physicalOriginal==null) {
			throw new LoadBalancingException("No physical receiver with Pipe "+oldPipeId+" found.");
		}

		copy.setSchemaName(physicalOriginal.getOutputSchema().getURI());

		JxtaReceiverPO physicalCopy = new JxtaReceiverPO(copy);

		physicalCopy.setOutputSchema(physicalOriginal.getOutputSchema());
		physicalCopy.setName(physicalOriginal.getName());
		physicalCopy.addOwner(physicalOriginal.getOwner());

		replaceReceiver(physicalOriginal,physicalCopy);
		status.addReplacedOperator(physicalCopy,physicalOriginal);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void replaceSender(String newPeerId, String oldPipeId,
			String newPipeId, ILogicalOperator logicalOriginal,MovingStateSlaveStatus status)
			throws DataTransmissionException, LoadBalancingException {
		JxtaSenderAO logicalSender = (JxtaSenderAO) logicalOriginal;
		JxtaSenderAO copy = (JxtaSenderAO) logicalSender.clone();
		
		copy.setPipeID(newPipeId);
		copy.setPeerID(newPeerId);

		JxtaSenderPO physicalCopy = new JxtaSenderPO(copy);
		JxtaSenderPO physicalOriginal = (JxtaSenderPO) LoadBalancingHelper
				.getPhysicalJxtaOperator(true, oldPipeId);
		
				
		if(physicalOriginal==null) {
			throw new LoadBalancingException("No physical Sender with Pipe ID " + oldPipeId + " found.");
		}
		
		physicalCopy.setOutputSchema(physicalOriginal.getOutputSchema());
		physicalCopy.setName(physicalOriginal.getName());
		physicalCopy.addOwner(physicalOriginal.getOwner());
		
		replaceSender(physicalOriginal,physicalCopy);
		
		status.addReplacedOperator(physicalCopy,physicalOriginal);
	}
		
		
		

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void replaceReceiver(JxtaReceiverPO original, JxtaReceiverPO replacement) throws LoadBalancingException{

		List<AbstractPhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = original.getSubscriptions();

		ArrayList<IPhysicalOperator> emptyCallPath = new ArrayList<IPhysicalOperator>();
		
		for (AbstractPhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {
			
			ISink sink = subscription.getTarget();

			original.unsubscribeSink(subscription);
			
			replacement.subscribeSink(sink,
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(),
					subscription.getSchema().clone(), true,
					0);


			Collection<AbstractPhysicalSubscription> revSubscriptions = sink.getSubscribedToSource();
			for (AbstractPhysicalSubscription subscr : revSubscriptions) {
				if(subscr.getTarget().equals(original)) {
					sink.unsubscribeFromSource(subscr);
					sink.subscribeToSource(replacement, subscr.getSinkInPort(), subscr.getSourceOutPort(), subscr.getSchema().clone());
				}
			}
			
			replacement.open(sink, subscription.getSourceOutPort(), subscription.getSinkInPort(), emptyCallPath,original.getOwner());
			
		}
		
		
		Optional<Integer> queryId = LoadBalancingHelper.getQueryForRoot(original);
		if(queryId.isPresent()) {
			//OsgiServiceManager.getExecutor().getExecutionPlan().getQueryById(queryId.get()).replaceOperator(original, replacement);
		}
		original.getTransmission().close();
	
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void replaceSender(JxtaSenderPO original, JxtaSenderPO replacement) throws LoadBalancingException {

		AbstractPhysicalSubscription subscription = original.getSubscribedToSource(0);
		
		if (subscription.getTarget() instanceof AbstractSource) {
			
			AbstractSource source = (AbstractSource)subscription.getTarget();
			AbstractPhysicalSubscription subscr=null;
			
			
			for(Object sub : source.getSubscriptions()) {
				if(sub instanceof AbstractPhysicalSubscription) {
					if (((AbstractPhysicalSubscription)sub).getTarget().equals(original)) {
						subscr = (AbstractPhysicalSubscription)sub;
					}
				}
			}
			
			if(subscr==null) {
				throw new LoadBalancingException("No Subscription to Sender found.");
			}
			
			if(subscr instanceof ControllablePhysicalSubscription) {
				//Just set new Target on Subscription to keep buffer.
				((ControllablePhysicalSubscription)subscr).setTarget(replacement);
				
				replacement.subscribeToSource((ISource) subscription.getTarget(), subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema().clone());
				original.unsubscribeFromAllSources();
				

				Optional<Integer> queryId = LoadBalancingHelper.getQueryForRoot(original);
				if(queryId.isPresent()) {
					//OsgiServiceManager.getExecutor().getExecutionPlan().getQueryById(queryId.get()).replaceOperator(original, replacement);
					OsgiServiceManager.getExecutor().getExecutionPlan().getQueryById(queryId.get()).replaceRoot(original, replacement);
				}
			}
			else {
				LOG.error("Subscription not controllable.");
				throw new LoadBalancingException("Subscription to sender not controllable.");
			}
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
	public static synchronized void stopBuffering(String pipeID)
			throws LoadBalancingException {
	
		LOG.debug("Resuming Sender with pipe " + pipeID);
		
		IPhysicalOperator operator = LoadBalancingHelper.getPhysicalJxtaOperator(true, pipeID);
		
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

		ArrayList<PeerID> downstreamPeers = new ArrayList<PeerID>();
		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;

				String newPipe  = sender.getPipeID();
				String pipe = status.getReplacedPipes().get(newPipe);
				
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(sender
						.getPeerID());
				if (!downstreamPeers .contains(destinationPeer)) {
					downstreamPeers.add(destinationPeer);
				}
				peersForPipe.put(pipe, destinationPeer);

				dispatcher.sendReplaceReceiverMessage(destinationPeer,
						volunteeringPeer, pipe, newPipe,deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);
		status.setDownstramPeers(downstreamPeers);
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
				String newPipe  = receiver.getPipeID();
				String pipe = status.getReplacedPipes().get(newPipe);
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(receiver
						.getPeerID());
				if (!upstreamPeers.contains(destinationPeer))
					upstreamPeers.add(destinationPeer);
				peersForPipe.put(pipe, destinationPeer);

				dispatcher.sendInstallBufferAndReplaceSenderMessage(
						destinationPeer, volunteeringPeer, pipe, newPipe,
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

}
