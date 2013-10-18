package de.uniol.inf.is.odysseus.p2p_new.fragment;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IFragmentPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.fragment.logicaloperator.ReplicationMergeAO;

/**
 * The class for data replication which is therefore an exception of data fragmentation strategies, 
 * because replication of source accesses remain and no fragmentation operator will be inserted.
 * @author Michael Brand
 */
public class Replication extends AbstractDataFragmentation {

	/**
	 * @see #getName()
	 */
	public static final String NAME = "replication";
	
	@Override
	public IFragmentPlan fragment(IFragmentPlan fragmentPlan, int numFragments, int numReplicates, 
			QueryBuildConfiguration parameters, String sourceName) {
		
		// Preconditions
		Preconditions.checkArgument(numFragments == 1);
		Preconditions.checkArgument(numReplicates > 0);
		
		return super.fragment(fragmentPlan, numFragments, numReplicates, parameters, sourceName);
		
	}
	
	@Override
	public String getName() {
		
		return Replication.NAME;
		
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	protected IFragmentPlan insertOperatorForFragmentation(IFragmentPlan fragmentPlan, int numFragments, int numReplicates, 
			QueryBuildConfiguration parameters, String sourceName) {
		
		return fragmentPlan;
		
		
	}
	
	@Override
	protected IPair<IFragmentPlan, Optional<AggregateAO>> replaceAggregation(IFragmentPlan fragmentPlan, ILogicalQuery query) {
		
		Optional<AggregateAO> agg = Optional.absent();
		return new Pair<IFragmentPlan, Optional<AggregateAO>>(fragmentPlan, agg);
		
	}

	@Override
	protected ILogicalOperator createOperatorForDataReunion() {
		
		return new ReplicationMergeAO();
		
	}

}