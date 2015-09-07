package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
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
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryCostMap;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common.QueryLoadInformation;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class CostEstimationHelper {
	

	private static final Logger LOG = LoggerFactory
			.getLogger(CostEstimationHelper.class);
	
	private static IP2PNetworkManager networkManager;
	private static IServerExecutor executor;
	private static IPeerResourceUsageManager usageManager;
	private static IPhysicalCostModel physicalCostModel;
	private static ISession activeSession;
	
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		networkManager = serv;
	}
	
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if(networkManager==serv) {
			networkManager = null;
		}
	}
	
	

	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor)serv;
	}
	
	public static void unbindExecutor(IExecutor serv) {
		if(executor==serv) {
			executor=null;
		}
	}
	
	

	public static void bindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		usageManager = serv;
	}
	
	public static void unbindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		if(usageManager==serv) {
			networkManager = null;
		}
	}
	
	public static void bindPhysicalCostModel(IPhysicalCostModel serv) {
		physicalCostModel = serv;
	}
	
	public static void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		physicalCostModel = null;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static double estimateNetloadFromJxtaOperatorCount(Set<IPhysicalOperator> operatorList) {
		
		double netLoad;
		int sendersToRemotePeersCount = 0;
		int receiversFromRemotePeersCount = 0;
		String localPeerIDString = networkManager.getLocalPeerID().toString();
		for(IPhysicalOperator op : operatorList) {
			if(op instanceof JxtaSenderPO) {
				JxtaSenderPO sender = (JxtaSenderPO) op;
				//Don't count sender operators that send to local peer, as this does not produce "real" network traffic.
				if(sender.getPeerIDString().equals(localPeerIDString))
					continue;
				sendersToRemotePeersCount++;	
			}
			if(op instanceof JxtaReceiverPO) {
				JxtaReceiverPO receiver = (JxtaReceiverPO) op;
				//Don't count receiver operators that receive from local peer, as this does not produce "real" network traffic.
				if(receiver.getPeerIDString().equals(localPeerIDString))
					continue;
				receiversFromRemotePeersCount++;	
			}
		}
		Double networkOut = sendersToRemotePeersCount*OdyLoadConstants.BandwithPerSender;
		Double networkIn = receiversFromRemotePeersCount*OdyLoadConstants.BandwithPerReceiver;
		netLoad = Math.min(1.0,(networkOut+networkIn));
		return netLoad;
	}
	
	

	public static double estimateNetFreeFromJxtaOperatorCount() {
		return (1.0-estimateNetUsedFromJxtaOperatorCount());
	}
	
	public static double estimateNetUsedFromJxtaOperatorCount() {

		List<IPhysicalOperator> allOperatorsOnPeer = Lists.newArrayList();
		
		
		for(IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			allOperatorsOnPeer.addAll(query.getAllOperators());
		}
		
		
		Set<IPhysicalOperator> allOperatorsOnPeerSet = new HashSet<IPhysicalOperator>(allOperatorsOnPeer);
	
		double neededLoad = estimateNetloadFromJxtaOperatorCount(allOperatorsOnPeerSet);
		
		return Math.min(1.0, neededLoad);
	}

	
	
	public static double calculateIndividualMigrationCostsForQuery(Collection<IPhysicalOperator> operators) {
		
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
		
		return (OdyLoadConstants.WEIGHT_RECEIVERS*numberOfReceivers)+(OdyLoadConstants.WEIGHT_SENDERS*numberOfSenders)+(OdyLoadConstants.WEIGHT_STATE*memoryForStates);
		
	}
	

	public static QueryCostMap generateCostMapForAllQueries() {

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
	

	public static HashMap<Integer, IPhysicalQuery> getPhysicalQueries() {

		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		IExcludedQueriesRegistry excludedQueryRegistry = OsgiServiceProvider
				.getExcludedQueriesRegistry();

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
	


	public static void addQueryToCostMap(QueryCostMap queryCostMap, int queryId,Set<IPhysicalOperator> operatorList) {
		
		
		double cpuMax = usageManager.getLocalResourceUsage().getCpuMax();
		double netMax = usageManager.getLocalResourceUsage().getNetBandwidthMax();
		long memMax = usageManager.getLocalResourceUsage().getMemMaxBytes();
		
		Collection<IPhysicalOperator> operatorsInQuery = operatorList;
		IPhysicalCost queryCost = physicalCostModel.estimateCost(operatorsInQuery);
		
		
		double cpuLoad = queryCost.getCpuSum()/(cpuMax*OdyLoadConstants.CPU_LOAD_COSTMODEL_FACTOR);
		double netLoad = queryCost.getNetworkSum()/netMax;
		double memLoad = queryCost.getMemorySum()/memMax;
		
		//Workaround, as Cost Model does not provide real usage data for network :(
		if(OdyLoadConstants.COUNT_JXTA_OPERATORS_FOR_NETWORK_COSTS) {
			netLoad = estimateNetloadFromJxtaOperatorCount(operatorList);
		}
			
		
		double migrationCosts = calculateIndividualMigrationCostsForQuery(operatorsInQuery);
		
		QueryLoadInformation info = new QueryLoadInformation(queryId,cpuLoad,memLoad,netLoad,migrationCosts);
		
		queryCostMap.add(info);
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


}
