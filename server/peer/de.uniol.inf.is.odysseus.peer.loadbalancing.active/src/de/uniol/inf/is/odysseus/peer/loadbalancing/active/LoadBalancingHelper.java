package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
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
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingSynchronizerPO;

public class LoadBalancingHelper {
	
	

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
	 * Removes a query from current Peer.
	 * 
	 * @param queryId
	 */
	public static void deleteQuery(IExecutor executor, ISession session, int queryId) {
		executor.removeQuery(queryId, session);
	}
	

	public static HashMap<String,String> relinkQueryPart(IP2PNetworkManager p2pNetworkManager, ILogicalQueryPart part, PeerID newPeer, LoadBalancingMasterStatus status) {
		LoadBalancingHelper.removeTopAOs(part);
		Map<ILogicalOperator, Collection<ConnectionToOperator>> incomingConnections = LoadBalancingHelper.stripJxtaReceivers(part);
		Map<ILogicalOperator, Collection<ConnectionToOperator>> outgoingConnections = LoadBalancingHelper.stripJxtaSenders(part);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(part);
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(part);

		HashMap<String,String> replacedPipes = new HashMap<String,String>();
		ArrayList<String> receiverPipes = new ArrayList<String>();
		
		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<ConnectionToOperator> connections = incomingConnections
						.get(relativeSource);
				for (ConnectionToOperator connection : connections) {

					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();
					JxtaReceiverAO receiver = new JxtaReceiverAO();
					
					replacedPipes.put(newPipeID,connection.oldPipeID);
					
					receiver.setPipeID(newPipeID);
					receiver.setPeerID(connection.remotePeerID);
					receiver.setSchema(connection.schema.getAttributes());
					receiver.connectSink(relativeSource, connection.port, 0,
							relativeSource.getInputSchema(0));
					receiverPipes.add(connection.oldPipeID);
					
					
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
					
					replacedPipes.put(newPipeID,connection.oldPipeID);

					JxtaSenderAO sender = new JxtaSenderAO();
					sender.setPeerID(connection.remotePeerID);
					sender.setPipeID(newPipeID);
					sender.setOutputSchema(connection.schema);
					connection.localOperator.connectSink(sender, 0,
							connection.port,
							connection.localOperator.getOutputSchema());
				}
			}
		}
		status.setPipesToSync(receiverPipes);
		return replacedPipes;
	}
	
	
	

	/**
	 * Deletes Deprecated jxtaReceiverPO.
	 * 
	 * @param oldPipeId
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void deleteDeprecatedReceiver(IServerExecutor executor,String oldPipeId) {
		JxtaReceiverPO<?> physicalJxtaOperator = (JxtaReceiverPO<?>) getPhysicalJxtaOperator(executor,
				false, oldPipeId);
		if (physicalJxtaOperator != null) {
			PhysicalSubscription<?> receiverSubscription = physicalJxtaOperator
					.getSubscriptions().get(0);

			physicalJxtaOperator.unsubscribeFromAllSinks();

			if (receiverSubscription.getTarget() instanceof LoadBalancingSynchronizerPO) {
				LoadBalancingSynchronizerPO syncPO = (LoadBalancingSynchronizerPO<?>) receiverSubscription
						.getTarget();

				// Sync Operator has max 2 sources. so if there is another
				// receiver the size of the list need to
				// be 1, because the old receiver has already been removed
				if (!syncPO.getSubscribedToSource().isEmpty()
						&& syncPO.getSubscribedToSource().size() == 1) {
					PhysicalSubscription syncSourceSubscription = (PhysicalSubscription) syncPO
							.getSubscribedToSource().get(0);

					if (syncSourceSubscription.getTarget() instanceof JxtaReceiverPO) {
						JxtaReceiverPO<?> newReceiver = (JxtaReceiverPO<?>) syncSourceSubscription
								.getTarget();
						newReceiver.block();
						newReceiver.unsubscribeFromAllSinks();

						List<PhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = syncPO
								.getSubscriptions();

						for (PhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {

							syncPO.unsubscribeSink(subscription);

							newReceiver.subscribeSink(subscription.getTarget(),
									subscription.getSinkInPort(),
									subscription.getSourceOutPort(),
									subscription.getSchema(), true,
									subscription.getOpenCalls());
						}
						newReceiver.unblock();
					}
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
	public static IPhysicalOperator getPhysicalJxtaOperator(IServerExecutor executor, boolean lookForSender,
			String pipeID) {
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
	public static ILogicalOperator getLogicalJxtaOperator(IServerExecutor executor, ISession session, boolean lookForSender,
			String pipeID) {
		for (ILogicalQueryPart part : getInstalledQueryParts(executor, session)) {
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
	public static Collection<Integer> installAndRunQueryPartFromPql(IServerExecutor executor, ISession session, Context context, String pql) {
		// TODO better Error Handling.
			Collection<Integer> installedQueries = executor.addQuery(pql,
					"PQL", session, "Standard", context);
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
	public static Collection<ILogicalQueryPart> getInstalledQueryParts(IServerExecutor executor, ISession session) {
		ArrayList<ILogicalQueryPart> parts = new ArrayList<ILogicalQueryPart>();
		for (int queryId : executor.getLogicalQueryIds(session)) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId,session);

			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			parts.add(new LogicalQueryPart(operators));
		}
		return parts;
	}
	
	public static ILogicalQueryPart getInstalledQueryPart(IServerExecutor executor, ISession session, int queryId)  {
		ILogicalQuery query = executor.getLogicalQueryById(queryId,session);
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
	 * @Param lbProcessId LoadBalancingProcessId.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void findAndCopyLocalJxtaOperator(IServerExecutor executor,LoadBalancingMessageDispatcher dispatcher, ISession session, boolean isSender,
			String newPeerId, String oldPipeId, String newPipeId,
			int lbProcessId) {
		ILogicalOperator operator = getLogicalJxtaOperator(executor,session,isSender, oldPipeId);
		if (operator != null) {
			if (isSender) {
				JxtaSenderAO logicalSender = (JxtaSenderAO) operator;
				JxtaSenderAO copy = (JxtaSenderAO) logicalSender.clone();
				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);
				try {
					JxtaSenderPO physicalCopy = new JxtaSenderPO(copy);
					JxtaSenderPO physicalOriginal = (JxtaSenderPO) LoadBalancingHelper.getPhysicalJxtaOperator(executor,
							isSender, oldPipeId);
					physicalCopy.setOutputSchema(physicalOriginal
							.getOutputSchema());

					PhysicalSubscription subscription = physicalOriginal
							.getSubscribedToSource(0);

					if (subscription.getTarget() instanceof AbstractPipe) {
						((AbstractPipe) subscription.getTarget())
								.subscribeSink(physicalCopy, 0,
										subscription.getSourceOutPort(),
										subscription.getSchema(), true,
										subscription.getOpenCalls());
					} else if (subscription.getTarget() instanceof AbstractSource) {
						((AbstractSource) subscription.getTarget())
								.subscribeSink(physicalCopy, 0,
										subscription.getSourceOutPort(),
										subscription.getSchema(), true,
										subscription.getOpenCalls());
					}
				} catch (DataTransmissionException e) {
					//TODO
				}

			} else {
				JxtaReceiverAO logicalReceiver = (JxtaReceiverAO) operator;
				JxtaReceiverAO copy = (JxtaReceiverAO) logicalReceiver.clone();
				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);
				copy.setSchema(logicalReceiver.getSchema());
				try {
					JxtaReceiverPO physicalCopy = new JxtaReceiverPO(copy);
					JxtaReceiverPO physicalOriginal = (JxtaReceiverPO) LoadBalancingHelper.getPhysicalJxtaOperator(executor,
							isSender, oldPipeId);
					physicalCopy.setOutputSchema(physicalOriginal
							.getOutputSchema());

					LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>> synchronizer = new LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>>();
					
					LoadBalancingFinishedListener listener = new LoadBalancingFinishedListener(dispatcher,
							toPeerID(physicalOriginal.getPeerIDString()),oldPipeId);
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
							physicalOriginal.getOutputSchema());
					physicalCopy.open(synchronizer, 0, 1, emptyCallPath,
							physicalOriginal.getOwner());

					physicalOriginal.unblock();
				} catch (DataTransmissionException e) {
					//TODO
				}
			}
		}

	}

	
}
