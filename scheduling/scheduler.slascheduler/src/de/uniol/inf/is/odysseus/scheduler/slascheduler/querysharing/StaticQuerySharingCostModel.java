package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.LeftJoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * Simple standard cost model
 * @author Thomas Vogelgesang
 *
 */
public class StaticQuerySharingCostModel implements IQuerySharingCostModel {

	/**
	 * the returned costs are statically defined for the type of the given
	 * operator
	 */
	@Override
	public double getOperatorCost(IPhysicalOperator op) {
		if (op instanceof JoinTIPO<?, ?> || op instanceof LeftJoinTIPO<?, ?>) {
			// joins are expected to be expensive
			return 2.0;
		} else if (op instanceof IBuffer<?>) {
			// buffers do no action so they are expected to be very cheap
			return 0.0;
		} else {
			// other operators are axpected to have moderate costs
			return 1.0;
		}
	}

}
