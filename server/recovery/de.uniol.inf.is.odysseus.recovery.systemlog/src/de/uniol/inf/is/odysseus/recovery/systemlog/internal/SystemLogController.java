package de.uniol.inf.is.odysseus.recovery.systemlog.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.Tag;

/**
 * Controller to regulate the write access to the system log. <br />
 * Because not every entity has the permission to write certain tags (
 * {@link ISysLogEntry#getTag()}), this controller checks the rights of the
 * caller before writing to the system log.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class SystemLogController implements BundleActivator {

	/**
	 * The default time span (milliseconds) after which older entries will be
	 * deleted from the system log file.
	 */
	private static final long cDefaultThreshold = (long) (Math.pow(10, 3) * Math.pow(60, 2) * 24 * 30);

	/**
	 * Checks, if {@code caller} has the permission to write a given system log
	 * entry.
	 * 
	 * @param entry
	 *            The system log entry to write.
	 * @param caller
	 *            The entity, which wants to write {@code entry}.
	 * @return True, if {@code caller} has the permission to write {@code entry}
	 *         ; false, else.
	 */
	public static boolean validatePermission(ISysLogEntry entry, Object caller) {
		Optional<Tag> tag = Tag.fromString(entry.getTag());
		return tag.isPresent() && tag.get().getGrantedNames().contains(caller.toString());
	}

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
	 * Gets the time span after which older entries will be deleted from the
	 * system log file.
	 * 
	 * @return A time span in milliseconds: defined within Odysseus
	 *         configuration, key "systemlogThresholdMilliseconds" or a default
	 *         value.
	 */
	public static long getThreshold() {
		String property = OdysseusConfiguration.get("systemlogThresholdMilliseconds");
		if (property != null) {
			return Long.parseLong(property);
		}
		return cDefaultThreshold;
	}

	/**
	 * Load system log from file and delete very old logs.
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		SystemLog.loadSystemLogFile();
		SystemLog.delete(System.currentTimeMillis() - getThreshold());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// Nothing to do
	}

}