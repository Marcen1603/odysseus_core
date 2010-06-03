package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

/**
 * Sets the memory usage limit, when optimization requests should be refused.
 * 
 * @author Tobias Witt
 *
 */
public class SettingRefuseOptimizationAtMemoryLoad extends AbstractOptimizationSetting<Double>{

	public SettingRefuseOptimizationAtMemoryLoad(Double value) {
		super(value);
	}

}
