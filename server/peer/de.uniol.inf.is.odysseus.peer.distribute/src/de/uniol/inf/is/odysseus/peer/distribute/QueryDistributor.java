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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.adv.QueryPartAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.distribute.util.IOperatorGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.util.InterfaceParametersPair;
import de.uniol.inf.is.odysseus.peer.distribute.util.LoggingHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.ParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryDistributorHelper;

public class QueryDistributor implements IQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributor.class);

	private static IPQLGenerator pqlGenerator;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IP2PDictionary p2pDictionary;

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}
	
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
	
	@Override
	public void distribute(final IServerExecutor serverExecutor, final ISession caller, final Collection<ILogicalQuery> queriesToDistribute, final QueryBuildConfiguration config) {
		QueryDistributorThread thread = new QueryDistributorThread(serverExecutor, caller, queriesToDistribute, config);
		thread.start(); // calls distributeImpl (async)
	}

	static void distributeImpl(IServerExecutor serverExecutor, ISession caller, Collection<ILogicalQuery> queriesToDistribute, QueryBuildConfiguration config) throws QueryDistributionException {
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

		Collection<ILogicalQuery> localQueriesToExecutor = Lists.newArrayList();
		for (ILogicalQuery query : queries) {
			LOG.debug("Start distribution of query {}", query);
			
			QueryDistributorHelper.tryPreProcess(serverExecutor, caller, config, preProcessors, query);

			Collection<ILogicalOperator> operators = QueryDistributorHelper.collectRelevantOperators(query);
			LOG.debug("Following operators are condidered during distribution: {}", operators);
			
			QueryDistributorHelper.tryCheckDistribution(config, operators);
			Collection<ILogicalQueryPart> queryParts = QueryDistributorHelper.tryPartitionQuery(config, partitioners, operators);
			Collection<ILogicalQueryPart> modifiedQueryParts = QueryDistributorHelper.tryModifyQueryParts(config, modificators, queryParts );
			Map<ILogicalQueryPart, PeerID> allocationMap = QueryDistributorHelper.tryAllocate(config, allocators, modifiedQueryParts);

			if( ParameterHelper.isDoForceLocal(config)) {
				allocationMap = QueryDistributorHelper.forceLocalOperators(allocationMap);
				LoggingHelper.printAllocationMap(allocationMap, p2pDictionary);
			} else {
				LOG.debug("Ommitting forcing operators with 'local' to be locally executed");
			}
			
			if( ParameterHelper.isDoMerge(config)) {
				allocationMap = QueryDistributorHelper.mergeQueryPartsWithSamePeer(allocationMap);
				LoggingHelper.printAllocationMap(allocationMap, p2pDictionary);
			} else {
				LOG.debug("Merging query parts omitted");
			}
			
			QueryDistributorHelper.tryPostProcess(serverExecutor, caller, allocationMap, config, postProcessors);
			
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

	private static Collection<ILogicalQuery> copyAllQueries(Collection<ILogicalQuery> queriesToDistribute) {
		Collection<ILogicalQuery> copiedQueries = Lists.newArrayList();
		for (ILogicalQuery queryToDistribute : queriesToDistribute) {
			copiedQueries.add(LogicalQueryHelper.copyLogicalQuery(queryToDistribute));
		}

		return copiedQueries;
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
				adv.setPqlStatement(pqlGenerator.generatePQLStatement(part.getOperators().iterator().next()));
				adv.setSharedQueryID(sharedQueryID);
				adv.setTransCfgName(parameters.getName());

				jxtaServicesProvider.getDiscoveryService().remotePublish(peerID.toString(), adv, 15000);
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
		logicalQuery.setQueryText(pqlGenerator.generatePQLStatement(topAO));
		LOG.debug("Created logical query with queryText = {}", logicalQuery.getQueryText());

		return logicalQuery;
	}
}
