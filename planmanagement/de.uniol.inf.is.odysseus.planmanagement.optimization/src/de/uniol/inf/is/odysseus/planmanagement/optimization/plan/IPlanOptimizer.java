package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

public interface IPlanOptimizer {
	public IEditableExecutionPlan optimizePlan(IPlanOptimizable sender,
			OptimizeParameter parameters, ArrayList<IEditableQuery> allQueries)
			throws QueryOptimizationException;
}