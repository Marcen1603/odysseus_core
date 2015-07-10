package de.uniol.inf.is.odysseus.recovery.systemstatelogger;

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
	 * The system log, if bound.
	 */
	private static Optional<ISystemLog> cSystemLog = Optional.absent();

	/**
	 * True between the call of {@link #bindExecutor(IExecutor)} and
	 * {@link #unbindExecutor(IExecutor)} by OSGi.
	 */
	private static boolean cExecutorBound = false;

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
	 * Binds an implementation of the system log and logs the startup, if an
	 * implementation of the executor could be bound before.
	 * 
	 * @param log
	 *            The implementation to bind.
	 */
	public static void bindSystemLog(ISystemLog log) {
		cSystemLog = Optional.of(log);
		cLog.debug("Bound '{}' as an implementation of ISystemLog.", log
				.getClass().getSimpleName());
		if (cExecutorBound) {
			log(TAG_STARTUP);
		}
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
			if (cExecutorBound) {
				log(TAG_SHUTDOWN);
			}
			cLog.debug("Unbound '{}' as an implementation of ISystemLog.", log
					.getClass().getSimpleName());
		}
	}

	/**
	 * Binds an implementation of the executor and logs the startup, if an
	 * implementation of the system log could be bound before.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public static void bindExecutor(IExecutor executor) {
		cExecutorBound = true;
		if (cSystemLog.isPresent()) {
			log(TAG_STARTUP);
		}
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
		if (cSystemLog.isPresent()) {
			log(TAG_SHUTDOWN);
		}
	}

	/**
	 * Logs a system state action.
	 * 
	 * @param tag
	 *            {@link #TAG_STARTUP} or {@link #TAG_SHUTDOWN}.
	 */
	private static void log(String tag) {
		// Attention: No check, if system log is bound!
		cSystemLog.get().write(tag, System.currentTimeMillis(), COMMENT);
	}

}