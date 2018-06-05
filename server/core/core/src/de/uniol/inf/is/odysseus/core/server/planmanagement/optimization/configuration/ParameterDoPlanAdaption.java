package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Parameter which indicates if a plan adaption should be done.
 * 
 * @author Merlin Wasmann
 *
 */
public class ParameterDoPlanAdaption extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6366850539431434064L;

	/**
	 * Adapt the plan during runtime.
	 */
	public static final ParameterDoPlanAdaption TRUE = new ParameterDoPlanAdaption(true);
	
	/**
	 * Do not adapt the plan during runtime.
	 */
	public static final ParameterDoPlanAdaption FALSE = new ParameterDoPlanAdaption(false);
	
	
	protected ParameterDoPlanAdaption(Boolean value) {
		super(value);
	}

}
