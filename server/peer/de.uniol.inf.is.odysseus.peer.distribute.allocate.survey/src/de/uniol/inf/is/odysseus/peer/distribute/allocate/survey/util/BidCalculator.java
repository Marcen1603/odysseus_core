package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModel;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.CostSummary;

public class BidCalculator {

	private static final Logger log = LoggerFactory.getLogger(BidCalculator.class);

	@SuppressWarnings("rawtypes")
	private static OperatorCostModel costModel;

	// called by OSGi-DS
	public static void bindCostModel(ICostModel<?> serv) {
		costModel = (OperatorCostModel<?>)serv;
	}

	// called by OSGi-DS
	public static void unbindCostModel(ICostModel<?> serv) {
		if (costModel == serv) {
			costModel = null;
		}
	}
	
	public static double calcBid( ILogicalQuery query, String transCfgName ) {
		CostSummary summary = calcCostsForPlan(query, transCfgName);
		return calcBidImpl(query.getLogicalPlan(), summary.getCpuCost(), summary.getMemCost());
	}

	@SuppressWarnings("unchecked")
	private static CostSummary calcCostsForPlan(ILogicalQuery query, String transCfgName) {
		IPhysicalQuery p = Helper.getPhysicalQuery(query, transCfgName);

		ICost<IPhysicalOperator> cost = costModel.estimateCost(p.getPhysicalChilds(), false);
		OperatorCost<IPhysicalOperator> c = ((OperatorCost<IPhysicalOperator>) cost);

		double cpuCost = c.getCpuCost();
		double memCost = c.getMemCost();
		return new CostSummary(cpuCost, memCost, query.getLogicalPlan());
	}

	private static double calcBidImpl(ILogicalOperator query, double cpuCosts, double memCosts) {
		if (!Helper.allSourcesAvailable(query)) {
			log.debug("Not all sources are available. Bid = 0 then.");
			return 0;
		}

		OperatorCost<?> maximumCost = (OperatorCost<?>) costModel.getMaximumCost();
		log.debug("Maximum costs     : {}", maximumCost);

		OperatorCost<?> overallCost = (OperatorCost<?>) costModel.getOverallCost();
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

		double bid = ( remainingMemPerc + remainingCpuPerc ) / 2;
		log.debug("Resulting bid = {}", bid);

		return bid;
	}

	private static String formatCosts(double memCost, double cpuCost) {
		return String.format("m = %-10.6f MB ; c = %-10.6f", memCost / 1024 / 1024, cpuCost);
	}
}