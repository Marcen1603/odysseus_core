package de.uniol.inf.is.odysseus.recovery.duplicatesdetector.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.recovery.TrustUpdatePunctuation;

/**
 * Operator to wait for a {@link TrustUpdatePunctuation}. All elements before
 * will get their trust values changed, because they might be duplicates.
 * 
 * @author Michael Brand
 *
 */
public class DupliatesDetectorAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 1664553070826611873L;

	/**
	 * The trust value to be used for duplicates.
	 */
	private final double trust;

	/**
	 * Creates a new {@link DupliatesDetectorAO}.
	 * 
	 * @param value
	 *            The trust value to be used for duplicates.
	 */
	public DupliatesDetectorAO(double value) {
		this.trust = value;
	}

	/**
	 * Creates a new {@link DupliatesDetectorAO} as a copy of an existing one.
	 */
	public DupliatesDetectorAO(DupliatesDetectorAO other) {
		this(other.trust);
	}

	@Override
	public DupliatesDetectorAO clone() {
		return new DupliatesDetectorAO(this);
	}

	/**
	 * Gets the trust value to be used for duplicates.
	 */
	public double getTrustValue() {
		return this.trust;
	}

}