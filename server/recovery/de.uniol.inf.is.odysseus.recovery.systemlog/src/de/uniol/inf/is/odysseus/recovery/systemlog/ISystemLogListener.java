package de.uniol.inf.is.odysseus.recovery.systemlog;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.recovery.ISysLogEntry;

/**
 * Listeners for actions within the system log.
 * 
 * @author Michael Brand
 *
 */
public interface ISystemLogListener {

	/**
	 * Called after reading the system log from file.
	 * 
	 * @param entries
	 *            All read entries.
	 * @throws Throwable
	 *             if any error occurs.
	 */
	public void onInitialSystemLogRead(List<ISysLogEntry> entries)
			throws Throwable;

	/**
	 * Called after writing into the system log.
	 * 
	 * @param entry
	 *            The new entry.
	 * @throws Throwable
	 *             if any error occurs.
	 */
	public void onSystemLogEntryWritten(ISysLogEntry entry) throws Throwable;

	/**
	 * Called after deleting old entries of the system log.
	 * 
	 * @param entries
	 *            All deleted entries.
	 * @throws Throwable
	 *             if any error occurs.
	 */
	public void onSystemLogEntriesDeleted(List<ISysLogEntry> entries)
			throws Throwable;

}