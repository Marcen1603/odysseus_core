package de.uniol.inf.is.odysseus.recovery.badast.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

/**
 * Operator to synchronize incoming elements with stored elements at BaDaSt. The
 * operator has two different modes: <br />
 * <br />
 * In backup mode, the operator transfers incoming elements without delay and
 * keeps the last transfered one in mind. If a checkpoint is reached, that
 * element is stored as operator state, because only newer elements shall be
 * recovered. <br />
 * <br />
 * The recovery mode enhances the backup mode. Elements are retrieved from
 * BaDaSt and elements after the element, that has been loaded as operator state
 * will be transferred. Additionally, the transfer mode will be switched again
 * from BaDaSt to the original source, if it is possible without data loss.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStSyncAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 2467527648921271902L;

	/**
	 * The access to the source, which is recorded by BaDaSt.
	 */
	private final AbstractAccessAO sourceAccess;

	/**
	 * True, if data stream elements shall be recovered from BaDaSt.
	 */
	private final boolean recoveryMode;

	/**
	 * Creates a new {@link BaDaStSyncAO}.
	 * 
	 * @param sourceAccess
	 *            The access to the source, which is recorded by BaDaSt.
	 * @param recoveryMode
	 *            True, if data stream elements shall be recovered from BaDaSt.
	 */
	public BaDaStSyncAO(AbstractAccessAO sourceAccess, boolean recoveryMode) {
		super();
		this.sourceAccess = sourceAccess;
		this.recoveryMode = recoveryMode;
	}

	/**
	 * Creates a new {@link BaDaStSyncAO} as a copy of an existing one.
	 */
	public BaDaStSyncAO(BaDaStSyncAO other) {
		super(other);
		this.sourceAccess = other.sourceAccess;
		this.recoveryMode = other.recoveryMode;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new BaDaStSyncAO(this);
	}

	/**
	 * Gets the source access.
	 */
	public AbstractAccessAO getSource() {
		return this.sourceAccess;
	}

	/**
	 * Checks, if the operator is in recovery mode.
	 */
	public boolean isInRecoveryMode() {
		return this.recoveryMode;
	}

}
