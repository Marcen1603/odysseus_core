package de.uniol.inf.is.odysseus.recovery.gaprecovery.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.interval_trust.IntervalTrust;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.GapRecoveryExecutor;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Operator to be automatically inserted after a gap recovery (
 * {@link GapRecoveryExecutor}). <br />
 * <br />
 * For element windows, it can not be checked, if an element is inside a
 * convergence phase or not. This is because we do not know the elements inside
 * the offline phase. So the trust value ({@link Trust}) will always set to 0.5
 * (50%). <br />
 * <br />
 * In a logical plan, a {@link EWConvergenceDetectorPO} should be placed
 * directly after {@link ElementWindowAO}s
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings("nls")
public class EWConvergenceDetectorPO<StreamObject extends IStreamObject<IntervalTrust>>
		extends AbstractConvergenceDetectorPO<StreamObject> {

	/**
	 * Creates a new {@link EWConvergenceDetectorPO} as a copy of an existing
	 * one.
	 * 
	 * @param other
	 *            The {@link EWConvergenceDetectorPO} to copy.
	 */
	public EWConvergenceDetectorPO(EWConvergenceDetectorPO<StreamObject> other) {
		super(other);
	}

	/**
	 * Creates a new {@link EWConvergenceDetectorPO}.
	 * 
	 * @param omega
	 *            The width of the window (elements).
	 * @param beta
	 *            The advance of the window (elements).
	 */
	public EWConvergenceDetectorPO(long omega, long beta) {
		super(omega, beta);
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		object.getMetadata().setTrust(0.5);
		transfer(object);

	}

}