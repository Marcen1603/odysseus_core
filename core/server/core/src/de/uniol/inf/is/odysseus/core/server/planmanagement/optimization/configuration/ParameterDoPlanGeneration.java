package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Parameter which indicates if the plan generation should be triggered or not.
 * 
 * @author Merlin Wasmann
 *
 */
public class ParameterDoPlanGeneration extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5927656930906303806L;

	/**
	 * Generate multiple plans from a query.
	 */
	public static final ParameterDoPlanGeneration TRUE = new ParameterDoPlanGeneration(true);
	
	/**
	 * Do not generatte multiple plans from a query.
	 */
	public static final ParameterDoPlanGeneration FALSE = new ParameterDoPlanGeneration(false);
	
	protected ParameterDoPlanGeneration(Boolean value) {
		super(value);
	}

}
