package de.uniol.inf.is.odysseus.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AbstractTypeSafeMap;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting.SettingCalculateLatency;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting.SettingDefaultTransformationConfiguration;

public class ExecutionConfiguration extends AbstractTypeSafeMap<AbstractExecutionSetting<?>> {

	public ExecutionConfiguration(AbstractExecutionSetting<?>... entries) {
		super(entries);
	}

	public SettingCalculateLatency getCalculateLatency() {
		if(!this.contains(SettingCalculateLatency.class)) {
			set(SettingCalculateLatency.FALSE, false);
		}
		return (SettingCalculateLatency) this.get(SettingCalculateLatency.class);
	}
	
	public SettingDefaultTransformationConfiguration getSettingDefaultTransformationConfiguration() {
		if(!this.contains(SettingCalculateLatency.class)) {
			set(new SettingDefaultTransformationConfiguration("relational", "de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"), false);
		}
		return (SettingDefaultTransformationConfiguration) this.get(SettingDefaultTransformationConfiguration.class);
	}
}
