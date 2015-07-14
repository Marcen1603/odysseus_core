package de.uniol.inf.is.odysseus.recovery.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;

/**
 * The only parameter for the Odysseus Script keyword
 * {@link RecoveryConfigKeyword}.
 * 
 * @author Michael Brand
 *
 */
public class RecoveryExecutorConfigSettingParameter extends
		Setting<IRecoveryExecutor> {

	/**
	 * Creates a new parameter.
	 * 
	 * @param value
	 *            The {@link IRecoveryExecutor} to be represented.
	 */
	public RecoveryExecutorConfigSettingParameter(IRecoveryExecutor value) {
		super(value);
	}

}