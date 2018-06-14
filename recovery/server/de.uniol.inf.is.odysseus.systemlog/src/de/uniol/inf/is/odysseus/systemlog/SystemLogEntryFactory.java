package de.uniol.inf.is.odysseus.systemlog;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.systemlog.internal.SystemLogEntry;

/**
 * Factory to create system log entries.
 * 
 * @author Michael Brand
 *
 */
public class SystemLogEntryFactory {
	
	/**
	 * Creates a new entry with additional information.
	 * 
	 * @param tag
	 *            A string identifying the action.
	 * @param timeStamp
	 *            The time stamp of the action in milliseconds.
	 * @param information
	 *            An optional string or {@link Optional#absent()}, if there are no
	 *         additional information.
	 * @return A new system log entry for that action.
	 */
	public static ISystemLogEntry createEntry(String tag, long timeStamp, String information) {
		return new SystemLogEntry(tag, timeStamp, information);
	}

	/**
	 * Creates a new entry without additional information.
	 * 
	 * @param tag
	 *            A string identifying the action.
	 * @param timeStamp
	 *            The time stamp of the action in milliseconds.
	 * @return A new system log entry for that action.
	 */
	public static ISystemLogEntry createEntry(String tag, long timeStamp) {
		return new SystemLogEntry(tag, timeStamp);
	}

}