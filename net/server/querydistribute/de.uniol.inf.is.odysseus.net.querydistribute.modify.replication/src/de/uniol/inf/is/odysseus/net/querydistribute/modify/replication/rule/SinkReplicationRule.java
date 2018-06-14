package de.uniol.inf.is.odysseus.net.querydistribute.modify.replication.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.net.querydistribute.modify.replication.ReplicationParameterHelper;

/**
 * A sink without being a source at the same time can not be replicated.
 * 
 * @author Michael Brand
 *
 */
public class SinkReplicationRule implements IReplicationRule<ILogicalOperator> {

	@Override
	public boolean canOperatorBeReplicated(ILogicalOperator operator,
			ReplicationParameterHelper helper) {

		return !operator.isSinkOperator() || operator.isSourceOperator();

	}

	@Override
	public Class<ILogicalOperator> getOperatorClass() {

		return ILogicalOperator.class;

	}

}