package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.recovery.protectionpoints.IProtectionPointManager;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Logical operator to be placed directly after source access operators. <br />
 * <br />
 * It can operate in backup mode or in recovery mode.<br />
 * <br />
 * In backup mode, it transfers all incoming elements without delay. But it
 * adjusts it's offset as an {@code ISubscriber}: It reads stored elements from
 * a publish subscribe system and checks which offset the first element of
 * process_next has (initial state) and which offset the first element after a
 * protection point has. All elements with a lower index shall not be considered
 * for this operator/query in case of recovery, because they are either not
 * processed (initial state) or before the last protection point. <br />
 * <br />
 * In recovery mode, it acts as an {@code ISubscriber} and pushes data stream
 * elements from public subscribe system to it's next operators, until it gets
 * the same elements from both public subscribe system and original source.
 * Elements from the original source will be discarded in this time. But they
 * are not lost, since they are backed up by BaDaSt. <br />
 * <br />
 * Additionally, the operator decreases the {@link Trust} value for elements,
 * which are consumed from BaDaSt and for which there is a possibility, that
 * they are duplicates. In a rollback recovery, all elements are duplicates,
 * which are between the last checkpoint and the system failure. But they can
 * not be determined precisely. The first element, which is for sure not a
 * duplicate, is the first element received from the original source after
 * system restart.
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
	private final AbstractAccessAO mSourceAccess;

	/**
	 * True, if data stream elements shall be recovered from BaDaSt.
	 */
	private final boolean mRecoveryMode;

	/**
	 * The protection point manager to be used to inform, if a protection point
	 * is reached.
	 */
	private final IProtectionPointManager mProtectionPointManager;

	/**
	 * Creates a new {@link SourceRecoveryAO}.
	 * 
	 * @param sourceAccess
	 *            The access to the source, which is recorded by BaDaSt.
	 * @param recoveryMode
	 *            True, if data stream elements shall be recovered from BaDaSt.
	 * @param protectionPointManager
	 *            The protection point manager to be used to inform, if a
	 *            protection point is reached.
	 */
	public SourceRecoveryAO(AbstractAccessAO sourceAccess, boolean recoveryMode,
			IProtectionPointManager protectionPointManager) {
		super();
		this.mSourceAccess = sourceAccess;
		this.mRecoveryMode = recoveryMode;
		this.mProtectionPointManager = protectionPointManager;
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
		this.mProtectionPointManager = other.mProtectionPointManager;
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

	/**
	 * Gets the protection point manager.
	 * 
	 * @return The protection point manager to be used to inform, if a
	 *         protection point is reached.
	 */
	public IProtectionPointManager getProtectionPointManager() {
		return this.mProtectionPointManager;
	}

}
