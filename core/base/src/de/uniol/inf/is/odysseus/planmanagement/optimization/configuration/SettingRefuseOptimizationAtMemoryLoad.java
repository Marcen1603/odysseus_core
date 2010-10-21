package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

/**
 * Sets the memory usage limit, when optimization requests should be refused.
 * 
 * @author Tobias Witt
 *
 */
public class SettingRefuseOptimizationAtMemoryLoad extends Setting<Double> implements IOptimizationSetting<Double>{

	public SettingRefuseOptimizationAtMemoryLoad(Double value) {
		super(value);
	}

}
