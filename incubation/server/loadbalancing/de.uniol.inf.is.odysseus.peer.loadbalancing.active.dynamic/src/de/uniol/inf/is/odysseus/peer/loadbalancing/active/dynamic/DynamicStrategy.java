package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SharedQueryIDModifier;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingCommunicatorRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class DynamicStrategy implements ILoadBalancingStrategy, IMonitoringThreadListener, IQueryTransmissionHandlerListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(DynamicStrategy.class);
	private Object threadManipulationLock = new Object();
	private IPeerResourceUsageManager usageManager;
	private IServerExecutor executor;
	private static ISession activeSession;
	private IPhysicalCostModel physicalCostModel;
	private ILoadBalancingAllocator allocator;
	private IPeerDictionary peerDictionary;
	private IP2PNetworkManager networkManager;
	private ILoadBalancingLock lock;
	private ILoadBalancingCommunicatorRegistry communicatorRegistry;
	private IPeerCommunicator peerCommunicator;
	private List<QueryTransmissionHandler> transmissionHandlerList;
	private IQueryPartController queryPartController;
	private IExcludedQueriesRegistry excludedQueryRegistry;
	

	private MonitoringThread monitoringThread = null;
	
	
	public void bindExcludedQueryRegistry(IExcludedQueriesRegistry serv) {
		this.excludedQueryRegistry = serv;
	}
	
	public void unbindExcludedQueryRegistry(IExcludedQueriesRegistry serv) {
		if(this.excludedQueryRegistry==serv) {
			this.excludedQueryRegistry = null;
		}
	}
	
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		this.peerCommunicator = serv;
	}
	
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if(this.peerCommunicator==serv) {
			this.peerCommunicator = null;
		}
	}
	
	public void bindQueryPartController(IQueryPartController serv) {
			this.queryPartController = serv;
	}
	
	public void unbindQueryPartController(IQueryPartController serv) {
		if(this.queryPartController==serv) {
			this.queryPartController=null;
		}
	}
	
	public void bindLoadBalancingCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
		this.communicatorRegistry = serv;
	}

	public void unbindLoadBalancingCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
		if(this.communicatorRegistry==serv) {
			this.communicatorRegistry = null;
		}
	}
	
	public void bindPhysicalCostModel(IPhysicalCostModel serv) {
		this.physicalCostModel = serv;
		
	}
	
	public void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		if(this.physicalCostModel==serv) {
			this.physicalCostModel = null;
		}
	}
	
	public void bindLoadBalancingLock(ILoadBalancingLock serv) {
		this.lock = serv;
	}


	public void unbindLoadBalancingLock(ILoadBalancingLock serv) {
		if(this.lock == serv) {
			lock = null;
		}
	}

	
	public void bindResourceUsageManager(IPeerResourceUsageManager serv) {
		this.usageManager = serv;
	}

	public void unbindResourceUsageManager(IPeerResourceUsageManager serv) {
		if (usageManager == serv) {
			usageManager = null;
		}
	}
	
	public void bindExecutor(IExecutor serv) {
		this.executor = (IServerExecutor)serv;
	}
	
	public void unbindExecutor(IExecutor serv) {
		if(executor==serv){
			executor=null;
		}
	}
	
	public void bindPeerDictionary(IPeerDictionary serv) {
		this.peerDictionary = serv;
	}
	
	public void unbindPeerDictionary(IPeerDictionary serv) {
		if(serv==peerDictionary) {
			this.peerDictionary = null;
		}
	}
	
	public void bindNetworkManager(IP2PNetworkManager serv) {
		this.networkManager = serv;
	}
	
	public void unbindNetworkManager(IP2PNetworkManager serv) {
		if(this.networkManager==serv) {
			this.networkManager = null;
		}
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
		this.allocator = allocator;
	}

	@Override
	public void setCommunicator(ILoadBalancingCommunicator communicator) {
		LOG.error("Setting of Communicator not implemented yet.");

	}

	@Override
	public void startMonitoring() throws LoadBalancingException {

		if (usageManager == null) {
			LOG.error("Could not start monitoring: No resource Usage Manager bound.");
			return;
		}

		synchronized (threadManipulationLock) {
			if (monitoringThread == null) {
				LOG.info("Starting to monitor Peer.");
				monitoringThread = new MonitoringThread(usageManager,this);
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
		return DynamicLoadBalancingConstants.STRATEGY_NAME;
	}

	private HashMap<Integer, IPhysicalQuery> getPhysicalQueries() {

		HashMap<Integer, IPhysicalQuery> queries = new HashMap<Integer, IPhysicalQuery>();

		for (int queryId : executor.getLogicalQueryIds(getActiveSession())) {
			//Ignore excluded Quries.
			if(excludedQueryRegistry.isQueryIDExcludedFromLoadBalancing(queryId))
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
	public void triggerLoadBalancing(double cpuUsage, double memUsage, double netUsage) {
		
		Preconditions.checkNotNull(this.networkManager);
		Preconditions.checkNotNull(this.executor);
		Preconditions.checkNotNull(this.queryPartController);
		Preconditions.checkNotNull(this.communicatorRegistry);
		Preconditions.checkNotNull(this.allocator);
		Preconditions.checkNotNull(this.lock);
		Preconditions.checkNotNull(this.peerDictionary);
		Preconditions.checkNotNull(this.physicalCostModel);
		
		if(executor.getLogicalQueryIds(getActiveSession()).size()==0) {
			LOG.warn("Load Balancing triggered, but no queries installed. Continuing monitoring.");
		}
		
		
		synchronized(threadManipulationLock) {
			if(monitoringThread!=null) {
				monitoringThread.removeListener(this);
				monitoringThread.setInactive();
				monitoringThread = null;
			}
		}
		LOG.info("Re-Allocation of queries triggered.");
					
		double cpuLoadToRemove = Math.max(0.0, cpuUsage-DynamicLoadBalancingConstants.CPU_THRESHOLD);
		double memLoadToRemove = Math.max(0.0, memUsage-DynamicLoadBalancingConstants.MEM_THRESHOLD);
		double netLoadToRemove = Math.max(0.0, netUsage-DynamicLoadBalancingConstants.NET_THRESHOLD);
		
		PeerID localPeerID = networkManager.getLocalPeerID();
		
		Collection<Integer> queryIDs = getNotExcludedQueries();
		
		//Replaces Sources with Sender-Receiver constructs.
		for(int queryID : queryIDs) {
			SharedQueryIDModifier.addSharedQueryIDIfNeccessary(queryID, executor, queryPartController, networkManager, getActiveSession());
			//SourceTransformer.replaceSources(queryID, localPeerID, getActiveSession(), networkManager, executor,queryPartController,excludedQueryRegistry);
			//SinkTransformer.replaceSinks(queryID, localPeerID, getActiveSession(), networkManager, executor, queryPartController,excludedQueryRegistry);
		}
		
		QueryCostMap allQueries = generateCostMapForAllQueries();
		IQuerySelectionStrategy greedySelector = new GreedyQuerySelector();
		QueryCostMap greedyResult = greedySelector.selectQueries(allQueries.clone(),cpuLoadToRemove, memLoadToRemove, netLoadToRemove);
		IQuerySelectionStrategy simulatedAnnealingSelector = new SimulatedAnnealingQuerySelector(greedyResult);
		QueryCostMap simulatedAnnealingResult =  simulatedAnnealingSelector.selectQueries(allQueries.clone(), cpuLoadToRemove, memLoadToRemove, netLoadToRemove);
			
		QueryCostMap chosenResult;
		if(simulatedAnnealingResult == null) {
			LOG.info("No feasible simulated Annealing Result found. Using Greedy Result.");
			chosenResult = greedyResult;
		}
		else if(greedyResult.getCosts()<simulatedAnnealingResult.getCosts()) {
			LOG.info("Greedy result is better than Simulated Annealing result ({}<{}), choosing Greedy result.",greedyResult.getCosts(),simulatedAnnealingResult.getCosts());
			chosenResult = greedyResult;
		}
		else {
			LOG.info("Simulated Annealing result is better than Greedy result ({}<{}), choosing Simulated Annealing result.",simulatedAnnealingResult.getCosts(),greedyResult.getCosts());
			chosenResult = simulatedAnnealingResult;
		}
			
		if(allocator==null) {
			LOG.error("No Allocator set.");
			return;
		}
			
			
		HashMap<ILogicalQueryPart,Integer> queryPartIDMapping = new HashMap<ILogicalQueryPart,Integer>();
		
		for (int queryId : chosenResult.getQueryIds()) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId, getActiveSession());
			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			queryPartIDMapping.put(new LogicalQueryPart(operators),queryId);
		}
			
		Collection<PeerID> knownRemotePeers = peerDictionary.getRemotePeerIDs();
		
		if(chosenResult.getQueryIds().size()==0) {
			LOG.error("No Queries to remove.");
			return;
		}
		//TODO What if no Object in List?
		int firstQueryId = chosenResult.getQueryIds().get(0);
			
		//Parameter Query is used to get Build Configuration...
		ILogicalQuery query = executor.getLogicalQueryById(firstQueryId, getActiveSession());
			
		
			
		try {
			Map<ILogicalQueryPart,PeerID> allocationMap = allocator.allocate(queryPartIDMapping.keySet(), query, knownRemotePeers, localPeerID);
			
			if(LOG.isDebugEnabled()) {
				LOG.debug("Allocation finished.");
				LOG.debug("Local PID is {}", localPeerID.toString());
				for (ILogicalQueryPart queryPart : allocationMap.keySet()) {
					if(allocationMap.get(queryPart).equals(localPeerID)) {
						LOG.warn("No other Peer wanted to take Query {}.",queryPartIDMapping.get(queryPart));
					}
					else {
						LOG.debug("({} goes to Peer {}",queryPartIDMapping.get(queryPart),this.peerDictionary.getRemotePeerName(allocationMap.get(queryPart)));
					}
				}
			}
			
			
			HashMap<Integer,ILoadBalancingCommunicator> communicatorMapping = chooseCommunicators(chosenResult.getQueryIds());
			
			this.transmissionHandlerList = new ArrayList<QueryTransmissionHandler>();
			
			for (ILogicalQueryPart queryPart : allocationMap.keySet()) {
				//Create a Query Transmission handler for every  transmission we have to do.
				int queryId = queryPartIDMapping.get(queryPart);
				PeerID slavePeerId = allocationMap.get(queryPart);
				if(slavePeerId.equals(localPeerID))
					continue;
				ILoadBalancingCommunicator communicator = communicatorMapping.get(queryId);
				transmissionHandlerList.add(new QueryTransmissionHandler(queryId,slavePeerId,communicator, peerCommunicator, lock));
			}
			
		} catch (QueryPartAllocationException e) {
			LOG.error("Could not allocate Query Parts: {}",e.getMessage());
			e.printStackTrace();
		}
		
		if(transmissionHandlerList!=null && transmissionHandlerList.size()>0) {
			transmissionHandlerList.get(0).initiateTransmission(this);
		}
		
	}
	
	
	private HashMap<Integer,ILoadBalancingCommunicator> chooseCommunicators(List<Integer> queryIds) {
		
		
		HashMap<Integer,ILoadBalancingCommunicator> queryCommunicatorMapping = new HashMap<Integer,ILoadBalancingCommunicator>();
		for (int queryId : queryIds) {
			if(!isActive(queryId)) {
				ILoadBalancingCommunicator inactiveQueryCommunicator = communicatorRegistry.getCommunicator(DynamicLoadBalancingConstants.INACTIVE_QUERIES_COMMUNICATOR_NAME);
				queryCommunicatorMapping.put(queryId, inactiveQueryCommunicator);
			}
			if(!hasStatefulOperator(queryId)) {
				ILoadBalancingCommunicator inactiveQueryCommunicator = communicatorRegistry.getCommunicator(DynamicLoadBalancingConstants.STATELESS_QUERIES_COMMUNICATOR_NAME);
				queryCommunicatorMapping.put(queryId, inactiveQueryCommunicator);
			}
			ILoadBalancingCommunicator inactiveQueryCommunicator = communicatorRegistry.getCommunicator(DynamicLoadBalancingConstants.STATELESS_QUERIES_COMMUNICATOR_NAME);
			queryCommunicatorMapping.put(queryId, inactiveQueryCommunicator);
			
		}
		
		return queryCommunicatorMapping;
	}
	
	private boolean isActive(int queryId) {
		QueryState queryState = executor.getQueryState(queryId);
		if(queryState==QueryState.INACTIVE) {
			return false;
		}
		return true;
	}
	
	private boolean hasStatefulOperator(int queryId) {
		HashMap<Integer,IPhysicalQuery> queries = getPhysicalQueries();
		Collection<IPhysicalOperator> operatorsInQuery = queries.get(queryId).getAllOperators();
		for (IPhysicalOperator op : operatorsInQuery) {
			if(op instanceof IStatefulPO)
				return true;
		}
		return false;
		
		
	}
	
	private QueryCostMap generateCostMapForAllQueries() {
		
		HashMap<Integer,IPhysicalQuery> queries = getPhysicalQueries();
		
		QueryCostMap queryCostMap = new QueryCostMap();
		//Get cost and load information for each query.
		for (int queryId : queries.keySet()) {

			double cpuMax = usageManager.getLocalResourceUsage().getCpuMax();
			double netMax = usageManager.getLocalResourceUsage().getNetBandwidthMax();
			long memMax = usageManager.getLocalResourceUsage().getMemMaxBytes();
			
			Collection<IPhysicalOperator> operatorsInQuery = queries.get(queryId).getAllOperators();
			IPhysicalCost queryCost = physicalCostModel.estimateCost(operatorsInQuery);
			
			double cpuLoad = queryCost.getCpuSum()/(cpuMax*DynamicLoadBalancingConstants.CPU_LOAD_COSTMODEL_FACTOR);
			double netLoad = queryCost.getNetworkSum()/netMax;
			double memLoad = queryCost.getMemorySum()/memMax;
				
			
			double migrationCosts = calculateIndividualMigrationCostsForQuery(operatorsInQuery,queryCost);
			
			QueryLoadInformation info = new QueryLoadInformation(queryId,cpuLoad,memLoad,netLoad,migrationCosts);
			
			queryCostMap.add(info);
		}
		return queryCostMap;
	}
	

	private double calculateIndividualMigrationCostsForQuery(Collection<IPhysicalOperator> operators, IPhysicalCost costmodelCosts) {
		
		int numberOfReceivers = 0;
		int numberOfSenders = 0;
		double memoryForStates = 0.0;
		
		
		for (IPhysicalOperator op : operators) {
			if(op instanceof JxtaSenderPO) {
				numberOfSenders++;
				continue;
			}
			if(op instanceof JxtaReceiverPO) {
				numberOfReceivers++;
				continue;
			}
			if(op instanceof IStatefulPO) {
				//Estimate State Size Method 
				try {
					IStatefulPO statefulOP = (IStatefulPO) op;
					IOperatorState state = statefulOP.getState();
					memoryForStates += state.estimateSizeInBytes();
				}
				 catch (Exception e) {
						LOG.error("Error estimating state size of {} Operator",op.getName());
						LOG.error(e.getMessage());
						e.printStackTrace();
						LOG.error("Assuming Infinite Migration Cost for state!");
						memoryForStates+=Double.POSITIVE_INFINITY;
				}
			}
		}
		
		
		
		return (DynamicLoadBalancingConstants.WEIGHT_RECEIVERS*numberOfReceivers)+(DynamicLoadBalancingConstants.WEIGHT_SENDERS*numberOfSenders)+(DynamicLoadBalancingConstants.WEIGHT_STATE*memoryForStates);
		
	}

	@Override
	public void tranmissionFailed(QueryTransmissionHandler transmission) {
		transmission.removeListener(this);
		transmissionHandlerList.remove(transmission);
		LOG.error("Transmission of Query {} to Peer {} failed.",transmission.getQueryId(), peerDictionary.getRemotePeerName(transmission.getSlavePeerID()));
		if(transmissionHandlerList.size()>0) {
				transmissionHandlerList.get(0).initiateTransmission(this);
		}
		else {
				LOG.info("Tried to transfer all Queries.");
				//TODO Restart monitoring Thread. and retry sendind failed Queries?
			}
	}

	@Override
	public void transmissionSuccessful(QueryTransmissionHandler transmission) {
		transmission.removeListener(this);
		transmissionHandlerList.remove(transmission);

		LOG.error("Transmission of Query {} to Peer {} successful.",transmission.getQueryId(), peerDictionary.getRemotePeerName(transmission.getSlavePeerID()));
		if(transmissionHandlerList.size()>0) {
				transmissionHandlerList.get(0).initiateTransmission(this);
		}
		else {
				LOG.info("Tried to transfer all Queries.");
				//TODO Restart monitoring Thread. and retry sendind failed Queries?
			}
	}


	@Override
	public void localLockFailed(QueryTransmissionHandler transmission) {
		try {
			Thread.sleep(DynamicLoadBalancingConstants.WAITING_TIME_FOR_LOCAL_LOCK);
			transmission.initiateTransmission(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Collection<Integer> getNotExcludedQueries() {
		Collection<Integer> queryIDs =  executor.getLogicalQueryIds(getActiveSession());
		Iterator<Integer> iter = queryIDs.iterator();
		while(iter.hasNext()) {
			int nextQId = iter.next();
			if(excludedQueryRegistry.isQueryIDExcludedFromLoadBalancing(nextQId)) {
				iter.remove();
			}
		}
		return queryIDs;
	}

	@Override
	public void forceLoadBalancing() throws LoadBalancingException {
		
		triggerLoadBalancing(DynamicLoadBalancingConstants.CPU_THRESHOLD+0.01, DynamicLoadBalancingConstants.MEM_THRESHOLD+0.01, DynamicLoadBalancingConstants.NET_THRESHOLD+0.01);
		
	}

}


