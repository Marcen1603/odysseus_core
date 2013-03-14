package de.uniol.inf.is.odysseus.p2p_new.distributor.modelbased;

import java.util.List;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public class ModelBasedDistributor implements ILogicalQueryDistributor {

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender, List<ILogicalQuery> queriesToDistribute) {
		return queriesToDistribute;
	}

}
