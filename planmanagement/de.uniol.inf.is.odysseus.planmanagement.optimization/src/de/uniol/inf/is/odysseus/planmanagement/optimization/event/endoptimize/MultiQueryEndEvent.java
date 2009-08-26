package de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;

public class MultiQueryEndEvent extends AbstractEndEvent<ArrayList<IEditableQuery>> {

	public static String QUERIES_ADD = "QUERIES_ADD";

	public MultiQueryEndEvent(IOptimizer sender, String id, ArrayList<IEditableQuery> value,
			IExecutionPlan newExecutionPlan) {
		super(sender, id, value, newExecutionPlan);
	}
}
