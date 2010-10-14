package de.uniol.inf.is.odysseus.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Configuration;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting.SettingBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.setting.SettingSchedulingStrategy;

/**
 * ExecutionConfiguration stores the configuration of an {@link IExecutor}.
 * 
 * @author Wolf Bauer
 *
 */
public class ExecutionConfiguration extends Configuration<AbstractExecutionSetting<?>> {

	
	/**
	 * Creates a new ExecutionConfiguration based on {@link AbstractExecutionSetting} entries.
	 * 
	 * @param entries
	 */
	public ExecutionConfiguration(AbstractExecutionSetting<?>... entries) {
		super(entries);
		
	}

	public String getBufferPlacementStrategy(){
		SettingBufferPlacementStrategy s = get(SettingBufferPlacementStrategy.class);
		if (s != null){
			return s.getValue();			
		}
		return null;
	}
	
	public String getSchedulingStrategy(){
		SettingSchedulingStrategy s = get(SettingSchedulingStrategy.class);
		if (s != null){
			return s.getValue();			
		}
		return null;
	}
	
	
	
	

}
