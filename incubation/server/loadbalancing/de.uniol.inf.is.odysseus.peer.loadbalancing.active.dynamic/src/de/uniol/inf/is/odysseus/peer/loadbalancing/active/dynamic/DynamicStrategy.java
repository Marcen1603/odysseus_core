package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicatorRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class DynamicStrategy implements ILoadBalancingStrategy, IMonitoringThreadListener {

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
	

	private MonitoringThread monitoringThread = null;
	
	
	public void bindCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
		this.communicatorRegistry = serv;
	}

	public void unbindCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
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
			IPhysicalQuery query = executor.getExecutionPlan().getQueryById(
					queryId);
			if (query == null)
				continue;
			queries.put(queryId, query);
		}

		return queries;
	}

	
	@SuppressWarnings("unused")
	@Override
	public void triggerLoadBalancing(double cpuUsage, double memUsage, double netUsage) {
		synchronized(threadManipulationLock) {
			monitoringThread.removeListener(this);
			monitoringThread.setInactive();
			monitoringThread = null;
		}
		LOG.info("Re-Allocation of queries triggered.");
					
		double cpuLoadToRemove = Math.max(0.0, DynamicLoadBalancingConstants.CPU_THRESHOLD-cpuUsage);
		double memLoadToRemove = Math.max(0.0, DynamicLoadBalancingConstants.MEM_THRESHOLD-memUsage);
		double netLoadToRemove = Math.max(0.0, DynamicLoadBalancingConstants.NET_THRESHOLD-netUsage);
		
		QueryCostMap allQueries = generateCostMapForAllQueries();
		IQuerySelectionStrategy greedySelector = new GreedyQuerySelector();
		QueryCostMap greedyResult = greedySelector.selectQueries(allQueries.clone(),cpuLoadToRemove, memLoadToRemove, netLoadToRemove);
		IQuerySelectionStrategy simulatedAnnealingSelector = new SimulatedAnnealingQuerySelector();
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
			
			
		List<ILogicalQueryPart> queryParts = new ArrayList<ILogicalQueryPart>();
		
		for (int queryId : chosenResult.getQueryIds()) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId, getActiveSession());
			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			queryParts.add(new LogicalQueryPart(operators));
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
			
		PeerID localPeerID = networkManager.getLocalPeerID();
			
		try {
			Map<ILogicalQueryPart,PeerID> allocationMap = allocator.allocate(queryParts, query, knownRemotePeers, localPeerID);
			if(LOG.isDebugEnabled()) {
				LOG.debug("Allocation finished.");
				LOG.debug("Local PID is {}", localPeerID.toString());
				for (ILogicalQueryPart queryPart : allocationMap.keySet()) {
					LOG.debug(queryPart.toString() + " goes to " + allocationMap.get(queryPart));
				}
			}
		} catch (QueryPartAllocationException e) {
			LOG.error("Could not allocate Query Parts: {}",e.getMessage());
			e.printStackTrace();
		}
			
		HashMap<Integer,ILoadBalancingCommunicator> communicatorMapping = chooseCommunicators(chosenResult.getQueryIds());
		/**
		if(lock.requestLocalLock()) {
			
			List<PeerID> otherPeers = this.mCommunicator.getInvolvedPeers(queryAndID.getE2());

			if (!otherPeers.contains(peerID))
				otherPeers.add(peerID);

			this.volunteer = peerID;
			this.queryID = queryAndID.getE2();

			// Acquire Locks for other peers
			PeerLockContainer peerLocks = new PeerLockContainer(peerCommunicator, otherPeers, this);
			peerLocks.requestLocks();
			this.lockContainer = peerLocks;
			
			
			lock.releaseLocalLock();
		}
		
		*/
		
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
			
			QueryLoadInformation info = new QueryLoadInformation(queryId,cpuLoad,netLoad,memLoad,migrationCosts);
			
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
						LOG.error("Assuming Infinite Migration Cost for state!");
						memoryForStates+=Double.POSITIVE_INFINITY;
				}
			}
		}
		
		
		
		return (DynamicLoadBalancingConstants.WEIGHT_RECEIVERS*numberOfReceivers)+(DynamicLoadBalancingConstants.WEIGHT_SENDERS*numberOfSenders)+(DynamicLoadBalancingConstants.WEIGHT_STATE*memoryForStates);
		
	}
	



}
