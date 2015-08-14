package de.uniol.inf.is.odysseus.recovery.protectionpoints.internal;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

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
public class SystemTimeProtectionPointManager implements
		IProtectionPointManager {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(SystemTimeProtectionPointManager.class);

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
	 * All listeners to call, if a protection point is reached.
	 */
	private Set<IProtectionPointHandler> mHandlers = Sets.newHashSet();

	@Override
	public void addHandler(IProtectionPointHandler handler) {
		this.mHandlers.add(handler);
	}

	@Override
	public void removeHandler(IProtectionPointHandler handler) {
		this.mHandlers.remove(handler);
	}

	@Override
	public String getName() {
		return SystemTimeProtectionPointManager.class.getSimpleName();
	}

	/**
	 * {@code period} musst assure {@link ProtectionPointUnit#isTimeUnit()}.
	 */
	@Override
	public IProtectionPointManager newInstance(ProtectionPointUnit unit,
			long period) throws NullPointerException, IllegalArgumentException {
		SystemTimeProtectionPointManager instance = new SystemTimeProtectionPointManager();

		if (!unit.isTimeUnit()) {
			throw new IllegalArgumentException(unit + " is not a time unit!");
		}
		instance.mUnit = unit;

		if (period <= 0) {
			throw new IllegalArgumentException(period
					+ " is not a valid time interval!");
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

	@Override
	public void start() {
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				fireProtectionPointReachedEvent();
			}
		};

		Timer timer = new Timer(this.getClass().getSimpleName(), true);
		long period = this.mPeriod * this.mUnit.getConversionFactor();
		timer.scheduleAtFixedRate(timerTask, period, period);
	}

	/**
	 * Calls all listeners that a protection point is reached.
	 */
	void fireProtectionPointReachedEvent() {
		for (IProtectionPointHandler handler : this.mHandlers) {
			try {
				handler.onProtectionPointReached();
			} catch (Exception e) {
				cLog.error("Error in ProtectionPointHandler "
						+ handler.getClass().getSimpleName(), e);
			}
		}
	}

}