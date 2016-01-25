package de.uniol.inf.is.odysseus.recovery.gaprecovery.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.interval_trust.IntervalTrust;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.GapRecoveryExecutor;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Operator to be automatically inserted after a gap recovery (
 * {@link GapRecoveryExecutor}). <br />
 * <br />
 * It checks for each element, if it is inside a convergence phase or not. If an
 * element is inside a convergence phase, its trust value ({@link Trust}) will
 * be decreased (to 0.0). <br />
 * <br />
 * In a logical plan, a {@link TWConvergenceDetectorAO} should be placed
 * directly after {@link TimeWindowAO}s.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings("nls")
public class TWConvergenceDetectorPO<StreamObject extends IStreamObject<IntervalTrust>>
		extends AbstractConvergenceDetectorPO<StreamObject> {

	/**
	 * The logger for all classes related to the Convergence Detector.
	 */
	private static final Logger cLog = LoggerFactory.getLogger("ConvergenceDetector");

	/**
	 * The time stamp, which marks the end of the convergence phase. Inclusive,
	 * so all time stamps AFTER this are outside the convergence phase.
	 */
	private PointInTime mTconv = null;

	/**
	 * Shortcut to check, if we are AFTER {@link #mTconv}.
	 */
	private boolean mEndReached = false;

	/**
	 * Creates a new {@link TWConvergenceDetectorPO} as a copy of an existing
	 * one.
	 * 
	 * @param other
	 *            The {@link TWConvergenceDetectorPO} to copy.
	 */
	public TWConvergenceDetectorPO(TWConvergenceDetectorPO<StreamObject> other) {
		super(other);
		this.mTconv = other.mTconv;
		this.mEndReached = other.mEndReached;
	}

	/**
	 * Creates a new {@link TWConvergenceDetectorPO}.
	 * 
	 * @param omega
	 *            The width of the window (time instants).
	 * @param beta
	 *            The advance of the window (time instants).
	 */
	public TWConvergenceDetectorPO(long omega, long beta) {
		super(omega, beta);
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		if (this.mEndReached) {
			// We're done - shortcut
			transfer(object);
			return;
		}

		PointInTime ts = object.getMetadata().getStart();
		if (this.mTconv == null) {
			/*
			 * First element after gap recovery, so we have to calculate Tconv:
			 * 
			 * Tconf = ts + lc
			 * 
			 * lc is the length of the convergence phase, so its endpoint is at
			 * ts + lc.
			 * 
			 * lc = omega - pos(e)
			 * 
			 * pos(e) is the position of the element within the latest open
			 * window measured in time instants, so omega - pos(e) is the time
			 * span left in window and therefore it is the convergence phase
			 * length.
			 * 
			 * pos(e) = (ts-t0) mod b
			 * 
			 * t0 is the point in time opening the first window (in Odysseus t0
			 * is always 0).
			 */
			this.mTconv = PointInTime.plus(ts, getWindowWidth() - (ts.getMainPoint() % getWindowAdvance()));

			if (cLog.isDebugEnabled()) {
				cLog.debug("Start of convergence phase is {}.", ts);
				cLog.debug("End of convergence phase is calculated as {}.", this.mTconv);
			}
		} else if (ts.after(this.mTconv)) {
			/*
			 * Were are after the convergence phase
			 */
			this.mEndReached = true;
			if (cLog.isDebugEnabled()) {
				cLog.debug("First point in time after convergence phase is {}.", ts);
			}
		}
		// Else we are in a convergence phase

		if (!this.mEndReached) {
			/*
			 * We can not trust that element, e.g., a wrong aggregation due to the
			 * gap. At this juncture, there is no possibility in Odysseus to
			 * determine Repeatable operators.
			 */
			object.getMetadata().setTrust(0);
		}
		transfer(object);

	}

}