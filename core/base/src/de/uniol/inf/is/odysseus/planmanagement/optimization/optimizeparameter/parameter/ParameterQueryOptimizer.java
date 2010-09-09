package de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter;

import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;

/**
 * Parameter which indicates contains a query optimizer. Can be used to provide
 * such a n optimizer in a plan optimizer.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterQueryOptimizer extends
		AbstractOptimizationParameter<IQueryOptimizer> {

	/**
	 * Creates a new ParameterQueryOptimizer with a specific query optimizer.
	 * 
	 * @param value
	 *            query optimizer which should be provided.
	 */
	public ParameterQueryOptimizer(IQueryOptimizer value) {
		super(value);
	}
}
