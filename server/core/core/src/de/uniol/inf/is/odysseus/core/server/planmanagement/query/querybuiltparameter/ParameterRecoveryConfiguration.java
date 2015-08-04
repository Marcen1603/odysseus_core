package de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter;

import java.util.Properties;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;

/**
 * Parameter for recovery configurations. <br />
 * <br />
 * It contains the name of the recovery executor to use. A recovery executor is
 * like a strategy defining what to backup/recover in which order. Additionally,
 * the parameter contains the configuration for the recovery executor as key
 * value pairs.
 * 
 * @author Michael Brand
 *
 */
public class ParameterRecoveryConfiguration extends Setting<Object>implements IQueryBuildSetting<Object> {

	/**
	 * The default is not to do any recovery.
	 */
	public static ParameterRecoveryConfiguration NoConfiguration = new ParameterRecoveryConfiguration(null, null);

	/**
	 * The name of the recovery executor to use. A recovery executor is like a
	 * strategy defining what to backup/recover in which order.
	 */
	private final String mExecutor;

	/**
	 * Gets the recovery executor to use.
	 * 
	 * @return The name of the recovery executor to use. A recovery executor is
	 *         like a strategy defining what to backup/recover in which order.
	 */
	public String getRecoveryExecutor() {
		return this.mExecutor;
	}

	/**
	 * The configuration for the recovery executor as key value pairs.
	 */
	private final Properties mConfig;

	/**
	 * Gets the configuration.
	 * 
	 * @return The configuration for the recovery executor as key value pairs.
	 */
	public Properties getConfiguration() {
		return this.mConfig;
	}

	/**
	 * Creates a new parameter for recovery configurations
	 * 
	 * @param recoveryExecutor
	 *            The name of the recovery executor to use. A recovery executor
	 *            is like a strategy defining what to backup/recover in which
	 *            order.
	 * @param configuration
	 *            The configuration for the recovery executor as key value
	 *            pairs.
	 */
	public ParameterRecoveryConfiguration(String recoveryExecutor, Properties configuration) {
		super(null);
		this.mExecutor = recoveryExecutor;
		this.mConfig = configuration;
	}

}