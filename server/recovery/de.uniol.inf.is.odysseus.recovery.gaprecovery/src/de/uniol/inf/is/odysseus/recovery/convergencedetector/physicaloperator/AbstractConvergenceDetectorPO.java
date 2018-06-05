package de.uniol.inf.is.odysseus.recovery.convergencedetector.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.convergencedetector.logicaloperator.ConvergenceDetectorAO;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Operator to be automatically inserted after a gap recovery. <br />
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
public abstract class AbstractConvergenceDetectorPO<StreamObject extends IStreamObject<? extends IMetaAttribute>>
		extends AbstractPipe<StreamObject, StreamObject> {

	/**
	 * The width of the windows (time instants or elements).
	 */
	protected final long wndWidth;

	/**
	 * The advance of the windows (time instants or elements).
	 */
	protected final long wndAdvance;

	/**
	 * Creates a new {@link AbstractConvergenceDetectorPO} as a copy of an
	 * existing one.
	 *
	 * @param other
	 *            The {@link AbstractConvergenceDetectorPO} to copy.
	 */
	public AbstractConvergenceDetectorPO(AbstractConvergenceDetectorPO<StreamObject> other) {
		super(other);
		this.wndAdvance = other.wndAdvance;
		this.wndWidth = other.wndWidth;
	}

	/**
	 * Creates a new {@link AbstractConvergenceDetectorPO}.
	 *
	 * @param width
	 *            The width of the window (time instants or elements).
	 * @param advance
	 *            The advance of the window (time instants or elements).
	 */
	public AbstractConvergenceDetectorPO(long width, long advance) {
		super();
		this.wndAdvance = advance;
		this.wndWidth = width;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation, port);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}