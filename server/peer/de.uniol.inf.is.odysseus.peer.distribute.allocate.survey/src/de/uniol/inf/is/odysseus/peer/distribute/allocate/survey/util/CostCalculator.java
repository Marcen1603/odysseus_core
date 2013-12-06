package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service.CostModelService;

public class CostCalculator {

	private static final Logger log = LoggerFactory.getLogger(CostCalculator.class);

	public static double calcBid(String pqlStatement, String transCfgName) {
		return calcBid(pqlStatement, transCfgName, true);
	}

	@SuppressWarnings("unchecked")
	public static double calcBid(String pqlStatement, String transCfgName, boolean respectSourceAvailability) {
		ILogicalQuery query = Helper.getLogicalQuery(pqlStatement).get(0);

		IPhysicalQuery q = Helper.getPhysicalQuery(query, transCfgName);
		ICost<IPhysicalOperator> cost = CostModelService.get().estimateCost(q.getPhysicalChilds(), respectSourceAvailability);
		if (cost instanceof OperatorCost) {
			OperatorCost<IPhysicalOperator> c = ((OperatorCost<IPhysicalOperator>) cost);
			return calcBid(query.getLogicalPlan(), c.getCpuCost(), c.getMemCost(), respectSourceAvailability);
		}

		throw new RuntimeException("Did not expect this implementation of ICost: " + cost.getClass().getName());
	}

	public static double calcBid(ILogicalOperator query, double cpuCosts, double memCosts, boolean respectSourceAvailability) {
		if (respectSourceAvailability && !Helper.allSourcesAvailable(query)) {
			log.debug("Not all sources are available. Bid = 0 then.");
			return 0;
		}

		OperatorCost<?> maximumCost = (OperatorCost<?>) CostModelService.get().getMaximumCost();
		log.debug("Maximum costs     : {}", maximumCost);

		OperatorCost<?> overallCost = (OperatorCost<?>) CostModelService.get().getOverallCost();
		log.debug("Current costs     : {}", overallCost);

		double remainingCpu = maximumCost.getCpuCost() - overallCost.getCpuCost();
		double remainingMem = maximumCost.getMemCost() - overallCost.getMemCost();
		log.debug("Cost for query    : {}", formatCosts(memCosts, cpuCosts));
		log.debug("Remaining costs   : {}", formatCosts(remainingMem, remainingCpu));
		if (memCosts > remainingMem || cpuCosts > remainingCpu) {
			log.debug("Costs are too high. Bid = 0 then.");
			return 0;
		}

		double remainingMemPerc = (remainingMem - memCosts) / maximumCost.getMemCost();
		double remainingCpuPerc = (remainingCpu - cpuCosts) / maximumCost.getCpuCost();
		log.debug("Remaining costs(%): MEM at {} %, CPU at {} %", remainingMemPerc * 100, remainingCpuPerc * 100);

		double bid = 0.5 * remainingMemPerc + 0.5 * remainingCpuPerc;
		log.debug("Resulting bid = {}", bid);

		return bid;
	}

	private static String formatCosts(double memCost, double cpuCost) {
		return String.format("m = %-10.6f MB ; c = %-10.6f", memCost / 1024 / 1024, cpuCost);
	}

	public static CostSummary calcBearableCostsInPercentage(String id, double cpuCosts, double memCosts, ILogicalOperator plan) {
		return new CostSummary(id, calcBearableCpuCostsInPercentage(cpuCosts), calcBearableMemCostsInPercentage(memCosts), plan);
	}

	public static double calcBearableCpuCostsInPercentage(double cpuCost) {
		double totalCpuCost = Helper.getCpuCostTotal() + cpuCost;

		if (totalCpuCost > 1)
			return 1 / totalCpuCost;

		return 1d;
	}

	public static double calcBearableMemCostsInPercentage(double memCost) {
		double freeMem = Helper.getFreeMemory();
		double remainingMem = freeMem - memCost;

		if (remainingMem <= 0) {
			return freeMem / memCost;
		}

		return 1d;
	}
	

	@SuppressWarnings("unchecked")
	public static Map<String, CostSummary> calcCostsProOperator(ILogicalOperator query, String transCfgName) {
		Map<String, CostSummary> operatorCost = new HashMap<>();

		int i = 0;
		List<SubPlan> parts = PlanProOperatorPartitioner.partitionWithDummyOperators(query, null, null);

		for (SubPlan part : parts) {
			if (part.getOperators().isEmpty() || skipOperator(part))
				continue;
			ILogicalQuery q = Helper.transformToQuery(part.getOperators().get(0),  "subplan_" + i);
			IPhysicalQuery p = Helper.getPhysicalQuery(q, transCfgName);
			String id = getOperatorId(part);

			ICost<IPhysicalOperator> cost = CostModelService.get().estimateCost(p.getPhysicalChilds(), false);
			OperatorCost<IPhysicalOperator> c = ((OperatorCost<IPhysicalOperator>) cost);

			double cpuCost = c.getCpuCost();
			double memCost = c.getMemCost();
			operatorCost.put(id, new CostSummary(id, cpuCost, memCost, query));
			i++;

		}
		return operatorCost;
	}

	private static boolean skipOperator(SubPlan part) {
		// return (part.getOperators().get(0) instanceof AccessAO) ||
		// (part.getOperators().get(0) instanceof TimestampAO);
		return (part.getOperators().get(0) instanceof AccessAO);
	}

	private static String getOperatorId(SubPlan part) {
		return Helper.getId(part.getOperators().get(0));
	}
	
	@SuppressWarnings("unchecked")
	public static CostSummary calcCostsForPlan(ILogicalQuery query, String transCfgName) {
		IPhysicalQuery p = Helper.getPhysicalQuery(query, transCfgName);

		ICost<IPhysicalOperator> cost = CostModelService.get().estimateCost(p.getPhysicalChilds(), false);
		OperatorCost<IPhysicalOperator> c = ((OperatorCost<IPhysicalOperator>) cost);

		double cpuCost = c.getCpuCost();
		double memCost = c.getMemCost();
		return new CostSummary(null, cpuCost, memCost, query.getLogicalPlan());
	}

	public static CostSummary calcBearableCostsInPercentage(CostSummary absoluteCosts) {
		return calcBearableCostsInPercentage(absoluteCosts.getOperatorId(), absoluteCosts.getCpuCost(), absoluteCosts.getMemCost(), absoluteCosts.getPlan());
	}

	public static double calcBid(ILogicalOperator plan, CostSummary absolutePlanCosts) {
		return calcBid(plan, absolutePlanCosts, true);
	}

	public static double calcBid(ILogicalOperator plan, CostSummary absolutePlanCosts, boolean respectSourceAvailibility) {
		return calcBid(plan, absolutePlanCosts.getCpuCost(), absolutePlanCosts.getMemCost(), respectSourceAvailibility);
	}

	public static Map<String, CostSummary> calcCostsProOperator(ILogicalQuery plan, String transCfgName, boolean discardPlan) {
		if (!discardPlan) {
			plan = Helper.copyQuery(plan);
		}

		return calcCostsProOperator(plan.getLogicalPlan(), transCfgName);
	}
}
