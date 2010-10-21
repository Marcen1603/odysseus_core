package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Parameter which indicates if a query should be restructured.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterDoRewrite extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {
	/**
	 * Restructure a query.
	 */
	public static final ParameterDoRewrite TRUE = new ParameterDoRewrite(true);
	
	/**
	 * Do not restructure a query.
	 */
	public static final ParameterDoRewrite FALSE = new ParameterDoRewrite(false);

	/**
	 * Creates a new ParameterDoRestruct parameter. This is private because
	 * only TRUE and FALSE should be used.
	 * 
	 * @param value
	 *            new value of this parameter
	 */
	public ParameterDoRewrite(Boolean value) {
		super(value);
	}
}
