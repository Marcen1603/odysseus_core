package de.uniol.inf.is.odysseus.core.server.distribution;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IQueryDistributor {

	public void distribute( IServerExecutor executor, ISession caller, Collection<ILogicalQuery> queries, QueryBuildConfiguration config) throws QueryDistributionException; 
}
