package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator;

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

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
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
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.ParallelTrackMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status.ParallelTrackSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingSynchronizerPO;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;

public class ParallelTrackHelper {

	private static final Logger LOG = LoggerFactory.getLogger(ParallelTrackHelper.class);

	/**
	 * Sends abort Message to all Peers involved in LoadBalancing Process
	 * 
	 * @param status
	 */
	public static void notifyInvolvedPeers(ParallelTrackMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = ParallelTrackCommunicatorImpl.getInstance();

		status.setPhase(ParallelTrackMasterStatus.LB_PHASES.FAILURE);
		// Get Distinct List of involved Peers:
		ArrayList<PeerID> involvedPeers = new ArrayList<PeerID>(new HashSet<PeerID>(status.getPeersForPipe().values()));
		involvedPeers.add(status.getVolunteeringPeer());
		for (PeerID peer : involvedPeers) {
			status.getMessageDispatcher().sendAbortInstruction(peer, deliveryFailedListener);
		}
	}

	/**
	 * Send Message to all incoming Peers to add a duplicate connection
	 * 
	 * @param status
	 *            Status
	 */
	public static void notifyDownstreamPeers(ParallelTrackMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = ParallelTrackCommunicatorImpl.getInstance();

		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		HashMap<String, String> replacedPipes = status.getReplacedPipes();
		ParallelTrackMessageDispatcher dispatcher = status.getMessageDispatcher();

		HashMap<String, PeerID> peersForPipe = new HashMap<String, PeerID>();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;

				String newPipe = sender.getPipeID();
				String oldPipe = replacedPipes.get(sender.getPipeID());
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(sender.getPeerID());
				peersForPipe.put(newPipe, destinationPeer);
				dispatcher.sendCopyOperator(false, destinationPeer, oldPipe, newPipe, volunteeringPeer, deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);

	}

	/**
	 * Send Message to all outing Peers to add a duplicate connection
	 * 
	 * @param status
	 *            Status
	 */
	public static void notifyUpstreamPeers(ParallelTrackMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = ParallelTrackCommunicatorImpl.getInstance();

		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		HashMap<String, String> replacedPipes = status.getReplacedPipes();
		ParallelTrackMessageDispatcher dispatcher = status.getMessageDispatcher();

		HashMap<String, PeerID> peersForPipe = status.getPeersForPipe();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;

				String newPipe = receiver.getPipeID();
				String oldPipe = replacedPipes.get(receiver.getPipeID());
				PeerID destinationPeer = LoadBalancingHelper.toPeerID(receiver.getPeerID());
				peersForPipe.put(newPipe, destinationPeer);

				dispatcher.sendCopyOperator(true, destinationPeer, oldPipe, newPipe, volunteeringPeer, deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);

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
	public static void findAndCopyLocalJxtaOperator(ParallelTrackSlaveStatus status, boolean isSender, String newPeerId, String oldPipeId, String newPipeId) throws DataTransmissionException, LoadBalancingException {

		ParallelTrackMessageDispatcher dispatcher = status.getMessageDispatcher();

		ILogicalOperator operator = LoadBalancingHelper.getLogicalJxtaOperator(isSender, oldPipeId);

		if (operator == null) {
			LOG.error("No operator with pipe ID " + oldPipeId + " found.");
			throw new LoadBalancingException("No operator with pipe ID " + oldPipeId + " found.");
		}
		if (isSender) {
				JxtaSenderAO logicalSender = (JxtaSenderAO) operator;
				JxtaSenderAO copy = (JxtaSenderAO) logicalSender.clone();

				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);

				JxtaSenderPO physicalCopy = new JxtaSenderPO(copy);
				JxtaSenderPO physicalOriginal = (JxtaSenderPO) LoadBalancingHelper.getPhysicalJxtaOperator(isSender, oldPipeId);

				if (physicalOriginal == null) {
					throw new LoadBalancingException("No physical Op with isSender=" + isSender + " and Pipe ID " + oldPipeId + " found.");
				}

				physicalCopy.setOutputSchema(physicalOriginal.getOutputSchema().clone());
				physicalCopy.addOwner(physicalOriginal.getOwner());

				AbstractPhysicalSubscription subscription = physicalOriginal.getSubscribedToSource(0);

				if (subscription.getTarget() instanceof AbstractSource) {
					ISource source = (ISource) subscription.getTarget();
					source.subscribeSink(physicalCopy, 0, subscription.getSourceOutPort(), subscription.getSchema().clone(), true, subscription.getOpenCalls());
					physicalCopy.subscribeToSource(source, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema().clone());
					
					for (IOperatorOwner owner : (List<IOperatorOwner>) physicalCopy.getOwner()) {
						physicalCopy.open(owner);
					}
					Optional<Integer> queryId = LoadBalancingHelper.getQueryForRoot(physicalOriginal);
					if (queryId.isPresent()) {
						OsgiServiceManager.getExecutor().getExecutionPlan().getQueryById(queryId.get()).replaceOperator(physicalOriginal, physicalCopy);
						OsgiServiceManager.getExecutor().getExecutionPlan().getQueryById(queryId.get()).replaceRoot(physicalOriginal, physicalCopy);
					}
					
					status.pushSenderToRemoveList(physicalOriginal);
				}

				LOG.debug("Installed additional Sender with PeerID " + physicalCopy.getPeerIDString() + " and PipeID " + physicalCopy.getPipeIDString());

			} else {
				JxtaReceiverAO logicalReceiver = (JxtaReceiverAO) operator;
				JxtaReceiverAO copy = (JxtaReceiverAO) logicalReceiver.clone();
				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);

				JxtaReceiverPO physicalOriginal = (JxtaReceiverPO) LoadBalancingHelper.getPhysicalJxtaOperator(isSender, oldPipeId);
				LOG.debug("Start Buffering.");
				physicalOriginal.startBuffering();
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					LOG.warn("Could not wait for Receiver.");
				}

				JxtaReceiverPO physicalCopy = new JxtaReceiverPO(copy);

				
				ArrayList<IPhysicalOperator> emptyCallPath = new ArrayList<IPhysicalOperator>();
				physicalCopy.addOwner(physicalOriginal.getOwner());

				LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>> synchronizer = new LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>>();

				LoadBalancingFinishedListener listener = new LoadBalancingFinishedListener(dispatcher, LoadBalancingHelper.toPeerID(physicalOriginal.getPeerIDString()), oldPipeId);
				synchronizer.addListener(listener);
				synchronizer.addOwner(physicalOriginal.getOwner());
				physicalOriginal.startBuffering();
				
				List<AbstractPhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = physicalOriginal.getSubscriptions();

				for (AbstractPhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {

					physicalOriginal.unsubscribeSink(subscription);
					synchronizer.subscribeSink(subscription.getTarget(), subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema(), true, subscription.getOpenCalls());
					
					ISink sink = subscription.getTarget();
					
					Collection<AbstractPhysicalSubscription> revSubscriptions = sink.getSubscribedToSource();
					for (AbstractPhysicalSubscription subscr : revSubscriptions) {
						if(subscr.getTarget().equals(physicalOriginal)) {
							sink.unsubscribeFromSource(subscr);
							sink.subscribeToSource(synchronizer, subscr.getSinkInPort(), subscr.getSourceOutPort(), subscr.getSchema().clone());
						}
					}
					synchronizer.open(sink, subscription.getSourceOutPort(), subscription.getSinkInPort(), null, synchronizer.getOwner());
					
				}

				synchronizer.startSynchronizing();
				
				
				physicalOriginal.subscribeSink(synchronizer, 0, 0, physicalOriginal.getOutputSchema().clone());
				synchronizer.subscribeToSource(physicalOriginal, 0, 0, physicalOriginal.getOutputSchema().clone());
				physicalOriginal.open(synchronizer, 0, 0, emptyCallPath, physicalOriginal.getOwner());

				physicalCopy.subscribeSink(synchronizer, 1, 0, physicalCopy.getOutputSchema().clone());
				synchronizer.subscribeToSource(physicalCopy, 1, 0, physicalOriginal.getOutputSchema().clone());
				physicalCopy.open(synchronizer, 0, 1, emptyCallPath, physicalCopy.getOwner());

				
				physicalOriginal.stopBuffering();
			}

	}
	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean removeDuplicateJxtaReceiver(String pipeID) {
		IPhysicalOperator operator = LoadBalancingHelper.getPhysicalJxtaOperator(false, pipeID);

		if (operator == null) {
			LOG.error("No Receiver with pipe ID {} found", pipeID);
			return false;
		}
		if (operator instanceof JxtaReceiverPO) {

			LOG.debug("Removing RECEIVER with pipe ID {}", pipeID);
			JxtaReceiverPO receiver = (JxtaReceiverPO) operator;

			AbstractPhysicalSubscription<?> receiverSubscription = (AbstractPhysicalSubscription) receiver
					.getSubscriptions().get(0);

			if (receiverSubscription.getTarget() instanceof LoadBalancingSynchronizerPO) {
				LoadBalancingSynchronizerPO sync = (LoadBalancingSynchronizerPO) receiverSubscription
						.getTarget();
				int port = receiverSubscription.getSinkInPort();
				int otherPort = (port + 1) % 2;

				JxtaReceiverPO otherReceiver = (JxtaReceiverPO) sync
						.getSubscribedToSource(otherPort).getTarget();

				LOG.debug("Start Buffering.");
				otherReceiver.startBuffering();
				LOG.debug("Waiting for Sync to process all tuples.");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					LOG.warn("Could not wait for Synchronizer.");
				}
				
				LOG.debug("Done.");

				otherReceiver.unsubscribeFromAllSinks();
				
				receiver.unsubscribeFromAllSinks();
				
				sync.unsubscribeFromAllSources();

				List<AbstractPhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = sync
						.getSubscriptions();
				
				

				for (AbstractPhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {

					sync.unsubscribeSink(subscription);

					otherReceiver.subscribeSink(subscription.getTarget(),
							subscription.getSinkInPort(), subscription
									.getSourceOutPort(), subscription
									.getSchema().clone(), true, subscription
									.getOpenCalls());

					subscription.getTarget().subscribeToSource(otherReceiver,
							subscription.getSinkInPort(),
							subscription.getSourceOutPort(),
							subscription.getSchema().clone());
				}
				LOG.debug("Stop Buffering.");
				otherReceiver.stopBuffering();
				receiver.getTransmission().close();

			}
		}
		return true;
	}

	/**
	 * Removes a duplicate Jxta Receiver or sender used in LoadBalancing. Called
	 * during abort and after sync.
	 * 
	 * @param pipeID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void removeDuplicateJxtaSender(String pipeID,
			ParallelTrackSlaveStatus status) {

		JxtaSenderPO sender = status.popSenderfromRemoveList(pipeID);

		if (sender == null) {
			LOG.error("No Sender with Pipe ID {} found.", pipeID);
			return;
		}
		LOG.debug("Removing SENDER with pipe ID {}", pipeID);

		List<ISubscription> subscriptions = sender.getSubscribedToSource();
		for (ISubscription subscr : subscriptions) {
			ISource source = ((ISource) subscr.getTarget());

			Collection<ISubscription> reverseSubscriptions = source
					.getSubscriptions();
			for (ISubscription revSubscr : reverseSubscriptions) {
				if (revSubscr.getTarget().equals(sender)) {
					source.unsubscribeSink(revSubscr);
				}
			}
		}

		sender.unsubscribeFromAllSources();

	}

	/***
	 * Updates all operatos with given oldPipeID with new Pipe and PeerID.
	 * 
	 * @param oldPipeID
	 * @param newPipeID
	 * @param newPeerID
	 */
	public static void updatePipeID(String oldPipeID, String newPipeID, String newPeerID) {

		ILogicalOperator operator = LoadBalancingHelper.getLogicalJxtaOperator(true, oldPipeID);
		while (operator != null) {
			JxtaSenderAO sender = (JxtaSenderAO) operator;
			LOG.debug("Updating Sender with old Pipe " + oldPipeID, " to new Pipe " + newPipeID);
			sender.setPeerID(newPeerID);
			sender.setPipeID(newPipeID);
			operator = LoadBalancingHelper.getLogicalJxtaOperator(true, oldPipeID);
		}

		operator = LoadBalancingHelper.getLogicalJxtaOperator(false, oldPipeID);
		while (operator != null) {
			LOG.debug("Updating Receiver with old Pipe " + oldPipeID, " to new Pipe " + newPipeID);
			JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
			receiver.setPeerID(newPeerID);
			receiver.setPipeID(newPipeID);
			operator = LoadBalancingHelper.getLogicalJxtaOperator(false, oldPipeID);
		}

	}

	/**
	 * Relinks a logical Query Part to a new peer.
	 * 
	 * @param modifiedPart
	 * @param status
	 * @return
	 */
	public static HashMap<String, String> relinkQueryPart(ILogicalQueryPart modifiedPart, ParallelTrackMasterStatus status) {

		IP2PNetworkManager p2pNetworkManager = OsgiServiceManager.getP2pNetworkManager();

		LoadBalancingHelper.removeTopAOs(modifiedPart);

		Map<ILogicalOperator, Collection<UpstreamConnection>> incomingConnections = LoadBalancingHelper.stripJxtaReceivers(modifiedPart);
		Map<ILogicalOperator, Collection<DownstreamConnection>> outgoingConnections = LoadBalancingHelper.stripJxtaSenders(modifiedPart);

		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(modifiedPart);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper.getRelativeSourcesOfLogicalQueryPart(modifiedPart);

		HashMap<String, String> replacedPipes = new HashMap<String, String>();
		ArrayList<String> senderPipes = new ArrayList<String>();

		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<UpstreamConnection> connections = incomingConnections.get(relativeSource);
				for (UpstreamConnection connection : connections) {

					String newPipeID = IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()).toString();

					JxtaReceiverAO receiver = LoadBalancingHelper.createReceiverAO(connection, newPipeID);

					replacedPipes.put(newPipeID, connection.oldPipeID);
					modifiedPart.addOperator(receiver);

				}
			}
		}

		for (ILogicalOperator relativeSink : relativeSinks) {
			if (outgoingConnections.containsKey(relativeSink)) {
				Collection<DownstreamConnection> connections = outgoingConnections.get(relativeSink);
				for (DownstreamConnection connection : connections) {
					String newPipeID = IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()).toString();

					replacedPipes.put(newPipeID, connection.oldPipeID);

					JxtaSenderAO sender = LoadBalancingHelper.createSenderAO(connection, newPipeID);
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
		status.setReplacedPipes(replacedPipes);
		return replacedPipes;
	}
}
