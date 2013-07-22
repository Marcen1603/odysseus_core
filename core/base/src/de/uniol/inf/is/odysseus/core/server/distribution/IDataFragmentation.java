package de.uniol.inf.is.odysseus.core.server.distribution;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * The interface for the data fragmentation service. <br />
 * A data fragmentation strategy, which implements this interface, is able to insert operators for data distribution and data combining.
 * @author Michael Brand
 */
public interface IDataFragmentation {
	
	/**
	 * Inserts (incl. subscriptions) an {@link ILogicalOperator}, which distributes the stream objects to several partitions, 
	 * into a collection of {@link ILogicalOperator}s.
	 * @param operators The collection of {@link ILogicalOperator}s, into which the {@link ILogicalOperator} shall be inserted.
	 * @param parameters The set of all used settings.
	 * @return The altered collection of {@link ILogicalOperator}s.
	 */
	public Collection<ILogicalOperator> insertOperatorForDistribution(Collection<ILogicalOperator> operators, int degreeOfParallelism, 
			QueryBuildConfiguration parameters);
	
	/**
	 * Inserts an {@link ILogicalOperator}, which combines the results of several partitions, 
	 * into a collection of {@link ILogicalOperator}s.
	 * @param operators The collection of {@link ILogicalOperator}s, into which the {@link ILogicalOperator} shall be inserted.
	 * @param parameters The set of all used settings.
	 * @return The altered collection of {@link ILogicalOperator}s.
	 */
	public Collection<ILogicalOperator> insertOperatorForJunction(Collection<ILogicalOperator> logicalPlan, 
			QueryBuildConfiguration parameters);
	
	/**
	 * Returns the unique name of the fragmentation strategy.
	 */
	public String getName();

}