package de.uniol.inf.is.odysseus.recovery.systemlog.internal;

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

import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLogListener;
import de.uniol.inf.is.odysseus.recovery.systemlog.SystemLogConfiguration;

/**
 * Service to log system events like startup and shutdown. <br />
 * See {@link ISysLogEntry} for how log entries look like and
 * {@link SystemLogConfiguration} for the file location and name.
 * 
 * @author Michael Brand
 *
 */
public class SystemLog implements ISystemLog {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(SystemLog.class);

	/**
	 * All log entries ordered by time stamp (older entries first).
	 */
	private static final LinkedList<ISysLogEntry> cEntries = new LinkedList<ISysLogEntry>();

	/**
	 * True, if the initial loading from file has been done.
	 */
	private static boolean cLogLoaded = false;

	/**
	 * All bound listeners.
	 */
	private static final Set<ISystemLogListener> cListeners = Sets.newHashSet();

	/**
	 * Binds a listener.
	 * 
	 * @param listener
	 *            The listener to bind.
	 */
	public static void bindListener(ISystemLogListener listener) {
		synchronized (cListeners) {
			cListeners.add(listener);
		}
		cLog.debug("Bound '{}' as an implementation of ISystemLogListener.",
				listener.getClass().getSimpleName());
		if (cLogLoaded) {
			try {
				listener.onInitialSystemLogRead(cEntries);
			} catch (Throwable t) {
				cLog.error("Error occured for listener "
						+ listener.getClass().getSimpleName(), t);
			}
		}
	}

	/**
	 * Unbinds a listener.
	 * 
	 * @param listener
	 *            The listener to unbind.
	 */
	public static void unbindListener(ISystemLogListener listener) {
		synchronized (cListeners) {
			cListeners.remove(listener);
		}
		cLog.debug("Unbound '{}' as an implementation of ISystemLogListener.",
				listener.getClass().getSimpleName());
	}

	/**
	 * Loads all entries from the log file. Should be called once! File will be
	 * created if necessary.
	 * 
	 * @throws Exception
	 *             if any error occurs.
	 */
	public static void loadSystemLogFile() throws Exception {
		cLog.debug("Start loading system log from file...");

		// Get the log file
		File logFile = new File(SystemLogConfiguration.getSystemLogFile());
		if (!logFile.exists()) {
			// Create the log file
			File dir = logFile.getParentFile();
			if (dir == null || !logFile.createNewFile()) {
				throw new IOException("Could not create file: "
						+ SystemLogConfiguration.getSystemLogFile());
			}
			cLog.debug("Created system log file '{}'.",
					SystemLogConfiguration.getSystemLogFile());
			return;
		}

		// Read out file entry
		try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line,
						SystemLogConfiguration.getSeparator());
				// There should be 2 or 3 fields
				if (token.countTokens() < 2) {
					cLog.error("Ignore system log entry '" + line
							+ "'. There are too many fields!");
					continue;
				} else if (token.countTokens() > 3) {
					cLog.warn(
							"System log entry '{}' has more than 3 fields. Additional fields will be ignored!",
							line);
				}
				String tag = token.nextToken().trim();
				long timeStamp = Long.parseLong(token.nextToken().trim());
				ISysLogEntry entry;
				if (token.hasMoreTokens()) {
					// Comment is optional
					String comment = token.nextToken().trim();
					entry = SystemLogConfiguration.createEntry(tag, timeStamp,
							comment);
				} else {
					entry = SystemLogConfiguration.createEntry(tag, timeStamp);
				}
				synchronized (cEntries) {
					cEntries.add(entry);
				}
				cLog.debug("Loaded system log entry '{}'", entry);
			}
		} catch (Exception e) {
			throw e;
		}

		// Call listeners
		for (ISystemLogListener listener : cListeners) {
			try {
				listener.onInitialSystemLogRead(cEntries);
			} catch (Throwable t) {
				cLog.error("Error occured for listener "
						+ listener.getClass().getSimpleName(), t);
			}
		}
		cLogLoaded = true;
		cLog.debug("Finished loading system log from file.");
	}

	@Override
	public ImmutableList<ISysLogEntry> read() {
		synchronized (cEntries) {
			return ImmutableList.copyOf(cEntries);
		}
	}

	@Override
	public ImmutableList<ISysLogEntry> read(long since) {
		// Assumption: entries are sorted by time stamp
		ISysLogEntry[] entries;
		synchronized (cEntries) {
			entries = new ISysLogEntry[cEntries.size()];
			cEntries.toArray(entries);
		}
		int index = binSearchTimeStampIndex(since, entries, 0,
				entries.length - 1);
		List<ISysLogEntry> subList = new LinkedList<ISysLogEntry>();
		if (index != -1) {
			synchronized (cEntries) {
				subList = cEntries.subList(index, entries.length);
			}
		}
		return ImmutableList.copyOf(subList);
	}

	/**
	 * Searches the earliest entry with a given time stamp.
	 * 
	 * @param timeStamp
	 *            The given time stamp.
	 * @param entries
	 *            The look-up field (ordered!).
	 * @param start
	 *            The first index to consider (inclusive).
	 * @param end
	 *            The last index to consider (inclusive).
	 * @return The index of the earliest entry with a given time stamp or -1 if
	 *         no entry could be found.
	 */
	private static int binSearchTimeStampIndex(long timeStamp,
			ISysLogEntry[] entries, int start, int end) {
		int p = start + (end - start) / 2;
		if (p < start) {
			return -1;
		}

		long pivot = entries[p].getTimeStamp();
		if (p == 0) {
			// Found it?
			if (pivot == timeStamp) {
				return p;
			} else {
				return -1;
			}
		} else if (pivot > timeStamp) {
			// Have a look on the left side
			return binSearchTimeStampIndex(timeStamp, entries, start, p - 1);
		} else if (pivot < timeStamp) {
			// Have a look on the right side
			return binSearchTimeStampIndex(timeStamp, entries, p + 1, end);
		}
		// Find the most left element with the same time stamp
		for (; p >= 0; p--) {
			if (entries[p].getTimeStamp() != timeStamp) {
				break;
			}
		}
		return p + 1;
	}

	/**
	 * Writes an entry to the log.
	 * 
	 * @param entry
	 *            The given entry.
	 * @param append
	 *            True, if former entry shall remain within the log.
	 * @return True, if the entry could be written; false, else (e.g., entry is
	 *         too old).
	 */
	private static boolean write(ISysLogEntry entry, boolean append) {
		synchronized (cEntries) {
			if (!append) {
				cEntries.clear();
			} else if (!cEntries.isEmpty()
					&& entry.getTimeStamp() < cEntries.peekLast()
							.getTimeStamp()) {
				cLog.error(
						"System log entry '{}' to add is not the newest log entry!",
						entry);
				return false;
			}
			cEntries.add(entry);
		}

		// Write it into the log file
		File logFile = new File(SystemLogConfiguration.getSystemLogFile());
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,
				append))) {
			writer.append(entry.toString());
			writer.newLine();
			writer.close();
			cLog.debug("Wrote system log entry '{}'", entry);
		} catch (IOException e) {
			cLog.error("Could not write into system log file!", e);
			return false;
		}

		// Call listeners
		for (ISystemLogListener listener : cListeners) {
			try {
				listener.onSystemLogEntryWritten(entry);
			} catch (Throwable t) {
				cLog.error("Error occured for listener "
						+ listener.getClass().getSimpleName(), t);
			}
		}
		return true;
	}

	@Override
	public boolean write(ISysLogEntry entry) {
		return write(entry, true);
	}

	@Override
	public boolean write(String tag, long timeStamp) {
		return write(SystemLogConfiguration.createEntry(tag, timeStamp));
	}

	@Override
	public boolean write(String tag, long timeStamp, String comment) {
		return write(SystemLogConfiguration
				.createEntry(tag, timeStamp, comment));
	}

	/**
	 * Writes several entries to the log.
	 * 
	 * @param entries
	 *            The given entries ordered by time stamp (older entries first).
	 * @param append
	 *            True, if former entry shall remain within the log.
	 * @return True, if the entries could be written; false, else (e.g., an
	 *         entry is too old).
	 */
	private static boolean write(List<ISysLogEntry> entries, boolean append) {
		for (ISysLogEntry entry : entries) {
			if (!write(entry, append)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean write(List<ISysLogEntry> entries) {
		return write(entries, true);
	}

	/**
	 * Deletes old entries, which are before a given point in time.
	 * 
	 * @param before
	 *            Time stamp in nanoseconds.
	 */
	public static void delete(long before) {
		if (before < 0) {
			return;
		}

		List<ISysLogEntry> entriesToDelete = new LinkedList<ISysLogEntry>();
		List<ISysLogEntry> entriesToKeep = new LinkedList<ISysLogEntry>();
		int indexOfOldestEntryToKeep = 0;

		// Check, what to delete and what to keep
		synchronized (cEntries) {
			if (cEntries.isEmpty()
					|| cEntries.getFirst().getTimeStamp() >= before) {
				return;
			} else if (cEntries.getLast().getTimeStamp() < before) {
				// Clear the log
				cEntries.clear();
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(
						new File(SystemLogConfiguration.getSystemLogFile()),
						false))) {
					writer.write("");
					writer.close();
					cLog.debug("Cleared the system log file.");
				} catch (IOException e) {
					cLog.error("Could not write into system log file!", e);
				}
				return;
			}

			while (cEntries.get(indexOfOldestEntryToKeep).getTimeStamp() < before
					&& indexOfOldestEntryToKeep < cEntries.size() - 1) {
				cLog.debug("Entry to be deleted: ",
						cEntries.get(indexOfOldestEntryToKeep++));
			}
			if (indexOfOldestEntryToKeep == 0) {
				return;
			}
			entriesToDelete = cEntries.subList(0, indexOfOldestEntryToKeep);
			entriesToKeep = cEntries.subList(indexOfOldestEntryToKeep,
					cEntries.size());
		}

		// Overwrite log and file
		write(entriesToKeep, false);

		// Call listeners
		for (ISystemLogListener listener : cListeners) {
			try {
				listener.onSystemLogEntriesDeleted(entriesToDelete);
			} catch (Throwable t) {
				cLog.error("Error occured for listener "
						+ listener.getClass().getSimpleName(), t);
			}
		}
	}

}