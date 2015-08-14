package de.uniol.inf.is.odysseus.recovery.protectionpoints;

import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.recovery.protectionpoints.internal.SystemTimeProtectionPointManager;

/**
 * Registry/factory for protection point managers. <br />
 * <br />
 * Registry: Implementations of {@link IProtectionPointManager} are bound as
 * declarative services (assuming unique names,
 * {@link IProtectionPointManager#getName()}). <br />
 * <br />
 * Factory: Concrete protection point manager instances can be get by
 * {@link #newInstance(String, ProtectionPointUnit, long)} or by
 * {@link #createFromConfig(Properties)}, if unit and period are within a given
 * configuration.
 * 
 * @author Michael Brand
 *
 */
public class ProtectionPointManagerRegistry {

	/**
	 * The configuration key for the name of the protection point manager to
	 * use.
	 */
	public static final String NAME_KEY = "protectionpointname";

	/**
	 * The default value for {@link #NAME_KEY} read out from
	 * {@link OdysseusConfiguration}.
	 */
	public static final String NAME_VALUE_DEFAULT = OdysseusConfiguration
			.get("protectionpointDefaultManager");

	/**
	 * The configuration key for the protection point unit to use.
	 */
	public static final String UNIT_KEY = "protectionpointunit";

	/**
	 * The default value for {@link #UNIT_KEY} read out from
	 * {@link OdysseusConfiguration}.
	 */
	public static final ProtectionPointUnit UNIT_VALUE_DEFAULT = ProtectionPointUnit
			.valueOf(OdysseusConfiguration.get("protectionpointDefaultUnit"));

	/**
	 * The configuration key for the period between two protection points to
	 * use.
	 */
	public static final String PERIOD_KEY = "protectionpointperiod";

	/**
	 * The default value for {@link #PERIOD_KEY} read out from
	 * {@link OdysseusConfiguration}.
	 */
	public static final long PERIOD_VALUE_DEFAULT = Long
			.parseLong(OdysseusConfiguration
					.get("protectionpointDefaultInterval"));

	/**
	 * All bound implementations of {@link IProtectionPointManager}.
	 */
	private static final Map<String, IProtectionPointManager> cManagers = Maps
			.newHashMap();

	// Registers those implementations, which are within this bundle.
	static {
		IProtectionPointManager systemTimeProtectionPointManager = new SystemTimeProtectionPointManager();
		cManagers.put(systemTimeProtectionPointManager.getName(),
				systemTimeProtectionPointManager);
	}

	/**
	 * Binds a new implementation of {@link IProtectionPointManager}.
	 * 
	 * @param manager
	 *            The implementation to bind, which should have an unique
	 *            {@link IProtectionPointManager#getName()}.
	 */
	public static void bindProtectionPointManager(
			IProtectionPointManager manager) {
		cManagers.put(manager.getName(), manager);
	}

	/**
	 * Unbinds a new implementation of {@link IProtectionPointManager}.
	 * 
	 * @param manager
	 *            The implementation to unbind.
	 */
	public static void unbindProtectionPointManager(
			IProtectionPointManager manager) {
		cManagers.remove(manager.getName());
	}

	/**
	 * All protection point managers in use mapped to the queries, which use
	 * them, represented by their ids.
	 */
	private static final Map<Integer, IProtectionPointManager> cUsedManagers = Maps
			.newHashMap();

	/**
	 * Gets the protection point manager, which is used for a given query.
	 * 
	 * @param queryId
	 *            The id of the given query.
	 * @return The protection point manager, which is used for a given query, or
	 *         {@code null}, if none is set.
	 */
	public static IProtectionPointManager getInstance(int queryId) {
		return cUsedManagers.get(queryId);
	}

	/**
	 * Sets the protection point manager, which is used for a given query.
	 * 
	 * @param queryId
	 *            The id of the given query.
	 * @return The protection point manager, which is used for a given query.
	 */
	public static void setProtectionPointManager(int queryId,
			IProtectionPointManager manager) {
		cUsedManagers.put(queryId, manager);
	}

	/**
	 * Creates a new protection point manager.
	 * 
	 * @see IProtectionPointManager#newInstance(ProtectionPointUnit, long)
	 * @param unit
	 *            The unit to use to determine, when to set protection points.
	 * @param period
	 *            The amount of {@code units} between tweo protection points.
	 * @return A new created protection point manager ready to use.
	 * @throws NullPointerException
	 *             if {@code unit} is null.
	 * @throws IllegalArgumentException
	 *             if {@code period} is not greater than 0.
	 */
	public static IProtectionPointManager newInstance(String name,
			ProtectionPointUnit unit, long period) throws NullPointerException,
			IllegalArgumentException {
		return cManagers.get(name).newInstance(unit, period);
	}

	/**
	 * Creates a new protection point manager.
	 * 
	 * @param config
	 *            A configuration, which may contain manager name, unit and
	 *            period (see
	 *            {@link #newInstance(String, ProtectionPointUnit, long)}). If
	 *            not, default values are used.
	 * @return A new created protection point manager ready to use.
	 */
	public static IProtectionPointManager createFromConfig(Properties config) {
		String managerName = config.getProperty(NAME_KEY, NAME_VALUE_DEFAULT);
		ProtectionPointUnit unit;
		if (config.containsKey(UNIT_KEY)) {
			unit = ProtectionPointUnit.valueOf(config.getProperty(NAME_KEY));
		} else {
			unit = UNIT_VALUE_DEFAULT;
		}
		long interval;
		if (config.containsKey(PERIOD_KEY)) {
			interval = Long.parseLong(config.getProperty(PERIOD_KEY));
		} else {
			interval = PERIOD_VALUE_DEFAULT;
		}
		return newInstance(managerName, unit, interval);
	}

}