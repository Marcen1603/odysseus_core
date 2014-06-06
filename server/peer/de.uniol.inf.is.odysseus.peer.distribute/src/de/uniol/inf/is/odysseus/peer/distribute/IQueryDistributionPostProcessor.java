package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

public interface IQueryDistributionPostProcessor extends INamedInterface {

	public void postProcess( IServerExecutor serverExecutor, ISession caller, Map<ILogicalQueryPart, PeerID> allocationMap, ILogicalQuery query, QueryBuildConfiguration config, List<String> parameters ) throws QueryDistributionPostProcessorException;
	
}
