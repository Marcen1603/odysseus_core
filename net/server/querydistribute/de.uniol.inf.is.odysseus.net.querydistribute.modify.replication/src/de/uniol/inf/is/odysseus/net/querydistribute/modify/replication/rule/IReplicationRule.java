package de.uniol.inf.is.odysseus.net.querydistribute.modify.replication.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.net.querydistribute.modify.replication.ReplicationParameterHelper;

/**
 * A replication rule can be implemented, if an operator can not be replicated.
 * 
 * @author Michael Brand
 * 
 * @param <Operator>
 *            The given operator type.
 *
 */
public interface IReplicationRule<Operator extends ILogicalOperator> {

	/**
	 * The class of the logical operator.
	 * 
	 * @return The class of the generic parameter <code>Operator</code>.
	 */
	public Class<Operator> getOperatorClass();

	/**
	 * Checks, if a given operator can be replicated.
	 * 
	 * @param operator
	 *            The given operator.
	 * @param helper
	 *            The current replication helper.
	 * @return True, if <code>operator</code> can be replicated.
	 */
	public boolean canOperatorBeReplicated(Operator operator,
			ReplicationParameterHelper helper);

}