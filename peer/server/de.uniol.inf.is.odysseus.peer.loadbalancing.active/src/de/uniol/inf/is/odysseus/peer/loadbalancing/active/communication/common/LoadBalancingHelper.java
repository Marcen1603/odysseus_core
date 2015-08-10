package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
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
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;

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

	public static Collection<PeerID> toPeerIDCollection(
			List<String> peerIDStrings) {
		ArrayList<PeerID> resultList = new ArrayList<PeerID>();
		for (String peerString : peerIDStrings) {
			resultList.add(toPeerID(peerString));
		}
		return resultList;

	}

	public static List<PeerID> getInvolvedPeers(int queryID) {

		List<PeerID> involvedPeers = new ArrayList<PeerID>();

		ILogicalQueryPart part = getInstalledQueryPart(queryID);

		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				String peerIDString = ((JxtaSenderAO) operator).getPeerID();
				PeerID peerID = toPeerID(peerIDString);
				if (!involvedPeers.contains(peerID)) {
					involvedPeers.add(peerID);
				}
			}
			if (operator instanceof JxtaReceiverAO) {
				String peerIDString = ((JxtaReceiverAO) operator).getPeerID();
				PeerID peerID = toPeerID(peerIDString);
				if (!involvedPeers.contains(peerID)) {
					involvedPeers.add(peerID);
				}
			}
		}

		// Delete local Peer ID if it is in List as this would cause problem
		// while trying to lock them.
		PeerID localPeerID = OsgiServiceManager.getP2pNetworkManager()
				.getLocalPeerID();
		if (involvedPeers.contains(localPeerID)) {
			involvedPeers.remove(localPeerID);
		}

		return involvedPeers;
	}

	/**
	 * Removes a query from current Peer.
	 * 
	 * @param queryId
	 */
	public static void deleteQuery(int queryId) {
		LOG.debug("Deleting Query " + queryId);
		ISession session = OsgiServiceManager.getActiveSession();
		IServerExecutor executor = OsgiServiceManager.getExecutor();
		executor.removeQuery(queryId, session);
	}

	/**
	 * Takes a QueryPart and cut's all receivers from it. Used so that, when
	 * deleting a Query the other Parts won't be influenced (e.g. stopped) by
	 * the executor.
	 * 
	 * @param queryID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void cutReceiversFromQuery(int queryID) {

		IServerExecutor executor = OsgiServiceManager.getExecutor();
		LOG.debug("Cutting physical Query " + queryID);

		IPhysicalQuery query = executor.getExecutionPlan()
				.getQueryById(queryID);

		for (IPhysicalOperator operator : query.getAllOperators()) {

			if (operator instanceof JxtaReceiverPO) {
				JxtaReceiverPO receiver = (JxtaReceiverPO) operator;
				for (Object subscriptionObject : receiver.getSubscriptions()) {
					AbstractPhysicalSubscription subscription = (AbstractPhysicalSubscription) subscriptionObject;
					ISink target = (ISink) subscription.getTarget();
					ArrayList<AbstractPhysicalSubscription> toUnsubscribe = new ArrayList<AbstractPhysicalSubscription>();
					for (Object reverseSubscriptionObj : target
							.getSubscribedToSource()) {
						AbstractPhysicalSubscription reverseSubscription = (AbstractPhysicalSubscription) reverseSubscriptionObj;
						if (reverseSubscription.getTarget().equals(receiver)) {
							toUnsubscribe.add(reverseSubscription);
						}
					}
					
					for (AbstractPhysicalSubscription revSubscription : toUnsubscribe) {
						target.unsubscribeFromSource(revSubscription);
					}
					
				}

				receiver.unsubscribeFromAllSinks();
			}

		}
	}

	@SuppressWarnings("rawtypes")
	public static void setNewPeerID(JxtaReceiverPO receiver, String peerID) {
		try {
			receiver.receiveFromNewPeer(peerID);
		} catch (DataTransmissionException e) {
			LOG.error("Error while relinking receiver:", e);
		}
	}

	public static JxtaReceiverAO createReceiverAO(
			UpstreamConnection connection, String pipeID) {
		JxtaReceiverAO receiver = new JxtaReceiverAO();

		String btu = "MILLISECONDS";

		SDFConstraint c = connection.schema
				.getConstraint(SDFConstraint.BASE_TIME_UNIT);
		if (c != null) {
			btu = ((TimeUnit) c.getValue()).toString();
		}

		receiver.setPipeID(pipeID);
		receiver.setPeerID(connection.remotePeerID);
		receiver.setSchema(connection.schema.clone().getAttributes());
		receiver.setSchemaName(connection.schema.clone().getURI());
		receiver.connectSink(connection.localOperator, connection.port, 0,
				connection.schema.clone());
		receiver.setBaseTimeunit(btu);
		receiver.setMetaschemata(connection.schema.getMetaschema());
		LOG.debug("Created RECEIVER with PeerID " + connection.remotePeerID);
		return receiver;
	}

	public static JxtaSenderAO createSenderAO(DownstreamConnection connection,
			String pipeID) {
		JxtaSenderAO sender = new JxtaSenderAO();
		sender.setPeerID(connection.remotePeerID);
		sender.setPipeID(pipeID);
		sender.setOutputSchema(connection.schema.clone());
		connection.localOperator.connectSink(sender, 0, connection.port,
				connection.localOperator.getOutputSchema());
		LOG.debug("Created SENDER with PeerID " + connection.remotePeerID);
		return sender;
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

		IServerExecutor executor = OsgiServiceManager.getExecutor();

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

		IServerExecutor executor = OsgiServiceManager.getExecutor();
		ISession session = OsgiServiceManager.getActiveSession();

		Collection<Integer> installedQueries = executor.addQuery(pql, "PQL",
				session, context);
		for (int query : installedQueries) {
			executor.startQuery(query, session);
		}
		return installedQueries;

	}

	public static Collection<Integer> installQueryPartFromPql(Context context,
			String pql) {
		IServerExecutor executor = OsgiServiceManager.getExecutor();
		ISession session = OsgiServiceManager.getActiveSession();

		Collection<Integer> installedQueries = executor.addQuery(pql, "PQL",
				session, context);
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

	public static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			return null;
		}
	}

	/**
	 * Removes all JxtaSenders from a QueryPart.
	 * 
	 * @param part
	 *            Part, where Senders should be removed.
	 * @return Map with Logical Operators and their Connections to external
	 *         Operators.
	 */
	public static Map<ILogicalOperator, Collection<DownstreamConnection>> stripJxtaSenders(
			ILogicalQueryPart part) {
		HashMap<ILogicalOperator, Collection<DownstreamConnection>> result = new HashMap<ILogicalOperator, Collection<DownstreamConnection>>();
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
								new ArrayList<DownstreamConnection>());
					}
					result.get(incomingOperator).add(
							new DownstreamConnection(incomingOperator, sender
									.getPeerID(), sender.getPipeID(),
									subscription.getSourceOutPort(),
									subscription.getSchema().clone()));

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
	public static Map<ILogicalOperator, Collection<UpstreamConnection>> stripJxtaReceivers(
			ILogicalQueryPart part) {

		HashMap<ILogicalOperator, Collection<UpstreamConnection>> result = new HashMap<ILogicalOperator, Collection<UpstreamConnection>>();

		ArrayList<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();

		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				for (LogicalSubscription subscription : receiver
						.getSubscriptions()) {

					ILogicalOperator targetOperator = subscription.getTarget();
					if (!result.containsKey(targetOperator)) {
						result.put(targetOperator,
								new ArrayList<UpstreamConnection>());
					}
					result.get(targetOperator).add(
							new UpstreamConnection(targetOperator, receiver
									.getPeerID(), receiver.getPipeID(),
									subscription.getSinkInPort(), subscription
											.getSchema().clone()));

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

		IServerExecutor executor = OsgiServiceManager.getExecutor();
		ISession session = OsgiServiceManager.getActiveSession();

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

		IServerExecutor executor = OsgiServiceManager.getExecutor();
		ISession session = OsgiServiceManager.getActiveSession();

		ILogicalQuery query = executor.getLogicalQueryById(queryId, session);
		ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
		RestructHelper.collectOperators(query.getLogicalPlan(), operators);
		return new LogicalQueryPart(operators);
	}

	public static Optional<Integer> getQueryForRoot(IPhysicalOperator operator) {
		IExecutor executor = OsgiServiceManager.getExecutor();
		ISession session = OsgiServiceManager.getActiveSession();
		Collection<Integer> queries = executor.getLogicalQueryIds(session);
		for (int query : queries) {
			List<IPhysicalOperator> roots = executor.getPhysicalRoots(query,
					session);
			if (roots.contains(operator)) {
				return Optional.fromNullable(query);
			}
		}
		return Optional.fromNullable(null);
	}

}
