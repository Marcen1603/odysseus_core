package de.uniol.inf.is.odysseus.recovery.gaprecovery.logicaloperator;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
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
	private static final long serialVersionUID = 5458529952253386714L;

	/**
	 * The width of the windows (time or elements).
	 */
	private final long mOmega;

	/**
	 * The advance of the windows (time or elements).
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
	 *            The width of the window (elements).
	 * @param beta
	 *            The advance of the window (elements).
	 */
	public ConvergenceDetectorAO(long omega, long beta) {
		super();
		this.mBeta = beta;
		this.mOmega = omega;
	}

	/**
	 * Creates a new {@link ConvergenceDetectorAO}.
	 * 
	 * @param omega
	 *            The width of the window (time value items).
	 * @param beta
	 *            The advance of the window (time value items).
	 * @param baseTimeUnit
	 *            The used base time unit
	 */
	public ConvergenceDetectorAO(TimeValueItem omega, TimeValueItem beta, TimeUnit baseTimeUnit) {
		this(baseTimeUnit.convert(omega.getTime(), omega.getUnit()),
				baseTimeUnit.convert(beta.getTime(), beta.getUnit()));
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