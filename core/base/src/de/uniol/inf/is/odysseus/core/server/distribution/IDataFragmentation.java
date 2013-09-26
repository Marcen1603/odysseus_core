package de.uniol.inf.is.odysseus.core.server.distribution;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
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
	 * Inserts operators for fragmentation and data reunion into several copies of a logical plan to merge them.
	 * @param logicalPlans A list of semantically equivalent logical plans, which shall be used as partitions.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @return The logical plan of the fragments put together by several operators for data reunion.
	 */
	public ILogicalOperator fragment(List<ILogicalOperator> logicalPlans, QueryBuildConfiguration parameters, String sourceName);

}