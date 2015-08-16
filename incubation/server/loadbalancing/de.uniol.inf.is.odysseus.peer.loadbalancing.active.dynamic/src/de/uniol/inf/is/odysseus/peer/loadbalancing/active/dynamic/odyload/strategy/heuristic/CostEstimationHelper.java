package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.QueryLoadInformation;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class CostEstimationHelper {
	

	private static final Logger LOG = LoggerFactory
			.getLogger(CostEstimationHelper.class);

	
	
	@SuppressWarnings("rawtypes")
	public static double estimateNetloadFromJxtaOperatorCount(
			Set<IPhysicalOperator> operatorList,IP2PNetworkManager networkManager) {
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
	


	public static void addQueryToCostMap(QueryCostMap queryCostMap, int queryId,
			Set<IPhysicalOperator> operatorList, IPeerResourceUsageManager usageManager, IPhysicalCostModel physicalCostModel, IP2PNetworkManager networkManager) {
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
			LOG.info("Using JxtaOperators to estimate NetLoad instead of using value from CostModel.");
			netLoad = estimateNetloadFromJxtaOperatorCount(operatorList,networkManager);
		}
			
		
		double migrationCosts = calculateIndividualMigrationCostsForQuery(operatorsInQuery);
		
		QueryLoadInformation info = new QueryLoadInformation(queryId,cpuLoad,memLoad,netLoad,migrationCosts);
		
		queryCostMap.add(info);
	}

}
