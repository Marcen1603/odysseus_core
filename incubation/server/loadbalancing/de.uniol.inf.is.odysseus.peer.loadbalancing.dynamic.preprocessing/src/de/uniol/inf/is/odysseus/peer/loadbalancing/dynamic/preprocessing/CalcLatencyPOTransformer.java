package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.latency.physicaloperator.CalcLatencyPO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;

public class CalcLatencyPOTransformer {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(CalcLatencyPOTransformer.class);

	@SuppressWarnings({ "rawtypes" })
	public static void replaceCalcLatencyPO(int queryID, PeerID localPeerID,
			ISession session, IServerExecutor executor, IP2PNetworkManager networkManager, IExcludedQueriesRegistry excludedQueriesRegistry,IQueryPartController queryPartController) {


		LOG.debug("Replacing Sinks for Query {}", queryID);
		List<ISink> sinks = Lists.newArrayList();
		Collection<IPhysicalOperator> operatorsInQuery = executor
				.getExecutionPlan().getQueryById(queryID).getAllOperators();

		// Get all Subscriptions that have a calclatency.
		for (IPhysicalOperator op : operatorsInQuery) {
			if (op instanceof CalcLatencyPO) {
				LOG.debug("Operator {} seems to be a real sink.", op.getName());
				ISink sink = (ISink) op;
				for (Object obj : sink.getSubscribedToSource()) {
					if (obj instanceof ISubscription) {
						ISubscription subscr = (ISubscription) obj;
						if (operatorsInQuery.contains(subscr.getTarget())
								&& !(subscr.getTarget() instanceof JxtaReceiverPO)
								&& (!sinks.contains(sink))) {
							sinks.add(sink);
						}
					}
				}
			}
		}

		for (ISink sink : sinks) {

			replaceSubscriptionWithSenderAndReceiver(sink, localPeerID,
					queryID, session,executor,networkManager,excludedQueriesRegistry,queryPartController);
		}

	}

	@SuppressWarnings({ "rawtypes" })
	private static void replaceSubscriptionWithSenderAndReceiver(
			ISink sinkOperator, PeerID peerID, int queryID, ISession session, IServerExecutor executor, IP2PNetworkManager networkManager, IExcludedQueriesRegistry excludedQueriesRegistry,IQueryPartController queryPartController) {

		ILogicalOperator logicalSink = TransformationHelper
				.getLogicalSinkToPhysicalSink(sinkOperator, queryID, session,executor);

		List<ControllablePhysicalSubscription> physicalSubscriptionsToReplace = getSubscriptionsToReplace(
				sinkOperator, queryID,executor);

		List<IPhysicalOperator> newRoots = Lists.newArrayList();

		int newQueryID = 0;
		boolean firstSubscription = true;

		Collection<ILogicalOperator> addedSenders = Lists.newArrayList();

		Iterator<ControllablePhysicalSubscription> iter = physicalSubscriptionsToReplace
				.iterator();
		while (iter.hasNext()) {

			JxtaReceiverAO receiverAO = null;
			ControllablePhysicalSubscription subscr = iter.next();

			PipeID newPipe = IDFactory.newPipeID(networkManager
					.getLocalPeerGroupID());

			JxtaReceiverPO receiverPO = null;

			if (firstSubscription) {

				receiverAO = TransformationHelper.createReceiverAO(
						sinkOperator, peerID, newPipe, subscr.getSchema());
				Pair<Integer, IPhysicalOperator> queryIdReceiverPair = TransformationHelper
						.createNewQueryWithFromLogicalOperator(receiverAO,
								queryID, session, logicalSink.getName(),executor);
				newQueryID = queryIdReceiverPair.getE1();
				receiverPO = (JxtaReceiverPO) queryIdReceiverPair.getE2();
				firstSubscription = false;
			} else {
				receiverAO = TransformationHelper.createReceiverAO(
						sinkOperator, peerID, newPipe, subscr.getSchema());
				try {
					receiverPO = new JxtaReceiverPO(receiverAO);
					executor.getExecutionPlan().getQueryById(newQueryID)
							.addChild(receiverPO);
				} catch (DataTransmissionException e) {
					LOG.error("Error while creating receiver!");
					e.printStackTrace();
					return;
				}
			}

			JxtaSenderAO senderAO = TransformationHelper.createJxtaSenderAO(
					sinkOperator, peerID, newPipe);
			addedSenders.add(senderAO);
			JxtaSenderPO senderPO;

			try {
				senderPO = new JxtaSenderPO(senderAO);
			} catch (DataTransmissionException e) {
				LOG.error("Could not create JxtaSenderPO.");
				e.printStackTrace();
				return;
			}
			if (logicalSink != null) {

				Collection<ILogicalOperator> newRootsSinkSide = Lists
						.newArrayList();
				
				ILogicalOperator realSink = logicalSink;
				for(ISubscription logSubscrToRealSink : logicalSink.getSubscriptions()) {
					if(logSubscrToRealSink.getTarget() instanceof TopAO)
						continue;
					realSink = (ILogicalOperator)logSubscrToRealSink.getTarget();
					break;
				}
				
				newRootsSinkSide.add(realSink);
				TransformationHelper.modifyLogicalQuery(logicalSink, senderAO,
						receiverAO, subscr, queryID, newQueryID, addedSenders,
						newRootsSinkSide, session, true,executor);
			} else {
				LOG.warn("Could not find logical Sink to physical Sink {}",
						sinkOperator.getName());
			}

			ISubscription sourceToSinkSubscription = getSourceToSinkSubscription(
					sinkOperator, subscr);
			TransformationHelper
					.modifyPhyiscalQuery(sinkOperator,
							(ISource) subscr.getTarget(), queryID, session,
							newRoots, iter, sourceToSinkSubscription,
							receiverPO, senderPO, false,executor);

			// Set last root manually.
			if (!iter.hasNext()) {
				executor.getExecutionPlan().getQueryById(queryID)
						.replaceOperator(sinkOperator, senderPO);
			}
		}

		reorganizeRootsOfQuery(sinkOperator, queryID, newRoots, newQueryID,
				session, executor);

		addNewQueryToSharedQuery(queryID, session, newQueryID,executor,queryPartController);
		excludedQueriesRegistry.excludeQueryIdFromLoadBalancing(newQueryID);

	}

	@SuppressWarnings("rawtypes")
	private static void reorganizeRootsOfQuery(ISink calcLatencyOpAsSink, int queryID,
			List<IPhysicalOperator> newRoots, int newQueryID, ISession session, IServerExecutor executor) {

		CalcLatencyPO calcLatencyOp = (CalcLatencyPO)calcLatencyOpAsSink;
		
		ISink sinkOperator = null;
		
		if(calcLatencyOp.getSubscriptions().isEmpty()) {
			sinkOperator = calcLatencyOp;
		}
		else {
			//As this is only for evalation, we assume that there is only one Subscription e.g. to FileSink.
			Object subscrObj = calcLatencyOp.getSubscriptions().get(0);
			sinkOperator = (ISink)((ISubscription)subscrObj).getTarget();
		}
		

		IPhysicalQuery newQueryWithSink = executor.getExecutionPlan()
				.getQueryById(newQueryID);
		List<IOperatorOwner> ownerList = new ArrayList<IOperatorOwner>();
		ownerList.add(newQueryWithSink);
		// sinkOperator.open(newQueryWithSink);
		

		executor.getExecutionPlan().getQueryById(newQueryID)
		.addChild(calcLatencyOp);
		executor.getExecutionPlan().getQueryById(newQueryID)
				.addChild(sinkOperator);

		List<IPhysicalOperator> newRootsForNewQuery = Lists.newArrayList();
		newRootsForNewQuery.add(sinkOperator);
		executor.getExecutionPlan().getQueryById(newQueryID)
				.setRoots(newRootsForNewQuery);
		executor.getExecutionPlan().getQueryById(queryID).setRoots(newRoots);

		//TODO Remove "real" Source from PhysicalPlan. Executor has no method for this?
		executor.startQuery(newQueryID, session);
		
	}

	private static void addNewQueryToSharedQuery(int queryID, ISession session,
			int newQueryID,IServerExecutor executor, IQueryPartController queryPartController) {
		
		// Add query to (newly created) shared query.
		ID sharedQueryID = queryPartController.getSharedQueryID(queryID);
		if (sharedQueryID != null) {
			Collection<PeerID> otherPeers = queryPartController
					.getOtherPeers(sharedQueryID);
			queryPartController.registerAsMaster(
					executor.getLogicalQueryById(newQueryID, session),
					newQueryID, sharedQueryID, otherPeers);
		}
	}

	@SuppressWarnings("rawtypes")
	private static List<ControllablePhysicalSubscription> getSubscriptionsToReplace(
			ISink sinkOperator, int queryID,IServerExecutor executor) {
		
		
		List<ControllablePhysicalSubscription> subscriptionsToReplace = Lists
				.newArrayList();
		Set<IPhysicalOperator> operatorsInPhysicalQuery = executor
				.getExecutionPlan().getQueryById(queryID).getAllOperators();
		for (Object subscrObj : sinkOperator.getSubscribedToSource()) {
			if (subscrObj instanceof ControllablePhysicalSubscription) {
				ControllablePhysicalSubscription subscr = (ControllablePhysicalSubscription) subscrObj;
				if (operatorsInPhysicalQuery.contains(subscr.getTarget()))
					subscriptionsToReplace.add(subscr);
			} else {
				// TODO Find some solution here.
				LOG.warn("Can not process non ControllablePhysicalSubscription");
			}
		}
		return subscriptionsToReplace;
	}

	@SuppressWarnings("rawtypes")
	private static ISubscription getSourceToSinkSubscription(ISink sink,
			ISubscription subscr) {
		ISource source = (ISource) subscr.getTarget();
		for (Object obj : source.getSubscriptions()) {
			if (obj instanceof ISubscription) {
				ISubscription revSubscr = (ISubscription) obj;
				if (revSubscr.getTarget().equals(sink)
						&& revSubscr.getSourceOutPort() == subscr
								.getSourceOutPort()
						&& revSubscr.getSinkInPort() == subscr.getSinkInPort()) {
					return revSubscr;
				}
			}
		}
		return null;
	}


}
