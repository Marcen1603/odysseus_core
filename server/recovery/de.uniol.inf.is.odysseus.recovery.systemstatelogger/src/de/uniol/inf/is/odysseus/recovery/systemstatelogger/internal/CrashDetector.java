package de.uniol.inf.is.odysseus.recovery.systemstatelogger.internal;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.server.recovery.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLogListener;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ICrashDetectionListener;

/**
 * Class to check, if there has been a crash of Odysseus. It uses the system log
 * to do so.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class CrashDetector implements ISystemLogListener {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(CrashDetector.class);

	/**
	 * The time stamp in milliseconds of the last startup, if a crash has been
	 * detected, or -1 else.
	 */
	private static long cLastStartup = -1L;

	/**
	 * True, if a startup is detected.
	 */
	private static boolean cStartup = false;

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
	public void bindSystemLog(ISystemLog log) {
		cSystemLog = Optional.of(log);
		log.addListener(this);
	}

	/**
	 * Unbinds an implementation of the system log.
	 * 
	 * @param log
	 *            The implementation to unbind.
	 */
	public void unbindSystemLog(ISystemLog log) {
		if (cSystemLog.isPresent() && log == cSystemLog.get()) {
			cSystemLog = Optional.absent();
			log.removeListener(this);
		}
	}

	/**
	 * All bound listeners.
	 */
	private static final Set<ICrashDetectionListener> cListeners = Sets
			.newConcurrentHashSet();

	/**
	 * Binds a listener.
	 * 
	 * @param listener
	 *            A new listener.
	 */
	public static void bindListener(ICrashDetectionListener listener) {
		cListeners.add(listener);
		if (canCallListeners()) {
			callListener(listener);
		}
	}

	/**
	 * Unregisters a listener.
	 * 
	 * @param listener
	 *            A listener to unregister.
	 */
	public static void unbindListener(ICrashDetectionListener listener) {
		cListeners.remove(listener);
	}

	/**
	 * Calls {@link ICrashDetectionListener#onCrashDetected(long)} with
	 * {@value #cLastStartup} for a listener.
	 * 
	 * @param listener
	 *            The listener to call.
	 */
	private static void callListener(ICrashDetectionListener listener) {
		try {
			listener.onCrashDetected(cLastStartup);
		} catch (Throwable t) {
			cLog.error("Error occured for listener "
					+ listener.getClass().getSimpleName(), t);
		}
	}

	/**
	 * Calls {@link ICrashDetectionListener#onCrashDetected(long)} with
	 * {@value #cLastStartup} for all bound listeners and logs the crash.
	 */
	private static void callAllListenersAndLog() {
		for (ICrashDetectionListener listener : cListeners) {
			callListener(listener);
		}
		if (cSystemLog.isPresent()) {
			cSystemLog.get().write(TAG_CRASH, System.currentTimeMillis());
		}
	}

	/**
	 * The tag for the system log entry for a crash of Odysseus.
	 */
	private static final String TAG_CRASH = "CRASH DETECTED";

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
			cLastStartup = entries.get(indexOfLastStartup).getTimeStamp();
			if (canCallListeners()) {
				callAllListenersAndLog();
			}
		}
	}

	/**
	 * Checks, if the startup has been logged, so Odysseus is ready.
	 */
	@Override
	public void onSystemLogEntryWritten(ISysLogEntry entry) throws Throwable {
		if (entry.getTag().equals(SystemStateLogger.TAG_STARTUP)) {
			cStartup = true;
			if (canCallListeners()) {
				callAllListenersAndLog();
			}
		}
	}

	@Override
	public void onSystemLogEntriesDeleted(List<ISysLogEntry> entries)
			throws Throwable {
		// Nothing to do
	}

	/**
	 * Checks, if the listeners can/shall be called.
	 * 
	 * @return True, if a startup and a crash of Odysseus is both detected.
	 */
	private static boolean canCallListeners() {
		return cStartup && cLastStartup != -1;
	}

}