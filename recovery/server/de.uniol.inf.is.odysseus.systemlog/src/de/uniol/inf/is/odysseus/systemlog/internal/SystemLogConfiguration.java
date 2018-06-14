package de.uniol.inf.is.odysseus.systemlog.internal;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.systemlog.ISystemLogEntry;

/**
 * Utility class for the system log.
 * 
 * @author Michael Brand
 *
 */
public class SystemLogConfiguration {

	/**
	 * The default time span (milliseconds) after which older entries will be
	 * deleted from the system log file.
	 */
	private static final long cDefaultThreshold = (long) (Math.pow(10, 3) * Math.pow(60, 2) * 24 * 30);

	/**
	 * A map of system log tags (keys) and classes (values), which have the
	 * permission to write the given tag.
	 */
	private static final Map<String, Class<?>> cPermissions = Maps.newHashMap();

	/**
	 * The system log class has the permission to write all tags.
	 */
	private static final Class<?> cWildCard = SystemLog.class;

	/**
	 * Checks, if {@code caller} has the permission to write a given system log
	 * entry.
	 * 
	 * @param entry
	 *            The system log entry to write.
	 * @param caller
	 *            The class, which wants to write {@code entry}.
	 * @return True, if {@code caller} has the permission to write {@code entry}
	 *         , false, else.
	 */
	public static boolean validatePermission(ISystemLogEntry entry, Class<?> caller) {
		String tag = entry.getTag();
		if (cWildCard.equals(caller)) {
			return true;
		} else if (cPermissions.containsKey(tag) && cPermissions.get(tag).equals(caller)) {
			return true;
		} else if (!cPermissions.containsKey(tag)) {
			cPermissions.put(tag, caller);
			return true;
		}
		return false;
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
		String property = OdysseusConfiguration.instance.get("systemlogThresholdMilliseconds");
		if (property != null) {
			return Long.parseLong(property);
		}
		return cDefaultThreshold;
	}

}