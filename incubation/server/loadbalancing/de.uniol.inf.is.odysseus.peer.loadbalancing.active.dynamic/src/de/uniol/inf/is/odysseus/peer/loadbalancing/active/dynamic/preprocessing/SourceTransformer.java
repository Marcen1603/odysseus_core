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

public class SourceTransformer {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(SourceTransformer.class);

	@SuppressWarnings({ "rawtypes" })
	public static void replaceSources(int queryID, PeerID localPeerID,
			ISession session, IP2PNetworkManager networkManager,
			IServerExecutor executor,IQueryPartController queryPartController) {
		LOG.debug("Replacing Sources for Query {}", queryID);
		List<ISource> sources = Lists.newArrayList();
		Collection<IPhysicalOperator> operatorsInQuery = executor
				.getExecutionPlan().getQueryById(queryID).getAllOperators();

		
		
		for (IPhysicalOperator op : operatorsInQuery) {
			if (!(op instanceof ISink) && (op instanceof ISource)
					&& !(op instanceof JxtaReceiverPO)) {
				LOG.debug("Operator {} seems to be a real source.",
						op.getName());
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
			PipeID newPipe = IDFactory.newPipeID(networkManager
					.getLocalPeerGroupID());
			replaceSourceWithJxtaReceiver(source, localPeerID, newPipe,
					queryID, executor, session,queryPartController,networkManager);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void replaceSourceWithJxtaReceiver(ISource sourceOperator,PeerID peerID, PipeID pipeID, int queryID, IServerExecutor executor, ISession session, IQueryPartController queryPartController, IP2PNetworkManager networkManager) {
		
		ILogicalOperator logicalSource = getLogicalSourceToPhysicalSource(sourceOperator, queryID, executor, session);
		
		List<ControllablePhysicalSubscription> physicalSubscriptionsToReplace = getSubscriptionsToReplace(
				sourceOperator, queryID, executor);
		
		List<ISubscription> logicalSubscriptionsToReplace = getLogicalSubscriptionsToReplace(sourceOperator,queryID,executor,session);
		
		
		Pair<Integer,JxtaSenderPO> queryIdSenderPair = createNewQueryWithJxtaSender(sourceOperator, peerID, pipeID, queryID, executor, session);
		
		int newQueryId = queryIdSenderPair.getE1();
		JxtaSenderPO senderPO = queryIdSenderPair.getE2();
		
		JxtaReceiverAO receiverAO = createJxtaReceiverAO(sourceOperator, peerID, pipeID);
		
		JxtaReceiverPO receiverPO;
		
		try {
			receiverPO = new JxtaReceiverPO(receiverAO);
			//receiverPO.addOwner(new ArrayList<IOperatorOwner>(sourceOperator.getOwner()));
		} catch (DataTransmissionException e) {
			LOG.error("Could not create JxtaSenderPO.");
			e.printStackTrace();
			return;
		}
		
		if(logicalSource!=null) {
			for(ISubscription logicalSubscription : logicalSubscriptionsToReplace) {
				replaceLogicalSubscription(logicalSubscription,logicalSource,receiverAO);
			}
		}
		else {
			LOG.warn("Could not find logical Source to physical Source {}",sourceOperator.getName());
		}
		
		if(physicalSubscriptionsToReplace.size()==1) {
			physicalSubscriptionsToReplace.get(0).suspend();
		}
		else {
			//TODO support multiple connections from source.
			LOG.warn("Sources with multiple outgoing subscriptions are not (properly) supported and will probably result in tuple loss.");
		}
		
		
		for(ControllablePhysicalSubscription subscr : physicalSubscriptionsToReplace) {
			replacePhysicalSourceWithReceiver(sourceOperator,subscr,executor.getExecutionPlan().getQueryById(queryID),receiverPO);
		}
		
		ControllablePhysicalSubscription firstSub = physicalSubscriptionsToReplace.get(0);
		
		senderPO.subscribeToSource(sourceOperator, firstSub.getSinkInPort(),
				firstSub.getSourceOutPort(), firstSub.getSchema());
		
		firstSub.setTarget(senderPO);
		
		if(physicalSubscriptionsToReplace.size()==1) {
			physicalSubscriptionsToReplace.get(0).resume();
		}
		
		
		executor.getExecutionPlan().getQueryById(queryID).replaceOperator(sourceOperator, receiverPO);
		executor.getExecutionPlan().getQueryById(newQueryId).addChild(sourceOperator);
		
		
		senderPO.open(executor.getLogicalQueryById(newQueryId, session));
		
		//Add query to (newly created) shared query.
		ID sharedQueryID = queryPartController.getSharedQueryID(queryID);
		if(sharedQueryID==null) {
			sharedQueryID = IDFactory.newContentID(networkManager.getLocalPeerGroupID(), false, String.valueOf(System.currentTimeMillis()).getBytes());
			Collection<PeerID> otherPeers = new ArrayList<PeerID>();
			queryPartController.registerAsMaster(executor.getLogicalQueryById(queryID, session), queryID, sharedQueryID, otherPeers);
		}
		queryPartController.addLocalQueryToShared(sharedQueryID, newQueryId);
		
		
	}
	
	@SuppressWarnings("rawtypes")
	private static void replaceLogicalSubscription(ISubscription logicalSubscription, ILogicalOperator source, ILogicalOperator replacement) {
		ILogicalOperator sink = (ILogicalOperator) logicalSubscription.getTarget();
		int sinkInPort = logicalSubscription.getSinkInPort();
		int sourceOutPort = logicalSubscription.getSinkInPort();
		
		source.unsubscribeSink(sink, sinkInPort, sourceOutPort, logicalSubscription.getSchema());
		replacement.subscribeSink(sink, sinkInPort, sourceOutPort, source.getOutputSchema().clone());
		
		sink.unsubscribeFromSource(source, sinkInPort, sourceOutPort, sink.getInputSchema(sinkInPort));
		sink.subscribeToSource(replacement, sinkInPort, sourceOutPort, source.getOutputSchema().clone());
	}

	@SuppressWarnings("rawtypes")
	private static List<ControllablePhysicalSubscription> getSubscriptionsToReplace(
			ISource sourceOperator, int queryID, IServerExecutor executor) {
		List<ControllablePhysicalSubscription> subscriptionsToReplace = Lists
				.newArrayList();
		Set<IPhysicalOperator> operatorsInPhysicalQuery = executor
				.getExecutionPlan().getQueryById(queryID).getAllOperators();
		for (Object subscrObj : sourceOperator.getSubscriptions()) {
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
	private static List<ISubscription> getLogicalSubscriptionsToReplace(ISource sourceOperator, int queryID, IServerExecutor executor,ISession session) {

		List<ISubscription> subscriptionsFound = Lists.newArrayList();
		List<ILogicalOperator> operatorsInLogicalQuery = Lists.newArrayList();
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID, session);
		RestructHelper.collectOperators(logicalQuery.getLogicalPlan(), operatorsInLogicalQuery);
		
		ILogicalOperator source = getLogicalSourceToPhysicalSource(sourceOperator, queryID, executor, session);
		
		if(source==null) {
			LOG.warn("No logical Source operator for phyiscal Source {} found.",sourceOperator.getName());
		} else {
			
			for(ISubscription subscription : source.getSubscriptions()) {
				if(operatorsInLogicalQuery.contains(subscription.getTarget())) {
					subscriptionsFound.add(subscription);
				}
			}
		}
		
		return subscriptionsFound;
	}

	@SuppressWarnings("rawtypes")
	private static JxtaReceiverAO createJxtaReceiverAO(ISource source,
			PeerID peerID, PipeID pipeID) {
		JxtaReceiverAO receiverAO = new JxtaReceiverAO();
		receiverAO.setPeerID(peerID.toString());
		receiverAO.setPipeID(pipeID.toString());
		receiverAO.setSchema(source.getOutputSchema().clone().getAttributes());
		receiverAO.setName("JxtaReceiver " + source.getName());
		return receiverAO;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void replacePhysicalSourceWithReceiver(ISource source,
			ControllablePhysicalSubscription subscr,
			IPhysicalQuery physicalQuery,
			JxtaReceiverPO receiverPO) {
		
		ISink sink = (ISink)subscr.getTarget();
		ISubscription revSubscr = getReversePhysicalSubscription(source, subscr);
		
		// TODO What about ports? -> Perhaps add Port Route-Operator inbetween.

		sink.unsubscribeFromSource(revSubscr);
		receiverPO.subscribeSink(sink, subscr.getSinkInPort(), 0, subscr
				.getSchema().clone());
		sink.subscribeToSource(receiverPO, subscr.getSinkInPort(), 0,
				subscr.getSchema());

		List<IOperatorOwner> ownerList = new ArrayList<IOperatorOwner>();
		ownerList.add(physicalQuery);
		receiverPO.open(sink, 0, subscr.getSinkInPort(), Lists.newArrayList(),
				ownerList);
	}

	@SuppressWarnings("rawtypes")
	private static Pair<Integer, JxtaSenderPO> createNewQueryWithJxtaSender(
			ISource source, PeerID peerID, PipeID pipeID, int queryID,
			IServerExecutor executor, ISession session) {
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID,
				session);
		JxtaSenderAO senderAO = new JxtaSenderAO();
		senderAO.setPeerID(peerID.toString());
		senderAO.setPipeID(pipeID.toString());
		senderAO.setOutputSchema(source.getOutputSchema().clone());
		senderAO.setName("JxtaSender " + source.getName());

		List<ILogicalOperator> operatorList = Lists.newArrayList();
		operatorList.add(senderAO);
		TopAO top = RestructHelper.generateTopAO(operatorList);
		int queryIdForNewQuery = executor.addQuery(top, session, executor
				.getBuildConfigForQuery(logicalQuery).getName());
		IPhysicalQuery newPhysicalQuery = executor.getExecutionPlan()
				.getQueryById(queryIdForNewQuery);
		newPhysicalQuery.addChild(source);

		JxtaSenderPO senderPO = getSenderFromQuery(queryIdForNewQuery, session,
				executor);
		return new Pair<Integer, JxtaSenderPO>(queryIdForNewQuery, senderPO);
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
	
	public static ILogicalOperator getLogicalSourceToPhysicalSource(IPhysicalOperator sourceOperator,int queryID, IServerExecutor executor,ISession session) {
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID, session);
		List<ILogicalOperator> operatorsInLogicalQuery = Lists.newArrayList();
		RestructHelper.collectOperators(logicalQuery.getLogicalPlan(), operatorsInLogicalQuery);
		
		ILogicalOperator source=null;
		
		for(ILogicalOperator op: operatorsInLogicalQuery) {
			if(!op.isSourceOperator())
				continue;
			if(op.getName().equals(sourceOperator.getName())) {
				source = op;
				break;
			}
		}
		
		return source;
	}

	@SuppressWarnings("rawtypes")
	private static ISubscription getReversePhysicalSubscription(ISource source,
			ISubscription subscr) {
		ISink sink = (ISink) subscr.getTarget();
		for (Object obj : sink.getSubscribedToSource()) {
			if (obj instanceof ISubscription) {
				ISubscription revSubscr = (ISubscription) obj;
				if (revSubscr.getTarget().equals(source)
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
	private static JxtaSenderPO getSenderFromQuery(int queryId,
			ISession session, IServerExecutor executor) {
		for (IPhysicalOperator op : executor.getExecutionPlan()
				.getQueryById(queryId).getAllOperators()) {
			if (op instanceof JxtaSenderPO) {
				return (JxtaSenderPO) op;
			}
		}
		return null;
	}
}
