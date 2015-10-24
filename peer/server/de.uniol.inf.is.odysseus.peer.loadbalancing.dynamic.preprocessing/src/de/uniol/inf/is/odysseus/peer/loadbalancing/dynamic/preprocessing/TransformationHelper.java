package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterQueryName;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterPriority;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.datarate.logicaloperator.DatarateAO;
import de.uniol.inf.is.odysseus.datarate.physicaloperator.DataratePO;
import de.uniol.inf.is.odysseus.latency.physicaloperator.CalcLatencyPO;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;

/**
 * Helper class for transformation of Queries.
 * @author Carsten Cordes
 *
 */
public class TransformationHelper {
	

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(TransformationHelper.class);
	
	/**
	 * Checks if Operator is real Sink 
	 * @param op Operator to check
	 * @return true if Operator is real sink for Query.
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isRealSink(IPhysicalOperator op) {
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
				else {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if Query has real sources 
	 * @param queryID Query to check
	 * @param executor Executor
	 * @return true if Query has real sources.
	 */
	public static boolean hasRealSources(int queryID,IServerExecutor executor) {
		
		
		Set<IPhysicalOperator> physicalOps = executor.getExecutionPlan().getQueryById(queryID).getAllOperators();
		for(IPhysicalOperator op : physicalOps) {
			if(isRealSource(op))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if Query has real sinks 
	 * @param queryID Query to check
	 * @param executor Executor
	 * @return true if Query has real sinks.
	 */
	public static boolean hasRealSinks(int queryID,IServerExecutor executor) {
		
		
		Set<IPhysicalOperator> physicalOps = executor.getExecutionPlan().getQueryById(queryID).getAllOperators();
		for(IPhysicalOperator op : physicalOps) {
			if(isRealSink(op))
				return true;
		}
		return false;
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

	/**
	 * Checks if Operator is real Source 
	 * @param op Operator to check
	 * @return true if Operator is Source.
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isRealSource(IPhysicalOperator op) {
		if(op instanceof JxtaReceiverPO)
			return false;
		if(op instanceof ISource) {
			ISource opAsSource = (ISource)op;
			//Op might be only operator (no incoming connections -> no need to further seperate)
			if(opAsSource.getSubscriptions().size()<=0) {
				return false;
			}
			else {
				if(op instanceof ISink) {
					ISink opAsSink = (ISink)op;
					if(opAsSink.getSubscribedToSource().size()==0) {
						return true;
					}
				}
				else {
					return true;
				}
			}
		}
		
		return false;
	}
	

	/**
	 * Tries to get Logical Source Operator to Physical Source Operator
	 * @param sourceOperator Physical Source Operator
	 * @param queryID Query ID
	 * @param session Session 
	 * @param executor Executor
	 * @return Logical Operator for Physical Operator or null.
	 */
	public static ILogicalOperator getLogicalSourceToPhysicalSource(
			IPhysicalOperator sourceOperator, int queryID,ISession session,IServerExecutor executor) {
		
		
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID,
				session);
		List<ILogicalOperator> operatorsInLogicalQuery = Lists.newArrayList();
		RestructHelper.collectOperators(logicalQuery.getLogicalPlan(),
				operatorsInLogicalQuery);

		ILogicalOperator source = null;

		for (ILogicalOperator op : operatorsInLogicalQuery) {
			if (!op.isSourceOperator())
				continue;
			if (op.getName().equals(sourceOperator.getName())) {
				source = op;
				break;
			}
		}
		return source;
	}
	
	/***
	 * Attempts to find logical DatarateAO for physical DataratePO. This is done by comparing the names of the source for DataratePO.
	 * This probably won't always work but is still better than not getting the logical Op.
	 * Might be replaced by .getLogicalOperator in the future.
	 * @param datarateOp
	 * @param queryID
	 * @param session
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ILogicalOperator getLogicalForPhysicalDatarateOperator(IPhysicalOperator datarateOp, int queryID,ISession session,IServerExecutor executor) {
		
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID,
				session);
		List<ILogicalOperator> operatorsInLogicalQuery = Lists.newArrayList();
		RestructHelper.collectOperators(logicalQuery.getLogicalPlan(),
				operatorsInLogicalQuery);

		ILogicalOperator source = null;
		
		ISource realSource = (ISource)((DataratePO)datarateOp).getSubscribedToSource(0).getTarget();
		String nameOfRealSource = realSource.getName();
		

		for (ILogicalOperator op : operatorsInLogicalQuery) {
			if (!(op instanceof DatarateAO))
				continue;
			if (op.getSubscribedToSource(0).getTarget().getName().equals(nameOfRealSource)) {
				source = op;
				break;
			}
		}
		return source;
	}
	

	
	/**
	 * Tries to get Logical Sink Operator to Physical Sink Operator
	 * @param sinkOperator Physical Sink Operator
	 * @param queryID Query ID
	 * @param session Session 
	 * @param executor Executor
	 * @return Logical Operator for Physical Operator or null.
	 */
	public static ILogicalOperator getLogicalSinkToPhysicalSink(IPhysicalOperator sinkOperator,int queryID,ISession session,IServerExecutor executor) {
		
		
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
	

	/***
	 * Attempts to get logical CalcLatencyOp for Physical OP. Compared by name.
	 * This is probably not a good solution but a.t.m. the only working one.
	 * @param calclatencyOp
	 * @param queryID
	 * @param session
	 * @return
	 */
	public static ILogicalOperator getLogicalCalcLatencyOperatorToPhysicalCalcLatencyOperator(IPhysicalOperator calclatencyOp,int queryID,ISession session,IServerExecutor executor) {
		
		
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID, session);
		List<ILogicalOperator> operatorsInLogicalQuery = Lists.newArrayList();
		RestructHelper.collectOperators(logicalQuery.getLogicalPlan(), operatorsInLogicalQuery);
		
		ILogicalOperator sink=null;
		
		for(ILogicalOperator op: operatorsInLogicalQuery) {
			if(!(op instanceof CalcLatencyAO))
				continue;
			if(op.getName().equals(calclatencyOp.getName())) {
				sink = op;
				break;
			}
		}
		return sink;
	}
	
	/**
	 * Checks if Query has DataratePOs
	 * @param queryID Query ID
	 * @param executor Executor
	 * @return true if Query has Datarate POs
	 */
	public static boolean hasDataratePOs(int queryID,IServerExecutor executor) {
		
		Set<IPhysicalOperator> physicalOps = executor.getExecutionPlan().getQueryById(queryID).getAllOperators();
		for(IPhysicalOperator op : physicalOps) {
			if(op instanceof DataratePO)
				return true;
		}
		return false;
	}
	

	/**
	 * Checks if Operator has CalcLatencyPOs
	 * @param queryID Query 
	 * @param executor Executor
	 * @return true if Operator has CalcLatencyPOs.
	 */
	public static boolean hasCalclatencyPOs(int queryID,IServerExecutor executor) {
		Set<IPhysicalOperator> physicalOps = executor.getExecutionPlan().getQueryById(queryID).getAllOperators();
		for(IPhysicalOperator op : physicalOps) {
			if(op instanceof CalcLatencyPO)
				return true;
		}
		return false;
	}
	

	/**
	 * Gets Subscriptions that need to be replaced with Sender/Receiver
	 * @param operator Operator
	 * @param queryID Query
	 * @param isSink is Operator Sink(true) or Source(False)
	 * @param executor Executor
	 * @return List of Subscriptions.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<ControllablePhysicalSubscription> getSubscriptionsToReplace(
			IPhysicalOperator operator, int queryID, boolean isSink,IServerExecutor executor) {
		
		
		List<ControllablePhysicalSubscription> subscriptionsToReplace = Lists
				.newArrayList();
		Set<IPhysicalOperator> operatorsInPhysicalQuery = executor
				.getExecutionPlan().getQueryById(queryID).getAllOperators();
		
		Iterator<Object> iter;
		
		if(isSink) {
			iter = ((ISink)operator).getSubscribedToSource().iterator();
		}
		else {
			iter = ((ISource)operator).getSubscriptions().iterator();
		}
		while(iter.hasNext()) {
			Object subscrObj = iter.next();
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
	

	/** 
	 * Returns first physical Root of Query
	 * @param queryId QueryID
	 * @param executor Executor
	 * @return First physical Root for Query.
	 */
	public static IPhysicalOperator getFirstPhysicalRootOfQuery(int queryId,IServerExecutor executor) {
		
		
		return executor.getExecutionPlan().getQueryById(queryId).getRoots().get(0);
	}
	

	/**
	 * Creates a new Query from logical Operator
	 * @param operator Operator to be in Query.
	 * @param oldQueryID QueryID of old Query
	 * @param session Sessoin
	 * @param queryNamePostfix String that is put after old Query name to get new Query Name.
	 * @param executor Executor
	 * @return Pair with Integer (new Query ID ) and Physical Operator
	 */
	public static Pair<Integer, IPhysicalOperator> createNewQueryWithFromLogicalOperator(
			ILogicalOperator operator, int oldQueryID,
			ISession session, String queryNamePostfix,IServerExecutor executor) {
		

		ILogicalQuery logicalQuery = executor.getLogicalQueryById(oldQueryID,
				session);

		List<ILogicalOperator> operatorList = Lists.newArrayList();
		operatorList.add(operator);
		TopAO top = RestructHelper.generateTopAO(operatorList);

		List<IQueryBuildSetting<?>> settings = Lists.newArrayList();

		settings.add(new ParameterQueryName(logicalQuery.getName() + "_"
				+ queryNamePostfix));
		settings.add(new ParameterParserID(logicalQuery.getParserId()));
		settings.add(new ParameterPriority(logicalQuery.getPriority()));

		int queryIdForNewQuery = executor.addQuery(top, session, executor
				.getBuildConfigForQuery(logicalQuery).getName(), settings);

		//As there is only ONE Op -> we return this as Operator
		return new Pair<Integer, IPhysicalOperator>(queryIdForNewQuery, TransformationHelper.getFirstPhysicalRootOfQuery(queryIdForNewQuery,executor));
	}
	

	/**
	 * Creates a new JxtaSenderAO
	 * @param op Physical Operator (to get Schema)
	 * @param peerID PeerID
	 * @param pipeID PipeID
	 * @return new JxtaSenderAO
	 */
	public static JxtaSenderAO createJxtaSenderAO(IPhysicalOperator op, PeerID peerID,
			PipeID pipeID) {
		JxtaSenderAO senderAO = new JxtaSenderAO();
		senderAO.setPeerID(peerID.toString());
		senderAO.setPipeID(pipeID.toString());
		senderAO.setOutputSchema(op.getOutputSchema().clone());
		senderAO.setName("JxtaSender " + op.getName());
		return senderAO;
	}
	

	/**
	 * Creates a new JxtaReceiverAO
	 * @param op Operator (for configuration)
	 * @param peerID PeerID
	 * @param pipeID pipeID
	 * @param schema Schema
	 * @return new JxtaReceiverAO
	 */
	public static JxtaReceiverAO createReceiverAO(IPhysicalOperator op, PeerID peerID,
			PipeID pipeID,SDFSchema schema) {
		JxtaReceiverAO receiverAO = new JxtaReceiverAO();
		receiverAO.setPeerID(peerID.toString());
		receiverAO.setPipeID(pipeID.toString());
		receiverAO.setOutputSchema(schema.clone());
		receiverAO.setSchema(schema.clone().getAttributes());
		receiverAO.setLocalMetaAttribute(MetadataRegistry.getMetadataType(schema.getMetaAttributeNames()));
		receiverAO.setName("JxtaReceiver " + op.getName());
		receiverAO.setSchemaName(schema.getURI());
		return receiverAO;
	}
	

	/**
	 * Tries to get Logical for pyhsical Subscription
	 * @param op Operator
	 * @param subscr physical Subscription
	 * @param reverse is it a reverse subscription(Sink to Source?)
	 * @return LogicalSubscription that fits or null
	 */
	@SuppressWarnings("rawtypes")
	public static ISubscription getLogicalForPhysicalSubscription(ILogicalOperator op,ISubscription subscr,boolean reverse) {
		Iterator<LogicalSubscription> iter;
		if(reverse) {
			iter = op.getSubscribedToSource().iterator();
		}
		else {
			iter = op.getSubscriptions().iterator();
		}
		
		
		//TODO Would be better if we had some way to get logical Operator for target
		while(iter.hasNext()) {
			
			LogicalSubscription subscriptionOfOp = iter.next();

				if(subscriptionOfOp.getSourceOutPort()!=subscr.getSourceOutPort())
					continue;
				if(subscriptionOfOp.getSinkInPort()!=subscr.getSinkInPort())
					continue;
				return subscriptionOfOp;
		}
		return null;
	}
	
	

	/**
	 * Replaces logical Subscription with Sender/Receiver
	 * @param logicalSubscription 
	 * @param source
	 * @param sink
	 * @param senderAO
	 * @param receiverAO
	 */
	@SuppressWarnings("rawtypes")
	public static void replaceLogicalSubscription(
			ISubscription logicalSubscription, ILogicalOperator source,ILogicalOperator sink,
			JxtaSenderAO senderAO, JxtaReceiverAO receiverAO) {
		
		int sinkInPort = logicalSubscription.getSinkInPort();
		int sourceOutPort = logicalSubscription.getSourceOutPort();

		source.unsubscribeSink(sink, sinkInPort, sourceOutPort,
				logicalSubscription.getSchema());

		sink.unsubscribeFromSource(source, sinkInPort, sourceOutPort,
				sink.getInputSchema(sinkInPort));

		source.subscribeSink(senderAO, 0, sourceOutPort, source
				.getOutputSchema().clone());
		receiverAO.subscribeSink(sink, sinkInPort, 0, source.getOutputSchema()
				.clone());

		sink.subscribeToSource(receiverAO, sinkInPort, 0, source
				.getOutputSchema().clone());
		senderAO.subscribeToSource(source, 0, sourceOutPort, source
				.getOutputSchema().clone());

	}
	
	/**
	 * Modifies logical Query to represent changes done on physical Query.
	 * @param operator
	 * @param jxtaSender
	 * @param jxtaReceiver
	 * @param subscription
	 * @param sourceQueryID
	 * @param sinkQueryID
	 * @param newRootsSourceSide
	 * @param newRootsSinkSide
	 * @param session
	 * @param reverseSubscription Is Subscription Sink->Source?
	 * @param executor
	 */
	@SuppressWarnings("rawtypes")
	public static void modifyLogicalQuery(ILogicalOperator operator,JxtaSenderAO jxtaSender,JxtaReceiverAO jxtaReceiver, ISubscription subscription,int sourceQueryID,int sinkQueryID,Collection<ILogicalOperator> newRootsSourceSide,Collection<ILogicalOperator> newRootsSinkSide,ISession session, boolean reverseSubscription,IServerExecutor executor) {
		
		
		ISubscription logicalSubscription = TransformationHelper.getLogicalForPhysicalSubscription(operator, subscription,reverseSubscription);
		
		ISubscription sourceToSinkLogicalSubscription;
		ILogicalOperator originalSource;
		ILogicalOperator originalSink;
		
		
		if(reverseSubscription) {
			originalSink = operator;
			originalSource = (ILogicalOperator)logicalSubscription.getTarget();
			sourceToSinkLogicalSubscription = getSourceToSinkSubscriptionFromReversed(originalSource,originalSink,logicalSubscription);
		}
		else {
			originalSink = (ILogicalOperator)logicalSubscription.getTarget();
			originalSource = operator;
			sourceToSinkLogicalSubscription = logicalSubscription;
			
		}
		
		TransformationHelper.replaceLogicalSubscription(sourceToSinkLogicalSubscription, originalSource, originalSink, jxtaSender, jxtaReceiver);

		ILogicalQuery sourceQuery = executor.getLogicalQueryById(sourceQueryID,session);
		ILogicalQuery sinkQuery = executor.getLogicalQueryById(sinkQueryID, session);
		
		IPhysicalQuery sourcePhysicalQuery = executor.getExecutionPlan().getQueryById(sourceQueryID);
		IPhysicalQuery sinkPhysicalQuery = executor.getExecutionPlan().getQueryById(sinkQueryID);

		TopAO sourceSideTop = RestructHelper.generateTopAO(newRootsSourceSide);
		TopAO sinkSideTop = RestructHelper.generateTopAO(newRootsSinkSide);


		//Set new roots on Sink Side.
		sinkQuery.setLogicalPlan(sinkSideTop, true);
		sinkPhysicalQuery.setLogicalQuery(sinkQuery);

		//Set new roots on Source Side.
		sourceQuery.setLogicalPlan(sourceSideTop, true);
		sourcePhysicalQuery.setLogicalQuery(sourceQuery);

		List<ILogicalOperator> operatorsSourceSide = Lists.newArrayList();
		List<ILogicalOperator> operatorsSinkSide = Lists.newArrayList();

		RestructHelper.collectOperators(sourceSideTop, operatorsSourceSide);
		RestructHelper.collectOperators(sinkSideTop, operatorsSinkSide);

		for (ILogicalOperator op : operatorsSourceSide) {
			op.removeAllOwners();
			op.addOwner(sourceQuery);
		}

		for (ILogicalOperator op : operatorsSinkSide) {
			op.removeAllOwners();
			op.addOwner(sinkQuery);
		}
	}

	/**
	 * Tries to get Forward Subscription  (source->Sink) for Backward Subscription (sink->source)
	 * @param originalSource
	 * @param originalSink
	 * @param logicalSubscription
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static LogicalSubscription getSourceToSinkSubscriptionFromReversed(
			ILogicalOperator originalSource, ILogicalOperator originalSink,
			ISubscription logicalSubscription) {
		
		for(LogicalSubscription subscr: originalSource.getSubscriptions()) {
			if(subscr.getSinkInPort()==logicalSubscription.getSinkInPort() && subscr.getSourceOutPort()==logicalSubscription.getSourceOutPort() && subscr.getTarget().equals(originalSink)) {
				return subscr;
			}
		}
		
		return null;
	}

	/**
	 * Tries to get Logical For physical Operator by provided Methods in Physical Op (does not work)
	 * @param physOp
	 * @return
	 */
	public static ILogicalOperator getLogicalForPhysicalOperator(IPhysicalOperator physOp) {
		return physOp.getLogicalOperator();
	}
	

	/**
	 * Replaces Physical Subscsription with Sender and Receiver
	 * @param source
	 * @param sink
	 * @param subscr
	 * @param senderPO
	 * @param receiverPO
	 * @param openReceivers
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void replacePhysicalConnectionWithSenderReceiverPair(ISource source,ISink sink,ISubscription subscr, JxtaSenderPO senderPO,JxtaReceiverPO receiverPO,boolean openReceivers) {

		if (subscr instanceof ControllablePhysicalSubscription) {

			int sinkInPort = subscr.getSinkInPort();
			int sourceOutPort = subscr.getSourceOutPort();
			
			ControllablePhysicalSubscription cSub = (ControllablePhysicalSubscription) subscr;
			cSub.suspend();

			cSub.setTarget(senderPO);

			sink.unsubscribeFromSource(source,sinkInPort,sourceOutPort,subscr.getSchema());

			sink.subscribeToSource(receiverPO, subscr.getSinkInPort(), 0,subscr.getSchema().clone());
			senderPO.subscribeToSource(source, 0, subscr.getSourceOutPort(),subscr.getSchema());

			receiverPO.subscribeSink(sink, subscr.getSinkInPort(), 0, subscr.getSchema().clone());

			cSub.setSinkInPort(0);
			cSub.resume();
			
			if(openReceivers) {
				List<IPhysicalOperator> emptyCallPath = Lists.newArrayList();
				receiverPO.open(sink, sourceOutPort, 0, emptyCallPath, sink.getOwner());
			}
		} else {
			LOG.error("Subscription is no controllable Subscription. Not supported.");
		}
		
		

	}
	
	/**
	 * Modifies Physical Query to set new Roots after Transformation.
	 * @param sinkOperator
	 * @param sourceOperator
	 * @param sourceSideQueryID
	 * @param session
	 * @param newRoots
	 * @param iter
	 * @param subscr
	 * @param receiverPO
	 * @param senderPO
	 * @param openReceivers
	 * @param executor
	 */
	@SuppressWarnings("rawtypes")
	public static void modifyPhyiscalQuery(ISink sinkOperator,ISource sourceOperator, int sourceSideQueryID, ISession session,List<IPhysicalOperator> newRoots,Iterator<ControllablePhysicalSubscription> iter,
			ISubscription subscr, JxtaReceiverPO receiverPO,
			JxtaSenderPO senderPO, boolean openReceivers,IServerExecutor executor) {
		
		
		TransformationHelper.replacePhysicalConnectionWithSenderReceiverPair(sourceOperator, sinkOperator, subscr, senderPO, receiverPO,openReceivers);
		
		
		newRoots.add(senderPO);
		
		senderPO.open(executor.getLogicalQueryById(sourceSideQueryID, session));
		
		if (iter.hasNext()) {
			executor.getExecutionPlan().getQueryById(sourceSideQueryID)
					.addChild(senderPO);
		} 
	}


}
