package de.uniol.inf.is.odysseus.core.distribution;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public interface ILogicalQueryDistributor {

	public List<ILogicalQuery> distributeLogicalQueries( IExecutor sender, List<ILogicalQuery> queriesToDistribute, String cfgName);

	public String getName();
}
