package de.uniol.inf.is.odysseus.recovery.systemlog;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Service to log system events like startup and shutdown. <br />
 * See {@link ISysLogEntry} for how log entries look like and
 * {@link SystemLogConfiguration} for the file location and name.
 * 
 * @author Michael Brand
 *
 */
public interface ISystemLog {

	/**
	 * Gets all entries.
	 * 
	 * @return All entries ordered by time stamp (older entries first).
	 */
	public ImmutableList<ISysLogEntry> read();

	/**
	 * Gets all entries before a given point in time.
	 * 
	 * @param since
	 *            Time stamp in milliseconds.
	 * @return All entries, which are not older than "since", ordered by time
	 *         stamp (older entries first).
	 */
	public ImmutableList<ISysLogEntry> read(long since);

	/**
	 * Writes an entry to the log.
	 * 
	 * @param entry
	 *            The given entry.
	 * @param caller
	 *            The class, which wants to write the entry.
	 * @return True, if the entry could be written; false, else (e.g., caller
	 *         has no permission or entry is too old).
	 */
	public boolean write(ISysLogEntry entry, Class<?> caller);

	/**
	 * Writes an entry to the log.
	 * 
	 * @param tag
	 *            The tag for the given entry.
	 * @param timeStamp
	 *            The time stamp in milliseconds for the given entry.
	 * @param caller
	 *            The class, which wants to write the entry.
	 * @return True, if the entry could be written; false, else (e.g., caller
	 *         has no permission or entry is too old).
	 */
	public boolean write(String tag, long timeStamp, Class<?> caller);

	/**
	 * Writes an entry to the log.
	 * 
	 * @param tag
	 *            The tag for the given entry.
	 * @param timeStamp
	 *            The time stamp in milliseconds for the given entry.
	 * @param information
	 *            The additional information for the given entry.
	 * @param caller
	 *            The class, which wants to write the entry.
	 * @return True, if the entry could be written; false, else (e.g., caller
	 *         has no permission or entry is too old).
	 */
	public boolean write(String tag, long timeStamp, String information, Class<?> caller);

	/**
	 * Writes several entries to the log.
	 * 
	 * @param entries
	 *            The given entries ordered by time stamp (older entries first).
	 * @param caller
	 *            The class, which wants to write the entry.
	 * @return True, if the entry could be written; false, else (e.g., caller
	 *         has no permission or entry is too old).
	 */
	public boolean write(List<ISysLogEntry> entries, Class<?> caller);

	/**
	 * Registers a listener.
	 * 
	 * @param listener
	 *            A new listener.
	 */
	public void addListener(ISystemLogListener listener);

	/**
	 * Unregisters a listener.
	 * 
	 * @param listener
	 *            A listener to unregister.
	 */
	public void removeListener(ISystemLogListener listener);

}