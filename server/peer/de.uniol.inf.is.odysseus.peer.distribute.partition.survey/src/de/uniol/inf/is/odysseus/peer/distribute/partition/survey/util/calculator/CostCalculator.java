package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.calculator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.CostModelService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.PlanProOperatorPartitioner;

public class CostCalculator {

	private static final Logger LOG = LoggerFactory.getLogger(CostCalculator.class);

	private final OperatorCostsCalculator operatorCostsCalculator;

	public CostCalculator() {
		operatorCostsCalculator = new OperatorCostsCalculator(new PlanProOperatorPartitioner());
	}

	public Map<String, CostSummary> calcCostsProOperator(ILogicalQuery plan, String transCfgName, boolean discardPlan) {
		if (!discardPlan) {
			plan = Helper.copyQuery(plan);
		}

		return operatorCostsCalculator.calcCostsProOperator(plan.getLogicalPlan(), transCfgName);
	}

	public CostSummary calcBearableCostsInPercentage(CostSummary absoluteCosts) {
		return calcBearableCostsInPercentage(absoluteCosts.getOperatorId(), absoluteCosts.getCpuCost(), absoluteCosts.getMemCost(), absoluteCosts.getPlan());
	}

	public double calcBid(ILogicalOperator plan, CostSummary absolutePlanCosts) {
		return calcBid(plan, absolutePlanCosts, true);
	}

	public double calcBid(ILogicalOperator plan, CostSummary absolutePlanCosts, boolean respectSourceAvailibility) {
		return calcBid(plan, absolutePlanCosts.getCpuCost(), absolutePlanCosts.getMemCost(), respectSourceAvailibility);
	}

	private static double calcBid(ILogicalOperator query, double cpuCosts, double memCosts, boolean respectSourceAvailability) {
		if (respectSourceAvailability && !Helper.allSourcesAvailable(query)) {
			LOG.debug("Not all sources are available. Bid = 0 then.");
			return 0;
		}

		OperatorCost<?> maximumCost = (OperatorCost<?>) CostModelService.get().getMaximumCost();
		LOG.debug("Maximum costs     : {}", maximumCost);

		OperatorCost<?> overallCost = (OperatorCost<?>) CostModelService.get().getOverallCost();
		LOG.debug("Current costs     : {}", overallCost);

		double remainingCpu = maximumCost.getCpuCost() - overallCost.getCpuCost();
		double remainingMem = maximumCost.getMemCost() - overallCost.getMemCost();
		LOG.debug("Cost for query    : {}", formatCosts(memCosts, cpuCosts));
		LOG.debug("Remaining costs   : {}", formatCosts(remainingMem, remainingCpu));
		if (memCosts > remainingMem || cpuCosts > remainingCpu) {
			LOG.debug("Costs are too high. Bid = 0 then.");
			return 0;
		}

		double remainingMemPerc = (remainingMem - memCosts) / maximumCost.getMemCost();
		double remainingCpuPerc = (remainingCpu - cpuCosts) / maximumCost.getCpuCost();
		LOG.debug("Remaining costs(%): MEM at {} %, CPU at {} %", remainingMemPerc * 100, remainingCpuPerc * 100);

		double bid = 0.5 * remainingMemPerc + 0.5 * remainingCpuPerc;
		LOG.debug("Resulting bid = {}", bid);

		return bid;
	}

	private static String formatCosts(double memCost, double cpuCost) {
		return String.format("m = %-10.6f MB ; c = %-10.6f", memCost / 1024 / 1024, cpuCost);
	}

	private static CostSummary calcBearableCostsInPercentage(String id, double cpuCosts, double memCosts, ILogicalOperator plan) {
		return new CostSummary(id, calcBearableCpuCostsInPercentage(cpuCosts), calcBearableMemCostsInPercentage(memCosts), plan);
	}

	private static double calcBearableCpuCostsInPercentage(double cpuCost) {
		double totalCpuCost = Helper.getCpuCostTotal() + cpuCost;

		if (totalCpuCost > 1)
			return 1 / totalCpuCost;

		return 1d;
	}

	private static double calcBearableMemCostsInPercentage(double memCost) {
		double freeMem = Helper.getFreeMemory();
		double remainingMem = freeMem - memCost;

		if (remainingMem <= 0) {
			return freeMem / memCost;
		}

		return 1d;
	}
}
