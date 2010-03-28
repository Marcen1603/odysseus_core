package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * Cleans up an operator plan. Removes metadata listeners.
 * 
 * @author Tobias Witt
 *
 */
public class CleanOperatorsVisitor implements INodeVisitor<IPhysicalOperator, Object> {
	
	private IQuery query;
	
	public CleanOperatorsVisitor(IQuery query) {
		this.query = query;
	}

	@Override
	public void ascend(IPhysicalOperator to) {
	}

	@Override
	public void descend(IPhysicalOperator to) {
	}

	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public void node(IPhysicalOperator op) {
		for (String data : op.getProvidedMonitoringData()) {
			op.removeMonitoringData(data);
		}
		op.removeOwner((IOperatorOwner) this.query);
	}

}
