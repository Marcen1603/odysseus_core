package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

public interface IQueryPartModificator extends INamedInterface {

	public Collection<ILogicalQueryPart> modify( Collection<ILogicalQueryPart> queryParts, QueryBuildConfiguration config, List<String> modificatorParameters) throws QueryPartModificationException;
	
}
