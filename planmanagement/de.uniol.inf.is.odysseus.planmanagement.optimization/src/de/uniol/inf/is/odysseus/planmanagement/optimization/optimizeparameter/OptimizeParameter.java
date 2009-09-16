package de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AbstractTypeSafeMap;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterQueryOptimizer;

/**
 * OptimizeParameter stores all parameter for an optimization.
 * 
 * @author Wolf Bauer
 *
 */
public class OptimizeParameter extends AbstractTypeSafeMap<AbstractOptimizationParameter<?>> {
	
	/**
	 * Creates an empty OptimizeParameter.
	 */
	public OptimizeParameter() {
	}

	/**
	 *  Creates an OptimizeParameter with some entries.
	 * 
	 * @param entries {@link AbstractOptimizationParameter} that should be stored.
	 */
	public OptimizeParameter(AbstractOptimizationParameter<?>... entries) {
		super(entries);
	}

	/**
	 * Gets the current parameter for {@link ParameterDoRestruct}.
	 * 
	 * @return current parameter for {@link ParameterDoRestruct}
	 */
	public ParameterDoRestruct getParameterDoRestruct() {
		return (ParameterDoRestruct) this.get(ParameterDoRestruct.class);
	}

	/**
	 * Gets the current parameter for {@link ParameterQueryOptimizer}.
	 * 
	 * @return current parameter for {@link ParameterQueryOptimizer}
	 */
	public ParameterQueryOptimizer getParameterQueryOptimizer() {
		return (ParameterQueryOptimizer) this.get(ParameterQueryOptimizer.class);
	}
}
