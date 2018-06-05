package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.util.INamedInterface;

public interface IQueryPartAllocator extends INamedInterface {

	public Map<ILogicalQueryPart, IOdysseusNode> allocate( Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode, QueryBuildConfiguration config, List<String> allocatorParameters ) throws QueryPartAllocationException;
	public Map<ILogicalQueryPart, IOdysseusNode> reallocate( Map<ILogicalQueryPart, IOdysseusNode> previousAllocationMap, Collection<IOdysseusNode> faultyNodes, Collection<IOdysseusNode> knownRemoteNodes, IOdysseusNode localNode, QueryBuildConfiguration config, List<String> allocatorParameters ) throws QueryPartAllocationException;
	
}
