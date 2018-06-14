package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.util.INamedInterface;

public interface IQueryPartModificator extends INamedInterface {

	public Collection<ILogicalQueryPart> modify( Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, QueryBuildConfiguration config, List<String> modificatorParameters) throws QueryPartModificationException;
	
}
