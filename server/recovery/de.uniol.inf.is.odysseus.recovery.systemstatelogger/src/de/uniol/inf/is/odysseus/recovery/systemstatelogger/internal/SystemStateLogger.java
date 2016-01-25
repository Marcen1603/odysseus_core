package de.uniol.inf.is.odysseus.recovery.systemstatelogger.internal;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ISystemStateEventListener;

/**
 * Class to add the following actions to the system log: startup and shutdown of
 * Odysseus. Both is defined as the binding/unbinding of the executor.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class SystemStateLogger {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(SystemStateLogger.class);

	/**
	 * The system log tag for a successful startup.
	 * 
	 * @see ISysLogEntry#getTag()
	 */
	public static final String cStartupTag = "STARTUP";

	/**
	 * The system log tag for a successful shutdown.
	 * 
	 * @see ISysLogEntry#getTag()
	 */
	public static final String cShutdownTag = "SHUTDOWN";

	/**
	 * The comment for the system log entry for a startup of Odysseus.
	 */
	public static final String INFO_STARTUP = "Defined by binding of IExecutor";

	/**
	 * The comment for the system log entry for a shutdown of Odysseus.
	 */
	public static final String INFO_SHUTDOWN = "Defined by unbinding of IExecutor";

	/**
	 * The system log, if bound.
	 */
	private static Optional<ISystemLog> cSystemLog = Optional.absent();

	/**
	 * Binds an implementation of the system log and logs the startup, if an
	 * implementation of the executor could be bound before.
	 * 
	 * @param log
	 *            The implementation to bind.
	 */
	public static void bindSystemLog(ISystemLog log) {
		cSystemLog = Optional.of(log);
		tryLogStartup();
	}

	/**
	 * Unbinds an implementation of the system log and logs the shutdown, if an
	 * implementation of the executor is still bound.
	 * 
	 * @param log
	 *            The implementation to unbind.
	 */
	public static void unbindSystemLog(ISystemLog log) {
		if (cSystemLog.isPresent() && log == cSystemLog.get()) {
			cSystemLog = Optional.absent();
		}
	}

	/**
	 * True between the call of {@link #bindExecutor(IExecutor)} and
	 * {@link #unbindExecutor(IExecutor)} by OSGi.
	 */
	private static boolean cExecutorBound = false;

	/**
	 * Binds an implementation of the executor and logs the startup, if an
	 * implementation of the system log could be bound before.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public static void bindExecutor(@SuppressWarnings("unused") IExecutor executor) {
		cExecutorBound = true;
		tryLogStartup();
	}

	/**
	 * Unbinds an implementation of the executor and logs the shutdown, if an
	 * implementation of the system log is still bound.
	 * 
	 * @param executor
	 *            The implementation to unbind.
	 */
	public static void unbindExecutor(@SuppressWarnings("unused") IExecutor executor) {
		cExecutorBound = false;
		tryLogShutDown();
	}

	/**
	 * Checks, if Odysseus is started.
	 * 
	 * @return True, if an executor and a system log could be bound.
	 */
	private static boolean isSystemStarted() {
		return cExecutorBound && cSystemLog.isPresent();
	}

	/**
	 * All bound listeners.
	 */
	private static final Set<ISystemStateEventListener> cListeners = Sets.newConcurrentHashSet();

	/**
	 * Binds a listener.
	 * 
	 * @param listener
	 *            A new listener.
	 */
	public static void bindListener(ISystemStateEventListener listener) {
		cListeners.add(listener);
		if (isSystemStarted()) {
			callListener(listener, true);
		}
	}

	/**
	 * Unregisters a listener.
	 * 
	 * @param listener
	 *            A listener to unregister.
	 */
	public static void unbindListener(ISystemStateEventListener listener) {
		cListeners.remove(listener);
	}

	/**
	 * Calls {@link ISystemStateEventListener#onSystemStartup()} or
	 * {@link ISystemStateEventListener#onSystemShutdown()} for a listener.
	 * 
	 * @param listener
	 *            The listener to call.
	 * @param startup
	 *            if true, {@link ISystemStateEventListener#onSystemStartup()}
	 *            is called, otherwise
	 *            {@link ISystemStateEventListener#onSystemShutdown()} is
	 *            called.
	 */
	private static void callListener(ISystemStateEventListener listener, boolean startup) {
		try {
			if (startup) {
				listener.onSystemStartup();
			} else {
				listener.onSystemShutdown();
			}
		} catch (Throwable t) {
			cLog.error("Error occured for listener " + listener.getClass().getSimpleName(), t);
		}
	}

	/**
	 * Calls {@link ISystemStateEventListener#onSystemStartup()} or
	 * {@link ISystemStateEventListener#onSystemShutdown()} for all bound
	 * listener.
	 * 
	 * @param startup
	 *            if true, {@link ISystemStateEventListener#onSystemStartup()}
	 *            is called, otherwise
	 *            {@link ISystemStateEventListener#onSystemShutdown()} is
	 *            called.
	 */
	private static void callAllListeners(boolean startup) {
		for (ISystemStateEventListener listener : cListeners) {
			callListener(listener, startup);
		}
	}

	/**
	 * Checks, if Odysseus is started and logs it, if started.
	 */
	private static void tryLogStartup() {
		if (isSystemStarted()) {
			cSystemLog.get().write(cStartupTag, System.currentTimeMillis(), INFO_STARTUP, SystemStateLogger.class);
			callAllListeners(true);
		}
	}

	/**
	 * Checks, if the system log is still bound and logs the shutdown of
	 * Odysseus, if possible.
	 */
	private static void tryLogShutDown() {
		if (cSystemLog.isPresent()) {
			cSystemLog.get().write(cShutdownTag, System.currentTimeMillis(), INFO_SHUTDOWN, SystemStateLogger.class);
			callAllListeners(false);
		} else {
			cLog.error("Could not write a system log for the shutdown!");
		}
	}

}