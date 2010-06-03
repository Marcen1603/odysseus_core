package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

/**
 * Sets the maximum number of concurrent optimizations started by the
 * AdvancedOptimizer.
 * 
 * @author Tobias Witt
 * 
 */
public class SettingMaxConcurrentOptimizations extends AbstractOptimizationSetting<Integer> {

	public SettingMaxConcurrentOptimizations(Integer value) {
		super(value);
	}

}
