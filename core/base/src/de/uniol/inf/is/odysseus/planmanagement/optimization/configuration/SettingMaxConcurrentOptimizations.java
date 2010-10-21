package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

/**
 * Sets the maximum number of concurrent optimizations started by the
 * AdvancedOptimizer.
 * 
 * @author Tobias Witt
 * 
 */
public class SettingMaxConcurrentOptimizations extends Setting<Integer> implements IOptimizationSetting<Integer> {

	public SettingMaxConcurrentOptimizations(Integer value) {
		super(value);
	}

}
