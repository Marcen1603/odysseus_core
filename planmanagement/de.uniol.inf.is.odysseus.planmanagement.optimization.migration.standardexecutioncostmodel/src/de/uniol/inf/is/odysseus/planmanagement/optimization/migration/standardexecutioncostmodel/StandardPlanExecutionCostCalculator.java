package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardexecutioncostmodel;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.AbstractCostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICost;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

/**
 * 
 * @author Tobias Witt
 *
 */
public class StandardPlanExecutionCostCalculator extends AbstractCostCalculator<IPhysicalOperator> {
	
	private StandardPlanExecutionCostModel model;
	
	public StandardPlanExecutionCostCalculator(StandardPlanExecutionCostModel costModel) {
		this.model = costModel;
	}

	@Override
	public ICost<IPhysicalOperator> calculateCost(IPhysicalOperator candidate) {
		return AbstractTreeWalker.prefixWalk2(candidate, new PlanExecutionCostVisitor(this.model));
	}
	
}
