package de.uniol.inf.is.odysseus.recovery.protectionpoints.internal;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.recovery.protectionpoints.IProtectionPointHandler;
import de.uniol.inf.is.odysseus.recovery.protectionpoints.IProtectionPointManager;
import de.uniol.inf.is.odysseus.recovery.protectionpoints.ProtectionPointUnit;

/**
 * Entity to manage protection points. After a protection point manager is
 * configured ({@link #newInstance(ProtectionPointUnit, long)}) and started (
 * {@link #start(long)}, it notifies listeners (
 * {@link #addHandler(IProtectionPointHandler)}), if a protection point is
 * reached. <br />
 * <br />
 * This implementation uses the system time to determine the period between
 * protection point, so protection point units to use must fulfill
 * {@link ProtectionPointUnit#isTimeUnit()}.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class SystemTimeProtectionPointManager implements IProtectionPointManager {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 7063160623393779668L;

	/**
	 * The logger for this class.
	 */
	static final Logger cLog = LoggerFactory.getLogger(SystemTimeProtectionPointManager.class);

	/**
	 * The unit to use to determine, when to set protection points. <br />
	 * Must fulfill {@link ProtectionPointUnit#isTimeUnit()}.
	 */
	private ProtectionPointUnit mUnit;

	/**
	 * The amount of {@link #mUnit}s between two protection points.
	 */
	private long mPeriod;

	/**
	 * The ids of the logical queries to suspend/resume, if a protection point
	 * is reached.
	 */
	final Set<Integer> logicalQueries = new HashSet<>();

	/**
	 * All listeners to call, if a protection point is reached.
	 */
	private Set<IProtectionPointHandler> mHandlers = new HashSet<>();

	@Override
	public void addHandler(IProtectionPointHandler handler) {
		this.mHandlers.add(handler);
	}

	@Override
	public void removeHandler(IProtectionPointHandler handler) {
		this.mHandlers.remove(handler);
	}

	/**
	 * The server executor, if bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * Binds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public static void bindServerExecutor(IExecutor executor) {
		if (IServerExecutor.class.isInstance(executor)) {
			cExecutor = Optional.of((IServerExecutor) executor);
		}
	}

	/**
	 * Unbinds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to unbind.
	 */
	public static void unbindServerExecutor(IExecutor executor) {
		if (cExecutor.isPresent() && cExecutor.get() == executor) {
			cExecutor = Optional.absent();
		}
	}

	@Override
	public String getName() {
		return SystemTimeProtectionPointManager.class.getSimpleName();
	}

	/**
	 * {@code period} musst assure {@link ProtectionPointUnit#isTimeUnit()}.
	 */
	@Override
	public IProtectionPointManager newInstance(ProtectionPointUnit unit, long period)
			throws NullPointerException, IllegalArgumentException {
		SystemTimeProtectionPointManager instance = new SystemTimeProtectionPointManager();

		if (!unit.isTimeUnit()) {
			throw new IllegalArgumentException(unit + " is not a time unit!");
		}
		instance.mUnit = unit;

		if (period <= 0) {
			throw new IllegalArgumentException(period + " is not a valid time interval!");
		}
		instance.mPeriod = period;

		return instance;
	}

	@Override
	public IProtectionPointManager clone() {
		SystemTimeProtectionPointManager manager = new SystemTimeProtectionPointManager();
		if (this.mUnit != null) {
			manager.mUnit = this.mUnit;
		}
		if (this.mPeriod > 0) {
			manager.mPeriod = this.mPeriod;
		}
		return manager;
	}

	@Override
	public ProtectionPointUnit getUnit() {
		return this.mUnit;
	}

	@Override
	public long getPeriod() {
		return this.mPeriod;
	}

	/**
	 * The internal timer.
	 */
	private Timer mTimer;

	@Override
	public void start() {
		if (!cExecutor.isPresent()) {
			return;
		}
		Set<IPhysicalQuery> physicalQueries = new HashSet<>();
		for (Integer queryId : this.logicalQueries) {
			cExecutor.get().getExecutionPlan().getQueryById(queryId.intValue());
			physicalQueries.add(cExecutor.get().getExecutionPlan().getQueryById(queryId.intValue()));
		}
		long period = this.mPeriod * this.mUnit.getConversionFactor();
		this.mTimer = new Timer(this.getClass().getSimpleName(), true);
		this.mTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				SystemTimeProtectionPointManager.cLog.debug("Protection point reached.");
				final long start = System.currentTimeMillis();
				suspendOrResume(physicalQueries, true);
				fireProtectionPointReachedEvent();
				suspendOrResume(physicalQueries, false);
				SystemTimeProtectionPointManager.cLog.debug("Needed {} ms for protection point handlers.",
						new Long(System.currentTimeMillis() - start));
			}
		}, period, period);
	}

	@Override
	public void stop() {
		if (this.mTimer != null) {
			this.mTimer.cancel();
			this.mTimer = null;
		}
	}

	/**
	 * Suspends or resumes physical queries.
	 * 
	 * @param queries
	 *            The queries to suspend or resume.
	 * @param suspend
	 *            True = suspend, false = resume.
	 */
	static void suspendOrResume(Set<IPhysicalQuery> queries, boolean suspend) {
		for (IPhysicalQuery query : queries) {
			if (suspend) {
				query.suspend();
			} else {
				query.resume();
			}
		}
	}

	/**
	 * Calls all listeners that a protection point is reached.
	 */
	void fireProtectionPointReachedEvent() {
		Set<Thread> threads = new HashSet<>();
		for (final IProtectionPointHandler handler : SystemTimeProtectionPointManager.this.mHandlers) {
			Thread t = new Thread("ProtectionPointHandler " + handler.toString()) {

				@Override
				public void run() {
					try {
						handler.onProtectionPointReached();
					} catch (Exception e) {
						SystemTimeProtectionPointManager.cLog
								.error("Error in ProtectionPointHandler " + handler.getClass().getSimpleName(), e);
					}
				}

			};
			t.start();
			threads.add(t);
		}
		// Wait for all threads
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addLogicalQuery(int id) {
		this.logicalQueries.add(new Integer(id));
	}

}