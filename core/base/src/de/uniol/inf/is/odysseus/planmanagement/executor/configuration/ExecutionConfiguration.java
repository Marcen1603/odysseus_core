package de.uniol.inf.is.odysseus.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Configuration;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

/**
 * ExecutionConfiguration stores the configuration of an {@link IExecutor}.
 * 
 * @author Wolf Bauer
 *
 */
public class ExecutionConfiguration extends Configuration<IExecutionSetting<?>> {

	
	/**
	 * Creates a new ExecutionConfiguration based on {@link IExecutionSetting} entries.
	 * 
	 * @param entries
	 */
	public ExecutionConfiguration(IExecutionSetting<?>... entries) {
		super(entries);
		
	}

}
