package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

public interface IQueryPartModificator {

	public String getName();
	public Collection<ILogicalQueryPart> modify( Collection<ILogicalQueryPart> queryParts, QueryBuildConfiguration config) throws QueryPartModificationException;
	
}
