package de.uniol.inf.is.odysseus.core.server.distribution;

import java.util.Collection;
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
	 * Inserts operators for fragmentation and data reunion into several copies of a logical plan to 
	 * merge them.
	 * @param logicalPlans A list of semantically equivalent logical plans, which shall be used as 
	 * partitions.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @param operatorsChangedDueToFragmentation A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are added or modified in any way due to 
	 * fragmentation. A modification means e.g. a related StreamAO, which copies were deleted.
	 * @param operatorsChangedDueToDataReunion A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are added or modified in any way due to 
	 * data reunion. A modification means e.g. a related FileSinkAO, which has been moved within 
	 * the logical plan and which copies were deleted.
	 * @param operatorsDeleted A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are deleted due to fragmentation or data reunion.
	 * @return The logical plan of the fragments put together by several operators for data reunion.
	 */
	public ILogicalOperator fragment(List<ILogicalOperator> logicalPlans, 
			QueryBuildConfiguration parameters, String sourceName, 
			Collection<ILogicalOperator> operatorsChangedDueToFragmentation, 
			Collection<ILogicalOperator> operatorsChangedDueToDataReunion, 
			Collection<ILogicalOperator> operatorsDeleted);

}