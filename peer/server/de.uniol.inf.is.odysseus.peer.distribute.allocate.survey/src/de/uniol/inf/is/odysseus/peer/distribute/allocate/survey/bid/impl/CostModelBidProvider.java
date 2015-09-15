package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.SurveyBasedAllocationPlugIn;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class CostModelBidProvider implements IBidProvider {

	private static final Logger LOG = LoggerFactory.getLogger(CostModelBidProvider.class);

	private static IPhysicalCostModel costModel;
	private static IPeerResourceUsageManager resourceUsageManager;

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
	public static void bindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		resourceUsageManager = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		if (resourceUsageManager == serv) {
			resourceUsageManager = null;
		}
	}
	
	@Override
	public String getName() {
		return SurveyBasedAllocationPlugIn.DEFAULT_BID_PROVIDER_NAME;
	}

	@Override
	public Optional<Double> calculateBid(ILogicalQuery query, String configName) {
		CostSummary summary = calcCostsForPlan(query, configName);

		return calcBidImpl(query.getLogicalPlan(), summary.getCpuCost(), summary.getMemCost());
	}

	private static CostSummary calcCostsForPlan(ILogicalQuery query, String transCfgName) {
		IPhysicalQuery p = Helper.getPhysicalQuery(query, transCfgName);

		IPhysicalCost cost = costModel.estimateCost(p.getPhysicalChilds());

		double cpuCost = cost.getCpuSum();
		double memCost = cost.getMemorySum();

		return new CostSummary(cpuCost, memCost, query.getLogicalPlan());
	}

	private static Optional<Double> calcBidImpl(ILogicalOperator query, double cpuCosts, double memCosts) {
		if (!Helper.allSourcesAvailable(query)) {
			LOG.debug("Not all sources are available. No bid then.");
			return Optional.absent();
		}

		IResourceUsage localResourceUsage = resourceUsageManager.getLocalResourceUsage();
		
		double remainingCpu = localResourceUsage.getCpuFree();
		double remainingMem = localResourceUsage.getMemFreeBytes();
		
		LOG.debug("Cost for query        : {}", formatCosts(memCosts, cpuCosts));
		LOG.debug("Remaining resources   : {}", formatCosts(remainingMem, remainingCpu));
		if (memCosts > remainingMem || cpuCosts > remainingCpu) {
			LOG.debug("Costs are too high. No bid then.");
			return Optional.absent();
		}

		double remainingMemPerc = (remainingMem - memCosts) / localResourceUsage.getMemMaxBytes();
		double remainingCpuPerc = (remainingCpu - cpuCosts) / localResourceUsage.getCpuMax();
		
		LOG.debug("Remaining costs(%): MEM at {} %, CPU at {} %", remainingMemPerc * 100, remainingCpuPerc * 100);

		double bid = (remainingMemPerc + remainingCpuPerc) / 2;
		LOG.debug("Resulting bid = {}", bid);

		return Optional.of(bid);
	}

	private static String formatCosts(double memCost, double cpuCost) {
		return String.format("m = %-10.6f MB ; c = %-10.6f", memCost / 1024 / 1024, cpuCost);
	}
}
