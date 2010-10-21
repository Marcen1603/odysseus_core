package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

/**
 * Sets the CPU load limit, when optimization requests should be refused.
 * 
 * @author Tobias Witt
 *
 */
public class SettingRefuseOptimizationAtCpuLoad extends Setting<Double> implements IOptimizationSetting<Double> {

	public SettingRefuseOptimizationAtCpuLoad(Double value) {
		super(value);
	}

}
