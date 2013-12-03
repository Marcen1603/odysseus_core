package de.uniol.inf.is.odysseus.core.server.distribution;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

public interface ILogicalQueryDistributor {

	public List<ILogicalQuery> distributeLogicalQueries( IExecutor sender, List<ILogicalQuery> queriesToDistribute, QueryBuildConfiguration parameters);

	public String getName();
}
