package de.uniol.inf.is.odysseus.p2p_new.distribute.model;

import java.util.List;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public class ModelBasedDistributor implements ILogicalQueryDistributor {

	// called by OSGi-DS
	public final void activate() {
		
	}

	// called by OSGi-DS
	public final void deactivate() {
		
	}

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender, List<ILogicalQuery> queriesToDistribute) {
		return queriesToDistribute;
	}

}
