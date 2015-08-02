package de.uniol.inf.is.odysseus.recovery.rollbackrecovery;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;

/**
 * The rollback recovery executor represents a recovery strategy, which backups
 * and recovery query states (incl. sinks and sources), operator states (incl.
 * queue states), and incoming data streams. <br />
 * <br />
 * The rollback recovery fulfills the requirement of completeness, but not the
 * requirement of correctness.
 * 
 * @author Michael Brand
 *
 */
public class RollbackRecoveryExecutor implements IRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(RollbackRecoveryExecutor.class);

	@Override
	public String getName() {
		return "RollbackRecovery";
	}

	/**
	 * The blank recovery component for the incoming data stream elements (to be
	 * bound by OSGi).
	 */
	private static IRecoveryComponent cSourceStreamsComponent;

	// TODO RollbackRecoveryExecutor misses Operator Recovery

	/**
	 * The recovery components for the incoming data stream elements mapped to
	 * the source name.
	 */
	private final Map<String, IRecoveryComponent> mSourceStreamComponents = Maps.newHashMap();

	/**
	 * Binds either a recovery component for the incoming data stream elements
	 * or for the operator states.
	 * 
	 * @param component
	 *            The component to bind, if its name matches "Source Streams" or
	 *            "Operator Recovery".
	 */
	public static void bindComponent(IRecoveryComponent component) {
		if (component.getName().equals("Source Streams")) {
			cSourceStreamsComponent = component;
		}
		// TODO RollbackRecoveryExecutor misses Operator Recovery
	}

	/**
	 * Unbinds a recovery component
	 * 
	 * @param component
	 *            The component to unbind, if it has been bound before.
	 */
	public static void unbindComponent(IRecoveryComponent component) {
		if (cSourceStreamsComponent != null && cSourceStreamsComponent.equals(component)) {
			cSourceStreamsComponent = null;
		}
		// TODO RollbackRecoveryExecutor misses Operator Recovery
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		RollbackRecoveryExecutor executor = new RollbackRecoveryExecutor();
		executor.configureSourceStreamsComponents(config);
		return executor;
	}

	/**
	 * Configuration all needed source streams components
	 * 
	 * @param config
	 *            The complete configuration for the recovery executor.
	 */
	private void configureSourceStreamsComponents(Properties config) {
		final String prefix = "source";
		Map<Integer, Properties> sourceStreamsConfigs = Maps.newHashMap();
		for (String key : config.stringPropertyNames()) {
			if (key.toLowerCase().startsWith(prefix)) {
				int dotIndex = key.indexOf(".");
				if (dotIndex != -1) {
					int sourceNumber = Integer.parseInt(key.substring(prefix.length(), dotIndex));
					Properties sourceStreamsConfig;
					if (sourceStreamsConfigs.containsKey(sourceNumber)) {
						sourceStreamsConfig = sourceStreamsConfigs.get(sourceNumber);
					} else {
						sourceStreamsConfig = new Properties();
					}
					sourceStreamsConfig.setProperty(key.substring(dotIndex + 1), config.getProperty(key));
					sourceStreamsConfigs.put(sourceNumber, sourceStreamsConfig);
				}
			}
		}
		for (int sourceNumber : sourceStreamsConfigs.keySet()) {
			String sourcename = sourceStreamsConfigs.get(sourceNumber).getProperty("sourcename");
			this.mSourceStreamComponents.put(sourcename,
					cSourceStreamsComponent.newInstance(sourceStreamsConfigs.get(sourceNumber)));
		}
	}

	@Override
	public void recover(List<Integer> queryIds, ISession caller, List<ISysLogEntry> log) throws Exception {
		// TODO implement RollbackRecoveryExecutor.recover
	}

	/**
	 * Nothing to do, because backup of query states (incl. sinks and sources)
	 * is done globally.
	 */
	@Override
	public void activateBackup(List<Integer> queryIds, ISession caller) {
		if (cSourceStreamsComponent == null) {
			cLog.error("RollBackRecovery executor misses Source Streams component!");
			return;
		}
		// TODO What about protection points?
		cSourceStreamsComponent.activateBackup(queryIds, caller);
		// TODO RollbackRecoveryExecutor misses Operator Recovery
	}

}