package de.uniol.inf.is.odysseus.core.planmanagement.executor;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public interface IQueryListener {

	void queryAdded(ILogicalQuery logicalQuery);
	
}
