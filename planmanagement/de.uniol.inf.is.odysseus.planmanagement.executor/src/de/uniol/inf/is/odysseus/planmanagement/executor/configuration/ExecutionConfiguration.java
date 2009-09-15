package de.uniol.inf.is.odysseus.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AbstractTypeSafeMap;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting.SettingCalculateLatency;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting.SettingDefaultTransformationConfiguration;

/**
 * ExecutionConfiguration stores the configuration of an {@link IExecutor}.
 * 
 * @author Wolf Bauer
 *
 */
public class ExecutionConfiguration extends AbstractTypeSafeMap<AbstractExecutionSetting<?>> {

	/**
	 * Creates a new ExecutionConfiguration based on {@link AbstractExecutionSetting} entires.
	 * 
	 * @param entries
	 */
	public ExecutionConfiguration(AbstractExecutionSetting<?>... entries) {
		super(entries);
	}

	/**
	 * Gets the current setting for {@link SettingCalculateLatency}.
	 * 
	 * @return current setting for {@link SettingCalculateLatency}
	 */
	public SettingCalculateLatency getCalculateLatency() {
		if(!this.contains(SettingCalculateLatency.class)) {
			set(SettingCalculateLatency.FALSE, false);
		}
		return (SettingCalculateLatency) this.get(SettingCalculateLatency.class);
	}
	
	/**
	 * Gets the current setting for {@link SettingDefaultTransformationConfiguration}.
	 * 
	 * @return current setting for {@link SettingDefaultTransformationConfiguration}
	 */
	public SettingDefaultTransformationConfiguration getSettingDefaultTransformationConfiguration() {
		if(!this.contains(SettingCalculateLatency.class)) {
			set(new SettingDefaultTransformationConfiguration("relational", "de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"), false);
		}
		return (SettingDefaultTransformationConfiguration) this.get(SettingDefaultTransformationConfiguration.class);
	}
}
