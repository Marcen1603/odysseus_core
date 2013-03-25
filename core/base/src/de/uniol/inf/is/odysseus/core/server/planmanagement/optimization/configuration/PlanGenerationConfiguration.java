/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration containing all information needed for a plan generation process.
 * 
 * @author Merlin Wasmann 
 *
 */
public class PlanGenerationConfiguration implements IOptimizationSetting<Map<String,String>>{

	Map<String, String> configuration = new HashMap<String, String>();
	
	public PlanGenerationConfiguration() {
		
	}
	
	public PlanGenerationConfiguration(Map<String, String> config) {
		this.configuration = config;
	}
	
	@Override
	public Map<String, String> getValue() {
		return this.configuration;
	}

}
