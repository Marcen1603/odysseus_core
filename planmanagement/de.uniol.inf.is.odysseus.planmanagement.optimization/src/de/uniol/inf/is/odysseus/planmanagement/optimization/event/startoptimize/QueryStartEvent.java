package de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.AbstractEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.QueryEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

public class QueryStartEvent extends AbstractStartEvent<IEditableQuery> {

	public static String QUERY_REMOVE = "QUERY_REMOVED";
	public static String QUERY_START = "QUERY_STARTED";
	public static String QUERY_STOP = "QUERY_STOPPED";
	public static String QUERY_REOPTIMIZE = "QUERY_REOPTIMIZE";
	
	public QueryStartEvent(IOptimizable sender, String id, IEditableQuery value,
			OptimizeParameter parameter) {
		super(sender, id, value, parameter);
	}

	public QueryStartEvent(IOptimizable sender, String id, IEditableQuery value,
			AbstractOptimizationParameter<?>... parameter) {
		super(sender, id, value, parameter);
	}
	
	@Override
	public AbstractEndEvent<?> createEndOptimizeEvent(IOptimizer optimizer,
			IExecutionPlan newExecutionPlan) {
		return new QueryEndEvent(optimizer, getID(), getValue(), newExecutionPlan);
	}
}
