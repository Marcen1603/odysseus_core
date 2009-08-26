package de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize;

import de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;

public class PlanEndEvent extends AbstractEndEvent<IEditablePlan> {

	public static String PLAN_REOPTIMIZE = "PLAN_REOPTIMIZE";

	public PlanEndEvent(IOptimizer sender, String id, IEditablePlan value,
			IExecutionPlan newExecutionPlan) {
		super(sender, id, value, newExecutionPlan);
	}
}
