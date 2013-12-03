package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.adv.QueryPartAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.registry.DistributionCheckerRegistry;
import de.uniol.inf.is.odysseus.peer.distribute.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.peer.distribute.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.peer.distribute.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.distribute.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.ParameterHelper;

public class QueryDistributor implements IQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributor.class);
	
	private static int jxtaConnectionCounter = 0;

	@Override
	public void distribute(final IServerExecutor serverExecutor, final ISession caller, final Collection<ILogicalQuery> queriesToDistribute, final QueryBuildConfiguration config) {
		Thread t = new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					distributeAsync(serverExecutor, caller, queriesToDistribute, config);
				} catch (QueryDistributionException ex) {
					LOG.error("Could not distribute queries", ex);
				}
			}
		});
		t.setName("Query distribution thread");
		t.setDaemon(true);
		
		t.start();
	}

	private static void distributeAsync(IServerExecutor serverExecutor, ISession caller, Collection<ILogicalQuery> queriesToDistribute, QueryBuildConfiguration config) throws QueryDistributionException {
		Preconditions.checkNotNull(serverExecutor, "Server executor for distributing queries must not be null!");
		Preconditions.checkNotNull(queriesToDistribute, "Collection of queries to distribute must not be null!");
		Preconditions.checkNotNull(config, "QueryBuildConfiguration for distribution must not be null!");

		if (queriesToDistribute.isEmpty()) {
			LOG.warn("Collection of queries to distribute is empty!");
			return;
		}
		
		LOG.debug("Begin with distributing queries");
		LOG.debug("Copying queries");
		Collection<ILogicalQuery> queries = copyAllQueries(queriesToDistribute);

		LOG.debug("{} queries will be distributed if possible.", queries.size());
		
		IQueryPartitioner partitioner = ParameterHelper.determineQueryPartitioner(config);
		IQueryPartModificator modificator = ParameterHelper.determineQueryPartModificator(config);
		IQueryPartAllocator allocator = ParameterHelper.determineQueryPartAllocator(config);
		
		LOG.debug("Using query partitioner: {}", partitioner.getName());
		LOG.debug("Using query part modificator: {}", modificator.getName());
		LOG.debug("Using query part allocator: {}", allocator.getName());
		
		Collection<ILogicalQuery> localQueriesToExecutor = Lists.newArrayList();
		for (ILogicalQuery query : queries) {
			LOG.debug("Start distribution of query {}", query);
			
			Collection<ILogicalOperator> operators = LogicalQueryHelper.getAllOperators(query);
			LogicalQueryHelper.removeTopAOs(operators);
			operators = LogicalQueryHelper.replaceStreamAOs(operators);
			tryCheckDistribution(config, operators);
			
			LOG.debug("Begin partitioning of query {}", query);
			Collection<ILogicalQueryPart> queryParts = tryPartitionQuery(config, partitioner, operators);
			if( queryParts == null || queryParts.isEmpty() ) {
				throw new QueryDistributionException("Query partitioner '" + partitioner.getName() + "' retured null or empty query part list!");
			}
			checkPartitions(queryParts, operators);
			if( LOG.isDebugEnabled()) {
				LOG.debug("Got {} query parts: ", queryParts.size());
				for( ILogicalQueryPart part : queryParts ) {
					LOG.debug("QueryPart: {}", part);
				}
			}
			
			LOG.debug("Begin modifying query parts of query {}", query);
			Collection<ILogicalQueryPart> modifiedQueryParts = tryModifyQueryParts(config, modificator, queryParts);
			if( modifiedQueryParts == null || modifiedQueryParts.isEmpty() ) {
				throw new QueryDistributionException("Query part modificator '" + modificator.getName() + "' retured null or empty query part list!");
			}
			if( LOG.isDebugEnabled()) {
				LOG.debug("Got {} modified query parts: ", modifiedQueryParts.size());
				for( ILogicalQueryPart part : modifiedQueryParts ) {
					LOG.debug("QueryPart: {}", part);
				}
			}
			
			LOG.debug("Begin allocation of query parts");
			Map<ILogicalQueryPart, PeerID> allocationMap = tryAllocate(config, allocator, modifiedQueryParts);
			if( allocationMap == null || allocationMap.isEmpty() ) {
				throw new QueryDistributionException("Query part allocation map from allocator '" + allocator.getName() + "' is null or empty!");
			}
			
			LOG.debug("Check allocation map returned by allocator {}", allocator.getName());
			Map<ILogicalQueryPart, PeerID> correctedAllocationMap = checkAllocationMap(allocationMap, modifiedQueryParts);
			if( LOG.isDebugEnabled() ) {
				for( ILogicalQueryPart part : correctedAllocationMap.keySet() ) {
					PeerID allocatedPeerID = correctedAllocationMap.get(part);
					Optional<String> remotePeerName = P2PDictionaryService.get().getRemotePeerName(allocatedPeerID);
					if( !allocatedPeerID.equals(P2PNetworkManagerService.get().getLocalPeerID())) {
						LOG.debug("Allocated query part {} --> {}", part, remotePeerName.isPresent() ? remotePeerName.get() : "<unknownName>");
					} else {
						LOG.debug("Allocated query part {} --> local", part);
					}
				}
			}
			
			insertJxtaOperators(correctedAllocationMap);
			Collection<ILogicalQueryPart> localQueryParts = distributeToRemotePeers(correctedAllocationMap, config);
			
			if( !localQueryParts.isEmpty() ) {
				LOG.debug("Building local logical query out of {} local query parts", localQueryParts.size());
				
				ILogicalQuery localQuery = buildLocalQuery(localQueryParts);
				localQuery.setName(query.getName());
				localQuery.setUser(caller);
				
				localQueriesToExecutor.add(localQuery);
			} else {
				LOG.debug("No local query part of query {} remains.", query);
			}
		}
		
		if( !localQueriesToExecutor.isEmpty() ) {
			LOG.debug("Calling executor for {} local queries", localQueriesToExecutor.size());
			callExecutorToAddLocalQueries(localQueriesToExecutor, serverExecutor, caller, config);
		} else {
			LOG.debug("There are no local queries in all {} given queries.", queriesToDistribute.size());
		}
	}

	private static void callExecutorToAddLocalQueries(Collection<ILogicalQuery> localQueriesToExecutor, IServerExecutor serverExecutor, ISession caller, QueryBuildConfiguration config) throws QueryDistributionException {
		try {
			for( ILogicalQuery query : localQueriesToExecutor ) {
				serverExecutor.addQuery(query.getLogicalPlan(), caller, config.getName());
			}
		} catch( Throwable ex ) {
			throw new QueryDistributionException("Could not add local query to server executor", ex);
		}
	}

	private static Collection<ILogicalQuery> copyAllQueries(Collection<ILogicalQuery> queriesToDistribute) {
		Collection<ILogicalQuery> copiedQueries = Lists.newArrayList();
		for( ILogicalQuery queryToDistribute : queriesToDistribute ) {
			copiedQueries.add(LogicalQueryHelper.copyLogicalQuery(queryToDistribute));
		}
		
		return copiedQueries;
	}

	private static void tryCheckDistribution(QueryBuildConfiguration config, Collection<ILogicalOperator> operators) throws QueryDistributionException {
		try {
			checkDistribution(operators, config);
		} catch (DistributionCheckException ex) {
			throw new QueryDistributionException("Could not distribute query", ex);
		}
	}

	private static void checkDistribution(Collection<ILogicalOperator> operators, QueryBuildConfiguration config) throws DistributionCheckException {
		ImmutableCollection<IDistributionChecker> checkers = DistributionCheckerRegistry.getInstance().getAll();
		if( checkers.isEmpty() ) {
			LOG.debug("No distribution checkers registered. No checks taken.");
			return;
		}
		
		LOG.debug("Begin distribution checks with {} checkers.", checkers.size());
		for( IDistributionChecker checker : checkers ) {
			LOG.debug("Checking with {}", checker.getClass());
			checker.check(operators, config);
		}
		LOG.debug("All checks passed");
	}

	private static Collection<ILogicalQueryPart> tryPartitionQuery(QueryBuildConfiguration config, IQueryPartitioner partitioner, Collection<ILogicalOperator> operators) throws QueryDistributionException {
		try {
			return partitioner.partition(operators, config);
		} catch (QueryPartitionException ex) {
			throw new QueryDistributionException("Cannot partition query", ex);
		}
	}

	private static void checkPartitions(Collection<ILogicalQueryPart> queryParts, Collection<ILogicalOperator> operators) throws QueryDistributionException {
		Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment = determineOperatorAssignment(queryParts);
		
		for( ILogicalOperator operator : operators ) {
			if( !queryPartAssignment.containsKey(operator)) {
				throw new QueryDistributionException("Operator " + operator + " was not partitioned to a logical query part");
			}
		}
	}

	private static Collection<ILogicalQueryPart> tryModifyQueryParts(QueryBuildConfiguration config, IQueryPartModificator modificator, Collection<ILogicalQueryPart> queryParts) throws QueryDistributionException {
		try {
			return modificator.modify(queryParts, config);
		} catch (QueryPartModificationException ex) {
			throw new QueryDistributionException("Could not modify query parts", ex);
		}
	}

	private static Map<ILogicalQueryPart, PeerID> tryAllocate(QueryBuildConfiguration config, IQueryPartAllocator allocator, Collection<ILogicalQueryPart> modifiedQueryParts) throws QueryDistributionException {
		try {
			return allocator.allocate(modifiedQueryParts, P2PDictionaryService.get().getRemotePeerIDs(), P2PNetworkManagerService.get().getLocalPeerID(), config);
		} catch (QueryPartAllocationException ex) {
			throw new QueryDistributionException("Could not allocate query parts", ex);
		}
	}

	private static Map<ILogicalQueryPart, PeerID> checkAllocationMap(Map<ILogicalQueryPart, PeerID> allocationMap, Collection<ILogicalQueryPart> queryParts) throws QueryDistributionException {
		Map<ILogicalQueryPart, PeerID> correctMap = Maps.newHashMap();
		
		ImmutableList<PeerID> knownRemotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();
		PeerID localPeerID = P2PNetworkManagerService.get().getLocalPeerID();
		
		for( ILogicalQueryPart queryPart : queryParts ) {
			if( allocationMap.containsKey(queryPart)) {
				PeerID allocatedPeer = allocationMap.get(queryPart);
				if( !knownRemotePeerIDs.contains(allocatedPeer) && !allocatedPeer.equals(localPeerID)) {
					throw new QueryDistributionException("Allocated query part " + queryPart + " to an unknown remote peer " + allocatedPeer);
				}
				correctMap.put(queryPart, allocatedPeer);
			} else {
				LOG.warn("Query part {} was not allocated! Allocate to local as default!");
				correctMap.put(queryPart, localPeerID);
			}
		}
		
		return correctMap;
	}
	
	private static void insertJxtaOperators(Map<ILogicalQueryPart, PeerID> allocationMap) {
		Map<ILogicalOperator, ILogicalQueryPart> queryPartAssignment = determineOperatorAssignment(allocationMap.keySet());
		Map<LogicalSubscription, ILogicalOperator> subsToReplace = determineSubscriptionsAcrossQueryParts(queryPartAssignment);

		for (LogicalSubscription subToReplace : subsToReplace.keySet()) {
			ILogicalOperator sourceOperator = subsToReplace.get(subToReplace);
			ILogicalOperator sinkOperator = subToReplace.getTarget();
			LOG.debug("Create JXTA-Connection #{} between {} and {}", new Object[] { jxtaConnectionCounter, sourceOperator, sinkOperator });

			PipeID pipeID = IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID());

			JxtaReceiverAO access = new JxtaReceiverAO();
			access.setPipeID(pipeID.toString());
			access.setOutputSchema(sourceOperator.getOutputSchema());
			access.setSchema(sourceOperator.getOutputSchema().getAttributes());
			access.setName("RCV_" + jxtaConnectionCounter);

			JxtaSenderAO sender = new JxtaSenderAO();
			sender.setPipeID(pipeID.toString());
			sender.setOutputSchema(sourceOperator.getOutputSchema());
			sender.setName("SND_" + jxtaConnectionCounter);

			sourceOperator.unsubscribeSink(subToReplace);

			sourceOperator.subscribeSink(sender, 0, subToReplace.getSourceOutPort(), sourceOperator.getOutputSchema());
			sinkOperator.subscribeToSource(access, subToReplace.getSinkInPort(), 0, access.getOutputSchema());

			jxtaConnectionCounter++;
		}
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
				if( !currentQueryPart.equals(targetQueryPart)) {
					subsToReplace.put(sinkSub, currentOperator);
				}
			}
		}
		return subsToReplace;
	}

	private static Map<ILogicalOperator, ILogicalQueryPart> determineOperatorAssignment(Collection<ILogicalQueryPart> queryParts) {
		Map<ILogicalOperator, ILogicalQueryPart> map = Maps.newHashMap();
		for( ILogicalQueryPart part : queryParts ) {
			for( ILogicalOperator operator : part.getOperators() ) {
				map.put(operator, part);
			}
		}
		return map;
	}

	private static Collection<ILogicalQueryPart> distributeToRemotePeers(Map<ILogicalQueryPart, PeerID> correctedAllocationMap, QueryBuildConfiguration parameters) {
		List<ILogicalQueryPart> localParts = Lists.newArrayList();
		final ID sharedQueryID = IDFactory.newContentID(P2PNetworkManagerService.get().getLocalPeerGroupID(), false, String.valueOf(System.currentTimeMillis()).getBytes());

		for (ILogicalQueryPart part : correctedAllocationMap.keySet()) {
			PeerID peerID = correctedAllocationMap.get(part);

			if (peerID.equals(P2PNetworkManagerService.get().getLocalPeerID())) {
				localParts.add(part);
				LOG.debug("Query part {} stays local", part);
				
			} else {
				final QueryPartAdvertisement adv = (QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
				adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
				adv.setPeerID(peerID);
				adv.setPqlStatement(PQLGeneratorService.get().generatePQLStatement(part.getOperators().iterator().next()));
				adv.setSharedQueryID(sharedQueryID);
				adv.setTransCfgName(parameters.getName());

				JxtaServicesProviderService.get().getDiscoveryService().remotePublish(peerID.toString(), adv, 15000);
				LOG.debug("Sent query part {} to peerID {}", part, peerID);
			}
		}
		return localParts;
	}

	private static ILogicalQuery buildLocalQuery(Collection<ILogicalQueryPart> localQueryParts) {
		Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for( ILogicalQueryPart localPart : localQueryParts ) {
			
			ILogicalOperator firstOp = localPart.getOperators().iterator().next();
			Collection<ILogicalOperator> allOperators = LogicalQueryHelper.getAllOperators(firstOp);
			
			for( ILogicalOperator operator : allOperators ) {
				if( operator.getSubscriptions().isEmpty() ) {
					LOG.debug("Operator {} is one local logical sink", operator);
					sinks.add(operator);
				}
			}
		}
		LOG.debug("Determined {} logical sinks", sinks.size());
		
		TopAO topAO = new TopAO();
		int inputPort = 0;
		for (ILogicalOperator sink : sinks) {
			topAO.subscribeToSource(sink, inputPort++, 0, sink.getOutputSchema());
		}
		LOG.debug("Created TopAO-Operator with {} sinks", sinks.size());

		ILogicalQuery logicalQuery = new LogicalQuery();
		logicalQuery.setLogicalPlan(topAO, true);
		logicalQuery.setParserId("PQL");
		logicalQuery.setPriority(0);
		logicalQuery.setQueryText(PQLGeneratorService.get().generatePQLStatement(topAO));
		LOG.debug("Created logical query with queryText = {}", logicalQuery.getQueryText());

		return logicalQuery;
	}
}
