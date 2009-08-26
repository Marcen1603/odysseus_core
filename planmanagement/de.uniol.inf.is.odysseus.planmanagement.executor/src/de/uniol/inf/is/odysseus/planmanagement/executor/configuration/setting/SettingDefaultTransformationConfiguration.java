package de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.AbstractExecutionSetting;

public final class SettingDefaultTransformationConfiguration extends AbstractExecutionSetting<TransformationConfiguration> {
	
	public SettingDefaultTransformationConfiguration(
			String dataType, String... metaTypes) {
		super(new TransformationConfiguration(dataType, metaTypes));
	}

	public SettingDefaultTransformationConfiguration(
			TransformationConfiguration value) {
		super(value);
	}
}
