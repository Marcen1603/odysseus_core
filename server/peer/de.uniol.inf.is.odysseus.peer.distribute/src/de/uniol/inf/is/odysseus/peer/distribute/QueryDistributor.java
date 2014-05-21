package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.part.QueryPartSender;
import de.uniol.inf.is.odysseus.peer.distribute.util.InterfaceParametersPair;
import de.uniol.inf.is.odysseus.peer.distribute.util.LoggingHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.ParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryDistributorHelper;

public class QueryDistributor implements IQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributor.class);
	private static final int MAX_TRANSMISSION_TRIES = 5;
	
	@Override
	public void distribute(final IServerExecutor serverExecutor, final ISession caller, final Collection<ILogicalQuery> queriesToDistribute, final QueryBuildConfiguration config) {
		QueryDistributorThread thread = new QueryDistributorThread(serverExecutor, caller, queriesToDistribute, config);
		thread.start(); // calls distributeImpl (async)
	}

	static synchronized void distributeImpl(IServerExecutor serverExecutor, ISession caller, Collection<ILogicalQuery> queriesToDistribute, QueryBuildConfiguration config) throws QueryDistributionException {
		Preconditions.checkNotNull(serverExecutor, "Server executor for distributing queries must not be null!");
		Preconditions.checkNotNull(caller, "Caller must not be null!");
		Preconditions.checkNotNull(queriesToDistribute, "Collection of queries to distribute must not be null!");
		Preconditions.checkNotNull(config, "QueryBuildConfiguration for distribution must not be null!");

		if (queriesToDistribute.isEmpty()) {
			LOG.warn("Collection of queries to distribute is empty!");
			return;
		}

		LOG.debug("Begin with distributing queries");
		Collection<ILogicalQuery> queries = copyAllQueries(queriesToDistribute);

		LOG.debug("{} queries will be distributed if possible.", queries.size());

		List<InterfaceParametersPair<IQueryDistributionPreProcessor>> preProcessors = ParameterHelper.determineQueryDistributionPreProcessors(config);
		List<InterfaceParametersPair<IQueryPartitioner>> partitioners = ParameterHelper.determineQueryPartitioners(config);
		List<InterfaceParametersPair<IQueryPartModificator>> modificators = ParameterHelper.determineQueryPartModificators(config);
		List<InterfaceParametersPair<IQueryPartAllocator>> allocators = ParameterHelper.determineQueryPartAllocators(config);
		List<InterfaceParametersPair<IQueryDistributionPostProcessor>> postProcessors = ParameterHelper.determineQueryDistributionPostProcessors(config);

		LoggingHelper.printUsedInterfaces(preProcessors, partitioners, modificators, allocators, postProcessors);

		for (ILogicalQuery query : queries) {
			LOG.debug("Start distribution of query {}", query);

			QueryDistributorHelper.tryPreProcess(serverExecutor, caller, config, preProcessors, query);

			Collection<ILogicalOperator> operators = QueryDistributorHelper.collectRelevantOperators(query);
			LOG.debug("Following operators are condidered during distribution: {}", operators);

			QueryDistributorHelper.tryCheckDistribution(config, query, operators);
			Collection<ILogicalQueryPart> queryParts = QueryDistributorHelper.tryPartitionQuery(config, partitioners, operators, query);
			Collection<ILogicalQueryPart> modifiedQueryParts = QueryDistributorHelper.tryModifyQueryParts(config, modificators, queryParts, query);
			Map<ILogicalQueryPart, PeerID> allocationMap = QueryDistributorHelper.tryAllocate(config, allocators, modifiedQueryParts, query);
			
			Map<ILogicalQueryPart, PeerID> allocationMapCopy = copyAllocationMap(allocationMap);
			
			QueryDistributorHelper.tryPostProcess(serverExecutor, caller, allocationMapCopy, config, postProcessors, query);

			QueryPartSender.waitFor();
			
			List<PeerID> faultyPeers = Lists.newArrayList();
			int tries = 0;
			try {
				QueryPartSender.getInstance().transmit(allocationMapCopy, serverExecutor, caller, query.getName(), config);
			} catch( QueryPartTransmissionException ex ) {
				tries++;
				if( tries == MAX_TRANSMISSION_TRIES ) {
					throw new QueryDistributionException("Could not distribute query even after " + tries + " tries. See previous errors for details.");
				}
				
				for( PeerID faultyPeer : ex.getFaultyPeers() ) {
					if( !faultyPeers.contains(faultyPeer)) {
						faultyPeers.add(faultyPeer);
					}
				}
				
				LOG.error("Exception during transmission query parts. Trying to reallocate... try {}", tries);
				allocationMap = QueryDistributorHelper.tryReallocate(config, allocators, allocationMap, faultyPeers);
				allocationMapCopy = copyAllocationMap(allocationMap);
				
				QueryDistributorHelper.tryPostProcess(serverExecutor, caller, allocationMapCopy, config, postProcessors, query);

				QueryPartSender.waitFor();				
			}
		}
	}

	private static Map<ILogicalQueryPart, PeerID> copyAllocationMap(Map<ILogicalQueryPart, PeerID> allocationMap) {
		Map<ILogicalQueryPart, PeerID> copy = Maps.newHashMap();
		
		// copy --> orginial
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartCopyMap = LogicalQueryHelper.copyQueryPartsDeep(allocationMap.keySet());
		// original --> copy
		queryPartCopyMap = revertMap(queryPartCopyMap);
		
		for( ILogicalQueryPart queryPart : allocationMap.keySet() ) {
			ILogicalQueryPart queryPartCopy = queryPartCopyMap.get(queryPart);
			
			copy.put(queryPartCopy, allocationMap.get(queryPart));
		}
		
		return copy;
	}

	private static Map<ILogicalQueryPart, ILogicalQueryPart> revertMap(Map<ILogicalQueryPart, ILogicalQueryPart> queryPartCopyMap) {
		Map<ILogicalQueryPart, ILogicalQueryPart> revertedMap = Maps.newHashMap();
		for( ILogicalQueryPart key : queryPartCopyMap.keySet() ) {
			revertedMap.put(queryPartCopyMap.get(key), key);
		}
		return revertedMap;
	}

	private static Collection<ILogicalQuery> copyAllQueries(Collection<ILogicalQuery> queriesToDistribute) {
		Collection<ILogicalQuery> copiedQueries = Lists.newArrayList();
		for (ILogicalQuery queryToDistribute : queriesToDistribute) {
			copiedQueries.add(LogicalQueryHelper.copyLogicalQuery(queryToDistribute));
		}

		return copiedQueries;
	}
}
