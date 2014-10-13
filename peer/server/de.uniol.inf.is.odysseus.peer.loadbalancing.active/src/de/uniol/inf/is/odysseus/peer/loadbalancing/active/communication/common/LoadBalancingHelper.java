package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ActiveLoadBalancingActivator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingSynchronizerPO;

/**
 * Encapsulates Methods needed for processing the active LoadBalancing.
 * 
 * @author Carsten Cordes
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
	 * Removes a query from current Peer.
	 * 
	 * @param queryId
	 */
	public static void deleteQuery(int queryId) {

		ISession session = ActiveLoadBalancingActivator.getActiveSession();
		IServerExecutor executor = ActiveLoadBalancingActivator.getExecutor();
		executor.removeQuery(queryId, session);
	}

	/**
	 * Takes a QueryPart and cut's all receivers from it. Used so that, when
	 * deleting a Query the other Parts won't be influenced (e.g. stopped) by
	 * the executor.
	 * 
	 * @param queryID
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void cutQuery(int queryID) {

		IServerExecutor executor = ActiveLoadBalancingActivator.getExecutor();
		IPhysicalQuery query = executor.getExecutionPlan()
				.getQueryById(queryID);

		for (IPhysicalOperator operator : query.getAllOperators()) {

			if (operator instanceof JxtaReceiverPO) {
				JxtaReceiverPO receiver = (JxtaReceiverPO) operator;
				receiver.unsubscribeFromAllSinks();
			}
		}

	}

	public static JxtaReceiverAO createReceiverAO(
			IncomingConnection connection, String pipeID) {
		JxtaReceiverAO receiver = new JxtaReceiverAO();
		// TODO more than 1 outgoing connection from Receiver!

		receiver.setPipeID(pipeID);
		receiver.setPeerID(connection.remotePeerID);
		receiver.setSchema(connection.schema.getAttributes());
		receiver.setSchemaName(connection.schema.getURI());
		receiver.connectSink(connection.localOperator, connection.port, 0,
				connection.schema);
		return receiver;
	}

	public static JxtaSenderAO createSenderAO(OutgoingConnection connection,
			String pipeID) {
		JxtaSenderAO sender = new JxtaSenderAO();
		sender.setPeerID(connection.remotePeerID);
		sender.setPipeID(pipeID);
		sender.setOutputSchema(connection.schema);
		connection.localOperator.connectSink(sender, 0, connection.port,
				connection.localOperator.getOutputSchema());
		return sender;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void insertOperatorBeforeSink(ISink sink,
			AbstractPipe operatorToInsert) {
		Collection<PhysicalSubscription> subscriptions = sink.getSubscribedToSource();
		ArrayList<IPhysicalOperator> emptyCallPath = new ArrayList<IPhysicalOperator>();
		for (PhysicalSubscription subscription : subscriptions) {
			sink.unsubscribeFromSource(subscription);
			operatorToInsert.subscribeToSource(subscription.getTarget(), subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
			operatorToInsert.subscribeSink(sink, subscription.getSinkInPort(), subscription.getSinkInPort(), subscription.getSchema(), true, subscription.getOpenCalls());
			operatorToInsert.open(sink, subscription.getSinkInPort(), subscription.getSinkInPort(), emptyCallPath, sink.getOwner());
			
		}
		//TODO Fix callpath.
		//TODO does this already work?
	}

	/**
	 * Removes a duplicate Jxta Receiver or sender used in LoadBalancing. Called
	 * during abort and after sync.
	 * 
	 * @param pipeID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void removeDuplicateJxtaOperator(String pipeID) {
		LOG.debug("Removing Operator with pipe ID " + pipeID);

		IPhysicalOperator operator = getPhysicalJxtaOperator(false, pipeID);

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

					subscription.getTarget().subscribeToSource(otherReceiver,
							subscription.getSinkInPort(),
							subscription.getSourceOutPort(),
							subscription.getSchema());
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
	public static IPhysicalOperator getPhysicalJxtaOperator(
			boolean lookForSender, String pipeID) {

		IServerExecutor executor = ActiveLoadBalancingActivator.getExecutor();

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
	public static ILogicalOperator getLogicalJxtaOperator(
			boolean lookForSender, String pipeID) {

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
	public static Collection<Integer> installAndRunQueryPartFromPql(
			Context context, String pql) {

		IServerExecutor executor = ActiveLoadBalancingActivator.getExecutor();
		ISession session = ActiveLoadBalancingActivator.getActiveSession();

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
	public static Map<ILogicalOperator, Collection<OutgoingConnection>> stripJxtaSenders(
			ILogicalQueryPart part) {
		HashMap<ILogicalOperator, Collection<OutgoingConnection>> result = new HashMap<ILogicalOperator, Collection<OutgoingConnection>>();
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
								new ArrayList<OutgoingConnection>());
					}
					result.get(incomingOperator).add(
							new OutgoingConnection(incomingOperator, sender
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
	public static Map<ILogicalOperator, Collection<IncomingConnection>> stripJxtaReceivers(
			ILogicalQueryPart part) {

		// TODO More than 1 out Port!
		HashMap<ILogicalOperator, Collection<IncomingConnection>> result = new HashMap<ILogicalOperator, Collection<IncomingConnection>>();

		ArrayList<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();

		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				for (LogicalSubscription subscription : receiver
						.getSubscriptions()) {

					ILogicalOperator targetOperator = subscription.getTarget();
					if (!result.containsKey(targetOperator)) {
						result.put(targetOperator,
								new ArrayList<IncomingConnection>());
					}
					result.get(targetOperator).add(
							new IncomingConnection(targetOperator, receiver
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

		IServerExecutor executor = ActiveLoadBalancingActivator.getExecutor();
		ISession session = ActiveLoadBalancingActivator.getActiveSession();

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

		IServerExecutor executor = ActiveLoadBalancingActivator.getExecutor();
		ISession session = ActiveLoadBalancingActivator.getActiveSession();

		ILogicalQuery query = executor.getLogicalQueryById(queryId, session);
		ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
		RestructHelper.collectOperators(query.getLogicalPlan(), operators);
		return new LogicalQueryPart(operators);
	}

}
