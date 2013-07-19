package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * This class represents the setting for the data fragmentation. <br />
 * In Odysseus Script it can be used as follows: <br />
 * #DODATAFRAGMENTATION true/false
 * @author Michael Brand
 */
public class ParameterDoDataFragmentation extends Setting<Boolean> implements
		IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {
	
	/**
	 * Constant for <code>true</code> as value.
	 */
	public static final ParameterDoDataFragmentation TRUE = new ParameterDoDataFragmentation(true);
	
	/**
	 * Constant for <code>false</code> as value.
	 */
	public static final ParameterDoDataFragmentation FALSE = new ParameterDoDataFragmentation(false);
	
	/**
	 * Constructs a new parameter "DODATAFRAGMENTATION".
	 * @param value The value for the parameter.
	 */
	protected ParameterDoDataFragmentation(Boolean value) {
		
		super(value);
		
	}

}