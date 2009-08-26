package de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting;

import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.AbstractExecutionSetting;


public class SettingCalculateLatency extends AbstractExecutionSetting<Boolean> {
	
	public static final SettingCalculateLatency TRUE = new SettingCalculateLatency(true);
	public static final SettingCalculateLatency FALSE = new SettingCalculateLatency(false);
	
	private SettingCalculateLatency(Boolean value) {
		super(value);
	}	
}
