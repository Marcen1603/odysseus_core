package de.uniol.inf.is.odysseus.recovery.systemstatelogger.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;

/**
 * Class to add the following actions to the system log: startup and shutdown of
 * Odysseus. Both is defined as the binding/unbinding of the executor.
 * 
 * @author Michael Brand
 *
 */
public class SystemStateLogger {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(SystemStateLogger.class);

	/**
	 * The tag for the system log entry for a startup of Odysseus.
	 */
	public static final String TAG_STARTUP = "STARTUP";

	/**
	 * The tag for the system log entry for a shutdown of Odysseus.
	 */
	public static final String TAG_SHUTDOWN = "SHUTDOWN";

	/**
	 * The tag for the system log entry for a startup of Odysseus as well as for
	 * a shutdown of Odysseus.
	 */
	public static final String COMMENT = "Defined by binding/unbinding of IExecutor";

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
	public static void bindExecutor(IExecutor executor) {
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
	public static void unbindExecutor(IExecutor executor) {
		cExecutorBound = false;
		tryLogShutDown();
	}

	/**
	 * Checks, if Odyseus is started.
	 * 
	 * @return True, if an executor and a system log could be bound.
	 */
	private static boolean isSystemStarted() {
		return cExecutorBound && cSystemLog.isPresent();
	}

	// TODO javaDoc
	private static void tryLogStartup() {
		if (isSystemStarted()) {
			cSystemLog.get().write(TAG_STARTUP, System.currentTimeMillis(),
					COMMENT);
		}
	}

	// TODO javaDoc
	private static void tryLogShutDown() {
		if (cSystemLog.isPresent()) {
			cSystemLog.get().write(TAG_SHUTDOWN, System.currentTimeMillis(),
					COMMENT);
		} else {
			cLog.error("Could not write a system log for the shutdown!");
		}
	}

}