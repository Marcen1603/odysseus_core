package de.uniol.inf.is.odysseus.recovery.incomingelements.trustpunctuation.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.recovery.incomingelements.trustpunctuation.TrustPunctuation;

/**
 * Operator to wait for a {@link TrustPunctuation}. All elements before will get
 * their trust values changed.
 * 
 * @author Michael
 *
 */
public class TrustPunctuationReaderAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -6904028550703217028L;

	/**
	 * The trust value to be used before a {@link TrustPunctuation} arrives.
	 */
	private final double trust;

	/**
	 * True, if trust shall be changed.
	 */
	private final boolean mRecoveryMode;

	/**
	 * Creates a new {@link TrustPunctuationReaderAO}.
	 * 
	 * @param value
	 *            The trust value to be used before a {@link TrustPunctuation}
	 *            arrives.
	 * @param recMode
	 *            True, if trust shall be changed.
	 */
	public TrustPunctuationReaderAO(double value, boolean recMode) {
		this.trust = value;
		this.mRecoveryMode = recMode;
	}

	/**
	 * Creates a new {@link TrustPunctuationReaderAO} as a copy of an existing
	 * one.
	 * 
	 * @param other
	 *            The {@link TrustPunctuationReaderAO} to copy.
	 */
	public TrustPunctuationReaderAO(TrustPunctuationReaderAO other) {
		this(other.trust, other.mRecoveryMode);
	}

	@Override
	public TrustPunctuationReaderAO clone() {
		return new TrustPunctuationReaderAO(this);
	}

	/**
	 * Gets the trust value to be used before a {@link TrustPunctuation}
	 * arrives.
	 */
	public double getTrustValue() {
		return this.trust;
	}

	/**
	 * Checks, if the operator is in recovery mode.
	 * 
	 * @return True, if trust shall be changed.
	 */
	public boolean isInRecoveryMode() {
		return this.mRecoveryMode;
	}

}