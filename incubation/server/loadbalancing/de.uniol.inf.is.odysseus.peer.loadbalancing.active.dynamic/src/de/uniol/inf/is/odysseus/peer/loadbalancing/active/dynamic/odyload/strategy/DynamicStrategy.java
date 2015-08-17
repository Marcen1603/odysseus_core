package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OsgiServiceProvider;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.ICommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IMonitoringThreadListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IQueryTransmissionHandlerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.CommunicatorChooser.OdyLoadCommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.CostEstimationHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.GreedyQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.QueryCostMap;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.SimulatedAnnealingQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.transfer.QueryTransmissionHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.trigger.MonitoringThread;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SharedQueryIDModifier;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SinkTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SourceTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.TransformationHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class DynamicStrategy implements ILoadBalancingStrategy,
		IMonitoringThreadListener, IQueryTransmissionHandlerListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(DynamicStrategy.class);
	private Object threadManipulationLock = new Object();

	private ILoadBalancingAllocator allocator;
	private static ISession activeSession;

	private QueryTransmissionHandler transmissionHandler;

	private List<Integer> failedAllocationQueryIDs;

	private MonitoringThread monitoringThread = null;

	private boolean firstAllocationTry = true;

	/**
	 * Gets currently active Session.
	 * 
	 * @return active Session
	 */
	public static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}

	@Override
	public void setAllocator(ILoadBalancingAllocator allocator) {
		this.allocator = allocator;
	}

	@Override
	public void setCommunicator(ILoadBalancingCommunicator communicator) {
		LOG.error("Setting of Communicator not implemented yet.");

	}

	@Override
	public void startMonitoring() throws LoadBalancingException {

		startNewMonitoringThread();

	}

	private void startNewMonitoringThread() {

		IPeerResourceUsageManager usageManager = OsgiServiceProvider
				.getUsageManager();

		if (usageManager == null) {
			LOG.error("Could not start monitoring: No resource Usage Manager bound.");
			return;
		}

		synchronized (threadManipulationLock) {
			if (monitoringThread == null) {
				LOG.info("Starting to monitor Peer.");
				monitoringThread = new MonitoringThread(usageManager, this);
				monitoringThread.start();
			} else {
				LOG.info("Monitoring Thread already running.");
			}
		}
	}

	@Override
	public void stopMonitoring() {
		synchronized (threadManipulationLock) {
			if (monitoringThread != null) {
				LOG.info("Stopping monitoring Peer.");
				monitoringThread.setInactive();
				monitoringThread = null;
			} else {
				LOG.info("No monitoring Thread running.");
			}
		}

	}

	@Override
	public String getName() {
		return OdyLoadConstants.STRATEGY_NAME;
	}

	private HashMap<Integer, IPhysicalQuery> getPhysicalQueries() {

		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		IExcludedQueriesRegistry excludedQueryRegistry = OsgiServiceProvider
				.getExcludedQueryRegistry();

		HashMap<Integer, IPhysicalQuery> queries = new HashMap<Integer, IPhysicalQuery>();

		for (int queryId : executor.getLogicalQueryIds(getActiveSession())) {

			// Ignore excluded Quries.
			if (excludedQueryRegistry
					.isQueryIDExcludedFromLoadBalancing(queryId))
				continue;
			IPhysicalQuery query = executor.getExecutionPlan().getQueryById(
					queryId);
			if (query == null)
				continue;
			queries.put(queryId, query);
		}

		return queries;
	}

	@Override
	public void triggerLoadBalancing(double cpuUsage, double memUsage,
			double netUsage) {

		Preconditions.checkNotNull(this.allocator, "No allocator bound.");
		IServerExecutor executor = OsgiServiceProvider.getExecutor();

		if (executor.getLogicalQueryIds(getActiveSession()).size() == 0) {
			LOG.warn("Load Balancing triggered, but no queries installed. Continuing monitoring.");
			return;
		}

		stopMonitoringThread();

		firstAllocationTry = true;

		LOG.info("Re-Allocation of queries triggered.");

		double cpuLoadToRemove = Math.max(0.0, cpuUsage
				- OdyLoadConstants.CPU_THRESHOLD);
		double memLoadToRemove = Math.max(0.0, memUsage
				- OdyLoadConstants.MEM_THRESHOLD);
		double netLoadToRemove = Math.max(0.0, netUsage
				- OdyLoadConstants.NET_THRESHOLD);

		Collection<Integer> queryIDs = getNotExcludedQueries();

		// Add Shared QueryID to every Query if needed.
		for (int queryID : queryIDs) {
			SharedQueryIDModifier.addSharedQueryIDIfNeccessary(queryID,
					getActiveSession());
		}

		QueryCostMap chosenResult;
		try {
			chosenResult = selectQueriesToRemove(cpuLoadToRemove,
					memLoadToRemove, netLoadToRemove);
		} catch (LoadBalancingException e) {
			LOG.error("Exception in Allocation: {}", e.getMessage());
			e.printStackTrace();
			startNewMonitoringThread();
			return;
		}

		allocateAndTransferQueries(chosenResult);

	}

	/***
	 * Transforms Sinks and Sources in Failed Queries to JxtaReceiver-Sender
	 * Constructs as this might have been a reason for the failure...
	 */
	private void transformFailedQueries(List<Integer> failedQueries) {

		IP2PNetworkManager networkManager = OsgiServiceProvider
				.getNetworkManager();

		for (int queryID : failedQueries) {
			if (TransformationHelper.hasRealSinks(queryID)) {
				SinkTransformer.replaceSinks(queryID,
						networkManager.getLocalPeerID(), getActiveSession());
			}
			if (TransformationHelper.hasRealSources(queryID)) {
				SourceTransformer.replaceSources(queryID,
						networkManager.getLocalPeerID(), getActiveSession());
			}
		}
	}

	public void allocateAndTransferQueries(QueryCostMap chosenResult) {

		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		IPeerDictionary peerDictionary = OsgiServiceProvider
				.getPeerDictionary();
		IP2PNetworkManager networkManager = OsgiServiceProvider
				.getNetworkManager();

		HashMap<ILogicalQueryPart, Integer> queryPartIDMapping = new HashMap<ILogicalQueryPart, Integer>();

		for (int queryId : chosenResult.getQueryIds()) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId,
					getActiveSession());
			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			queryPartIDMapping.put(new LogicalQueryPart(operators), queryId);
		}

		if (chosenResult.getQueryIds().size() == 0) {
			LOG.error("No Queries to remove.");
			startNewMonitoringThread();
			return;
		}

		Collection<PeerID> knownRemotePeers = peerDictionary.getRemotePeerIDs();

		allocateQueries(networkManager.getLocalPeerID(), chosenResult,
				queryPartIDMapping, knownRemotePeers);

		if (transmissionHandler != null) {
			transmissionHandler.addListener(this);
			transmissionHandler.startTransmissions();
		} else {
			LOG.info("No transmission to do... Restarting Monitoring Thread.");
			startNewMonitoringThread();
		}
	}

	private void stopMonitoringThread() {
		synchronized (threadManipulationLock) {
			if (monitoringThread != null) {
				monitoringThread.removeListener(this);
				monitoringThread.setInactive();
				monitoringThread = null;
			}
		}
	}

	private QueryCostMap selectQueriesToRemove(double cpuLoadToRemove,
			double memLoadToRemove, double netLoadToRemove)
			throws LoadBalancingException {
		QueryCostMap allQueries = generateCostMapForAllQueries();
		IQuerySelectionStrategy greedySelector = new GreedyQuerySelector();
		QueryCostMap greedyResult = greedySelector.selectQueries(
				allQueries.clone(), cpuLoadToRemove, memLoadToRemove,
				netLoadToRemove);
		IQuerySelectionStrategy simulatedAnnealingSelector = new SimulatedAnnealingQuerySelector(
				greedyResult);
		QueryCostMap simulatedAnnealingResult = simulatedAnnealingSelector
				.selectQueries(allQueries.clone(), cpuLoadToRemove,
						memLoadToRemove, netLoadToRemove);

		QueryCostMap chosenResult;
		if (simulatedAnnealingResult == null) {
			LOG.info("No feasible simulated Annealing Result found. Using Greedy Result.");
			chosenResult = greedyResult;
		} else if (greedyResult.getCosts() < simulatedAnnealingResult
				.getCosts()) {
			LOG.info(
					"Greedy result is better than Simulated Annealing result ({}<{}), choosing Greedy result.",
					greedyResult.getCosts(),
					simulatedAnnealingResult.getCosts());
			chosenResult = greedyResult;
		} else {
			LOG.info(
					"Simulated Annealing result is better than Greedy result ({}<{}), choosing Simulated Annealing result.",
					simulatedAnnealingResult.getCosts(),
					greedyResult.getCosts());
			chosenResult = simulatedAnnealingResult;
		}

		if (allocator == null) {
			throw new LoadBalancingException("No Allocator set.");
		}
		return chosenResult;
	}

	private void allocateQueries(PeerID localPeerID, QueryCostMap chosenResult,
			HashMap<ILogicalQueryPart, Integer> queryPartIDMapping,
			Collection<PeerID> knownRemotePeers) {

		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		IPeerDictionary peerDictionary = OsgiServiceProvider
				.getPeerDictionary();

		failedAllocationQueryIDs = Lists.newArrayList();

		int firstQueryId = chosenResult.getQueryIds().get(0);

		// Parameter Query is used to get Build Configuration...
		ILogicalQuery query = executor.getLogicalQueryById(firstQueryId,
				getActiveSession());

		try {
			Map<ILogicalQueryPart, PeerID> allocationMap = allocator.allocate(
					queryPartIDMapping.keySet(), query, knownRemotePeers,
					localPeerID);

			if (LOG.isDebugEnabled()) {
				LOG.debug("Allocation finished.");
				LOG.debug("Local PID is {}", localPeerID.toString());
				for (ILogicalQueryPart queryPart : allocationMap.keySet()) {
					if (allocationMap.get(queryPart).equals(localPeerID)) {
						LOG.warn("No other Peer wanted to take Query {}.",
								queryPartIDMapping.get(queryPart));
						failedAllocationQueryIDs.add(queryPartIDMapping
								.get(queryPart));

					} else {
						LOG.debug("({} goes to Peer {}", queryPartIDMapping
								.get(queryPart),
								peerDictionary.getRemotePeerName(allocationMap
										.get(queryPart)));
					}
				}
			}

			ICommunicatorChooser communicatorChooser = new OdyLoadCommunicatorChooser();

			HashMap<Integer, ILoadBalancingCommunicator> communicatorMapping = communicatorChooser
					.chooseCommunicators(chosenResult.getQueryIds(),
							getActiveSession());

			this.transmissionHandler = new QueryTransmissionHandler();
			

			for (ILogicalQueryPart queryPart : allocationMap.keySet()) {
				// Create a Query Transmission handler for every transmission we
				// have to do.
				int queryId = queryPartIDMapping.get(queryPart);
				PeerID slavePeerId = allocationMap.get(queryPart);
				if (slavePeerId.equals(localPeerID))
					continue;
				ILoadBalancingCommunicator communicator = communicatorMapping
						.get(queryId);
				transmissionHandler.addTransmission(queryId, slavePeerId,
						communicator);
			}
			
			transmissionHandler.startTransmissions();

		} catch (QueryPartAllocationException e) {
			LOG.error("Could not allocate Query Parts: {}", e.getMessage());
			e.printStackTrace();
		}

	}

	public QueryCostMap generateCostMapForAllQueries() {

		HashMap<Integer, IPhysicalQuery> queries = getPhysicalQueries();

		QueryCostMap queryCostMap = new QueryCostMap();
		// Get cost and load information for each query.
		for (int queryId : queries.keySet()) {

			Set<IPhysicalOperator> operatorList = queries.get(queryId)
					.getAllOperators();

			CostEstimationHelper.addQueryToCostMap(queryCostMap, queryId,
					operatorList);
		}
		return queryCostMap;
	}

	@Override
	public void forceLoadBalancing() throws LoadBalancingException {
		triggerLoadBalancing(OdyLoadConstants.CPU_THRESHOLD + 0.01,
				OdyLoadConstants.MEM_THRESHOLD + 0.01,
				OdyLoadConstants.NET_THRESHOLD + 0.01);

	}

	private Collection<Integer> getNotExcludedQueries() {

		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		IExcludedQueriesRegistry excludedQueryRegistry = OsgiServiceProvider
				.getExcludedQueryRegistry();

		Collection<Integer> queryIDs = executor
				.getLogicalQueryIds(getActiveSession());
		Iterator<Integer> iter = queryIDs.iterator();
		while (iter.hasNext()) {
			int nextQId = iter.next();
			if (excludedQueryRegistry
					.isQueryIDExcludedFromLoadBalancing(nextQId)) {
				iter.remove();
			}
		}
		return queryIDs;
	}

	@Override
	public void transmissionsFinished() {

		List<Integer> queriesToReprocess = Lists.newArrayList();

		transmissionHandler.removeListener(this);
		List<Integer> failedTransmissions = transmissionHandler
				.getFailedTransmissions();
		transmissionHandler = null;

		if (failedTransmissions != null) {
			queriesToReprocess.addAll(failedTransmissions);
		}
		if (failedAllocationQueryIDs != null) {
			queriesToReprocess.addAll(failedAllocationQueryIDs);
		}

		if (firstAllocationTry && queriesToReprocess.size() > 0) {
			LOG.debug("Second try.");
			reallocateAndTransmitQueries(queriesToReprocess);
		} else {
			LOG.info("Load Balancing Process finished. Restarting Monitoring.");
			startNewMonitoringThread();
		}

	}

	private void reallocateAndTransmitQueries(List<Integer> queriesToReprocess) {

		firstAllocationTry = false;

		IServerExecutor executor = OsgiServiceProvider.getExecutor();

		transformFailedQueries(queriesToReprocess);

		QueryCostMap costMapOfFailedQueries = new QueryCostMap();

		// Build new CostMap
		for (int queryID : queriesToReprocess) {
			Set<IPhysicalOperator> operatorsInQuery = executor
					.getExecutionPlan().getQueryById(queryID).getAllOperators();
			CostEstimationHelper.addQueryToCostMap(costMapOfFailedQueries,
					queryID, operatorsInQuery);
		}

		// And allocate another time...
		allocateAndTransferQueries(costMapOfFailedQueries);

	}

}
