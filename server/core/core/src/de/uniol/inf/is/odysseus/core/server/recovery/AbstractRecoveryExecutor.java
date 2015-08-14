package de.uniol.inf.is.odysseus.core.server.recovery;

import java.util.Properties;

/**
 * A recovery executor represents a complete recovery strategy by calling
 * certain {@link IRecoveryComponent}s in a certain order.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public abstract class AbstractRecoveryExecutor implements IRecoveryExecutor {

	/**
	 * The configuration for the recovery executor.
	 */
	protected Properties mConfig;

	/**
	 * The configuration key for {@link #isRecoveryNeeded()}.
	 */
	public static final String RECOVERY_NEEDED_KEY = "recoveryneeded";

	@Override
	public boolean isRecoveryNeeded() {
		return Boolean.parseBoolean(this.mConfig.getProperty(
				RECOVERY_NEEDED_KEY, "false"));
	}

}
