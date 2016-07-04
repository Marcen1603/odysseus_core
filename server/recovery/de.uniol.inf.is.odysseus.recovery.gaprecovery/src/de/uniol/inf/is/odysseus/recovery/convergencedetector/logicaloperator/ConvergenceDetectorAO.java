package de.uniol.inf.is.odysseus.recovery.convergencedetector.logicaloperator;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.trust.ITrust;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Operator to be automatically inserted after a gap recovery. <br />
 * <br />
 * It checks for each element, if it is inside a convergence phase or not. If an
 * element is inside a convergence phase, its trust value ({@link Trust}) will
 * be decreased. <br />
 * <br />
 * In a logical plan, a {@link ConvergenceDetectorAO} should be placed directly
 * after window operators. <br />
 * <br />
 * ATTENTION: Make sure, that {@link ITrust} is set as one of the meta
 * attributes.
 * 
 * @author Michael Brand
 *
 */
public class ConvergenceDetectorAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -5091013624360423002L;

	/**
	 * The width of the windows (time or elements).
	 */
	private final long wndWidth;

	/**
	 * The advance of the windows (time or elements).
	 */
	private final long wndAdvance;

	/**
	 * Creates a new {@link ConvergenceDetectorAO} as a copy of an existing one.
	 * 
	 * @param other
	 *            The {@link ConvergenceDetectorAO} to copy.
	 */
	public ConvergenceDetectorAO(ConvergenceDetectorAO other) {
		super(other);
		this.wndAdvance = other.wndAdvance;
		this.wndWidth = other.wndWidth;
	}

	/**
	 * Creates a new {@link ConvergenceDetectorAO} without base time unit (for
	 * element windows).
	 * 
	 * @param width
	 *            The width of the window (elements).
	 * @param advance
	 *            The advance of the window (elements).
	 */
	public ConvergenceDetectorAO(long width, long advance) {
		super();
		this.wndAdvance = advance;
		this.wndWidth = width;
	}

	/**
	 * Creates a new {@link ConvergenceDetectorAO}.
	 * 
	 * @param width
	 *            The width of the window (time value items).
	 * @param advance
	 *            The advance of the window (time value items).
	 * @param baseTimeUnit
	 *            The used base time unit
	 */
	public ConvergenceDetectorAO(TimeValueItem width, TimeValueItem advance, TimeUnit baseTimeUnit) {
		this(baseTimeUnit.convert(width.getTime(), width.getUnit()),
				baseTimeUnit.convert(advance.getTime(), advance.getUnit()));
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
		return this.wndWidth;
	}

	/**
	 * Gets the window advance.
	 * 
	 * @return The advance of the windows (time instants or elements).
	 */
	public final long getWindowAdvance() {
		return this.wndAdvance;
	}

}