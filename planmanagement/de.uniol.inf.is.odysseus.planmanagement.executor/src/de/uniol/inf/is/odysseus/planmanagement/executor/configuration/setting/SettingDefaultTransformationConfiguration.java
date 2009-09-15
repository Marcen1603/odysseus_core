package de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.AbstractExecutionSetting;

/**
 * Setting which defines the configuration for transformations from logical to
 * physical operators.
 * 
 * @author Wolf Bauer
 * 
 */
public final class SettingDefaultTransformationConfiguration extends
		AbstractExecutionSetting<TransformationConfiguration> {

	/**
	 * TODO: das kann Jonas besser beschreiben
	 * 
	 * @param dataType
	 * @param metaTypes
	 */
	public SettingDefaultTransformationConfiguration(String dataType,
			String... metaTypes) {
		super(new TransformationConfiguration(dataType, metaTypes));
	}

	/**
	 * TODO: das kann Jonas besser beschreiben
	 * 
	 * @param value
	 */
	public SettingDefaultTransformationConfiguration(
			TransformationConfiguration value) {
		super(value);
	}
}
