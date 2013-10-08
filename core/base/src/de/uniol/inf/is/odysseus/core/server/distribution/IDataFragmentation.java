package de.uniol.inf.is.odysseus.core.server.distribution;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
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
	 * @param logicalPlans A mapping of all origin logical plans to their query.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @return An ADT-class, which provides the result of a fragmentation.
	 */
	public IFragmentPlan fragment(Map<ILogicalQuery, ILogicalOperator> logicalPlans, 
			QueryBuildConfiguration parameters, String sourceName);

}