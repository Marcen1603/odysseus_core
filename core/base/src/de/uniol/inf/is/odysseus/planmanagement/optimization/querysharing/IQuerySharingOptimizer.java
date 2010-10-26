package de.uniol.inf.is.odysseus.planmanagement.optimization.querysharing;

import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public interface IQuerySharingOptimizer {
	public IPlan applyQuerySharing(IPlanOptimizable sender, IPlan oldPlan);
	public List<IQuery> eliminateIdenticalQueries(List<IQuery> newQueries, IPlan oldPlan);

}
