package de.uniol.inf.is.odysseus.recovery.systemstatelogger;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLogListener;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.crashdetection.ICrashDetectionListener;

/**
 * Class to check, if there has been a crash of Odysseus. It uses the system log
 * to do so.
 * 
 * @author Michael Brand
 *
 */
public class CrashDetector implements ISystemLogListener {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(CrashDetector.class);

	/**
	 * The time stamp in milliseconds of the last startup, if a crash has been detected, or -1
	 * else.
	 */
	private static long cLastStartup = -1L;

	/**
	 * All bound listeners.
	 */
	private static final Set<ICrashDetectionListener> cListeners = Sets
			.newHashSet();

	/**
	 * Binds a listener.
	 * 
	 * @param listener
	 *            The listener to bind.
	 */
	public static void bindListener(ICrashDetectionListener listener) {
		synchronized (cListeners) {
			cListeners.add(listener);
		}
		cLog.debug(
				"Bound '{}' as an implementation of ICrashDetectionListener.",
				listener.getClass().getSimpleName());
		if (cLastStartup != -1) {
			try {
				listener.onCrashDetected(cLastStartup);
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
	public static void unbindListener(ICrashDetectionListener listener) {
		synchronized (cListeners) {
			cListeners.remove(listener);
		}
		cLog.debug(
				"Unbound '{}' as an implementation of ICrashDetectionListener.",
				listener.getClass().getSimpleName());
	}
	
	/**
	 * The system log, if bound.
	 */
	private static Optional<ISystemLog> cSystemLog = Optional.absent();
	
	/**
	 * Binds an implementation of the system log.
	 * 
	 * @param log
	 *            The implementation to bind.
	 */
	public static void bindSystemLog(ISystemLog log) {
		cSystemLog = Optional.of(log);
		cLog.debug("Bound '{}' as an implementation of ISystemLog.", log
				.getClass().getSimpleName());
	}

	/**
	 * Unbinds an implementation of the system log.
	 * 
	 * @param log
	 *            The implementation to unbind.
	 */
	public static void unbindSystemLog(ISystemLog log) {
		if (cSystemLog.isPresent() && log == cSystemLog.get()) {
			cSystemLog = Optional.absent();
			cLog.debug("Unbound '{}' as an implementation of ISystemLog.", log
					.getClass().getSimpleName());
		}
	}
	
	/**
	 * The tag for the system log entry for a crash of Odysseus.
	 */
	private static final String TAG_CRASH = "CRASH";

	/**
	 * Checks, if there has been a crash (last startup after last shutdown) and
	 * calls its listeners.
	 */
	@Override
	public void onInitialSystemLogRead(List<ISysLogEntry> entries)
			throws Throwable {
		if (entries.isEmpty()) {
			return;
		}

		// Check for normal shut down
		int indexOfLastShutdown = -1;
		int indexOfLastStartup = -1;
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getTag().equals(SystemStateLogger.TAG_STARTUP)) {
				indexOfLastStartup = i;
			} else if (entries.get(i).getTag()
					.equals(SystemStateLogger.TAG_SHUTDOWN)) {
				indexOfLastShutdown = i;
			}
		}
		if (indexOfLastStartup > indexOfLastShutdown) {
			// There must been a crash
			cLog.debug("Crash detected!");
			if(cSystemLog.isPresent()) {
				cSystemLog.get().write(TAG_CRASH, System.currentTimeMillis());
			}
			for (ICrashDetectionListener listener : cListeners) {
				try {
					listener.onCrashDetected(entries.get(indexOfLastStartup)
							.getTimeStamp());
					;
				} catch (Throwable t) {
					cLog.error("Error occured for listener "
							+ listener.getClass().getSimpleName(), t);
				}
			}
		}
	}

	@Override
	public void onSystemLogEntryWritten(ISysLogEntry entry) throws Throwable {
		// Nothing to do
	}

	@Override
	public void onSystemLogEntriesDeleted(List<ISysLogEntry> entries)
			throws Throwable {
		// Nothing to do
	}

}