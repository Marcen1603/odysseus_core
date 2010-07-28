package de.uniol.inf.is.odysseus.rcp.viewer.model.graph;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;

public interface IOdysseusGraphModel extends IGraphModel<IPhysicalOperator>{

	public IQuery getQuery();
}
