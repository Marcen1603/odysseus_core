package de.uniol.inf.is.odysseus.recovery.gaprecovery.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
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
 * after window operators.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractConvergenceDetectorPO<StreamObject extends IStreamObject<ITimeIntervalTrust>>
		extends AbstractPipe<StreamObject, StreamObject> implements IStatefulPO {

	/**
	 * The width of the windows (time instants or elements).
	 */
	private final long mOmega;

	/**
	 * The advance of the windows (time instants or elements).
	 */
	private final long mBeta;
	
	/**
	 * True, if the end of the convergence phase is reached.
	 */
	protected boolean mEndReached = false;

	/**
	 * Creates a new {@link AbstractConvergenceDetectorPO} as a copy of an
	 * existing one.
	 * 
	 * @param other
	 *            The {@link AbstractConvergenceDetectorPO} to copy.
	 */
	public AbstractConvergenceDetectorPO(AbstractConvergenceDetectorPO<StreamObject> other) {
		super(other);
		this.mBeta = other.mBeta;
		this.mOmega = other.mOmega;
		this.mEndReached = other.mEndReached;
	}

	/**
	 * Creates a new {@link AbstractConvergenceDetectorPO}.
	 * 
	 * @param omega
	 *            The width of the window (time instants or elements).
	 * @param beta
	 *            The advance of the window (time instants or elements).
	 */
	public AbstractConvergenceDetectorPO(long omega, long beta) {
		super();
		this.mBeta = beta;
		this.mOmega = omega;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation, port);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	/**
	 * Gets the window width.
	 * @return The width of the windows (time instants or elements).
	 */
	protected final long getWindowWidth() {
		return this.mOmega;
	}
	
	/**
	 * Gets the window advance.
	 * @return The advance of the windows (time instants or elements).
	 */
	protected final long getWindowAdvance() {
		return this.mBeta;
	}

}