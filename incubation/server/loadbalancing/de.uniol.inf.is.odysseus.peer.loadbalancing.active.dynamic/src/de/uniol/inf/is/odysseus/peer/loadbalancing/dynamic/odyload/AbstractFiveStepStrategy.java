package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload;

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
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ICommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingTrigger;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQueryTransmissionHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryCostMap;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.ILoadBalancingTriggerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.IQueryTransmissionHandlerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing.CalcLatencyPOTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing.DataratePOTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing.SharedQueryIDModifier;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing.SinkTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing.SourceTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.preprocessing.TransformationHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public abstract class AbstractFiveStepStrategy implements ILoadBalancingStrategy,ILoadBalancingTriggerListener, IQueryTransmissionHandlerListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractFiveStepStrategy.class);

	private static ISession activeSession;

	private List<Integer> failedAllocationQueryIDs;

	private boolean firstAllocationTry = true;
	
	private boolean strategyIsRunning=false;
	
	private ILoadBalancingTrigger trigger;
	private List<IQuerySelector> selectors;
	private ILoadBalancingAllocator allocator;
	private ICommunicatorChooser communicatorChooser;
	private IQueryTransmissionHandler transmissionHandler;
	
	
	protected void setTrigger(String name) {
		trigger = OsgiServiceProvider.getTriggerRegistry().getTrigger(name);
	}
	
	protected void setSelector(String name) {
		selectors = Lists.newArrayList();
		selectors.add(OsgiServiceProvider.getSelectorRegistry().getSelector(name));
	}
	
	protected void setSelectors(List<String> names) {
		selectors = Lists.newArrayList();
		for(String selectorName : names) {
			selectors.add(OsgiServiceProvider.getSelectorRegistry().getSelector(selectorName));
		}
	}
	

	protected void setAllocator(String name) {
		allocator = OsgiServiceProvider.getAllocatorRegistry().getAllocator(name);
	}
	
	protected void setCommunicatorChooser(String name) {
		communicatorChooser = OsgiServiceProvider.getCommunicatorChooserRegistry().getCommunicatorChooser(name);
	}
	
	protected void setTransmissionHandler(String name) {
		transmissionHandler = OsgiServiceProvider.getTransmissionHandlerRegistry().getTransmissionHandler(name);
	}
	

	@Override
	public void triggerLoadBalancing(double cpuUsage, double memUsage,
			double netUsage) {

		Preconditions.checkNotNull(allocator,"No Allocator bound.");
		Preconditions.checkNotNull(communicatorChooser,"No Communicator Chooser bound.");
		Preconditions.checkNotNull(transmissionHandler,"No TransmissionHandler bound.");
		Preconditions.checkNotNull(selectors,"No Selectors bound.");
		Preconditions.checkNotNull(trigger,"Trigger is null.");
		for(IQuerySelector selector : selectors) {
			Preconditions.checkNotNull(selector,"At least one selector is null.");
		}
		
		IServerExecutor executor = OsgiServiceProvider.getExecutor();

		if (executor.getLogicalQueryIds(getActiveSession()).size() == 0) {
			LOG.warn("Load Balancing triggered, but no queries installed. Continuing monitoring.");
			return;
		}
		trigger.removeListener(this);
		trigger.setInactive();

		firstAllocationTry = true;

		LOG.info("Re-Allocation of queries triggered.");

		//TODO this has to be in the trigger!
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
					getActiveSession(),OsgiServiceProvider.getExecutor(),OsgiServiceProvider.getQueryPartController(),OsgiServiceProvider.getNetworkManager());
		}

		QueryCostMap chosenResult;
		try {
			chosenResult = selectQueriesToRemove(cpuLoadToRemove,
					memLoadToRemove, netLoadToRemove);
		} catch (LoadBalancingException e) {
			LOG.error("Exception in Allocation: {}", e.getMessage());
			e.printStackTrace();
			restartMonitoring();
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
			if (TransformationHelper.hasRealSinks(queryID,OsgiServiceProvider.getExecutor())) {
				if(TransformationHelper.hasCalclatencyPOs(queryID,OsgiServiceProvider.getExecutor())) {
					LOG.debug("Found CalcLatencyPO in Query. Using CalcLatencyTransformer instead.");
					CalcLatencyPOTransformer.replaceCalcLatencyPO(queryID, networkManager.getLocalPeerID(), getActiveSession(),OsgiServiceProvider.getExecutor(),OsgiServiceProvider.getNetworkManager(),OsgiServiceProvider.getExcludedQueriesRegistry(),OsgiServiceProvider.getQueryPartController());
				}
				else {
					SinkTransformer.replaceSinks(queryID,
							networkManager.getLocalPeerID(), getActiveSession(),OsgiServiceProvider.getExecutor(),OsgiServiceProvider.getNetworkManager(),OsgiServiceProvider.getExcludedQueriesRegistry(),OsgiServiceProvider.getQueryPartController());
				}
				}
			
			if (TransformationHelper.hasRealSources(queryID,OsgiServiceProvider.getExecutor())) {
				if(TransformationHelper.hasDataratePOs(queryID,OsgiServiceProvider.getExecutor())) {
					LOG.debug("Found DataratePO in Query. Using DatarateTransformer instead.");
					DataratePOTransformer.replaceDataratePOs(queryID, networkManager.getLocalPeerID(), getActiveSession(),OsgiServiceProvider.getExecutor(),OsgiServiceProvider.getNetworkManager(),OsgiServiceProvider.getExcludedQueriesRegistry(),OsgiServiceProvider.getQueryPartController());
				}
				else {
					SourceTransformer.replaceSources(queryID,
							networkManager.getLocalPeerID(), getActiveSession(),OsgiServiceProvider.getExecutor(),OsgiServiceProvider.getNetworkManager(),OsgiServiceProvider.getExcludedQueriesRegistry(),OsgiServiceProvider.getQueryPartController());
				}
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
			restartMonitoring();
			return;
		}

		Collection<PeerID> knownRemotePeers = peerDictionary.getRemotePeerIDs();

		allocateQueries(networkManager.getLocalPeerID(), chosenResult,
				queryPartIDMapping, knownRemotePeers);

		
		
		transmissionHandler.addListener(this);
		transmissionHandler.startTransmissions();
	}


	private QueryCostMap selectQueriesToRemove(double cpuLoadToRemove,
			double memLoadToRemove, double netLoadToRemove)
			throws LoadBalancingException {
		QueryCostMap allQueries = CostEstimationHelper.generateCostMapForAllQueries();
		List<QueryCostMap> results = Lists.newArrayList();

		QueryCostMap chosenResult=null;
		for(IQuerySelector selector : selectors) {
			QueryCostMap result = selector.selectQueries(new QueryCostMap(allQueries), cpuLoadToRemove, memLoadToRemove, netLoadToRemove);
			LOG.debug("Result of Selector: {}",selector.getName());
			LOG.debug(result.toString());
			if(result!=null) {
				results.add(result);
			}
			if(chosenResult==null || chosenResult.getCosts()>result.getCosts()) {
				LOG.debug("Setting last result as best result.");
				//TODO Feasability!
				chosenResult = result;
			}
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
			transmissionHandler.clear();
			transmissionHandler.setCommunicatorChooser(communicatorChooser);
			

			for (ILogicalQueryPart queryPart : allocationMap.keySet()) {
				// Create a Query Transmission handler for every transmission we
				// have to do.
				int queryId = queryPartIDMapping.get(queryPart);
				PeerID slavePeerId = allocationMap.get(queryPart);
				if (slavePeerId.equals(localPeerID))
					continue;
				transmissionHandler.addTransmission(queryId, slavePeerId);
			}
			

		} catch (QueryPartAllocationException e) {
			LOG.error("Could not allocate Query Parts: {}", e.getMessage());
			e.printStackTrace();
		}

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
				.getExcludedQueriesRegistry();

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
		
		transmissionHandler.clear();

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
			restartMonitoring();
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
		// empty as this is not needed.
		//TODO Remove?
		
	}

	@Override
	public void setCommunicator(ILoadBalancingCommunicator communicator) {
		// empty as this is not needed.
		//TODO Remove?
		
	}

	@Override
	public void startMonitoring() throws LoadBalancingException {
		Preconditions.checkNotNull(trigger,"Trigger must not be null!");
		strategyIsRunning = true;
		restartMonitoring();
	}

	@Override
	public void stopMonitoring() {
		strategyIsRunning = false;
		trigger.removeListener(this);
		trigger.setInactive();
	}
	
	private void restartMonitoring() {
		Preconditions.checkNotNull(trigger,"Trigger is null");
		if(strategyIsRunning) {
			trigger.addListener(this);
			trigger.start();
		}
		else {
			LOG.debug("Not restarting Monitoring thread as strategy is not running.");
		}
	}

}
