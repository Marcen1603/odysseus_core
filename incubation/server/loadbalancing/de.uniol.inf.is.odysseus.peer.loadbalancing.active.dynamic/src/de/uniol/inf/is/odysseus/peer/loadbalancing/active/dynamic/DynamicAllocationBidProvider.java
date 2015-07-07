package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

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
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class DynamicAllocationBidProvider implements IBidProvider {

	private static final Logger LOG = LoggerFactory
			.getLogger(DynamicAllocationBidProvider.class);

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
			neededCpuLoad = DynamicLoadBalancingConstants.FALLBACK_CPU_COSTS;
			neededMemLoad = DynamicLoadBalancingConstants.FALLBACK_MEM_COSTS;
			neededNetLoad = DynamicLoadBalancingConstants.FALLBACK_NET_COSTS;
		}

		LOG.debug("needed Cpu Load:{}", neededCpuLoad);
		LOG.debug("needed Mem Load:{}", neededMemLoad);
		LOG.debug("needed Net Load:{}", neededNetLoad);

		double freeCpuAfterLoadBalancing = cpuFree-neededCpuLoad;
		double freeMemAfterLoadBalancing = memFree-neededMemLoad;
		double freeNetAfterLoadBalancing = netFree-neededNetLoad;

		LOG.debug("CPU Free after LB:{} (Currently Free:{})",
				freeCpuAfterLoadBalancing, cpuFree);
		LOG.debug("MEM Free after LB:{} (Currently Free:{})",
				freeMemAfterLoadBalancing, memFree);
		LOG.debug("NET Free after LB:{} (Currently Free:{})",
				freeNetAfterLoadBalancing, netFree);

		//FIXME Remove Workaround ;)
		// This prevents peer from overloading itself.
		if (freeCpuAfterLoadBalancing <= (1.0-DynamicLoadBalancingConstants.CPU_THRESHOLD) || freeMemAfterLoadBalancing <= 0.3 //(1.0-DynamicLoadBalancingConstants.MEM_THRESHOLD)
				|| freeNetAfterLoadBalancing <= (1.0-DynamicLoadBalancingConstants.NET_THRESHOLD)) {
			LOG.debug("Not bidding to QueryPart is this would overload Peer:");
			return Optional.absent();
		}

		double bid = freeCpuAfterLoadBalancing + freeMemAfterLoadBalancing
				+ freeNetAfterLoadBalancing;
		LOG.debug("Bidding to QueryPart with Bid of {}", bid);
		return Optional.of(bid);

	}

}
