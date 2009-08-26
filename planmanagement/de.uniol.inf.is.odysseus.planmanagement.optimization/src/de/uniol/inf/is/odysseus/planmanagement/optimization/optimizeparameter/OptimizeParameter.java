package de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AbstractTypeSafeMap;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterQueryOptimizer;

public class OptimizeParameter extends AbstractTypeSafeMap<AbstractOptimizationParameter<?>> {
	
	public OptimizeParameter() {
	}

	public OptimizeParameter(AbstractOptimizationParameter<?>... entries) {
		super(entries);
	}

	public ParameterDoRestruct getParameterDoRestruct() {
		return (ParameterDoRestruct) this.get(ParameterDoRestruct.class);
	}

	public ParameterQueryOptimizer getParameterQueryOptimizer() {
		return (ParameterQueryOptimizer) this.get(ParameterQueryOptimizer.class);
	}
}
