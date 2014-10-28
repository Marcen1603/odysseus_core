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
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
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
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingBufferPO;

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
			for (PhysicalSubscription subscription : (Collection<PhysicalSubscription>)rootAsSource.getSubscriptions()) {
				knownOperators = traverseGraphAndFindStatefulOperators((IPhysicalOperator)subscription.getTarget(),knownOperators);
			}
			return knownOperators;
		}
		if(root instanceof IPipe) {
			IPipe rootAsSource = (IPipe) root;
			for (PhysicalSubscription subscription : (Collection<PhysicalSubscription>)rootAsSource.getSubscriptions()) {
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
	
	@SuppressWarnings("rawtypes")
	public static void stopBuffering(MovingStateSlaveStatus status) {
		for(LoadBalancingBufferPO buffer : status.getInstalledBuffers()) {
			buffer.stopBuffering();
		}
	}
	
	
	/***
	 * Adds a buffer for each incoming port of a jxta Sender
	 * @param pipeID PipeID of JxtaSender
	 * @return List of added Buffers.
	 */
	@SuppressWarnings("rawtypes")
	public static List<LoadBalancingBufferPO> insertBuffer(String pipeID) {
		IPhysicalOperator operator = LoadBalancingHelper.getPhysicalJxtaOperator(true, pipeID);
		if(operator==null) {
			LOG.error("No Sender with PipeID " + pipeID+ " found.");
			//TODO Error
			return null;
		}
		JxtaSenderPO sender = (JxtaSenderPO)operator;
		
		ArrayList<LoadBalancingBufferPO> addedBuffers = new ArrayList<LoadBalancingBufferPO>();
		
		for(int i=0;i<sender.getInputPortCount();i++) {
			LoadBalancingBufferPO<IStreamObject<ITimeInterval>> buffer = new LoadBalancingBufferPO<IStreamObject<ITimeInterval>>();
			LoadBalancingHelper.insertOperatorBefore(sender, buffer,i);
			addedBuffers.add(buffer);
		}
		
		return addedBuffers;
		
	}
	
	@SuppressWarnings("rawtypes")
	public static void removeBuffer(LoadBalancingBufferPO buffer) {
		LoadBalancingHelper.removeOperatorFromStream(buffer);
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
				
				dispatcher.sendReplaceReceiverMessage(destinationPeer, volunteeringPeer, oldPipe, newPipe,deliveryFailedListener);
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

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				
				String newPipe = receiver.getPipeID();
				String oldPipe = replacedPipes.get(receiver.getPipeID());
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(receiver.getPeerID());
				peersForPipe.put(newPipe,destinationPeer);
				
				dispatcher.sendInstallBufferAndReplaceSenderMessage(destinationPeer, volunteeringPeer, oldPipe, newPipe, deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);

	}
	
	@SuppressWarnings("rawtypes")
	public static LoadBalancingBufferPO installBufferBeforeSender(JxtaSenderPO sender) {
		LoadBalancingBufferPO buffer = new LoadBalancingBufferPO();
		LoadBalancingHelper.insertOperatorBefore(sender, buffer, 0);
		return buffer;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void attachSecondSenderToBufferPO (LoadBalancingBufferPO buffer, JxtaSenderPO sender) {
		buffer.subscribeSink(sender, 0, 1, buffer.getOutputSchema());
		//TODO open?
	}
	
	@SuppressWarnings("rawtypes")
	public static void addBufferAndCopySender(MovingStateSlaveStatus status, String newPeerId, String oldPipeId, String newPipeId) throws LoadBalancingException, DataTransmissionException {
		JxtaSenderAO senderAO = (JxtaSenderAO)LoadBalancingHelper.getLogicalJxtaOperator(true, oldPipeId);
		
		if(senderAO==null) {
			LOG.error("No sender with pipe ID " + oldPipeId + " found.");
			throw new LoadBalancingException("No sender with pipe ID " + oldPipeId + " found.");
		}
		
		JxtaSenderAO logicalSenderCopy = (JxtaSenderAO) senderAO.clone();
		logicalSenderCopy.setPipeID(newPipeId);
		logicalSenderCopy.setPeerID(newPeerId);

		JxtaSenderPO physicalSenderCopy = new JxtaSenderPO(logicalSenderCopy);
		JxtaSenderPO physicalOriginal = (JxtaSenderPO) LoadBalancingHelper
				.getPhysicalJxtaOperator(true, oldPipeId);
		physicalSenderCopy
				.setOutputSchema(physicalOriginal.getOutputSchema());

		LoadBalancingBufferPO buffer = installBufferBeforeSender(physicalOriginal);
		buffer.startBuffering();
		LOG.debug("Installed Buffer.");
		attachSecondSenderToBufferPO(buffer,physicalSenderCopy);
		LOG.debug("Installed Buffer and additional Sender with PeerID " + physicalSenderCopy.getPeerIDString() + " and PipeID " + physicalSenderCopy.getPipeIDString());
		status.storeBuffer(buffer);
		status.storeReplacedSender(newPipeId, physicalOriginal);
	}
	
	
	/**
	 * Finds a JxtaOperator by pipeId, creates a physical Copy and connects both
	 * Operators to the stream.
	 * 
	 * @param isSender
	 *            determines if Operator to look for is a Sender or a receiver.
	 * @param newPeerId
	 *            PeerId which the new Operator is connected to.
	 * @param oldPipeId
	 *            Pipe Id of old Operator
	 * @param newPipeId
	 *            Pipe Id of new Operator.
	 * @throws DataTransmissionException
	 * @Param lbProcessId LoadBalancingProcessId.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void findAndReplaceReceiver(MovingStateSlaveStatus status, String newPeerId, String oldPipeId,
			String newPipeId) throws DataTransmissionException, LoadBalancingException{
		
		JxtaReceiverAO receiver = (JxtaReceiverAO)LoadBalancingHelper.getLogicalJxtaOperator(false, oldPipeId);
		
		if(receiver==null) {
			LOG.error("No receiver with pipe ID " + oldPipeId + " found.");
			throw new LoadBalancingException("No receiver with pipe ID " + oldPipeId + " found.");
		}
		JxtaReceiverAO logicalReceiverCopy = (JxtaReceiverAO) receiver.clone();
		logicalReceiverCopy.setPipeID(newPipeId);
		logicalReceiverCopy.setPeerID(newPeerId);
		logicalReceiverCopy.setSchema(receiver.getSchema());
		
		JxtaReceiverPO physicalOriginal = (JxtaReceiverPO) LoadBalancingHelper
				.getPhysicalJxtaOperator(false, oldPipeId);
		logicalReceiverCopy.setSchemaName(physicalOriginal.getOutputSchema().getURI());
				
		JxtaReceiverPO physicalCopy = new JxtaReceiverPO(logicalReceiverCopy);
				
		physicalCopy
		.setOutputSchema(physicalOriginal.getOutputSchema());

		List<PhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = physicalOriginal
						.getSubscriptions();

				for (PhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {

					physicalOriginal.unsubscribeSink(subscription);
					physicalCopy.subscribeSink(subscription.getTarget(),
							subscription.getSinkInPort(),
							subscription.getSourceOutPort(),
							subscription.getSchema(), true,
							subscription.getOpenCalls());
					
					ArrayList<IPhysicalOperator> emptyCallPath = new ArrayList<IPhysicalOperator>();
					physicalOriginal.open(subscription.getTarget(), 0, 0, emptyCallPath,
							physicalOriginal.getOwner());
				}

				LOG.debug("Installed additional Receiver with PeerID " + physicalCopy.getPeerIDString() + " and PipeID " + physicalCopy.getPipeIDString());
				status.storeReplacedReceiver(newPipeId, physicalOriginal);
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
