package de.uniol.inf.is.odysseus.planmanagement.executor.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Configuration;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

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

}
