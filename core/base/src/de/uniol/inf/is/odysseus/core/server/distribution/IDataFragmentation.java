package de.uniol.inf.is.odysseus.core.server.distribution;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * The interface for fragmentation strategies.
 * @author Michael Brand
 */
public interface IDataFragmentation {
	
	/**
	 * Returns the name of the strategy.
	 */
	public String getName();
	
	/**
	 * Inserts operators for fragmentation and data reunion into several copies of a logical plan to 
	 * merge them.
	 * @param fragmentPlan An ADT-class, which provides the situation before a fragmentation.
	 * @param numFragments The number of fragments.
	 * @param numReplicates The number of replicates for each fragment.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @return An ADT-class, which provides the result of a fragmentation.
	 */
	public IFragmentPlan fragment(IFragmentPlan fragmentPlan, int numFragments, int numReplicates, 
			QueryBuildConfiguration parameters, String sourceName);

}