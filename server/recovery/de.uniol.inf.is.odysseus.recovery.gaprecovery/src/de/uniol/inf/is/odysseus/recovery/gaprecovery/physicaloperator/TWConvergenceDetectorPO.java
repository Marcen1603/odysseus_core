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
 * be decreased. <br />
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
	 * The time stamp of the first element after recovery.
	 */
	private PointInTime mTrec = null;

	/**
	 * Creates a new {@link TWConvergenceDetectorPO} as a copy of an existing
	 * one.
	 * 
	 * @param other
	 *            The {@link TWConvergenceDetectorPO} to copy.
	 */
	public TWConvergenceDetectorPO(TWConvergenceDetectorPO<StreamObject> other) {
		super(other);
		this.mTrec = other.mTrec;
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
		if (isEndReached()) {
			// We're done - shortcut
			transfer(object);
			return;
		}

		PointInTime ts = object.getMetadata().getStart();
		if (this.mTrec == null) {
			/*
			 * First element after gap recovery, so its time stamp is Trec
			 */
			this.mTrec = ts;
			if (cLog.isDebugEnabled()) {
				cLog.debug("Start of convergence phase is {}.", ts);
			}
		} else if (ts.afterOrEquals(PointInTime.plus(this.mTrec, getWindowWidth()))) {
			/*
			 * The time stamp of the element is at least omega time instants
			 * after Trec. So it can not be part of a convergence phase. Same
			 * for subsequent elements.
			 */
			setEndReached();
			if (cLog.isDebugEnabled()) {
				cLog.debug("End of convergence phase is {}.", ts);
			}
		}
		// Else we are in a convergence phase

		if (!isEndReached()) {
			/*
			 * We can not trust that element, e.g., a wrong aggregation due to
			 * the gap. TODO What, if all operations are repeatable. Results are
			 * by definition correct. Trust would be changed anyway. TODO Change
			 * trust always to 0?
			 */
			object.getMetadata().setTrust(0);
		}
		transfer(object);

	}

}