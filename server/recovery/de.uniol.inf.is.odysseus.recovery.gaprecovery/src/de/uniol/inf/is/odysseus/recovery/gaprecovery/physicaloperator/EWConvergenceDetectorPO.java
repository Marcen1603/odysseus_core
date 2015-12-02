package de.uniol.inf.is.odysseus.recovery.gaprecovery.physicaloperator;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.interval_trust.ITimeIntervalTrust;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.GapRecoveryExecutor;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.logicaloperator.ConvergenceDetectorAO;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Operator to be automatically inserted after a gap recovery (
 * {@link GapRecoveryExecutor}). <br />
 * <br />
 * It checks for each element, if it is inside a convergence phase or not. If an
 * element is inside a convergence phase, its trust value ({@link Trust}) will
 * be decreased. <br />
 * <br />
 * In a logical plan, a {@link ConvergenceDetectorAO} should be placed directly
 * after {@link ElementWindowAO}s
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings("nls")
public class EWConvergenceDetectorPO<StreamObject extends IStreamObject<ITimeIntervalTrust>>
		extends AbstractConvergenceDetectorPO<StreamObject> {

	/**
	 * The logger for all classes related to the Convergence Detector.
	 */
	private static final Logger cLog = LoggerFactory.getLogger("ConvergenceDetector");

	/**
	 * Amount of elements after recovery.
	 */
	private int mCounter = 0;

	/**
	 * Gets the elements counter.
	 * 
	 * @return Amount of elements after recovery.
	 */
	protected synchronized Integer getElementsCounter() {
		return new Integer(this.mCounter);
	}

	/**
	 * Creates a new {@link EWConvergenceDetectorPO} as a copy of an existing
	 * one.
	 * 
	 * @param other
	 *            The {@link EWConvergenceDetectorPO} to copy.
	 */
	public EWConvergenceDetectorPO(EWConvergenceDetectorPO<StreamObject> other) {
		super(other);
		this.mCounter = other.mCounter;
	}

	/**
	 * Creates a new {@link EWConvergenceDetectorPO}.
	 * 
	 * @param omega
	 *            The width of the window (time instants or elements).
	 * @param beta
	 *            The advance of the window (time instants or elements).
	 */
	public EWConvergenceDetectorPO(long omega, long beta) {
		super(omega, beta);
	}

	@Override
	public IOperatorState getState() {
		return new IOperatorState() {

			@Override
			public Serializable getSerializedState() {
				return getElementsCounter();
			}

			@Override
			public long estimateSizeInBytes() {
				return 4l;
			}

		};

	}

	@Override
	public void setState(Serializable state) {
		try {
			this.mCounter = ((Integer) state).intValue();
		} catch (Throwable t) {
			cLog.error("Can not set operator state!", t);
		}

	}

	@Override
	protected void process_next(StreamObject object, int port) {
		if (this.mEndReached) {
			// We're done - shortcut
			transfer(object);
			return;
		} else if (++this.mCounter >= this.getWindowWidth()) {
			/*
			 * There are at least omega elements after the gap. So this element
			 * can not be part of a convergence phase. Same for subsequent
			 * elements.
			 */
			this.mEndReached = true;
		} else {
			/*
			 * we are in a convergence phase and we can not trust that element,
			 * e.g., a wrong aggregation due to the gap. TODO What, if all
			 * operations are repeatable. Results are by definition correct.
			 * Trust would be changed anyway. TODO Change trust always to 0?
			 */
			object.getMetadata().setTrust(0);
		}
		transfer(object);

	}

}