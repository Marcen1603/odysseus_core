package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharing;

import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public interface IQuerySharingOptimizer {
	public void applyQuerySharing(List<IQuery> oldPlan, OptimizationConfiguration conf);
	public void applyQuerySharing(List<IQuery> oldPlan, List<IQuery> newQueries, OptimizationConfiguration conf);
}
