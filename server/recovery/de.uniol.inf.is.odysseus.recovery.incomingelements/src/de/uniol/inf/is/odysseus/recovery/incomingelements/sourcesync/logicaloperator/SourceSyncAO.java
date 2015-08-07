package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Logical operator to be placed directly after source access operators. <br />
 * <br />
 * In the phase of backup, it transfers all incoming elements. But it adjusts
 * it's offset as a Kafka consumer: It reads stored elements from Kafka server
 * and checks which offset the first element of process_next has. <br />
 * <br />
 * In the phase of recovery, it acts as a Kafka consumer and pushes data stream
 * elements from there into the DSMS. Elements from the original source will be
 * discarded in this time. But they are not lost, since this operator is also
 * always in backup mode.
 * 
 * @author Michael Brand
 *
 */
@LogicalOperator(name = "SOURCESYNC", minInputPorts = 1, maxInputPorts = 1, doc = "Should not be placed manually.", category = { LogicalOperatorCategory.ADVANCED })
public class SourceSyncAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -9156948424518889385L;

	/**
	 * The access to the source to synchronize.
	 */
	private AbstractAccessAO mSource;

	/**
	 * Creates a new {@link SourceSyncAO}.
	 */
	public SourceSyncAO() {
		super();
	}

	/**
	 * Creates a new {@link SourceSyncAO}.
	 * 
	 * @param access
	 *            The access to the source to synchronize.
	 */
	public SourceSyncAO(AbstractAccessAO access) {
		super();
		this.mSource = access;
	}

	/**
	 * Creates a new {@link SourceSyncAO} as a copy of an existing one.
	 * 
	 * @param other
	 *            The {@link SourceSyncAO} to copy.
	 */
	public SourceSyncAO(SourceSyncAO other) {
		super(other);
		this.mSource = other.mSource;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SourceSyncAO(this);
	}

	/**
	 * Gets the source to synchronize.
	 * 
	 * @return The access to the source to synchronize.
	 */
	public AbstractAccessAO getSource() {
		return this.mSource;
	}

}