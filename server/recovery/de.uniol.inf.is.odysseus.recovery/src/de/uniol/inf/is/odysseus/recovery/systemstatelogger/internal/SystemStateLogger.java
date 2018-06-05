package de.uniol.inf.is.odysseus.recovery.systemstatelogger.internal;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ISystemStateEventListener;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ISystemStateLogger;
import de.uniol.inf.is.odysseus.systemlog.ISystemLog;

/**
 * Component to add the following actions to the {@link ISystemLog}: startup and shutdown of
 * Odysseus. Both is defined as the binding/unbinding of the executor.
 * 
 * @author Michael Brand
 *
 */
public class SystemStateLogger implements ISystemStateLogger {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SystemStateLogger.class);

	/**
	 * The system log tag for a successful startup.
	 * 
	 * @see ISysLogEntry#getTag()
	 */
	public static final String STARTUP_TAG = "STARTUP";

	/**
	 * The system log tag for a successful shutdown.
	 * 
	 * @see ISysLogEntry#getTag()
	 */
	public static final String SHUTDOWN_TAG = "SHUTDOWN";

	/**
	 * The comment for the system log entry for a startup of Odysseus.
	 */
	public static final String STARTUP_INFO = "Defined by binding of IExecutor";

	/**
	 * The comment for the system log entry for a shutdown of Odysseus.
	 */
	public static final String SHUTDOWN_INFO = "Defined by unbinding of IExecutor";

	/**
	 * The system log.
	 */
	private ISystemLog systemLog;

	/**
	 * Binds an implementation of the system log and logs the startup, if an
	 * implementation of the executor could be bound before.
	 * 
	 * @param log
	 *            The implementation to bind.
	 */
	public void bindSystemLog(ISystemLog log) {
		this.systemLog = log;
		tryLogStartup();
	}

	/**
	 * Unbinds an implementation of the system log and logs the shutdown, if an
	 * implementation of the executor is still bound.
	 * 
	 * @param log
	 *            The implementation to unbind.
	 */
	public void unbindSystemLog(ISystemLog log) {
		if (log == this.systemLog) {
			tryLogShutDown();
			this.systemLog = null;
		}
	}

	/**
	 * True between the call of {@link #bindExecutor(IExecutor)} and
	 * {@link #unbindExecutor(IExecutor)} by OSGi.
	 */
	private boolean executorBound = false;

	/**
	 * Binds an implementation of the executor and logs the startup, if an
	 * implementation of the system log could be bound before.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public void bindExecutor(IExecutor executor) {
		this.executorBound = true;
		tryLogStartup();
	}

	/**
	 * Unbinds an implementation of the executor and logs the shutdown, if an
	 * implementation of the system log is still bound.
	 * 
	 * @param executor
	 *            The implementation to unbind.
	 */
	public void unbindExecutor(IExecutor executor) {
		this.executorBound = false;
	}

	/**
	 * All bound listeners.
	 */
	private final Set<ISystemStateEventListener> listeners = Sets.newConcurrentHashSet();

	@Override
	public void addListener(ISystemStateEventListener listener) {
		listeners.add(listener);
		if (isSystemStarted()) {
			callListener(listener, true);
		}
	}

	@Override
	public void removeListener(ISystemStateEventListener listener) {
		this.listeners.remove(listener);
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
	private void callListener(ISystemStateEventListener listener, boolean startup) {
		try {
			if (startup) {
				listener.onSystemStartup();
			} else {
				listener.onSystemShutdown();
			}
		} catch (Throwable t) {
			LOG.error("Error occured for listener " + listener.getClass().getSimpleName(), t);
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
	private void callAllListeners(boolean startup) {
		for (ISystemStateEventListener listener : this.listeners) {
			callListener(listener, startup);
		}
	}

	/**
	 * Checks, if Odysseus is started.
	 * 
	 * @return True, if an executor and a system log could be bound.
	 */
	private boolean isSystemStarted() {
		return this.executorBound && this.systemLog != null;
	}

	/**
	 * Checks, if Odysseus is started and logs it, if started.
	 */
	private void tryLogStartup() {
		if (isSystemStarted()) {
			this.systemLog.write(STARTUP_TAG, System.currentTimeMillis(), STARTUP_INFO, SystemStateLogger.class);
			callAllListeners(true);
		}
	}

	/**
	 * Checks, if the system log is still bound and logs the shutdown of
	 * Odysseus, if possible.
	 */
	private void tryLogShutDown() {
		if (!this.executorBound) {
			systemLog.write(SHUTDOWN_TAG, System.currentTimeMillis(), SHUTDOWN_INFO, SystemStateLogger.class);
			callAllListeners(false);
		}
	}

}