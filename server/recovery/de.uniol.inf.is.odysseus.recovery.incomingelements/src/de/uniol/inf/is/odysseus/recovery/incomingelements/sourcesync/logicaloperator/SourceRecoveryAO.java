package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

/**
 * Logical operator to be placed directly after source access operators. <br />
 * <br />
 * It can operate in backup mode or in recovery mode.<br />
 * <br />
 * In backup mode, it transfers all incoming elements without delay. But it
 * adjusts it's offset as a Kafka consumer: It reads stored elements from Kafka
 * server and checks which offset the first element of process_next has (initial
 * state) and which offset the first element after a protection point has. All
 * elements with a lower index shall not be considered for this operator/query
 * in case of recovery, because they are either not processed (initial state) or
 * before the last protection point. <br />
 * <br />
 * In recovery mode, it acts as a Kafka consumer and pushes data stream elements
 * from there to it's next operators, until it gets the same elements from both
 * Kafka and original source. Elements from the original source will be
 * discarded in this time. But they are not lost, since they are backed up by
 * BaDaSt.
 * 
 * @author Michael Brand
 */
public class SourceRecoveryAO extends UnaryLogicalOp {
	
	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -9156948424518889385L;
	
	/**
	 * The access to the source, which is recorded by BaDaSt.
	 */
	private AbstractAccessAO mSourceAccess;

	/**
	 * True, if data stream elements shall be recovered from BaDaSt.
	 */
	private boolean mRecoveryMode;

	/**
	 * Creates a new {@link SourceRecoveryAO}.
	 * 
	 * @param sourceAccess
	 *            The access to the source, which is recorded by BaDaSt.
	 * @param recoveryMode
	 *            True, if data stream elements shall be recovered from BaDaSt.
	 */
	public SourceRecoveryAO(AbstractAccessAO sourceAccess, boolean recoveryMode) {
		super();
		this.mSourceAccess = sourceAccess;
		this.mRecoveryMode = recoveryMode;
	}

	/**
	 * Creates a new {@link SourceRecoveryAO} as a copy of an existing one.
	 * 
	 * @param other
	 *            The {@link SourceRecoveryAO} to copy.
	 */
	public SourceRecoveryAO(SourceRecoveryAO other) {
		super(other);
		this.mSourceAccess = other.mSourceAccess;
		this.mRecoveryMode = other.mRecoveryMode;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SourceRecoveryAO(this);
	}

	/**
	 * Gets the source access.
	 * 
	 * @return The access to the source, which is recorded by BaDaSt.
	 */
	public AbstractAccessAO getSource() {
		return this.mSourceAccess;
	}

	/**
	 * Checks, if the operator is in recovery mode.
	 * 
	 * @return True, if data stream elements shall be recovered from BaDaSt.
	 */
	public boolean isInRecoveryMode() {
		return this.mRecoveryMode;
	}

}
