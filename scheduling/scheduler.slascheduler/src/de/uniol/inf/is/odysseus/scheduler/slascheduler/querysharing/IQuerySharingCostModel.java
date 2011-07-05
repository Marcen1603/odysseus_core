package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public interface IQuerySharingCostModel {

	public double getOperatorCost(IPhysicalOperator op);
	
}
