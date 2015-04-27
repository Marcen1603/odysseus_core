package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="PlanModificationAction", maxInputPorts=1, minInputPorts=1, doc="Executes plan modifications based on receiving tuple data", category = { LogicalOperatorCategory.PLAN})
public class PlanModificationActionAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -578154679444642283L;

	public PlanModificationActionAO() {
		super();
	}
	
	public PlanModificationActionAO( PlanModificationActionAO copy ) {
		super(copy);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PlanModificationActionAO(this);
	}

}
