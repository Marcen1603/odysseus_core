package de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter;

import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;

/**
 * Parameter which indicates if a query should be restructured.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterDoRestruct extends AbstractOptimizationParameter<Boolean> {
	/**
	 * Restructure a query.
	 */
	public static final ParameterDoRestruct TRUE = new ParameterDoRestruct(true);
	
	/**
	 * Do not restructure a query.
	 */
	public static final ParameterDoRestruct FALSE = new ParameterDoRestruct(false);

	/**
	 * Creates a new ParameterDoRestruct parameter. This is private because
	 * only TRUE and FALSE should be used.
	 * 
	 * @param value
	 *            new value of this parameter
	 */
	private ParameterDoRestruct(Boolean value) {
		super(value);
	}
}
