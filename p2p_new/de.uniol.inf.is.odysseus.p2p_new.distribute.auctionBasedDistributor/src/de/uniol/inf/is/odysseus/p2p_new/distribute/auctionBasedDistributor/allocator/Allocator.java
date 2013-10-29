package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.allocator;

import java.util.List;

import net.jxta.id.ID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;

public interface Allocator {
	public ILogicalQuery allocate(ILogicalQuery query,
			ID sharedQueryID,
			List<SubPlan> subPlans, QueryBuildConfiguration transCfg);
}
