package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.DistributionCheckException;
import de.uniol.inf.is.odysseus.peer.distribute.IDistributionChecker;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPostProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionPreProcessor;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;
import de.uniol.inf.is.odysseus.peer.distribute.registry.DistributionCheckerRegistry;

public final class QueryDistributorHelper {

	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributorHelper.class);
	
	private static IP2PDictionary p2pDictionary;
	private static IP2PNetworkManager p2pNetworkManager;

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
	
	public static void tryPreProcess(IServerExecutor serverExecutor, ISession caller, QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryDistributionPreProcessor>> preProcessors, ILogicalQuery query) throws QueryDistributionException {
		try  {
			for( InterfaceParametersPair<IQueryDistributionPreProcessor> preProcessor : preProcessors ) {
				preProcessor.getInterface().preProcess(serverExecutor, caller, query, config);
			}
			
		} catch( Throwable t ) {
			throw new QueryDistributionException("Could not preprocess query", t);
		}
	}
	
	public static Collection<ILogicalOperator> collectRelevantOperators(ILogicalQuery query) {
		Collection<ILogicalOperator> operators = LogicalQueryHelper.getAllOperators(query);
		LogicalQueryHelper.removeTopAOs(operators);
		operators = LogicalQueryHelper.replaceStreamAOs(operators);
		
		
		return operators;
	}
	
	public static Map<ILogicalQueryPart, PeerID> forceLocalOperators(Map<ILogicalQueryPart, PeerID> partMap) {
		LOG.debug("Forcing operators with 'local' as destination name (if any) to be locally executed");

		Map<ILogicalQueryPart, PeerID> resultMap = Maps.newHashMap();
		for( ILogicalQueryPart part : partMap.keySet() ) {
			
			Collection<ILogicalOperator> localOperators = collectLocalOperators(part.getOperators());
			if( localOperators.isEmpty() ) {
				resultMap.put(part, partMap.get(part));
			} else {
				LOG.debug("Splitting query part {} into local and non-local", part);
				Collection<ILogicalOperator> nonLocalOperators = Lists.newArrayList(part.getOperators());
				nonLocalOperators.removeAll(localOperators);
				
				if( !nonLocalOperators.isEmpty() ) {
					LogicalQueryPart nonLocalQueryPart = new LogicalQueryPart(nonLocalOperators);
					resultMap.put(nonLocalQueryPart, partMap.get(part));
					LOG.debug("Created new query part {}", nonLocalQueryPart);
				}
				LogicalQueryPart localQueryPart = new LogicalQueryPart(localOperators);
				resultMap.put(localQueryPart, p2pNetworkManager.getLocalPeerID());
				LOG.debug("Created new local query part {}", localQueryPart);
			}
		}
		return resultMap;
	}

	private static Collection<ILogicalOperator> collectLocalOperators(Collection<ILogicalOperator> operators) {
		Collection<ILogicalOperator> localOperators = Lists.newArrayList();
		for( ILogicalOperator operator : operators ) {
			if( isDestinationNameLocal(operator)) {
				localOperators.add(operator);
			}
		}
		return localOperators;
	}

	private static boolean isDestinationNameLocal(ILogicalOperator operator) {
		return !Strings.isNullOrEmpty(operator.getDestinationName()) && operator.getDestinationName().equalsIgnoreCase("local");
	}
	
	public static Map<ILogicalQueryPart, PeerID> mergeQueryPartsWithSamePeer(Map<ILogicalQueryPart, PeerID> allocationMap) {
		LOG.debug("Merging query parts with same peerid if possible");
		
		Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment = determineOperatorAssignment(allocationMap.keySet());
		Map<LogicalSubscription, ILogicalOperator> subs = determineSubscriptionsAcrossQueryParts(queryPartAssignment);
		
		Map<ILogicalQueryPart, PeerID> result = Maps.newHashMap(allocationMap);
		for( LogicalSubscription sub : subs.keySet() ) {
			ILogicalOperator startOperator = subs.get(sub);
			ILogicalOperator targetOperator = sub.getTarget();
			
			ILogicalQueryPart startQueryPart = queryPartAssignment.get(startOperator);
			ILogicalQueryPart targetQueryPart = queryPartAssignment.get(targetOperator);
			
			PeerID startPeerID = result.get(startQueryPart);
			PeerID targetPeerID = result.get(targetQueryPart);
			
			if( startPeerID.equals(targetPeerID) && !startQueryPart.equals(targetQueryPart)) {
				LOG.debug("Merging query parts {} and {}", startQueryPart, targetQueryPart);
				ILogicalQueryPart mergedPart = mergeQueryParts(startQueryPart, targetQueryPart);
				result.remove(startQueryPart);
				result.remove(targetQueryPart);
				result.put(mergedPart, startPeerID);
				
				for(ILogicalOperator op : mergedPart.getOperators()) {
					queryPartAssignment.put(op, mergedPart);
				}
			}
		}
		
		return result;
	}

	private static Map<LogicalSubscription, ILogicalOperator> determineSubscriptionsAcrossQueryParts(Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment) {
		List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(queryPartAssignment.keySet());

		Map<LogicalSubscription, ILogicalOperator> subsToReplace = Maps.newHashMap();
		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator currentOperator = operatorsToVisit.remove(0);

			Collection<LogicalSubscription> sinkSubs = currentOperator.getSubscriptions();
			for (LogicalSubscription sinkSub : sinkSubs) {
				ILogicalOperator targetOperator = sinkSub.getTarget();

				ILogicalQueryPart currentQueryPart = queryPartAssignment.get(currentOperator);
				ILogicalQueryPart targetQueryPart = queryPartAssignment.get(targetOperator);
				if (!currentQueryPart.equals(targetQueryPart)) {
					subsToReplace.put(sinkSub, currentOperator);
				}
			}
		}
		return subsToReplace;
	}

	private static Map<ILogicalOperator, ILogicalQueryPart> determineOperatorAssignment(Collection<ILogicalQueryPart> queryParts) {
		Map<ILogicalOperator, ILogicalQueryPart> map = Maps.newHashMap();
		for (ILogicalQueryPart part : queryParts) {
			for (ILogicalOperator operator : part.getOperators()) {
				map.put(operator, part);
			}
		}
		return map;
	}

	private static ILogicalQueryPart mergeQueryParts(ILogicalQueryPart startQueryPart, ILogicalQueryPart targetQueryPart) {
		Collection<ILogicalOperator> mergedOperators = Lists.newArrayList();
		mergedOperators.addAll(startQueryPart.getOperators());
		mergedOperators.addAll(targetQueryPart.getOperators());
		
		return new LogicalQueryPart(mergedOperators);
	}
	
	public static void tryPostProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> allocationMap, QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryDistributionPostProcessor>> postProcessors) throws QueryDistributionException {
		try {
			for( InterfaceParametersPair<IQueryDistributionPostProcessor> postProcessor : postProcessors ) {
				postProcessor.getInterface().postProcess(serverExecutor, caller, allocationMap, config);
			}
		} catch( Throwable t ) {
			throw new QueryDistributionException("Could not post process query distribution", t);
		}
	}
	

	public static void tryCheckDistribution(QueryBuildConfiguration config, Collection<ILogicalOperator> operators) throws QueryDistributionException {
		try {
			checkDistribution(operators, config);
		} catch (DistributionCheckException ex) {
			throw new QueryDistributionException("Could not distribute query", ex);
		}
	}

	private static void checkDistribution(Collection<ILogicalOperator> operators, QueryBuildConfiguration config) throws DistributionCheckException {
		ImmutableCollection<String> names = DistributionCheckerRegistry.getInstance().getNames();
		if (names.isEmpty()) {
			LOG.debug("No distribution checkers registered. No checks taken.");
			return;
		}

		LOG.debug("Begin distribution checks with {} checkers.", names.size());
		for (String name : names) {
			IDistributionChecker checker = DistributionCheckerRegistry.getInstance().get(name);
			LOG.debug("Checking with {}", checker.getName());
			
			checker.check(operators, config);
		}
		LOG.debug("All checks passed");
	}

	public static Collection<ILogicalQueryPart> tryPartitionQuery(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartitioner>> partitioners, Collection<ILogicalOperator> operators) throws QueryDistributionException {
		LOG.debug("Begin partitioning");

		try {
			InterfaceParametersPair<IQueryPartitioner> firstPartitioner = partitioners.get(0);
			Collection<ILogicalQueryPart> partitions = firstPartitioner.getInterface().partition(operators, config, firstPartitioner.getParameters());
			
			if (partitions == null || partitions.isEmpty()) {
				throw new QueryDistributionException("Query partitioner '" + firstPartitioner.getInterface().getName() + "' retured null or empty query part list!");
			}
			checkPartitions(partitions, operators);
			if (LOG.isDebugEnabled()) {
				LoggingHelper.printQueryParts(partitions);
			}
			
			return partitions;
		} catch (QueryPartitionException ex) {
			throw new QueryDistributionException("Cannot partition query", ex);
		}
	}

	private static void checkPartitions(Collection<ILogicalQueryPart> queryParts, Collection<ILogicalOperator> operators) throws QueryDistributionException {
		Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment = determineOperatorAssignment(queryParts);

		for (ILogicalOperator operator : operators) {
			if (!queryPartAssignment.containsKey(operator)) {
				throw new QueryDistributionException("Operator " + operator + " was not partitioned to a logical query part");
			}
		}
	}

	public static Collection<ILogicalQueryPart> tryModifyQueryParts(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartModificator>> modificators, Collection<ILogicalQueryPart> queryParts) throws QueryDistributionException {
		if( modificators.isEmpty() ) {
			LOG.debug("No modificator set");
			return queryParts;
		}
		
		LOG.debug("Begin modifying query parts");

		try {
			Collection<ILogicalQueryPart> currentPartitionState = queryParts;
			for( InterfaceParametersPair<IQueryPartModificator> modificator : modificators ) {
				currentPartitionState = modificator.getInterface().modify(currentPartitionState, config, modificator.getParameters());
				
				if (currentPartitionState == null || currentPartitionState.isEmpty()) {
					throw new QueryDistributionException("Query part modificator '" + modificator.getInterface().getName() + "' retured null or empty query part list!");
				}
			}
			
			if (LOG.isDebugEnabled()) {
				LoggingHelper.printQueryParts(currentPartitionState);
			}

			return currentPartitionState;
		} catch (QueryPartModificationException ex) {
			throw new QueryDistributionException("Could not modify query parts", ex);
		}
	}

	public static Map<ILogicalQueryPart, PeerID> tryAllocate(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartAllocator>> allocators, Collection<ILogicalQueryPart> modifiedQueryParts) throws QueryDistributionException {
		LOG.debug("Begin allocation of query parts");

		try {
			InterfaceParametersPair<IQueryPartAllocator> firstAllocator = allocators.get(0);
			Map<ILogicalQueryPart, PeerID> allocationMap = firstAllocator.getInterface().allocate(modifiedQueryParts, p2pDictionary.getRemotePeerIDs(), p2pNetworkManager.getLocalPeerID(), config, firstAllocator.getParameters());
			
			if (allocationMap == null || allocationMap.isEmpty()) {
				throw new QueryDistributionException("Query part allocation map from allocator '" + firstAllocator.getInterface().getName() + "' is null or empty!");
			}

			if (LOG.isDebugEnabled()) {
				LoggingHelper.printAllocationMap(allocationMap, p2pDictionary);
			}
			
			return allocationMap;
		} catch (QueryPartAllocationException ex) {
			throw new QueryDistributionException("Could not allocate query parts", ex);
		}
	}
}
