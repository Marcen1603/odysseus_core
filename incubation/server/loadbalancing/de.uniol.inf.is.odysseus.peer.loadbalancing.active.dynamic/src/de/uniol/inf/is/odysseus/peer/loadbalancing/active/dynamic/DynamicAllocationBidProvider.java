package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class DynamicAllocationBidProvider implements IBidProvider {


	private static final Logger LOG = LoggerFactory.getLogger(DynamicAllocationBidProvider.class);

	private static IPhysicalCostModel costModel;
	private static IPeerResourceUsageManager usageManager;

	// called by OSGi-DS
	public static void bindPhysicalCostModel(IPhysicalCostModel serv) {
		costModel = serv;
	}

	// called by OSGi-DS
	public static void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		if (costModel == serv) {
			costModel = null;
		}
	}

	// called by OSGi-DS
	public static void bindResourceUsageManager(IPeerResourceUsageManager serv) {
		usageManager = serv;
	}

	// called by OSGi-DS
	public static void unbindResourceUsageManager(IPeerResourceUsageManager serv) {
		if (usageManager == serv) {
			usageManager = null;
		}
	}
	
	
	@Override
	public String getName() {
		return DynamicLoadBalancingConstants.BID_PROVIDER_NAME;
	}

	@Override
	public Optional<Double> calculateBid(ILogicalQuery query, String configName) {
		IPhysicalQuery physicalQuery = Helper.getPhysicalQuery(query,configName);
		
		IPhysicalCost cost = costModel.estimateCost(physicalQuery.getAllOperators());
		
		IResourceUsage usage  = usageManager.getLocalResourceUsage();
		
		double cpuLoad = usage.getCpuFree()/usage.getCpuMax();
		double memLoad = 1.0*usage.getMemFreeBytes()/usage.getMemMaxBytes();
		double netLoad = (usage.getNetInputRate()+usage.getNetOutputRate())/usage.getNetBandwidthMax();
		
		double neededCpuLoad = cost.getCpuSum()/(usage.getCpuMax()*DynamicLoadBalancingConstants.CPU_LOAD_COSTMODEL_FACTOR);
		double neededMemLoad = cost.getMemorySum()/usage.getMemMaxBytes();
		double neededNetLoad = cost.getNetworkSum()/usage.getNetBandwidthMax();
		
		double freeCpuAfterLoadBalancing = DynamicLoadBalancingConstants.CPU_THRESHOLD-(cpuLoad+neededCpuLoad);
		double freeMemAfterLoadBalancing = DynamicLoadBalancingConstants.MEM_THRESHOLD-(memLoad+neededMemLoad);
		double freeNetAfterLoadBalancing = DynamicLoadBalancingConstants.NET_THRESHOLD-(netLoad+neededNetLoad);
		
		
		//This prevents peer from overloading itself.
		if(freeCpuAfterLoadBalancing<=0 || freeMemAfterLoadBalancing<=0 || freeNetAfterLoadBalancing<=0) {
			LOG.info("Not bidding to QueryPart is this would overload Peer.");
			return Optional.absent();
		}
		
		
		double bid = freeCpuAfterLoadBalancing+freeMemAfterLoadBalancing+freeNetAfterLoadBalancing;
		LOG.info("Bidding to QueryPart with Bid of {}",bid);
		return Optional.of(bid);
		
	}

}