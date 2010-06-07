package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	Logger logger = LoggerFactory.getLogger(CleanOperatorsVisitor.class);
	
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
		logger.debug("Remove Owner "+this.query+" from "+op);
		op.removeOwner((IOperatorOwner) this.query);
		if (!op.hasOwner()) {
			for (String data : op.getProvidedMonitoringData()) {
				// TODO: removeMonitoringData sollte auch die metadata listener aus der eventListener Map entfernen 
				op.removeMonitoringData(data);
			}
			op.close();
		}
	}

}
