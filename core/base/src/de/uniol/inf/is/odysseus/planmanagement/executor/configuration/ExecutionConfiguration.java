package de.uniol.inf.is.odysseus.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.AbstractTypeSafeMap;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting.SettingCalculateLatency;

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
}
