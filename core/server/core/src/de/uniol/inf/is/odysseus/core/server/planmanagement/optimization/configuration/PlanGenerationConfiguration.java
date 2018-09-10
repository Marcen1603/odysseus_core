/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Configuration containing all information needed for a plan generation process.
 * 
 * @author Merlin Wasmann 
 *
 */
public class PlanGenerationConfiguration extends Setting<Map<String, String>> implements IOptimizationSetting<Map<String,String>>, IQueryBuildSetting<Map<String, String>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3367195739139238470L;
	Map<String, String> configuration = new HashMap<String, String>();
	
	public PlanGenerationConfiguration() {
		super(null);
	}
	
	public PlanGenerationConfiguration(Map<String, String> config) {
		super(config);
		this.configuration = config;
	}
	
	@Override
	public Map<String, String> getValue() {
		return this.configuration;
	}

}
