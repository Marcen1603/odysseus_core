package de.uniol.inf.is.odysseus.recovery.convergencedetector.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Operator to be automatically inserted after a gap recovery. <br />
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
public class EWConvergenceDetectorPO<StreamObject extends IStreamObject<? extends IMetaAttribute>>
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
	 * @param width
	 *            The width of the window (elements).
	 * @param advance
	 *            The advance of the window (elements).
	 */
	public EWConvergenceDetectorPO(long width, long advance) {
		super(width, advance);
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		/*
		 * We can not trust any element, e.g., a wrong aggregation due to the
		 * gap. At this juncture, there is no possibility in Odysseus to
		 * determine Repeatable operators.
		 */
		@SuppressWarnings("unchecked")
		final StreamObject clone = (StreamObject) object.clone();
		((Trust) clone.getMetadata()).setTrust(0.5);
		transfer(clone);

	}

}