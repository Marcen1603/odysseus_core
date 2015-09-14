package de.uniol.inf.is.odysseus.core.server.recovery;

import java.util.Properties;

/**
 * A recovery executor represents a complete recovery strategy by calling
 * certain {@link IRecoveryComponent}s in a certain order.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings("nls")
public abstract class AbstractRecoveryExecutor implements IRecoveryExecutor {

	/**
	 * The configuration for the recovery executor.
	 */
	protected Properties mConfig;

	@Override
	public boolean isRecoveryNeeded() {
		return this.mConfig.containsKey("recoveryneeded")
				&& Boolean.parseBoolean(this.mConfig.getProperty("recoveryneeded"));
	}

}