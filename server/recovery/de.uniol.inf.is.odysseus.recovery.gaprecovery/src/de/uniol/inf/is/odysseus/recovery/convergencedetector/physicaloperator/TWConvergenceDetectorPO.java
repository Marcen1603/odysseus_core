package de.uniol.inf.is.odysseus.recovery.convergencedetector.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
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
public class TWConvergenceDetectorPO<StreamObject extends IStreamObject<? extends IMetaAttribute>>
		extends AbstractConvergenceDetectorPO<StreamObject> {

	/**
	 * Shortcut to check, if we marked the first element.
	 */
	private boolean firstMarked = false;

	/**
	 * Creates a new {@link TWConvergenceDetectorPO} as a copy of an existing
	 * one.
	 *
	 * @param other
	 *            The {@link TWConvergenceDetectorPO} to copy.
	 */
	public TWConvergenceDetectorPO(TWConvergenceDetectorPO<StreamObject> other) {
		super(other);
		this.firstMarked = other.firstMarked;
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
		if (this.firstMarked) {
			// We're done - shortcut
			transfer(object);
			return;
		}

		// FIXME find a way to mark the correct set of elements.
		// Because of groups there might be more than one element!

		@SuppressWarnings("unchecked")
		final StreamObject clone = (StreamObject) object.clone();
		((ITrust) clone.getMetadata()).setTrust(0);
		this.firstMarked = true;
		transfer(clone);
	}

}