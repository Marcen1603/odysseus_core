package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner;

import java.util.List;

import net.jxta.id.ID;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CouldNotPartitionException;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;

public interface Partitioner {
	public List<SubPlan> partitionWithDummyOperators(ILogicalOperator queryPlan, ID sharedQueryId, QueryBuildConfiguration transCfg) throws CouldNotPartitionException;
}
