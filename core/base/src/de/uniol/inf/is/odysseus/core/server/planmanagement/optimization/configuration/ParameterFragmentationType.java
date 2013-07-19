package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * This class represents the setting for the data fragmentation strategies. <br />
 * In Odysseus Script it can be used as follows: <br />
 * #DATAFRAGMENTATIONTYPE <name>
 * @author Michael Brand
 */
public class ParameterFragmentationType extends Setting<String> implements
		IOptimizationSetting<String>, IQueryBuildSetting<String> {
	
	/**
	 * The string representation for an undefined strategy.
	 */
	public static final String UNDEFINED = "undefined";
	
	/**
	 * Constructs a new parameter "DATAFRAGMENTATIONTYPE".
	 * @param value The value for the parameter.
	 */
	public ParameterFragmentationType(String value) {
		
		super(value);
		
	}

}