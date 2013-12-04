package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.CostModelService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.ServerExecutorService;

public class CostCalculator {

	private final OperatorCostsCalculator operatorCostsCalculator;
	private final RelativeCostsCalculator relativeCostsCalculator;
	private final BidCalculator bidCalculator;

	public CostCalculator() {
		operatorCostsCalculator = new OperatorCostsCalculator(ServerExecutorService.getServerExecutor(), PQLGeneratorService.get(), new PlanProOperatorPartitioner(), CostModelService.get(), new SubPlanManipulator());
		relativeCostsCalculator = new RelativeCostsCalculator(CostModelService.get(), ServerExecutorService.getServerExecutor(), PQLGeneratorService.get());
		bidCalculator = new BidCalculator(ServerExecutorService.getServerExecutor(), CostModelService.get());
	}

	public Map<String, CostSummary> calcCostsProOperator(ILogicalQuery plan, String transCfgName, boolean discardPlan) {

		if (!discardPlan) {
			plan = Helper.copyQuery(PQLGeneratorService.get(), ServerExecutorService.getServerExecutor(), plan);
		}

		return operatorCostsCalculator.calcCostsProOperator(plan.getLogicalPlan(), transCfgName);
	}

	public CostSummary calcBearableCostsInPercentage(CostSummary absoluteCosts) {
		return relativeCostsCalculator.calcBearableCostsInPercentage(absoluteCosts.getOperatorId(), absoluteCosts.getCpuCost(), absoluteCosts.getMemCost(), absoluteCosts.getPlan());
	}
	
	public double calcBid(ILogicalOperator plan, CostSummary absolutePlanCosts) {
		return calcBid(plan, absolutePlanCosts, true);
	}
	
	public double calcBid(ILogicalOperator plan, CostSummary absolutePlanCosts, boolean respectSourceAvailibility) {
		return bidCalculator.calcBid(plan, absolutePlanCosts.getCpuCost(), absolutePlanCosts.getMemCost(), respectSourceAvailibility);
	}
}
