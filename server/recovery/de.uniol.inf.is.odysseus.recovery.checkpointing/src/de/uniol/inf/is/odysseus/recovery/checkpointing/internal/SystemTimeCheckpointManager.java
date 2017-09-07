package de.uniol.inf.is.odysseus.recovery.checkpointing.internal;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.checkpointing.CheckpointUnit;
import de.uniol.inf.is.odysseus.recovery.checkpointing.ICheckpointListener;
import de.uniol.inf.is.odysseus.recovery.checkpointing.ICheckpointManager;

/**
 * Entity to manage checkpoints. After a checkpoint manager is configured
 * ({@link #newInstance(CheckpointUnit, long)}) and started (
 * {@link #start(long)}, it notifies listeners (
 * {@link #addListener(ICheckpointListener)}), if a checkpoint is reached.
 * <br />
 * <br />
 * This implementation uses the system time to determine the period between
 * checkpoints, so checkpoint units to use must fulfill
 * {@link CheckpointUnit#isTimeUnit()}.
 *
 * @author Michael Brand
 *
 */
public class SystemTimeCheckpointManager implements ICheckpointManager {

	static private final ISession superUser =  SessionManagement.instance.loginSuperUser(null);


	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 7063160623393779668L;

	/**
	 * The logger for this class.
	 */
	static final Logger LOG = LoggerFactory.getLogger(SystemTimeCheckpointManager.class);

	/**
	 * The name of this checkpoint manager.
	 */
	private static final String NAME = "SystemTimeCheckpointManager";

	/**
	 * The unit to use to determine, when to set checkpoints. <br />
	 * Must fulfill {@link CheckpointUnit#isTimeUnit()}.
	 */
	private CheckpointUnit unit;

	/**
	 * The amount of {@link #unit}s between two checkpoints.
	 */
	private long period;

	/**
	 * The ids of the logical queries to suspend/resume, if a checkpoint is
	 * reached.
	 */
	final Set<Integer> logicalQueries = new HashSet<>();

	/**
	 * All listeners to call, if a checkpoint is reached.
	 */
	private Set<ICheckpointListener> listeners = new HashSet<>();

	@Override
	public void addListener(ICheckpointListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(ICheckpointListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * The server executor, if bound.
	 */
	private static IServerExecutor executor;

	/**
	 * Binds an implementation of the executor.
	 */
	public static void bindExecutor(IExecutor exe) {
		if (exe instanceof IServerExecutor) {
			executor = (IServerExecutor) exe;
		}
	}

	/**
	 * Unbinds an implementation of the executor.
	 */
	public static void unbindExecutor(IExecutor exe) {
		if (exe == executor) {
			executor = null;
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * {@code period} must assure {@link CheckpointUnit#isTimeUnit()}.
	 */
	@Override
	public ICheckpointManager newInstance(CheckpointUnit unit, long period) throws Exception {
		SystemTimeCheckpointManager instance = new SystemTimeCheckpointManager();

		if (!unit.isTimeUnit()) {
			throw new IllegalArgumentException(unit + " is not a time unit!");
		}
		instance.unit = unit;

		if (period <= 0) {
			throw new IllegalArgumentException(period + " is not a valid time interval!");
		}
		instance.period = period;

		return instance;
	}

	@Override
	public ICheckpointManager clone() {
		SystemTimeCheckpointManager manager = new SystemTimeCheckpointManager();
		if (this.unit != null) {
			manager.unit = this.unit;
		}
		if (this.period > 0) {
			manager.period = this.period;
		}
		return manager;
	}

	@Override
	public CheckpointUnit getUnit() {
		return this.unit;
	}

	@Override
	public long getPeriod() {
		return this.period;
	}

	/**
	 * The internal timer.
	 */
	private Timer timer;

	@Override
	public void start() {
		Set<IPhysicalQuery> physicalQueries = new HashSet<>();
		for (Integer queryId : this.logicalQueries) {
			physicalQueries.add(executor.getExecutionPlan(superUser).getQueryById(queryId.intValue(), superUser));
		}
		long period = this.period * this.unit.getConversionFactor();
		this.timer = new Timer(this.getClass().getSimpleName(), true);
		this.timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				LOG.info("Checkpoint reached.");
				final long start = System.currentTimeMillis();
				suspendOrResume(physicalQueries, true);
				fireCheckpointReachedEvent();
				suspendOrResume(physicalQueries, false);
				LOG.debug("Needed {} ms for checkpoint handlers.", new Long(System.currentTimeMillis() - start));
			}
		}, period, period);
	}

	@Override
	public void stop() {
		if (this.timer != null) {
			this.timer.cancel();
			this.timer = null;
		}
	}

	/**
	 * Calls all listeners that a checkpoint is reached.
	 */
	void fireCheckpointReachedEvent() {
		Set<Thread> threads = new HashSet<>();
		for (final ICheckpointListener listener : SystemTimeCheckpointManager.this.listeners) {
			Thread t = new Thread("CheckpointListener " + listener.toString()) {

				@Override
				public void run() {
					try {
						listener.onCheckpointReached();
					} catch (Exception e) {
						SystemTimeCheckpointManager.LOG
								.error("Error in Checkpoint listener " + listener.getClass().getSimpleName(), e);
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

	@Override
	public void addLogicalQuery(int id) {
		this.logicalQueries.add(new Integer(id));
	}

}