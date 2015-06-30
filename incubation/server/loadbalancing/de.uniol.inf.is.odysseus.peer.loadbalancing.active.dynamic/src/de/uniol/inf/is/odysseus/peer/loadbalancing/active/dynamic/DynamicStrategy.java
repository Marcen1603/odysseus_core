package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class DynamicStrategy implements ILoadBalancingStrategy, IMonitoringThreadListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(DynamicStrategy.class);
	private static final String NAME = "OdyLoad";
	private Object threadManipulationLock = new Object();
	private IPeerResourceUsageManager usageManager;
	private IServerExecutor executor;
	private static ISession activeSession;
	private IPhysicalCostModel physicalCostModel;

	private MonitoringThread monitoringThread = null;
	

	public void bindPhysicalCostModel(IPhysicalCostModel serv) {
		this.physicalCostModel = serv;
	}


	public void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		if(this.physicalCostModel==serv) {
			this.physicalCostModel = null;
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
		LOG.error("Setting of Allocator not implemented yet.");

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
		return NAME;
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
		
		double cpuLoadToRemove = cpuUsage-MonitoringThread.CPU_THRESHOLD;
		double memLoadToRemove = memUsage-MonitoringThread.MEM_THRESHOLD;
		double netLoadToRemove = netUsage-MonitoringThread.NET_THRESHOLD;
		
		QueryCostMap allQueries = generateCostMapForAllQueries();
		IQuerySelectionStrategy greedySelector = new GreedyQuerySelector();
		QueryCostMap greedyResult = greedySelector.selectQueries(allQueries.clone(),cpuLoadToRemove, memLoadToRemove, netLoadToRemove);
		IQuerySelectionStrategy simulatedAnnealingSelector = new SimulatedAnnealingQuerySelector();
		QueryCostMap simulatedAnnealingResult =  simulatedAnnealingSelector.selectQueries(allQueries.clone(), cpuLoadToRemove, memLoadToRemove, netLoadToRemove);
		
		QueryCostMap chosenResult;
		if(greedyResult.getCosts()<simulatedAnnealingResult.getCosts()) {
			LOG.info("Greedy result is better than Simulated Annealing result ({}<{}), choosing Greedy result.",greedyResult.getCosts(),simulatedAnnealingResult.getCosts());
			chosenResult = greedyResult;
		}
		else {
			LOG.info("Simulated Annealing result is better than Greedy result ({}<{}), choosing Greedy result.",simulatedAnnealingResult.getCosts(),greedyResult.getCosts());
			chosenResult = simulatedAnnealingResult;
		}
		
		//TODO Allocate :)
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
				
				//Full serialization Method (probably takes loooong.)
				IStatefulPO statefulOp = (IStatefulPO)op;
				Serializable state = statefulOp.getState();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = null;
					try {
						out = new ObjectOutputStream(bos);
						out.writeObject(state);
						byte[] stateBytes = bos.toByteArray();
						memoryForStates+=stateBytes.length;	
					} catch (Exception e) {
						LOG.error("Error in serializing state of {} Operator",op.getName());
						LOG.error(e.getMessage());
						LOG.error("Assuming Infinite Migration Cost for state!");
						memoryForStates+=Double.POSITIVE_INFINITY;
					}
					
				//Cost-Model Method.
				//DetailCost costForOp = costmodelCosts.getDetailCost(op);
				//memoryForStates += costForOp.getMemCost();
			}
		}
		
		
		
		return (DynamicLoadBalancingConstants.WEIGHT_RECEIVERS*numberOfReceivers)+(DynamicLoadBalancingConstants.WEIGHT_SENDERS*numberOfSenders)+(DynamicLoadBalancingConstants.WEIGHT_STATE*memoryForStates);
		
	}
	



}
