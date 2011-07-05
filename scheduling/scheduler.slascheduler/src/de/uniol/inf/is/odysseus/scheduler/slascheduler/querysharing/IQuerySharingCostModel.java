package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * Interface for cost models used for determining the effort of query sharing.
 * @author Thomas Vogelgesang
 *
 */
public interface IQuerySharingCostModel {

	/**
	 * determines the cost of the given operator 
	 * @param op the operator
	 * @return the cost of the given operator
	 */
	public double getOperatorCost(IPhysicalOperator op);
	
}
