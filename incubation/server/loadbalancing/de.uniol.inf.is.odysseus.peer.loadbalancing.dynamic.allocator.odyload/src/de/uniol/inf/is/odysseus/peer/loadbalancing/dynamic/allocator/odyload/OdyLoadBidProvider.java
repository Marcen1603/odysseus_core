package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.allocator.odyload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.CostEstimationHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class OdyLoadBidProvider implements IBidProvider {

	private static final Logger LOG = LoggerFactory
			.getLogger(OdyLoadBidProvider.class);
	
	private IPhysicalCostModel costModel;
	private IPeerResourceUsageManager usageManager;
	
	
	public void bindPhysicalCostModel(IPhysicalCostModel serv) {
		costModel = serv;
	}
	
	public void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		if(costModel==serv) {
			costModel = null;
		}
	}
	
	public void bindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		usageManager = serv;
	}
	
	public void unbindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		if(usageManager==serv) {
			usageManager = null;
		}
	}

	
	@Override
	public String getName() {
		return OdyLoadConstants.BID_PROVIDER_NAME;
	}

	@Override
	public Optional<Double> calculateBid(ILogicalQuery query, String configName) {
		
		IPhysicalQuery physicalQuery = Helper.getPhysicalQuery(query,
				configName);

		IResourceUsage usage = usageManager.getLocalResourceUsage();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Processing Query:");
			SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
			LOG.debug(printer.createString(query.getLogicalPlan()));

		}

		double cpuFree = usage.getCpuFree() / usage.getCpuMax();
		double memFree = 1.0 * usage.getMemFreeBytes() / usage.getMemMaxBytes();
		double netFree = 1.0-((usage.getNetInputRate() + usage.getNetOutputRate())
				/ usage.getNetBandwidthMax());
		
		if(OdyLoadConstants.COUNT_JXTA_OPERATORS_FOR_NETWORK_COSTS) {
			netFree = CostEstimationHelper.estimateNetFreeFromJxtaOperatorCount();
		}

		double neededCpuLoad;
		double neededMemLoad;
		double neededNetLoad;

		try {
			IPhysicalCost cost = costModel.estimateCost(physicalQuery
					.getAllOperators());
			neededCpuLoad = cost.getCpuSum() / usage.getCpuMax();
			neededMemLoad = cost.getMemorySum() / usage.getMemMaxBytes();
			neededNetLoad = cost.getNetworkSum() / usage.getNetBandwidthMax();
		} catch (Exception ex) {
			LOG.error(
					"Error while estimating Query Costs, assuming Fallback values costs... {}",
					ex.getMessage());
			ex.printStackTrace();
			// To avoid other peers overloading themselves, the fallback values
			// Should be 1.0/infinite. For testing purpose other values can be
			// choosen.
			neededCpuLoad = OdyLoadConstants.FALLBACK_CPU_COSTS;
			neededMemLoad = OdyLoadConstants.FALLBACK_MEM_COSTS;
			neededNetLoad = OdyLoadConstants.FALLBACK_NET_COSTS;
		}
		
		
		if(OdyLoadConstants.COUNT_JXTA_OPERATORS_FOR_NETWORK_COSTS) {
			LOG.info("Counting Jxta Operators to remote Peers for network Costs.");
			neededNetLoad = CostEstimationHelper.estimateNetloadFromJxtaOperatorCount(physicalQuery
			.getAllOperators());
		}
		

		double freeCpuAfterLoadBalancing = cpuFree-neededCpuLoad;
		double freeMemAfterLoadBalancing = memFree-neededMemLoad;
		double freeNetAfterLoadBalancing = netFree-neededNetLoad;
		
		


		LOG.debug("needed Cpu Load:{}", String.format( "%.4f",neededCpuLoad));
		LOG.debug("needed Mem Load:{}", String.format( "%.4f",neededMemLoad));
		LOG.debug("needed Net Load:{}", String.format( "%.4f",neededNetLoad));
		LOG.debug("CPU Free after LB:{} (Currently Free:{})",
				String.format( "%.4f",freeCpuAfterLoadBalancing), String.format( "%.4f",cpuFree));
		LOG.debug("MEM Free after LB:{} (Currently Free:{})",
				String.format( "%.4f",freeMemAfterLoadBalancing), String.format( "%.4f",memFree));
		LOG.debug("NET Free after LB:{} (Currently Free:{})",
				String.format( "%.4f",freeNetAfterLoadBalancing), String.format( "%.4f",netFree));

		if (freeCpuAfterLoadBalancing <= (1.0-OdyLoadConstants.CPU_THRESHOLD) || freeMemAfterLoadBalancing <=  (1.0-OdyLoadConstants.MEM_THRESHOLD)
				|| freeNetAfterLoadBalancing <= (1.0-OdyLoadConstants.NET_THRESHOLD)) {
			LOG.debug("Not bidding to QueryPart is this would overload Peer:");
			return Optional.absent();
		}

		double bid = freeCpuAfterLoadBalancing + freeMemAfterLoadBalancing
				+ freeNetAfterLoadBalancing;
		LOG.debug("Bidding to QueryPart with Bid of {}", bid);
		return Optional.of(bid);

	}
	


}
