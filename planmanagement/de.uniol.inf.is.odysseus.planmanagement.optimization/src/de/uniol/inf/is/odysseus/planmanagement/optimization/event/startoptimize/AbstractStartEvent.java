package de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize;

import de.uniol.inf.is.odysseus.base.planmanagement.event.AbstractEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.AbstractEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

public abstract class AbstractStartEvent<ValueType> extends
		AbstractEvent<IOptimizable, ValueType> {

	private OptimizeParameter parameter = new OptimizeParameter();

	public AbstractStartEvent(IOptimizable sender, String id, ValueType value,
			OptimizeParameter parameter) {
		super(sender, id, value);
		if (parameter != null) {
			this.parameter = parameter;
		}
	}

	public AbstractStartEvent(IOptimizable sender, String id, ValueType value,
			AbstractOptimizationParameter<?>... parameter) {
		super(sender, id, value);
		this.parameter = new OptimizeParameter(parameter);
	}

	public OptimizeParameter getParameter() {
		return parameter;
	}

	public abstract AbstractEndEvent<?> createEndOptimizeEvent(
			IOptimizer optimizer, IExecutionPlan newExecutionPlan);
}
