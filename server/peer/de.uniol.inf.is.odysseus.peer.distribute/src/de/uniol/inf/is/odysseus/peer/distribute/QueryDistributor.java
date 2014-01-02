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
import com.google.common.base.Strings;
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
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.distribute.util.IOperatorGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.util.InterfaceParametersPair;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.ParameterHelper;

public class QueryDistributor implements IQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributor.class);

	@Override
	public void distribute(final IServerExecutor serverExecutor, final ISession caller, final Collection<ILogicalQuery> queriesToDistribute, final QueryBuildConfiguration config) {
		Thread t = new Thread(new Runnable() {
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

		List<InterfaceParametersPair<IQueryDistributionPreProcessor>> preProcessors = ParameterHelper.determineQueryDistributionPreProcessors(config);
		List<InterfaceParametersPair<IQueryPartitioner>> partitioners = ParameterHelper.determineQueryPartitioners(config);
		List<InterfaceParametersPair<IQueryPartModificator>> modificators = ParameterHelper.determineQueryPartModificators(config);
		List<InterfaceParametersPair<IQueryPartAllocator>> allocators = ParameterHelper.determineQueryPartAllocators(config);
		List<InterfaceParametersPair<IQueryDistributionPostProcessor>> postProcessors = ParameterHelper.determineQueryDistributionPostProcessors(config);
		
		if( LOG.isDebugEnabled() ) {
			LOG.debug("Using preprocessors: {}", interfaceListToString(preProcessors));
			LOG.debug("Using (first) partitioner: {}", interfaceListToString(partitioners));
			LOG.debug("Using modificators: {}", interfaceListToString(modificators));
			LOG.debug("Using (first) allocators: {}", interfaceListToString(allocators));
			LOG.debug("Using postprocessors: {}", interfaceListToString(postProcessors));
		}

		Collection<ILogicalQuery> localQueriesToExecutor = Lists.newArrayList();
		for (ILogicalQuery query : queries) {
			LOG.debug("Start distribution of query {}", query);
			
			tryPreProcess(serverExecutor, caller, config, preProcessors, query);

			Collection<ILogicalOperator> operators = collectRelevantOperators(query);
			
			tryCheckDistribution(config, operators);
			Collection<ILogicalQueryPart> queryParts = tryPartitionQuery(config, partitioners, operators);
			Collection<ILogicalQueryPart> modifiedQueryParts = tryModifyQueryParts(config, modificators, queryParts );
			Map<ILogicalQueryPart, PeerID> allocationMap = tryAllocate(config, allocators, modifiedQueryParts);

			if( ParameterHelper.isDoForceLocal(config)) {
				allocationMap = forceLocalOperators(allocationMap);
				if( LOG.isDebugEnabled() ) {
					printAllocationMap(allocationMap);
				}
			} else {
				LOG.debug("Ommitting forcing operators with 'local' to be locally executed");
			}
			
			if( ParameterHelper.isDoMerge(config)) {
				allocationMap = mergeQueryPartsWithSamePeer(allocationMap);
				if( LOG.isDebugEnabled() ) {
					printAllocationMap(allocationMap);
				}
			} else {
				LOG.debug("Merging query parts omitted");
			}
			
			tryPostProcess(serverExecutor, caller, allocationMap, config, postProcessors);
			
			insertJxtaOperators(allocationMap.keySet());
			Collection<ILogicalQueryPart> localQueryParts = distributeToRemotePeers(allocationMap, config);

			if (!localQueryParts.isEmpty()) {
				LOG.debug("Building local logical query out of {} local query parts", localQueryParts.size());

				ILogicalQuery localQuery = buildLocalQuery(localQueryParts);
				localQuery.setName(query.getName());
				localQuery.setUser(caller);

				localQueriesToExecutor.add(localQuery);
			} else {
				LOG.debug("No local query part of query {} remains.", query);
			}
		}

		if (!localQueriesToExecutor.isEmpty()) {
			LOG.debug("Calling executor for {} local queries", localQueriesToExecutor.size());
			callExecutorToAddLocalQueries(localQueriesToExecutor, serverExecutor, caller, config);
		} else {
			LOG.debug("There are no local queries in all {} given queries.", queriesToDistribute.size());
		}
	}

	private static <T extends INamedInterface> String interfaceListToString(List<InterfaceParametersPair<T>> interfaceMap) {
		if( interfaceMap.isEmpty() ) {
			return "{}";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for( InterfaceParametersPair<T> t : interfaceMap) {
			sb.append(t.getInterface().getName());
			
			List<String> params = t.getParameters();
			sb.append("[");
			for( String param : params ) {
				sb.append(param).append(", ");
			}
			sb.append("], ");
		}
		sb.append("}");
		return sb.toString();
	}

	private static void tryPreProcess(IServerExecutor serverExecutor, ISession caller, QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryDistributionPreProcessor>> preProcessors, ILogicalQuery query) throws QueryDistributionException {
		try  {
			for( InterfaceParametersPair<IQueryDistributionPreProcessor> preProcessor : preProcessors ) {
				preProcessor.getInterface().preProcess(serverExecutor, caller, query, config);
			}
			
		} catch( Throwable t ) {
			throw new QueryDistributionException("Could not preprocess query", t);
		}
	}
	
	private static Collection<ILogicalOperator> collectRelevantOperators(ILogicalQuery query) {
		Collection<ILogicalOperator> operators = LogicalQueryHelper.getAllOperators(query);
		LogicalQueryHelper.removeTopAOs(operators);
		operators = LogicalQueryHelper.replaceStreamAOs(operators);
		
		LOG.debug("Following operators are condidered during distribution: {}", operators);
		
		return operators;
	}

	private static void printQueryParts(Collection<ILogicalQueryPart> modifiedQueryParts) {
		LOG.debug("Got {} query parts: ", modifiedQueryParts.size());
		for (ILogicalQueryPart part : modifiedQueryParts) {
			LOG.debug("QueryPart: {}", part);
		}
	}

	private static void printAllocationMap(Map<ILogicalQueryPart, PeerID> allocationMap) {
		for (ILogicalQueryPart part : allocationMap.keySet()) {
			PeerID allocatedPeerID = allocationMap.get(part);
			Optional<String> remotePeerName = P2PDictionaryService.get().getRemotePeerName(allocatedPeerID);
			if (!allocatedPeerID.equals(P2PNetworkManagerService.get().getLocalPeerID())) {
				LOG.debug("Allocated query part {} --> {}", part, remotePeerName.isPresent() ? remotePeerName.get() : "<unknownName>");
			} else {
				LOG.debug("Allocated query part {} --> local", part);
			}
		}
	}

	private static Map<ILogicalQueryPart, PeerID> forceLocalOperators(Map<ILogicalQueryPart, PeerID> partMap) {
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
				resultMap.put(localQueryPart, P2PNetworkManagerService.get().getLocalPeerID());
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
	
	private static Map<ILogicalQueryPart, PeerID> mergeQueryPartsWithSamePeer(Map<ILogicalQueryPart, PeerID> allocationMap) {
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
	
	private static void tryPostProcess(IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> allocationMap, QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryDistributionPostProcessor>> postProcessors) throws QueryDistributionException {
		try {
			for( InterfaceParametersPair<IQueryDistributionPostProcessor> postProcessor : postProcessors ) {
				postProcessor.getInterface().postProcess(serverExecutor, caller, allocationMap, config);
			}
		} catch( Throwable t ) {
			throw new QueryDistributionException("Could not post process query distribution", t);
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

	private static boolean isDestinationNameLocal(ILogicalOperator operator) {
		return !Strings.isNullOrEmpty(operator.getDestinationName()) && operator.getDestinationName().equalsIgnoreCase("local");
	}

	private static void callExecutorToAddLocalQueries(Collection<ILogicalQuery> localQueriesToExecutor, IServerExecutor serverExecutor, ISession caller, QueryBuildConfiguration config) throws QueryDistributionException {
		try {
			for (ILogicalQuery query : localQueriesToExecutor) {
				serverExecutor.addQuery(query.getLogicalPlan(), caller, config.getName());
			}
		} catch (Throwable ex) {
			throw new QueryDistributionException("Could not add local query to server executor", ex);
		}
	}

	private static Collection<ILogicalQuery> copyAllQueries(Collection<ILogicalQuery> queriesToDistribute) {
		Collection<ILogicalQuery> copiedQueries = Lists.newArrayList();
		for (ILogicalQuery queryToDistribute : queriesToDistribute) {
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

	private static Collection<ILogicalQueryPart> tryPartitionQuery(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartitioner>> partitioners, Collection<ILogicalOperator> operators) throws QueryDistributionException {
		LOG.debug("Begin partitioning");

		try {
			InterfaceParametersPair<IQueryPartitioner> firstPartitioner = partitioners.get(0);
			Collection<ILogicalQueryPart> partitions = firstPartitioner.getInterface().partition(operators, config, firstPartitioner.getParameters());
			
			if (partitions == null || partitions.isEmpty()) {
				throw new QueryDistributionException("Query partitioner '" + firstPartitioner.getInterface().getName() + "' retured null or empty query part list!");
			}
			checkPartitions(partitions, operators);
			if (LOG.isDebugEnabled()) {
				printQueryParts(partitions);
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

	private static Collection<ILogicalQueryPart> tryModifyQueryParts(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartModificator>> modificators, Collection<ILogicalQueryPart> queryParts) throws QueryDistributionException {
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
				printQueryParts(currentPartitionState);
			}

			return currentPartitionState;
		} catch (QueryPartModificationException ex) {
			throw new QueryDistributionException("Could not modify query parts", ex);
		}
	}

	private static Map<ILogicalQueryPart, PeerID> tryAllocate(QueryBuildConfiguration config, List<InterfaceParametersPair<IQueryPartAllocator>> allocators, Collection<ILogicalQueryPart> modifiedQueryParts) throws QueryDistributionException {
		LOG.debug("Begin allocation of query parts");

		try {
			InterfaceParametersPair<IQueryPartAllocator> firstAllocator = allocators.get(0);
			Map<ILogicalQueryPart, PeerID> allocationMap = firstAllocator.getInterface().allocate(modifiedQueryParts, P2PDictionaryService.get().getRemotePeerIDs(), P2PNetworkManagerService.get().getLocalPeerID(), config, firstAllocator.getParameters());
			
			if (allocationMap == null || allocationMap.isEmpty()) {
				throw new QueryDistributionException("Query part allocation map from allocator '" + firstAllocator.getInterface().getName() + "' is null or empty!");
			}

			LOG.debug("Check allocation map returned by allocator {}", firstAllocator.getInterface().getName());
			checkAllocationMap(allocationMap, modifiedQueryParts);
			if (LOG.isDebugEnabled()) {
				printAllocationMap(allocationMap);
			}
			
			return allocationMap;
		} catch (QueryPartAllocationException ex) {
			throw new QueryDistributionException("Could not allocate query parts", ex);
		}
	}

	private static void checkAllocationMap(Map<ILogicalQueryPart, PeerID> allocationMap, Collection<ILogicalQueryPart> queryParts) throws QueryDistributionException {
		ImmutableList<PeerID> knownRemotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();
		PeerID localPeerID = P2PNetworkManagerService.get().getLocalPeerID();

		for (ILogicalQueryPart queryPart : queryParts) {
			if (allocationMap.containsKey(queryPart)) {
				PeerID allocatedPeer = allocationMap.get(queryPart);
				if (!knownRemotePeerIDs.contains(allocatedPeer) && !allocatedPeer.equals(localPeerID)) {
					throw new QueryDistributionException("Allocated query part " + queryPart + " to an unknown remote peer " + allocatedPeer);
				}
			} else {
				throw new QueryDistributionException("Query part " + queryPart + " is not allocated!");
			}
		}
	}

	private static void insertJxtaOperators(Collection<ILogicalQueryPart> queryParts) {
		LogicalQueryHelper.disconnectQueryParts(queryParts, new IOperatorGenerator() {
			
			private PipeID pipeID;
			private ILogicalOperator sourceOp;
			
			@Override
			public void beginDisconnect(ILogicalOperator sourceOperator, ILogicalOperator sinkOperator) {
				LOG.debug("Create JXTA-Connection between {} and {}", new Object[] {sourceOperator, sinkOperator });
				
				pipeID = IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID());
				sourceOp = sourceOperator;
			}
			
			@Override
			public ILogicalOperator createSourceofSink(ILogicalOperator sink) {
				JxtaReceiverAO access = new JxtaReceiverAO();
				access.setPipeID(pipeID.toString());
				access.setSchema(sourceOp.getOutputSchema().getAttributes());
				return access;
			}
			
			@Override
			public ILogicalOperator createSinkOfSource(ILogicalOperator source) {
				JxtaSenderAO sender = new JxtaSenderAO();
				sender.setPipeID(pipeID.toString());
				return sender;
			}
			
			@Override
			public void endDisconnect() {
				pipeID = null;
				sourceOp = null;
			}
		});
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
				LOG.debug("PQL-Query of query part {} is\n{}", part, adv.getPqlStatement());
			}
		}
		return localParts;
	}

	private static ILogicalQuery buildLocalQuery(Collection<ILogicalQueryPart> localQueryParts) {
		Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for( ILogicalQueryPart localPart : localQueryParts ) {
			
			ILogicalOperator firstOp = localPart.getOperators().iterator().next();
			Collection<ILogicalOperator> allOperators = LogicalQueryHelper.getAllOperators(firstOp);
			
			sinks.addAll(LogicalQueryHelper.getSinks(allOperators));
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
