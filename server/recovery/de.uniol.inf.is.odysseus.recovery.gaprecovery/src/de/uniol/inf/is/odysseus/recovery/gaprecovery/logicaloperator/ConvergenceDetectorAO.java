package de.uniol.inf.is.odysseus.recovery.gaprecovery.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.GapRecoveryExecutor;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Operator to be automatically inserted after a gap recovery (
 * {@link GapRecoveryExecutor}). <br />
 * <br />
 * It checks for each element, if it is inside a convergence phase or not. If an
 * element is inside a convergence phase, it's trust value ({@link Trust}) will
 * be decreased. <br />
 * <br />
 * In a logical plan, a {@link ConvergenceDetectorAO} should be placed directly
 * after window operators.
 * 
 * @author Michael Brand
 *
 */
public class ConvergenceDetectorAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -1284940349024652610L;

	/**
	 * The width of the windows (time instants or elements).
	 */
	private final long mOmega;

	/**
	 * The advance of the windows (time instants or elements).
	 */
	private final long mBeta;

	/**
	 * Creates a new {@link ConvergenceDetectorAO} as a copy of an existing one.
	 * 
	 * @param other
	 *            The {@link ConvergenceDetectorAO} to copy.
	 */
	public ConvergenceDetectorAO(ConvergenceDetectorAO other) {
		super(other);
		this.mBeta = other.mBeta;
		this.mOmega = other.mOmega;
	}

	/**
	 * Creates a new {@link ConvergenceDetectorAO}.
	 * 
	 * @param omega
	 *            The width of the window (time instants or elements).
	 * @param beta
	 *            The advance of the window (time instants or elements).
	 */
	public ConvergenceDetectorAO(long omega, long beta) {
		super();
		this.mBeta = beta;
		this.mOmega = omega;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ConvergenceDetectorAO(this);
	}

	/**
	 * Gets the window width.
	 * 
	 * @return The width of the windows (time instants or elements).
	 */
	public final long getWindowWidth() {
		return this.mOmega;
	}

	/**
	 * Gets the window advance.
	 * 
	 * @return The advance of the windows (time instants or elements).
	 */
	public final long getWindowAdvance() {
		return this.mBeta;
	}

}