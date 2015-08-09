package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Logical operator to be placed directly after source access operators. <br />
 * <br />
 * It transfers all incoming elements without delay. But it adjusts it's offset
 * as a Kafka consumer: It reads stored elements from Kafka server and checks
 * which offset the first element of process_next has (means which index has
 * this element on the Kafka server). All elements with a lower index shall not
 * be considered for this operator/query in case of recovery, because they are
 * not processed. <br />
 * <br />
 * TODO copy javaDoc from other PO In the case of recovery, it acts as a Kafka
 * consumer and pushes data stream elements from there into the DSMS. Elements
 * from the original source will be discarded in this time. But they are not
 * lost, since this operator is also always in backup mode.
 * 
 * @author Michael Brand
 *
 * @param <T>
 *            The type of stream objects to process.
 */
@LogicalOperator(name = "BADASTACCESS", minInputPorts = 1, maxInputPorts = 1, doc = "Should not be placed manually.", category = { LogicalOperatorCategory.ADVANCED })
public class BaDaStAccessAO extends UnaryLogicalOp {

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
	 * Creates a new {@link BaDaStAccessAO}.
	 */
	public BaDaStAccessAO() {
		super();
	}

	/**
	 * Creates a new {@link BaDaStAccessAO}.
	 * 
	 * @param sourceAccess
	 *            The access to the source, which is recorded by BaDaSt.
	 * @param recoveryMode
	 *            True, if data stream elements shall be recovered from BaDaSt.
	 */
	public BaDaStAccessAO(AbstractAccessAO sourceAccess, boolean recoveryMode) {
		super();
		this.mSourceAccess = sourceAccess;
		this.mRecoveryMode = recoveryMode;
	}

	/**
	 * Creates a new {@link BaDaStAccessAO} as a copy of an existing one.
	 * 
	 * @param other
	 *            The {@link BaDaStAccessAO} to copy.
	 */
	public BaDaStAccessAO(BaDaStAccessAO other) {
		super(other);
		this.mSourceAccess = other.mSourceAccess;
		this.mRecoveryMode = other.mRecoveryMode;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new BaDaStAccessAO(this);
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