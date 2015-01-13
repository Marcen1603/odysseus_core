package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.logicaloperator.ReplicationMergeAO;

/**
 * The recovery merge operator is an extension of the replication merge
 * operator. Untill one replica fails, it works as an replication merge. After
 * recovering at least one replica, the recovery merge will only process
 * elements from an input port, which replica has never been recovered, or which
 * has been recovered earliest.
 * 
 * @author Michael Brand
 *
 */
@LogicalOperator(name="RECOVERYMERGE", minInputPorts=2, maxInputPorts=Integer.MAX_VALUE, doc="Merge input from semantically equal queries for active standy recovery.", category = {LogicalOperatorCategory.PROCESSING})
public class RecoveryMergeAO extends ReplicationMergeAO {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -753004855989112068L;

	/**
	 * Empty default cnstructor
	 */
	public RecoveryMergeAO() {
		super();
	}

	/**
	 * Creates a new recovery merge operator as a copy of an existing one.
	 * 
	 * @param mergeAO
	 *            The mrecovery merge operator to copy.
	 */
	public RecoveryMergeAO(ReplicationMergeAO mergeAO) {
		super(mergeAO);
	}

}
