package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.datarate.physicaloperator.DataratePO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;

public class DataratePOTransformer {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(DataratePOTransformer.class);

	@SuppressWarnings({ "rawtypes" })
	public static void replaceDataratePOs(int queryID, PeerID localPeerID, ISession session,IServerExecutor executor, IP2PNetworkManager networkManager, IExcludedQueriesRegistry excludedQueriesRegistry,IQueryPartController queryPartController) {
		
		
		LOG.debug("Replacing Sources for Query {}", queryID);
		List<ISource> sources = Lists.newArrayList();
		Collection<IPhysicalOperator> operatorsInQuery = executor
				.getExecutionPlan().getQueryById(queryID).getAllOperators();

		// Get all Subscriptions that have a real sink
		for (IPhysicalOperator op : operatorsInQuery) {
			if (op instanceof DataratePO) {
				LOG.debug("Operator {} seems to be a datarate Operator.", op.getName());
				ISource source = (ISource) op;
				for (Object obj : source.getSubscriptions()) {
					if (obj instanceof ISubscription) {
						ISubscription subscr = (ISubscription) obj;
						if (operatorsInQuery.contains(subscr.getTarget())
								&& !(subscr.getTarget() instanceof JxtaSenderPO)
								&& (!sources.contains(source))) {
							sources.add(source);
						}
					}
				}
			}
		}

		for (ISource source : sources) {

			replaceSubscriptionWithSenderAndReceiver(source, localPeerID,
					queryID, session,executor,networkManager,excludedQueriesRegistry,queryPartController);
		}

	}
	

	@SuppressWarnings({ "rawtypes" })
	private static void replaceSubscriptionWithSenderAndReceiver(
			ISource sourceOperator, PeerID peerID, int queryID,ISession session, IServerExecutor executor, IP2PNetworkManager networkManager, IExcludedQueriesRegistry excludedQueriesRegistry,IQueryPartController queryPartController) {
		
		
		ILogicalOperator logicalSource = TransformationHelper.getLogicalForPhysicalDatarateOperator(sourceOperator, queryID, session,executor);

		List<ControllablePhysicalSubscription> physicalSubscriptionsToReplace = TransformationHelper.getSubscriptionsToReplace(sourceOperator, queryID, false,executor);

		List<IPhysicalOperator> newRoots = Lists.newArrayList();

		int newQueryID = 0;
		boolean firstSubscription = true;

		Collection<ILogicalOperator> addedSenders = Lists.newArrayList();

		Iterator<ControllablePhysicalSubscription> iter = physicalSubscriptionsToReplace
				.iterator();
		
		while (iter.hasNext()) {

			JxtaSenderAO senderAO = null;
			ControllablePhysicalSubscription subscr = iter.next();

			PipeID newPipe = IDFactory.newPipeID(networkManager.getLocalPeerGroupID());

			JxtaSenderPO senderPO = null;

			if (firstSubscription) {

				senderAO = TransformationHelper.createJxtaSenderAO(sourceOperator,peerID,newPipe);
				//Using source name instead of "Datarate" to set Queryname ;)
				Pair<Integer, IPhysicalOperator> queryIDSenderPair = TransformationHelper.createNewQueryWithFromLogicalOperator(senderAO, queryID,  session,logicalSource.getSubscribedToSource(0).getTarget().getName(),executor);
				newQueryID = queryIDSenderPair.getE1();
				senderPO = (JxtaSenderPO)queryIDSenderPair.getE2();
				firstSubscription = false;
				addedSenders.add(senderAO);
			} else {
				senderAO = TransformationHelper.createJxtaSenderAO(sourceOperator, peerID, newPipe);
				try {
					senderPO = new JxtaSenderPO(senderAO);
					executor.getExecutionPlan().getQueryById(newQueryID)
							.addChild(senderPO);
					addedSenders.add(senderAO);
				} catch (DataTransmissionException e) {
					LOG.error("Error while creating receiver!");
					e.printStackTrace();
					return;
				}
			}

			JxtaReceiverAO receiverAO = TransformationHelper.createReceiverAO(sourceOperator, peerID,newPipe,senderPO.getOutputSchema());
			JxtaReceiverPO receiverPO;

			try {
				receiverPO = new JxtaReceiverPO(receiverAO);
			} catch (DataTransmissionException e) {
				LOG.error("Could not create JxtaReceiverPO.");
				e.printStackTrace();
				return;
			}

			if (logicalSource != null) {
				
				ILogicalOperator oldTop = executor.getLogicalQueryById(queryID, session).getLogicalPlan();
				Collection<ILogicalOperator> oldRoots = getRootsFromTopAO(oldTop);
				
				TransformationHelper.modifyLogicalQuery(logicalSource, senderAO, receiverAO, subscr, newQueryID, queryID, addedSenders, oldRoots,  session,false,executor);
			} else {
				LOG.warn("Could not find logical Sink to physical Sink {}",
						sourceOperator.getName());
			}
			
			TransformationHelper.modifyPhyiscalQuery((ISink)subscr.getTarget(),sourceOperator, newQueryID,  session,newRoots, iter, subscr, receiverPO, senderPO,true,executor);
			
			if(!iter.hasNext()) {
				//Set last root manually.
				executor.getExecutionPlan().getQueryById(newQueryID).addChild(senderPO);
				executor.getExecutionPlan().getQueryById(queryID).replaceOperator(sourceOperator, receiverPO);
			} else {
				executor.getExecutionPlan().getQueryById(queryID).addChild(receiverPO);
			}

		}

		reorganizeRootsOfQuery(newQueryID, queryID, newRoots,
				session,sourceOperator,executor);

		addNewQueryToSharedQuery(queryID, session,newQueryID,executor,queryPartController);
		excludedQueriesRegistry.excludeQueryIdFromLoadBalancing(newQueryID);

	}



	private static Collection<ILogicalOperator> getRootsFromTopAO(
			ILogicalOperator oldTop) {
		List<ILogicalOperator> roots = Lists.newArrayList();
		for(LogicalSubscription subscription : oldTop.getSubscribedToSource()) {
			if(!roots.contains(subscription.getTarget()))
				roots.add(subscription.getTarget());
		}
		return roots;
	}

	

	@SuppressWarnings("rawtypes")
	private static void reorganizeRootsOfQuery(int sourceSideQueryID, int sinkSideQueryID, List<IPhysicalOperator> newRoots, ISession session,ISource sourceOperator,IServerExecutor executor) {
		
		IPhysicalQuery sourceSideQuery = executor.getExecutionPlan().getQueryById(sourceSideQueryID);
		List<IOperatorOwner> ownerList = new ArrayList<IOperatorOwner>();
		ownerList.add(sourceSideQuery);

		executor.getExecutionPlan().getQueryById(sourceSideQueryID).addChild(sourceOperator);
		executor.getExecutionPlan().getQueryById(sourceSideQueryID).setRoots(newRoots);
	}
	
	private static void addNewQueryToSharedQuery(int queryID,ISession session, int newQueryID, IServerExecutor executor, IQueryPartController queryPartController) {
		
		
		//Add query to (newly created) shared query.
		ID sharedQueryID = queryPartController.getSharedQueryID(queryID);
		if(sharedQueryID!=null) {
			Collection<PeerID> otherPeers = queryPartController.getOtherPeers(sharedQueryID);
			queryPartController.registerAsMaster(executor.getLogicalQueryById(queryID, session), newQueryID, sharedQueryID, otherPeers);
		}
	}






}
