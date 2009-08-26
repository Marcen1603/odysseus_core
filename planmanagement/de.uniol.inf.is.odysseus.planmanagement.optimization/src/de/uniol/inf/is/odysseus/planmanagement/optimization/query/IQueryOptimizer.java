package de.uniol.inf.is.odysseus.planmanagement.optimization.query;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

public interface IQueryOptimizer {
	public void optimizeQuery(IQueryOptimizable sender, IEditableQuery query,
			OptimizeParameter parameters)
			throws QueryOptimizationException;
}