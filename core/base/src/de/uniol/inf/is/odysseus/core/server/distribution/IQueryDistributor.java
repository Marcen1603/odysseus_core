package de.uniol.inf.is.odysseus.core.server.distribution;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IQueryDistributor {

	public Collection<ILogicalQuery> distribute( IExecutor executor, ISession caller, Collection<ILogicalQuery> queries, QueryBuildConfiguration config) throws QueryDistributionException; 
}
