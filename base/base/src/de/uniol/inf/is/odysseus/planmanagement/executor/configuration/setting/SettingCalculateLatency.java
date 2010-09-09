package de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting;

import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.AbstractExecutionSetting;

/**
 * Setting which indicates if the data latency should be calculated.
 * 
 * @author Wolf Bauer
 * 
 */
public class SettingCalculateLatency extends AbstractExecutionSetting<Boolean> {

	/**
	 * Calculate the data latency.
	 */
	public static final SettingCalculateLatency TRUE = new SettingCalculateLatency(
			true);

	/**
	 * Do not calculate the data latency.
	 */
	public static final SettingCalculateLatency FALSE = new SettingCalculateLatency(
			false);

	/**
	 * Creates a new SettingCalculateLatency setting. This is private because
	 * only TRUE and FALSE should be used.
	 * 
	 * @param value
	 *            new value of this setting
	 */
	private SettingCalculateLatency(Boolean value) {
		super(value);
	}
}
