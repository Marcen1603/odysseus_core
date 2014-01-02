package de.uniol.inf.is.odysseus.peer.distribute;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

public interface IQueryDistributionPreProcessor extends INamedInterface {

	public void preProcess( IServerExecutor serverExecutor, ISession caller, ILogicalQuery queryToDistribute, QueryBuildConfiguration config );
	
}
