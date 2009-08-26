package de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter;

import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;


public class ParameterDoRestruct extends AbstractOptimizationParameter<Boolean> {
	public static final ParameterDoRestruct TRUE = new ParameterDoRestruct(true);
	public static final ParameterDoRestruct FALSE = new ParameterDoRestruct(false);

	private ParameterDoRestruct(Boolean value) {
		super(value);
	}
}
