package de.uniol.inf.is.odysseus.recovery.convergencedetector.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.trust.ITrust;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Operator to be automatically inserted after a gap recovery. <br />
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
public class TWConvergenceDetectorPO<StreamObject extends IStreamObject<IMetaAttribute>>
		extends AbstractConvergenceDetectorPO<StreamObject> {

	/**
	 * The time stamp, which marks the end of the convergence phase. Inclusive,
	 * so all time stamps AFTER this are outside the convergence phase.
	 */
	private PointInTime convEnd = null;

	/**
	 * Shortcut to check, if we are AFTER {@link #convEnd}.
	 */
	private boolean convEndReached = false;

	/**
	 * Creates a new {@link TWConvergenceDetectorPO} as a copy of an existing
	 * one.
	 * 
	 * @param other
	 *            The {@link TWConvergenceDetectorPO} to copy.
	 */
	public TWConvergenceDetectorPO(TWConvergenceDetectorPO<StreamObject> other) {
		super(other);
		this.convEnd = other.convEnd;
		this.convEndReached = other.convEndReached;
	}

	/**
	 * Creates a new {@link TWConvergenceDetectorPO}.
	 * 
	 * @param width
	 *            The width of the window (time instants).
	 * @param advance
	 *            The advance of the window (time instants).
	 */
	public TWConvergenceDetectorPO(long width, long advance) {
		super(width, advance);
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		if (this.convEndReached) {
			// We're done - shortcut
			transfer(object);
			return;
		}

		/*
		 * It is not necessary to calculate the end of the convergence phase.
		 * The trust will be set to 0 for every element aggregated with the
		 * first after offline phase (meta data merge).
		 */

		((ITrust) object.getMetadata()).setTrust(0);
		this.convEndReached = true;
		transfer(object);
	}

}