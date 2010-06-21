package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

/**
 * Sets the CPU load limit, when optimization requests should be refused.
 * 
 * @author Tobias Witt
 *
 */
public class SettingRefuseOptimizationAtCpuLoad extends AbstractOptimizationSetting<Double> {

	public SettingRefuseOptimizationAtCpuLoad(Double value) {
		super(value);
	}

}
