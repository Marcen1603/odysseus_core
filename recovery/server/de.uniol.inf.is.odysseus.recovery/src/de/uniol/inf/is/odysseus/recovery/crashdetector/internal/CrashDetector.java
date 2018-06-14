package de.uniol.inf.is.odysseus.recovery.crashdetector.internal;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.recovery.crashdetector.ICrashDetector;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ISystemStateEventListener;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.internal.SystemStateLogger;
import de.uniol.inf.is.odysseus.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.systemlog.ISystemLogEntry;
import de.uniol.inf.is.odysseus.systemlog.ISystemLogListener;

/**
 * Component to check, if there has been a crash of Odysseus. It uses the {@link ISystemLog}
 * to do so.
 * 
 * @author Michael Brand
 *
 */
public class CrashDetector implements ISystemLogListener, ICrashDetector {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CrashDetector.class);

	/**
	 * The system log tag for a detected crash.
	 * 
	 * @see ISysLogEntry#getTag()
	 */
	public static final String CRASH_TAG = "CRASHDETECTED";

	/**
	 * The system log.
	 */
	private ISystemLog systemLog;

	/**
	 * Binds an implementation of the system log.
	 * 
	 * @param log
	 *            The implementation to bind.
	 */
	public void bindSystemLog(ISystemLog log) {
		log.addListener(this);
		this.systemLog = log;
	}

	/**
	 * Unbinds an implementation of the system log.
	 * 
	 * @param log
	 *            The implementation to unbind.
	 */
	public void unbindSystemLog(ISystemLog log) {
		if (log == this.systemLog) {
			log.removeListener(this);
			this.systemLog = null;
		}
	}

	/**
	 * The time stamp in milliseconds of the last startup, if a crash has been
	 * detected, or -1 else.
	 */
	private long lastStartup = -1L;

	/**
	 * True, if a startup is detected.
	 */
	private boolean startup = false;

	/**
	 * All bound listeners.
	 */
	private final Set<ISystemStateEventListener> listeners = Sets.newConcurrentHashSet();

	@Override
	public void addListener(ISystemStateEventListener listener) {
		this.listeners.add(listener);
		if (canCallListeners()) {
			callListener(listener);
		}
	}

	@Override
	public void removeListener(ISystemStateEventListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Calls {@link ISystemStateEventListener#onCrashDetected(long)} with
	 * {@value #lastStartup} for a listener.
	 * 
	 * @param listener
	 *            The listener to call.
	 */
	private void callListener(ISystemStateEventListener listener) {
		try {
			listener.onCrashDetected(this.lastStartup);
		} catch (Throwable t) {
			LOG.error("Error occured for listener " + listener.getClass().getSimpleName(), t);
		}
	}

	/**
	 * Calls {@link ISystemStateEventListener#onCrashDetected(long)} with
	 * {@value #lastStartup} for all bound listeners and logs the crash.
	 */
	private void callAllListenersAndLog() {
		for (ISystemStateEventListener listener : this.listeners) {
			callListener(listener);
		}
		this.systemLog.write(CRASH_TAG, System.currentTimeMillis(), CrashDetector.class);
	}

	/**
	 * Checks, if there has been a crash (last startup after last shutdown) and
	 * calls its listeners.
	 */
	@Override
	public void onInitialSystemLogRead(List<ISystemLogEntry> entries) throws Throwable {
		if (entries.isEmpty()) {
			return;
		}

		// Check for normal shut down
		int indexOfLastShutdown = -1;
		int indexOfLastStartup = -1;
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getTag().equals(SystemStateLogger.STARTUP_TAG)) {
				indexOfLastStartup = i;
			} else if (entries.get(i).getTag().equals(SystemStateLogger.SHUTDOWN_TAG)) {
				indexOfLastShutdown = i;
			}
		}
		if (indexOfLastStartup > indexOfLastShutdown) {
			// There must been a crash
			LOG.info("Crash detected!");
			this.lastStartup = entries.get(indexOfLastStartup).getTimeStamp();
			if (canCallListeners()) {
				callAllListenersAndLog();
			}
		}
	}

	/**
	 * Checks, if the startup has been logged, so Odysseus is ready.
	 */
	@Override
	public void onSystemLogEntryWritten(ISystemLogEntry entry) throws Throwable {
		if (entry.getTag().equals(SystemStateLogger.STARTUP_TAG)) {
			this.startup = true;
			if (canCallListeners()) {
				callAllListenersAndLog();
			}
		}
	}

	@Override
	public void onSystemLogEntriesDeleted(List<ISystemLogEntry> entries) throws Throwable {
		// Nothing to do
	}

	/**
	 * Checks, if the listeners can/shall be called.
	 * 
	 * @return True, if a startup and a crash of Odysseus is both detected.
	 */
	private boolean canCallListeners() {
		return this.startup && this.lastStartup != -1;
	}

}