package de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting;

import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.AbstractExecutionSetting;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;

/**
 * This Setting is an Example for using the {@link ExecutionConfiguration}. The
 * user can define the {@link IBufferPlacementStrategy} represented by an ID
 * which should be used by odysseus.
 * 
 * @author Wolf Bauer
 * 
 */
public class SettingBufferPlacementStrategy extends
		AbstractExecutionSetting<String> {

	/**
	 * Creates a new Setting.  
	 * 
	 * @param value The ID of the new {@link IBufferPlacementStrategy}.
	 */
	public SettingBufferPlacementStrategy(String value) {
		super(value);
	}
}
