package de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize;

import de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.AbstractEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.PlanEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

public class PlanStartEvent extends AbstractStartEvent<IEditablePlan> {

	public static String PLAN_REOPTIMIZE = "PLAN_REOPTIMIZE";
	
	public PlanStartEvent(IOptimizable sender, String id, IEditablePlan value,
			OptimizeParameter parameter) {
		super(sender, id, value, parameter);
	}

	public PlanStartEvent(IOptimizable sender, String id, IEditablePlan value,
			AbstractOptimizationParameter<?>... parameter) {
		super(sender, id, value, parameter);
	}

	@Override
	public AbstractEndEvent<?> createEndOptimizeEvent(
			IOptimizer optimizer,
			IExecutionPlan newExecutionPlan) {
		return new PlanEndEvent(optimizer, getID(), getValue(), newExecutionPlan);
	}
}
