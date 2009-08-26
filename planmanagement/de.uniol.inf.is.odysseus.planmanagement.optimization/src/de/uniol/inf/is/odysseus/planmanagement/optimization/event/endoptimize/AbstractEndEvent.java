package de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize;

import de.uniol.inf.is.odysseus.base.planmanagement.event.AbstractEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;

public abstract class AbstractEndEvent<ValueType> extends
		AbstractEvent<IOptimizer, ValueType> {

	private IExecutionPlan newExecutionPLan;

	public AbstractEndEvent(IOptimizer sender, String id, ValueType value,
			IExecutionPlan newExecutionPLan) {
		super(sender, id, value);
		this.newExecutionPLan = newExecutionPLan;
	}

	public IExecutionPlan getNewExecutionPlan() {
		return this.newExecutionPLan;
	}
}
