package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
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
	
	private static Map<ILogicalOperator, ILogicalQueryPart> determineOperatorAssignment(Collection<ILogicalQueryPart> queryParts) {
		Map<ILogicalOperator, ILogicalQueryPart> map = Maps.newHashMap();
		for (ILogicalQueryPart part : queryParts) {
			for (ILogicalOperator operator : part.getOperators()) {
				map.put(operator, part);
			}
		}
		return map;
	}

	public static void tryPostProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> allocationMap, QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryDistributionPostProcessor>> postProcessors, ILogicalQuery query) throws QueryDistributionException {
		try {
			for( InterfaceParametersPair<IQueryDistributionPostProcessor> postProcessor : postProcessors ) {
				postProcessor.getInterface().postProcess(serverExecutor, caller, allocationMap, query, config);
			}
		} catch( Throwable t ) {
			throw new QueryDistributionException("Could not post process query distribution", t);
		}
	}
	

	public static void tryCheckDistribution(QueryBuildConfiguration config, ILogicalQuery query, Collection<ILogicalOperator> operators) throws QueryDistributionException {
		try {
			checkDistribution(operators, query, config);
		} catch (DistributionCheckException ex) {
			throw new QueryDistributionException("Could not distribute query", ex);
		}
	}

	private static void checkDistribution(Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config) throws DistributionCheckException {
		ImmutableCollection<String> names = DistributionCheckerRegistry.getInstance().getNames();
		if (names.isEmpty()) {
			LOG.debug("No distribution checkers registered. No checks taken.");
			return;
		}

		LOG.debug("Begin distribution checks with {} checkers.", names.size());
		for (String name : names) {
			IDistributionChecker checker = DistributionCheckerRegistry.getInstance().get(name);
			LOG.debug("Checking with {}", checker.getName());
			
			checker.check(operators, query, config);
		}
		LOG.debug("All checks passed");
	}

	public static Collection<ILogicalQueryPart> tryPartitionQuery(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartitioner>> partitioners, Collection<ILogicalOperator> operators, ILogicalQuery query) throws QueryDistributionException {
		LOG.debug("Begin partitioning");

		try {
			InterfaceParametersPair<IQueryPartitioner> firstPartitioner = partitioners.get(0);
			Collection<ILogicalQueryPart> partitions = firstPartitioner.getInterface().partition(operators, query, config, firstPartitioner.getParameters());
			
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

	public static Collection<ILogicalQueryPart> tryModifyQueryParts(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartModificator>> modificators, Collection<ILogicalQueryPart> queryParts, ILogicalQuery query) throws QueryDistributionException {
		if( modificators.isEmpty() ) {
			LOG.debug("No modificator set");
			return queryParts;
		}
		
		LOG.debug("Begin modifying query parts");

		try {
			Collection<ILogicalQueryPart> currentPartitionState = queryParts;
			for( InterfaceParametersPair<IQueryPartModificator> modificator : modificators ) {
				currentPartitionState = modificator.getInterface().modify(currentPartitionState, query, config, modificator.getParameters());
				
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

	public static Map<ILogicalQueryPart, PeerID> tryAllocate(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartAllocator>> allocators, Collection<ILogicalQueryPart> modifiedQueryParts, ILogicalQuery query) throws QueryDistributionException {
		LOG.debug("Begin allocation of query parts");

		try {
			InterfaceParametersPair<IQueryPartAllocator> firstAllocator = allocators.get(0);
			Map<ILogicalQueryPart, PeerID> allocationMap = firstAllocator.getInterface().allocate(modifiedQueryParts, query, p2pDictionary.getRemotePeerIDs(), p2pNetworkManager.getLocalPeerID(), config, firstAllocator.getParameters());
			
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
	
	public static Map<ILogicalQueryPart, PeerID> tryReallocate( QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartAllocator>> allocators, Map<ILogicalQueryPart, PeerID> previousAllocationMap, Collection<PeerID> faultyPeers ) throws QueryDistributionException {
		LOG.debug("Begin reallocation of query parts");
		
		try {
			InterfaceParametersPair<IQueryPartAllocator> firstAllocator = allocators.get(0);
			Map<ILogicalQueryPart, PeerID> allocationMap = firstAllocator.getInterface().reallocate(previousAllocationMap, faultyPeers, p2pDictionary.getRemotePeerIDs(), p2pNetworkManager.getLocalPeerID(), config, firstAllocator.getParameters());
			
			if (allocationMap == null || allocationMap.isEmpty()) {
				throw new QueryDistributionException("Query part allocation map from allocator '" + firstAllocator.getInterface().getName() + "' is null or empty!");
			}

			if (LOG.isDebugEnabled()) {
				LoggingHelper.printAllocationMap(allocationMap, p2pDictionary);
			}
			
			return allocationMap;
		} catch (QueryPartAllocationException ex) {
			throw new QueryDistributionException("Could not reallocate query parts", ex);
		}
	}
	
	public static String toPeerNames(List<PeerID> faultyPeers) {
		StringBuilder sb = new StringBuilder();
		for( PeerID pid : faultyPeers ) {
			sb.append(p2pDictionary.getRemotePeerName(pid)).append(" ");
		}
		return sb.toString();
	}
}
