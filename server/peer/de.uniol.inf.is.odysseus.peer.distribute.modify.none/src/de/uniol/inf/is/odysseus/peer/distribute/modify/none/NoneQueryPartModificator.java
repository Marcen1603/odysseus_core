package de.uniol.inf.is.odysseus.peer.distribute.modify.none;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;

public class NoneQueryPartModificator implements IQueryPartModificator {

	@Override
	public String getName() {
		return "none";
	}

	@Override
	public Collection<ILogicalQueryPart> modify(Collection<ILogicalQueryPart> queryParts, QueryBuildConfiguration config) throws QueryPartModificationException {
		return Lists.newArrayList(queryParts);
	}

}
