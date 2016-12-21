package de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter;

import java.io.Serializable;
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
public class ParameterRecoveryConfiguration extends Setting<Object>
		implements IQueryBuildSetting<Object>, Serializable {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -6598635712941002028L;

	/**
	 * The default is not to do any recovery.
	 */
	public static final ParameterRecoveryConfiguration NoConfiguration = new ParameterRecoveryConfiguration(null, null);

	/**
	 * A string to represent the keyword in Odysseus Script.
	 */
	public static final String keyword = "RECOVERYCONFIGURATION";

	/**
	 * Returns a String "key1=value1 key2=value2..."
	 */
	private static String propertiesToString(Properties cfg) {
		if(cfg == null || cfg.isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (Object key : cfg.keySet()) {
			builder.append(key + "=" + cfg.get(key) + " ");
		}
		return builder.substring(0, builder.length()-1);
	}

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
	 * Adds an entry to the configuration.
	 *
	 * @param key
	 *            The key for the entry.
	 * @param value
	 *            The value for the entry.
	 */
	public void addConfiguration(String key, String value) {
		this.mConfig.put(key, value);
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

	@Override
	public String toOdysseusScript() {
		// No recovery if recovery executor is not set. In this case, generate no Odysseus Script.
		if(mExecutor == null) {
			throw new UnsupportedOperationException();
		}
		return "#" + keyword + " " + this.mExecutor + " " + propertiesToString(this.mConfig);
	}

}