package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
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
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;

public class SinkTransformer {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(SinkTransformer.class);

	@SuppressWarnings({ "rawtypes" })
	public static void replaceSinks(int queryID, PeerID localPeerID,
			ISession session, IP2PNetworkManager networkManager,
			IServerExecutor executor,IQueryPartController queryPartController) {
		LOG.debug("Replacing Sinks for Query {}", queryID);
		List<ISink> sinks = Lists.newArrayList();
		Collection<IPhysicalOperator> operatorsInQuery = executor
				.getExecutionPlan().getQueryById(queryID).getAllOperators();

		
		
		for (IPhysicalOperator op : operatorsInQuery) {
			if (isRealSink(op)) {
				LOG.debug("Operator {} seems to be a real sink.",
						op.getName());
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
			PipeID newPipe = IDFactory.newPipeID(networkManager
					.getLocalPeerGroupID());
			replaceSinkWithJxtaSender(sink, localPeerID, newPipe,
					queryID, executor, session,queryPartController,networkManager);
		}

	}

	@SuppressWarnings({ "rawtypes"})
	private static void replaceSinkWithJxtaSender(ISink sinkOperator,PeerID peerID, PipeID pipeID, int queryID, IServerExecutor executor, ISession session, IQueryPartController queryPartController, IP2PNetworkManager networkManager) {
		
		ILogicalOperator logicalSink = getLogicalSinkToPhysicalSink(sinkOperator, queryID, executor, session);
		
		List<ControllablePhysicalSubscription> physicalSubscriptionsToReplace = getSubscriptionsToReplace(
				sinkOperator, queryID, executor);
		
		List<ISubscription> logicalSubscriptionsToReplace = getLogicalSubscriptionsToReplace(sinkOperator,queryID,executor,session);
		
		
		Pair<Integer,JxtaReceiverPO> queryIdReceiverPair = createNewQueryWithJxtaReceiver(sinkOperator, peerID, pipeID, queryID, executor, session);
		
		int newQueryId = queryIdReceiverPair.getE1();
		JxtaReceiverPO receiverPO = queryIdReceiverPair.getE2();
		
		JxtaSenderAO senderAO = createJxtaSenderAO(sinkOperator, peerID, pipeID);
		
		JxtaSenderPO senderPO;
		
		try {
			senderPO = new JxtaSenderPO(senderAO);
			senderPO.addOwner(executor.getLogicalQueryById(queryID, session));
		} catch (DataTransmissionException e) {
			LOG.error("Could not create JxtaReceiverPO.");
			e.printStackTrace();
			return;
		}
		
		if(logicalSink!=null) {
			for(ISubscription logicalSubscription : logicalSubscriptionsToReplace) {
				replaceLogicalSubscription(logicalSubscription,logicalSink,senderAO);
			}
		}
		else {
			LOG.warn("Could not find logical Sink to physical Sink {}",sinkOperator.getName());
		}
		
		if(physicalSubscriptionsToReplace.size()>1) {
			LOG.error("More than one incoming Subcsription to Sink is not properly supported and will probably result in Errors.");
		}
		for(ControllablePhysicalSubscription subscr : physicalSubscriptionsToReplace) {
			replacePhysicalSinkWithSenderReceiverPair(sinkOperator,subscr,executor.getExecutionPlan().getQueryById(newQueryId),senderPO, receiverPO);
		}

		
		
		List<IPhysicalOperator> newRoots = Lists.newArrayList();
		newRoots.add(senderPO);
		
		executor.getExecutionPlan().getQueryById(queryID).replaceOperator(sinkOperator, senderPO);
		executor.getExecutionPlan().getQueryById(queryID).setRoots(newRoots);
		
		List<IPhysicalOperator> newRootsForNewQuery = Lists.newArrayList();
		newRootsForNewQuery.add(sinkOperator);
		
		executor.getExecutionPlan().getQueryById(newQueryId).addChild(sinkOperator);
		executor.getExecutionPlan().getQueryById(newQueryId).setRoots(newRootsForNewQuery);
		senderPO.open(executor.getLogicalQueryById(queryID, session));
		
		
		
		//Add query to (newly created) shared query.
		ID sharedQueryID = queryPartController.getSharedQueryID(queryID);
		if(sharedQueryID==null) {
			sharedQueryID = IDFactory.newContentID(networkManager.getLocalPeerGroupID(), false, String.valueOf(System.currentTimeMillis()).getBytes());
			Collection<PeerID> otherPeers = new ArrayList<PeerID>();
			queryPartController.registerAsMaster(executor.getLogicalQueryById(newQueryId, session), newQueryId, sharedQueryID, otherPeers);
			queryPartController.addLocalQueryToShared(sharedQueryID, queryID);
		}
		else {
			Collection<PeerID> otherPeers = queryPartController.getOtherPeers(sharedQueryID);
			queryPartController.registerAsMaster(executor.getLogicalQueryById(newQueryId, session), newQueryId, sharedQueryID, otherPeers);
			
		}
		
		
		
	}
	
	@SuppressWarnings("rawtypes")
	private static void replaceLogicalSubscription(ISubscription logicalSubscription, ILogicalOperator sink, ILogicalOperator replacement) {
		ILogicalOperator source = (ILogicalOperator) logicalSubscription.getTarget();
		int sinkInPort = logicalSubscription.getSinkInPort();
		int sourceOutPort = logicalSubscription.getSinkInPort();
		
		source.unsubscribeSink(sink, sinkInPort, sourceOutPort, logicalSubscription.getSchema());
		replacement.subscribeSink(sink, sinkInPort, sourceOutPort, source.getOutputSchema().clone());
		
		sink.unsubscribeFromSource(source, sinkInPort, sourceOutPort, sink.getInputSchema(sinkInPort));
		sink.subscribeToSource(replacement, sinkInPort, sourceOutPort, source.getOutputSchema().clone());
	}

	@SuppressWarnings("rawtypes")
	private static List<ControllablePhysicalSubscription> getSubscriptionsToReplace(
			ISink sinkOperator, int queryID, IServerExecutor executor) {
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
	private static List<ISubscription> getLogicalSubscriptionsToReplace(ISink sinkOperator, int queryID, IServerExecutor executor,ISession session) {

		List<ISubscription> subscriptionsFound = Lists.newArrayList();
		List<ILogicalOperator> operatorsInLogicalQuery = Lists.newArrayList();
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID, session);
		RestructHelper.collectOperators(logicalQuery.getLogicalPlan(), operatorsInLogicalQuery);
		
		ILogicalOperator sink = getLogicalSinkToPhysicalSink(sinkOperator, queryID, executor, session);
		
		if(sink==null) {
			LOG.warn("No logical Source operator for phyiscal Source {} found.",sinkOperator.getName());
		} else {
			
			for(ISubscription subscription : sink.getSubscribedToSource()) {
				if(operatorsInLogicalQuery.contains(subscription.getTarget())) {
					subscriptionsFound.add(subscription);
				}
			}
		}
		
		return subscriptionsFound;
	}

	@SuppressWarnings("rawtypes")
	private static JxtaSenderAO createJxtaSenderAO(ISink sink,
			PeerID peerID, PipeID pipeID) {
		JxtaSenderAO senderAO = new JxtaSenderAO();
		senderAO.setPeerID(peerID.toString());
		senderAO.setPipeID(pipeID.toString());
		senderAO.setOutputSchema(sink.getOutputSchema().clone());
		senderAO.setName("JxtaSender " + sink.getName());
		return senderAO;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void replacePhysicalSinkWithSenderReceiverPair(ISink sink,
			ControllablePhysicalSubscription revSubscr,IPhysicalQuery newQueryWithSink,
			JxtaSenderPO senderPO, JxtaReceiverPO receiverPO) {

		ISource source = (ISource)revSubscr.getTarget();
		ISubscription subscr = getReversePhysicalSubscription(sink, revSubscr);
		

		if(subscr instanceof ControllablePhysicalSubscription) {

			ControllablePhysicalSubscription cSub = (ControllablePhysicalSubscription) subscr;
			cSub.suspend();
			

			//Port has to be changed tooo, probably...
			cSub.setTarget(senderPO);
			
			sink.unsubscribeFromSource(revSubscr);

			sink.subscribeToSource(receiverPO, subscr.getSinkInPort(), 0,
					subscr.getSchema().clone());
			
			senderPO.subscribeToSource(source, subscr.getSinkInPort(), subscr.getSourceOutPort(), subscr.getSchema());
			
		
			receiverPO.subscribeSink(sink, subscr.getSinkInPort(), 0, subscr
					.getSchema().clone());
			
	
			List<IOperatorOwner> ownerList = new ArrayList<IOperatorOwner>();
			ownerList.add(newQueryWithSink);
			sink.open(newQueryWithSink);
			cSub.resume();
		}
		else {
			LOG.error("Subscription is no controllable Subscription. Not supported.");
		}
		
	}

	@SuppressWarnings("rawtypes")
	private static Pair<Integer, JxtaReceiverPO> createNewQueryWithJxtaReceiver(
			ISink sink, PeerID peerID, PipeID pipeID, int queryID,
			IServerExecutor executor, ISession session) {
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID,
				session);
		JxtaReceiverAO receiverAO = new JxtaReceiverAO();
		receiverAO.setPeerID(peerID.toString());
		receiverAO.setPipeID(pipeID.toString());
		receiverAO.setSchema(sink.getOutputSchema().clone().getAttributes());
		receiverAO.setName("JxtaReceiver " + sink.getName());

		List<ILogicalOperator> operatorList = Lists.newArrayList();
		operatorList.add(receiverAO);
		TopAO top = RestructHelper.generateTopAO(operatorList);
		int queryIdForNewQuery = executor.addQuery(top, session, executor
				.getBuildConfigForQuery(logicalQuery).getName());
		
		

		JxtaReceiverPO receiverPO = getReceiverFromQuery(queryIdForNewQuery, session,
				executor);
		return new Pair<Integer, JxtaReceiverPO>(queryIdForNewQuery, receiverPO);
	}

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
	
	public static ILogicalOperator getLogicalSinkToPhysicalSink(IPhysicalOperator sinkOperator,int queryID, IServerExecutor executor,ISession session) {
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID, session);
		List<ILogicalOperator> operatorsInLogicalQuery = Lists.newArrayList();
		RestructHelper.collectOperators(logicalQuery.getLogicalPlan(), operatorsInLogicalQuery);
		
		ILogicalOperator sink=null;
		
		for(ILogicalOperator op: operatorsInLogicalQuery) {
			if(!op.isSinkOperator())
				continue;
			if(op.getName().equals(sinkOperator.getName())) {
				sink = op;
				break;
			}
		}
		
		return sink;
	}

	@SuppressWarnings("rawtypes")
	private static ISubscription getReversePhysicalSubscription(ISink sink,
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


	@SuppressWarnings("rawtypes")
	private static JxtaReceiverPO getReceiverFromQuery(int queryId,
			ISession session, IServerExecutor executor) {
		for (IPhysicalOperator op : executor.getExecutionPlan()
				.getQueryById(queryId).getAllOperators()) {
			if (op instanceof JxtaReceiverPO) {
				return (JxtaReceiverPO) op;
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private static boolean isRealSink(IPhysicalOperator op) {
		if(op instanceof JxtaSenderPO)
			return false;
		if(op instanceof ISink) {
			ISink opAsSink = (ISink)op;
			//Op might be only operator (no incoming connections -> no need to further seperate)
			if(opAsSink.getSubscribedToSource().size()<=0) {
				return false;
			}
			else {
				if(op instanceof ISource) {
					ISource opAsSource = (ISource)op;
					if(opAsSource.getSubscriptions().size()==0) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
