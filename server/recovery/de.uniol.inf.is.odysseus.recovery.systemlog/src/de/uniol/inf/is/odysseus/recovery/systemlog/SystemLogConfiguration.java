package de.uniol.inf.is.odysseus.recovery.systemlog;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.internal.SysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.internal.SystemLog;

/**
 * Utility class for the system log including a factory to create system log
 * entries.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class SystemLogConfiguration implements BundleActivator {

	/**
	 * Gets the system log file.
	 * 
	 * @return A string representing the file (full qualified).
	 */
	public static String getSystemLogFile() {
		return OdysseusBaseConfiguration.getHomeDir() + "systemlog.store";
	}

	/**
	 * Gets the separator for system log entries.
	 * 
	 * @return A String splitting the fields of a system log entry.
	 */
	public static String getSeparator() {
		return ",";
	}

	/**
	 * The default time span (milliseconds) after which older entries will be
	 * deleted from the system log file.
	 */
	private static final long cDefaultThreshold = (long) (Math.pow(10, 3)
			* Math.pow(60, 2) * 24 * 30);

	/**
	 * Gets the time span after which older entries will be deleted from the
	 * system log file.
	 * 
	 * @return A time span in milliseconds: defined within odysseus
	 *         configuration, key "systemlogThresholdMilliseconds" or a default
	 *         value.
	 */
	public static long getThreshold() {
		return OdysseusConfiguration.getLong("systemlogThresholdMilliseconds",
				cDefaultThreshold);
	}

	/**
	 * Creates a new entry with comment.
	 * 
	 * @param tag
	 *            A string identifying the action.
	 * @param timeStamp
	 *            The time stamp of the action in milliseconds.
	 * @param comment
	 *            An optional string or null, if there is no comment.
	 * @return A new system log entry for that action.
	 */
	public static ISysLogEntry createEntry(String tag, long timeStamp,
			String comment) {
		return new SysLogEntry(tag, timeStamp, comment);
	}

	/**
	 * Creates a new entry without comment.
	 * 
	 * @param tag
	 *            A string identifying the action.
	 * @param timeStamp
	 *            The time stamp of the action in milliseconds.
	 * @return A new system log entry for that action.
	 */
	public static ISysLogEntry createEntry(String tag, long timeStamp) {
		return new SysLogEntry(tag, timeStamp);
	}

	/**
	 * Load system log from file and delete very old logs.
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		SystemLog.loadSystemLogFile();
		SystemLog.delete(System.currentTimeMillis()
				- SystemLogConfiguration.getThreshold());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// Nothing to do
	}

}