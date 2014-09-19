package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication;

import java.net.URI;
import java.net.URISyntaxException;
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

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingSynchronizerPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.status.LoadBalancingSlaveStatus;

/**
 * Encapsulates Methods needed for processing the active LoadBalancing.
 * @author Carsten
 *
 */
public class LoadBalancingHelper {

	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingHelper.class);
	
	/**
	 * Converts a String to a peer ID.
	 * 
	 * @param peerIDString
	 *            String to convert
	 * @return PeerId (if it exists), null else.
	 */
	public static PeerID toPeerID(String peerIDString) {
		try {
			final URI id = new URI(peerIDString);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			return null;
		}
	}

	/**
	 * Sends abort Message to all Peers involved in LoadBalancing Process
	 * 
	 * @param status
	 */
	public static void notifyInvolvedPeers(LoadBalancingMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = LoadBalancingCommunicationListener
				.getInstance();

		status.setPhase(LoadBalancingMasterStatus.LB_PHASES.FAILURE);
		// Get Distinct List of involved Peers:
		ArrayList<PeerID> involvedPeers = new ArrayList<PeerID>(
				new HashSet<PeerID>(status.getPeersForPipe().values()));
		involvedPeers.add(status.getVolunteeringPeer());
		for (PeerID peer : involvedPeers) {
			status.getMessageDispatcher().sendAbortInstruction(peer,
					deliveryFailedListener);
		}
	}
	
	/**
	 * Send Message to all incoming Peers to add a duplicate
	 * connection
	 * 
	 * @param status
	 *            Status
	 */
	public static void notifyOutgoingPeers(LoadBalancingMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = LoadBalancingCommunicationListener.getInstance();
		
		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		HashMap<String, String> replacedPipes = status.getReplacedPipes();
		LoadBalancingMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		HashMap<String,PeerID> peersForPipe = new HashMap<String,PeerID>();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				
				String newPipe = sender.getPipeID();
				String oldPipe = replacedPipes.get(sender.getPipeID());
				PeerID destinationPeer = toPeerID(sender.getPeerID());
				peersForPipe.put(newPipe,destinationPeer);
				
				dispatcher.sendCopyOperator(false,
						destinationPeer,
						oldPipe,newPipe, volunteeringPeer,deliveryFailedListener);
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
	public static void notifyIncomingPeers(LoadBalancingMasterStatus status) {

		IMessageDeliveryFailedListener deliveryFailedListener = LoadBalancingCommunicationListener.getInstance();
		
		String volunteeringPeer = status.getVolunteeringPeer().toString();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		HashMap<String, String> replacedPipes = status.getReplacedPipes();
		LoadBalancingMessageDispatcher dispatcher = status
				.getMessageDispatcher();

		HashMap<String,PeerID> peersForPipe = status.getPeersForPipe();

		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				
				String newPipe = receiver.getPipeID();
				String oldPipe = replacedPipes.get(receiver.getPipeID());
				PeerID destinationPeer = toPeerID(receiver.getPeerID());
				peersForPipe.put(newPipe,destinationPeer);
				
				dispatcher.sendCopyOperator(true,destinationPeer,
						oldPipe,
						newPipe, volunteeringPeer,deliveryFailedListener);
			}
		}
		status.setPeersForPipe(peersForPipe);

	}
	
	

	/**
	 * Removes a query from current Peer.
	 * 
	 * @param queryId
	 */
	public static void deleteQuery(int queryId) {
		
		ISession session = LoadBalancingCommunicationListener.getActiveSession();
		IServerExecutor executor = LoadBalancingCommunicationListener.getExecutor();
		executor.removeQuery(queryId, session);
	}

	/**
	 * Takes a QueryPart and cut's all receivers from it.
	 * Used so that, when deleting a Query the other Parts won't be influenced (e.g. stopped) by the executor.
	 * @param queryID
	 */
	@SuppressWarnings({ "rawtypes"})
	public static void cutQuery(int queryID) {
		
		IServerExecutor executor = LoadBalancingCommunicationListener.getExecutor();
		IPhysicalQuery query = executor.getExecutionPlan().getQueryById(queryID);
		
		for(IPhysicalOperator operator : query.getAllOperators()) {
			
			if(operator instanceof JxtaReceiverPO) {
				JxtaReceiverPO receiver = (JxtaReceiverPO) operator;
				receiver.unsubscribeFromAllSinks();
			}
		}
		
	}
	
	
	/**
	 * Relinks a logical Query Part to a new peer.
	 * @param modifiedPart
	 * @param status
	 * @return
	 */
	public static HashMap<String, String> relinkQueryPart(ILogicalQueryPart modifiedPart,LoadBalancingMasterStatus status) {
		
		IP2PNetworkManager p2pNetworkManager = LoadBalancingCommunicationListener.getP2pNetworkManager();
		
		LoadBalancingHelper.removeTopAOs(modifiedPart);
		
		Map<ILogicalOperator, Collection<ConnectionToOperator>> incomingConnections = LoadBalancingHelper
				.stripJxtaReceivers(modifiedPart);
		Map<ILogicalOperator, Collection<ConnectionToOperator>> outgoingConnections = LoadBalancingHelper
				.stripJxtaSenders(modifiedPart);
		
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(modifiedPart);
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(modifiedPart);

		HashMap<String, String> replacedPipes = new HashMap<String, String>();
		ArrayList<String> senderPipes = new ArrayList<String>();

		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<ConnectionToOperator> connections = incomingConnections
						.get(relativeSource);
				for (ConnectionToOperator connection : connections) {

					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();
					JxtaReceiverAO receiver = new JxtaReceiverAO();

					replacedPipes.put(newPipeID, connection.oldPipeID);

					receiver.setPipeID(newPipeID);
					receiver.setPeerID(connection.remotePeerID);
					receiver.setSchema(connection.schema.getAttributes());
					receiver.setSchemaName(connection.schema.getURI());
					receiver.connectSink(relativeSource, connection.port, 0,
							relativeSource.getInputSchema(0));
					modifiedPart.addOperator(receiver);
					
				}
			}
		}

		for (ILogicalOperator relativeSink : relativeSinks) {
			if (outgoingConnections.containsKey(relativeSink)) {
				Collection<ConnectionToOperator> connections = outgoingConnections
						.get(relativeSink);
				for (ConnectionToOperator connection : connections) {
					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();

					replacedPipes.put(newPipeID, connection.oldPipeID);

					JxtaSenderAO sender = new JxtaSenderAO();
					sender.setPeerID(connection.remotePeerID);
					sender.setPipeID(newPipeID);
					sender.setOutputSchema(connection.schema);
					connection.localOperator.connectSink(sender, 0,
							connection.port,
							connection.localOperator.getOutputSchema());
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
	

	/**
	 * Removes a duplicate Jxta Receiver or sender used in LoadBalancing.
	 * Called during abort and after sync.
	 * @param pipeID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void removeDuplicateJxtaOperator(String pipeID) {
		LOG.debug("Removing Operator with pipe ID " + pipeID);
		
		IPhysicalOperator operator = getPhysicalJxtaOperator(false,
				pipeID);

		if (operator instanceof JxtaSenderPO) {
			JxtaSenderPO sender = (JxtaSenderPO) operator;
			sender.unsubscribeFromAllSources();
		}

		if (operator instanceof JxtaReceiverPO) {
			JxtaReceiverPO receiver = (JxtaReceiverPO) operator;
			PhysicalSubscription<?> receiverSubscription = (PhysicalSubscription) receiver
					.getSubscriptions().get(0);

			if (receiverSubscription.getTarget() instanceof LoadBalancingSynchronizerPO) {
				LoadBalancingSynchronizerPO sync = (LoadBalancingSynchronizerPO) receiverSubscription
						.getTarget();
				int port = receiverSubscription.getSinkInPort();
				int otherPort = (port + 1) % 2;

				JxtaReceiverPO otherReceiver = (JxtaReceiverPO) sync
						.getSubscribedToSource(otherPort).getTarget();
				otherReceiver.unsubscribeFromAllSinks();
				receiver.unsubscribeFromAllSinks();

				List<PhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = sync
						.getSubscriptions();

				for (PhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {

					sync.unsubscribeSink(subscription);

					otherReceiver.subscribeSink(subscription.getTarget(),
							subscription.getSinkInPort(),
							subscription.getSourceOutPort(),
							subscription.getSchema(), true,
							subscription.getOpenCalls());
					
					subscription.getTarget().subscribeToSource(otherReceiver, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
				}

			}
		}

	}

	@SuppressWarnings("rawtypes")
	/**
	 * Get Physical JxtaOperator (Sender or Receiver) by PipeID.
	 * @param lookForSender true if we look for a sender, false if we look for receiver.
	 * @param pipeID Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	public static IPhysicalOperator getPhysicalJxtaOperator(boolean lookForSender, String pipeID) {
		
		IServerExecutor executor = LoadBalancingCommunicationListener.getExecutor();
		
		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			for (IPhysicalOperator operator : query.getAllOperators()) {
				if (lookForSender) {
					if (operator instanceof JxtaSenderPO) {
						JxtaSenderPO sender = (JxtaSenderPO) operator;
						if (sender.getPipeIDString().equals(pipeID)) {
							return sender;
						}
					}
				} else {
					if (operator instanceof JxtaReceiverPO) {
						JxtaReceiverPO receiver = (JxtaReceiverPO) operator;
						if (receiver.getPipeIDString().equals(pipeID)) {
							return receiver;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get Logical JxtaOperator (Sender or Receiver) by PipeID.
	 * 
	 * @param lookForSender
	 *            true if we look for a sender, false if we look for receiver.
	 * @param pipeID
	 *            Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	public static ILogicalOperator getLogicalJxtaOperator(boolean lookForSender,
			String pipeID) {
		
		for (ILogicalQueryPart part : getInstalledQueryParts()) {
			for (ILogicalOperator operator : part.getOperators()) {
				if (lookForSender) {
					if (operator instanceof JxtaSenderAO) {
						JxtaSenderAO sender = (JxtaSenderAO) operator;
						if (sender.getPipeID().equals(pipeID)) {
							return sender;
						}

					}
				} else {
					if (operator instanceof JxtaReceiverAO) {
						JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
						if (receiver.getPipeID().equals(pipeID)) {
							return receiver;
						}

					}
				}

			}
		}
		return null;
	}

	/**
	 * Installs and executes a Query from PQL.
	 * 
	 * @param pql
	 *            PQL to execute.
	 */
	public static Collection<Integer> installAndRunQueryPartFromPql(Context context,
			String pql) {
		
		IServerExecutor executor = LoadBalancingCommunicationListener.getExecutor();
		ISession session = LoadBalancingCommunicationListener.getActiveSession();
		
		Collection<Integer> installedQueries = executor.addQuery(pql, "PQL",
				session, "Standard", context);
		for (int query : installedQueries) {
			executor.startQuery(query, session);
		}
		return installedQueries;

	}

	/**
	 * Gets a (logical) Copy of a single Query Part.
	 * 
	 * @param part
	 *            Part to copy.
	 * @return Copy of part.
	 */
	public static ILogicalQueryPart getCopyOfQueryPart(ILogicalQueryPart part) {
		ILogicalQueryPart result = null;
		ArrayList<ILogicalQueryPart> partsList = new ArrayList<ILogicalQueryPart>();

		partsList.add(part);

		Map<ILogicalQueryPart, ILogicalQueryPart> copies = LogicalQueryHelper
				.copyQueryPartsDeep(partsList);

		for (Map.Entry<ILogicalQueryPart, ILogicalQueryPart> entry : copies
				.entrySet()) {
			result = entry.getKey();
		}

		return result;
	}

	/**
	 * Removes all JxtaSenders from a QueryPart.
	 * 
	 * @param part
	 *            Part, where Senders should be removed.
	 * @return Map with Logical Operators and their Connections to external
	 *         Operators.
	 */
	public static Map<ILogicalOperator, Collection<ConnectionToOperator>> stripJxtaSenders(
			ILogicalQueryPart part) {
		HashMap<ILogicalOperator, Collection<ConnectionToOperator>> result = new HashMap<ILogicalOperator, Collection<ConnectionToOperator>>();
		ArrayList<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				for (LogicalSubscription subscription : sender
						.getSubscribedToSource()) {

					ILogicalOperator incomingOperator = subscription
							.getTarget();

					if (!result.containsKey(incomingOperator)) {
						result.put(incomingOperator,
								new ArrayList<ConnectionToOperator>());
					}
					result.get(incomingOperator).add(
							new ConnectionToOperator(incomingOperator, sender
									.getPeerID(), sender.getPipeID(),
									subscription.getSourceOutPort(),
									subscription.getSchema()));

				}
				toRemove.add(sender);
			}
		}

		for (ILogicalOperator operator : toRemove) {
			operator.unsubscribeFromAllSources();
			part.removeOperator(operator);
		}
		return result;
	}

	/**
	 * Removes all JxtaReceivers from a QueryPart.
	 * 
	 * @param part
	 *            Part, where Receivers should be removed.
	 * @return Map with Logical Operators and their Connections to external
	 *         Operators.
	 */
	public static Map<ILogicalOperator, Collection<ConnectionToOperator>> stripJxtaReceivers(
			ILogicalQueryPart part) {

		HashMap<ILogicalOperator, Collection<ConnectionToOperator>> result = new HashMap<ILogicalOperator, Collection<ConnectionToOperator>>();

		ArrayList<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();

		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				for (LogicalSubscription subscription : receiver
						.getSubscriptions()) {

					ILogicalOperator targetOperator = subscription.getTarget();
					if (!result.containsKey(targetOperator)) {
						result.put(targetOperator,
								new ArrayList<ConnectionToOperator>());
					}
					result.get(targetOperator).add(
							new ConnectionToOperator(targetOperator, receiver
									.getPeerID(), receiver.getPipeID(),
									subscription.getSinkInPort(), subscription
											.getSchema()));

				}
				toRemove.add(receiver);
			}
		}

		for (ILogicalOperator operator : toRemove) {
			operator.unsubscribeFromAllSinks();
			part.removeOperator(operator);
		}
		return result;
	}

	/**
	 * Removes all TopAOs from a queryPart.
	 * 
	 * @param part
	 *            Part where TopAOs should be removed.
	 */
	public static void removeTopAOs(ILogicalQueryPart part) {
		ArrayList<ILogicalOperator> toRemove = new ArrayList<>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof TopAO) {
				toRemove.add(operator);
			}
		}

		for (ILogicalOperator topAO : toRemove) {
			topAO.unsubscribeFromAllSinks();
			topAO.unsubscribeFromAllSources();
			part.removeOperator(topAO);
		}
	}

	/**
	 * Get all currently Installed Queries as Query Parts.
	 * 
	 * @return List of installed LogicalQueryParts.
	 */
	public static Collection<ILogicalQueryPart> getInstalledQueryParts() {
		

		IServerExecutor executor = LoadBalancingCommunicationListener.getExecutor();
		ISession session = LoadBalancingCommunicationListener.getActiveSession();
		
		ArrayList<ILogicalQueryPart> parts = new ArrayList<ILogicalQueryPart>();
		for (int queryId : executor.getLogicalQueryIds(session)) {
			ILogicalQuery query = executor
					.getLogicalQueryById(queryId, session);

			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			parts.add(new LogicalQueryPart(operators));
		}
		return parts;
	}

	/**
	 * Get a particular Query as query part.
	 * 
	 * @param executor
	 * @param session
	 * @param queryId
	 * @return
	 */
	public static ILogicalQueryPart getInstalledQueryPart(int queryId) {
		

		IServerExecutor executor = LoadBalancingCommunicationListener.getExecutor();
		ISession session = LoadBalancingCommunicationListener.getActiveSession();
		
		ILogicalQuery query = executor.getLogicalQueryById(queryId, session);
		ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
		RestructHelper.collectOperators(query.getLogicalPlan(), operators);
		return new LogicalQueryPart(operators);
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
	public static void findAndCopyLocalJxtaOperator(LoadBalancingSlaveStatus status,
			boolean isSender, String newPeerId, String oldPipeId,
			String newPipeId) throws DataTransmissionException, LoadBalancingException{
		
		LoadBalancingMessageDispatcher dispatcher = status.getMessageDispatcher();
		
		ILogicalOperator operator = getLogicalJxtaOperator(isSender, oldPipeId);
		
		if(operator==null) {
			LOG.error("No operator with pipe ID " + oldPipeId + " found.");
			throw new LoadBalancingException("No operator with pipe ID " + oldPipeId + " found.");
		}
		
		if (operator != null) {
			
			
			if (isSender) {
				JxtaSenderAO logicalSender = (JxtaSenderAO) operator;
				JxtaSenderAO copy = (JxtaSenderAO) logicalSender.clone();
				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);

				JxtaSenderPO physicalCopy = new JxtaSenderPO(copy);
				JxtaSenderPO physicalOriginal = (JxtaSenderPO) LoadBalancingHelper
						.getPhysicalJxtaOperator(isSender, oldPipeId);
				physicalCopy
						.setOutputSchema(physicalOriginal.getOutputSchema());

				PhysicalSubscription subscription = physicalOriginal
						.getSubscribedToSource(0);
				
				if (subscription.getTarget() instanceof AbstractPipe) {
					((AbstractPipe) subscription.getTarget()).subscribeSink(
							physicalCopy, 0, subscription.getSourceOutPort(),
							subscription.getSchema(), true,
							subscription.getOpenCalls());
					physicalCopy.subscribeToSource(subscription.getTarget(), subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
				} else if (subscription.getTarget() instanceof AbstractSource) {
					((AbstractSource) subscription.getTarget()).subscribeSink(
							physicalCopy, 0, subscription.getSourceOutPort(),
							subscription.getSchema(), true,
							subscription.getOpenCalls());
					physicalCopy.subscribeToSource(subscription.getTarget(), subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
				}
				
				LOG.debug("Installed additional Sender with PeerID " + physicalCopy.getPeerIDString() + " and PipeID " + physicalCopy.getPipeIDString());
				

			} else {
				JxtaReceiverAO logicalReceiver = (JxtaReceiverAO) operator;
				JxtaReceiverAO copy = (JxtaReceiverAO) logicalReceiver.clone();
				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);
				copy.setSchema(logicalReceiver.getSchema());
				
				
				JxtaReceiverPO physicalOriginal = (JxtaReceiverPO) LoadBalancingHelper
						.getPhysicalJxtaOperator(isSender, oldPipeId);
				
				copy.setSchemaName(physicalOriginal.getOutputSchema().getURI());
				
				JxtaReceiverPO physicalCopy = new JxtaReceiverPO(copy);
				
				physicalCopy
						.setOutputSchema(physicalOriginal.getOutputSchema());

				LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>> synchronizer = new LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>>();

				LoadBalancingFinishedListener listener = new LoadBalancingFinishedListener(
						dispatcher,
						toPeerID(physicalOriginal.getPeerIDString()), oldPipeId);
				synchronizer.addListener(listener);

				physicalOriginal.block();

				List<PhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = physicalOriginal
						.getSubscriptions();

				for (PhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {

					physicalOriginal.unsubscribeSink(subscription);
					synchronizer.subscribeSink(subscription.getTarget(),
							subscription.getSinkInPort(),
							subscription.getSourceOutPort(),
							subscription.getSchema(), true,
							subscription.getOpenCalls());
				}

				ArrayList<IPhysicalOperator> emptyCallPath = new ArrayList<IPhysicalOperator>();

				physicalOriginal.subscribeSink(synchronizer, 0, 0,
						physicalOriginal.getOutputSchema());

				physicalOriginal.open(synchronizer, 0, 0, emptyCallPath,
						physicalOriginal.getOwner());
				
				physicalCopy.subscribeSink(synchronizer, 1, 0,
						physicalCopy.getOutputSchema());
				physicalCopy.open(synchronizer, 0, 1, emptyCallPath,
						physicalCopy.getOwner());

				physicalOriginal.unblock();
				synchronizer.startSynchronizing();
				LOG.debug("Installed additional Receiver with PeerID " + physicalCopy.getPeerIDString() + " and PipeID " + physicalCopy.getPipeIDString());
				
			}
			
		}

	}

}
