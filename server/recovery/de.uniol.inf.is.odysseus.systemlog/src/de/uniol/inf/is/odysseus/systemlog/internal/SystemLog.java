package de.uniol.inf.is.odysseus.systemlog.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.systemlog.ISystemLogEntry;
import de.uniol.inf.is.odysseus.systemlog.ISystemLogListener;
import de.uniol.inf.is.odysseus.systemlog.SystemLogEntryFactory;

/**
 * Service to log system events like startup and shutdown. <br />
 * See {@link ISystemLogEntry} for how log entries look like and
 * {@link SystemLogConfiguration} for the file location and name.
 * 
 * @author Michael Brand
 *
 */
public class SystemLog implements ISystemLog {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SystemLog.class);

	/**
	 * All log entries ordered by time stamp (older entries first).
	 */
	private final LinkedList<ISystemLogEntry> entries = new LinkedList<>();

	/**
	 * True, if the initial loading from file has been done.
	 */
	private boolean logLoaded = false;

	/**
	 * All bound listeners.
	 */
	private final Set<ISystemLogListener> listeners = Sets.newConcurrentHashSet();

	@Override
	public void loadSystemLogFile() throws Exception {
		LOG.trace("Start loading system log from file...");

		// Get the log file
		File logFile = new File(SystemLogConfiguration.getSystemLogFile());
		if (!logFile.exists()) {
			// Create the log file
			File dir = logFile.getParentFile();
			if (dir == null || !logFile.createNewFile()) {
				throw new IOException("Could not create file: " + SystemLogConfiguration.getSystemLogFile());
			}
			LOG.trace("Created system log file '{}'.", SystemLogConfiguration.getSystemLogFile());
			return;
		}
		// Read out file entry
		try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, SystemLogConfiguration.getSeparator());
				// There should be 2 or 3 fields
				if (token.countTokens() < 2) {
					LOG.error("Ignore system log entry '" + line + "'. There are too many fields!");
					continue;
				} else if (token.countTokens() > 3) {
					LOG.warn("System log entry '{}' has more than 3 fields. Additional fields will be ignored!", line);
				}
				String tag = token.nextToken().trim();
				long timeStamp = Long.parseLong(token.nextToken().trim());
				ISystemLogEntry entry;
				if (token.hasMoreTokens()) {
					// Comment is optional
					String comment = token.nextToken().trim();
					entry = SystemLogEntryFactory.createEntry(tag, timeStamp, comment);
				} else {
					entry = SystemLogEntryFactory.createEntry(tag, timeStamp);
				}
				synchronized (this.entries) {
					this.entries.add(entry);
				}
				// Information can be too long for log entry -> deadlock
				LOG.trace("Loaded system log entry '{}' at '{}'", entry.getTag(), new Long(entry.getTimeStamp()));
			}
		} catch (Exception e) {
			throw e;
		}

		// Call listeners
		for (ISystemLogListener listener : this.listeners) {
			try {
				listener.onInitialSystemLogRead(this.entries);
			} catch (Throwable t) {
				LOG.error("Error occured for listener " + listener.getClass().getSimpleName(), t);
			}
		}
		this.logLoaded = true;
		LOG.trace("Finished loading system log from file.");
	}

	@Override
	public ImmutableList<ISystemLogEntry> read() {
		synchronized (this.entries) {
			return ImmutableList.copyOf(this.entries);
		}
	}

	@Override
	public ImmutableList<ISystemLogEntry> read(long since) {
		// Assumption: entries are sorted by time stamp
		int index;
		synchronized (this.entries) {
			for (index = this.entries.size() - 1; index >= 0
					&& this.entries.get(index).getTimeStamp() >= since; index--) {
				// Nothing to do
			}
		}

		List<ISystemLogEntry> subList = new LinkedList<>();
		synchronized (this.entries) {
			if (++index < this.entries.size()) {
				subList = this.entries.subList(index, this.entries.size());
			}
		}
		return ImmutableList.copyOf(subList);
	}

	/**
	 * Writes an entry to the log.
	 * 
	 * @param entry
	 *            The given entry.
	 * @param caller
	 *            The class, which wants to write the entry.
	 * @param append
	 *            True, if former entry shall remain within the log.
	 * @return True, if the entry could be written; false, else (e.g., caller
	 *         has no permission or entry is too old).
	 */
	private boolean write(ISystemLogEntry entry, Class<?> caller, boolean append) {
		// Precondition
		if (!SystemLogConfiguration.validatePermission(entry, caller)) {
			return false;
		}

		synchronized (this.entries) {
			if (!append) {
				this.entries.clear();
			} else if (!this.entries.isEmpty() && entry.getTimeStamp() < this.entries.peekLast().getTimeStamp()) {
				LOG.error("System log entry '{}' to add is not the newest log entry!", entry);
				return false;
			}
			entries.add(entry);
		}

		// Write it into the log file
		File logFile = new File(SystemLogConfiguration.getSystemLogFile());
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, append))) {
			writer.append(entry.toString());
			writer.newLine();
			writer.flush();
			LOG.trace("Wrote system log entry '{}, {}'", entry.getTag(), new Long(entry.getTimeStamp()));
		} catch (IOException e) {
			LOG.error("Could not write into system log file!", e);
			return false;
		}

		// Call listeners
		for (ISystemLogListener listener : this.listeners) {
			try {
				listener.onSystemLogEntryWritten(entry);
			} catch (Throwable t) {
				LOG.error("Error occured for listener " + listener.getClass().getSimpleName(), t);
			}
		}
		return true;
	}

	@Override
	public boolean write(ISystemLogEntry entry, Class<?> caller) {
		return write(entry, caller, true);
	}

	@Override
	public boolean write(String tag, long timeStamp, Class<?> caller) {
		return write(SystemLogEntryFactory.createEntry(tag, timeStamp), caller);
	}

	@Override
	public boolean write(String tag, long timeStamp, String information, Class<?> caller) {
		return write(SystemLogEntryFactory.createEntry(tag, timeStamp, information), caller);
	}

	/**
	 * Writes several entries to the log.
	 * 
	 * @param entries
	 *            The given entries ordered by time stamp (older entries first).
	 * @param caller
	 *            The class, which wants to write the entries.
	 * @param append
	 *            True, if former entry shall remain within the log.
	 * @return True, if the entries could be written; false, else (e.g., an
	 *         entry is too old).
	 */
	private boolean write(List<ISystemLogEntry> entries, Class<?> caller, boolean append) {
		for (ISystemLogEntry entry : entries) {
			if (!write(entry, caller, append)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean write(List<ISystemLogEntry> entries, Class<?> caller) {
		return write(entries, caller, true);
	}

	@Override
	public void delete(long before) {
		if (before < 0) {
			return;
		}

		List<ISystemLogEntry> entriesToDelete = new LinkedList<>();
		List<ISystemLogEntry> entriesToKeep = new LinkedList<>();
		int indexOfOldestEntryToKeep = 0;

		// Check, what to delete and what to keep
		synchronized (this.entries) {
			if (this.entries.isEmpty() || this.entries.getFirst().getTimeStamp() >= before) {
				return;
			} else if (this.entries.getLast().getTimeStamp() < before) {
				// Clear the log
				this.entries.clear();
				try (BufferedWriter writer = new BufferedWriter(
						new FileWriter(new File(SystemLogConfiguration.getSystemLogFile()), false))) {
					writer.write("");
					writer.close();
					LOG.trace("Cleared the system log file.");
				} catch (IOException e) {
					LOG.error("Could not write into system log file!", e);
				}
				return;
			}

			while (this.entries.get(indexOfOldestEntryToKeep).getTimeStamp() < before
					&& indexOfOldestEntryToKeep < this.entries.size() - 1) {
				LOG.trace("Entry to be deleted: ", this.entries.get(indexOfOldestEntryToKeep++));
			}
			if (indexOfOldestEntryToKeep == 0) {
				return;
			}
			entriesToDelete = this.entries.subList(0, indexOfOldestEntryToKeep);
			entriesToKeep = this.entries.subList(indexOfOldestEntryToKeep, this.entries.size());
		}

		// Overwrite log and file
		write(entriesToKeep, SystemLog.class, false);

		// Call listeners
		for (ISystemLogListener listener : this.listeners) {
			try {
				listener.onSystemLogEntriesDeleted(entriesToDelete);
			} catch (Throwable t) {
				LOG.error("Error occured for listener " + listener.getClass().getSimpleName(), t);
			}
		}
	}

	@Override
	public void addListener(ISystemLogListener listener) {
		this.listeners.add(listener);
		if (this.logLoaded) {
			try {
				listener.onInitialSystemLogRead(this.entries);
			} catch (Throwable t) {
				LOG.error("Error occured for listener " + listener.getClass().getSimpleName(), t);
			}
		}
	}

	@Override
	public void removeListener(ISystemLogListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * When the system log component is activated, the log is loaded from file
	 * and entries that are too old are deleted.
	 * 
	 * @throws Exception
	 *             see {@link #loadSystemLogFile()}
	 */
	public void activate() throws Exception {
		loadSystemLogFile();
		delete(System.currentTimeMillis() - SystemLogConfiguration.getThreshold());
	}

}