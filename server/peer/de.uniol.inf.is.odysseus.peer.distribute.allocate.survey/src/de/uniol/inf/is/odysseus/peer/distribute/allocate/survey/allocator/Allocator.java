package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.allocator;

import java.util.List;

import net.jxta.id.ID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;

public interface Allocator {
	public ILogicalQuery allocate(ILogicalQuery query, ID sharedQueryID, List<SubPlan> subPlans, QueryBuildConfiguration transCfg);
}
