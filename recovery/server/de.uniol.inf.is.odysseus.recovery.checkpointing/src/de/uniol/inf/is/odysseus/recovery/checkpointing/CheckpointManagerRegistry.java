package de.uniol.inf.is.odysseus.recovery.checkpointing;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

/**
 * Registry/factory for checkpoint managers. <br />
 * <br />
 * Registry: Implementations of {@link ICheckpointManager} are bound as
 * declarative services (assuming unique names,
 * {@link ICheckpointManager#getName()}). <br />
 * <br />
 * Factory: Concrete checkpoint manager instances can be get by
 * {@link #newInstance(String, CheckpointUnit, long)} or by
 * {@link #createFromConfig(Properties)}, if unit and period are within a given
 * configuration.
 * 
 * @author Michael Brand
 *
 */
public class CheckpointManagerRegistry {

	/**
	 * The configuration key for the name of the checkpoint manager to use.
	 */
	public static final String NAME_KEY = "checkpoints";

	/**
	 * The default value for {@link #NAME_KEY} read out from
	 * {@link OdysseusConfiguration}.
	 */
	public static final String NAME_VALUE_DEFAULT = OdysseusConfiguration.instance.get("protectionpointDefaultManager");

	/**
	 * The configuration key for the checkpoint unit to use.
	 */
	public static final String UNIT_KEY = "checkpointunit";

	/**
	 * The default value for {@link #UNIT_KEY} read out from
	 * {@link OdysseusConfiguration}.
	 */
	public static final CheckpointUnit UNIT_VALUE_DEFAULT = CheckpointUnit
			.valueOf(OdysseusConfiguration.instance.get("checkpointDefaultUnit"));

	/**
	 * The configuration key for the period between two checkpoints to use.
	 */
	public static final String PERIOD_KEY = "checkpointperiod";

	/**
	 * The default value for {@link #PERIOD_KEY} read out from
	 * {@link OdysseusConfiguration}.
	 */
	public static final long PERIOD_VALUE_DEFAULT = Long
			.parseLong(OdysseusConfiguration.instance.get("checkpointDefaultPeriod"));

	/**
	 * All bound implementations of {@link ICheckpointManager}.
	 */
	private static final Map<String, ICheckpointManager> boundManagers = new HashMap<>();

	/**
	 * Binds a new implementation of {@link ICheckpointManager}.
	 */
	public static void bindManager(ICheckpointManager manager) {
		synchronized (boundManagers) {
			boundManagers.put(manager.getName(), manager);
			boundManagers.notifyAll();
		}
	}

	/**
	 * Unbinds an implementation of {@link ICheckpointManager}.
	 */
	public static void unbindManager(ICheckpointManager manager) {
		boundManagers.remove(manager.getName());
	}

	/**
	 * All checkpoint managers in use mapped to the queries, which use them,
	 * represented by their ids.
	 */
	private static final Map<Integer, ICheckpointManager> usedManagers = new HashMap<>();

	/**
	 * Gets the checkpoint manager, which is used for a given query.
	 * 
	 * @param queryId
	 *            The id of the given query.
	 */
	public static ICheckpointManager getInstance(int queryId) {
		return usedManagers.get(new Integer(queryId));
	}

	/**
	 * Sets the checkpoint manager, which is used for a given query.
	 * 
	 * @param queryId
	 *            The id of the given query.
	 * @param The
	 *            checkpoint manager.
	 */
	public static void setCheckpointManager(int queryId, ICheckpointManager manager) {
		usedManagers.put(new Integer(queryId), manager);
		manager.addLogicalQuery(queryId);
	}

	/**
	 * Creates a new checkpoint manager.
	 * 
	 * @see ICheckpointManager#newInstance(CheckpointUnit, long)
	 * @param unit
	 *            The unit to use to determine, when to set checkpoints.
	 * @param period
	 *            The amount of {@code units} between two checkpoints.
	 * @return A new created checkpoint manager ready to use.
	 */
	public static ICheckpointManager newInstance(String name, CheckpointUnit unit, long period) throws Exception {
		return boundManagers.get(name).newInstance(unit, period);
	}

	/**
	 * Creates a new checkpoint manager.
	 * 
	 * @param config
	 *            A configuration, which may contain manager name, unit and
	 *            period (see
	 *            {@link #newInstance(String, CheckpointUnit, long)}). If not,
	 *            default values are used.
	 * @return A new created checkpoint manager ready to use.
	 */
	public static ICheckpointManager createFromConfig(Properties config) throws Exception {
		String managerName = config.getProperty(NAME_KEY, NAME_VALUE_DEFAULT);
		CheckpointUnit unit;
		if (config.containsKey(UNIT_KEY)) {
			unit = CheckpointUnit.valueOf(config.getProperty(UNIT_KEY).toUpperCase());
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