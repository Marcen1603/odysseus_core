package de.uniol.inf.is.odysseus.core.server.distribution;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * The interface for the data fragmentation service. <br />
 * A data fragmentation strategy, which implements this interface, is able to insert operators for data distribution and data combining.
 * @author Michael Brand
 */
public interface IDataFragmentation {
	
	/**
	 * Inserts an {@link ILogicalOperator}, which distributes the stream objects to several partitions, into a logical plan.
	 * @param logicalPlan The logical plan, into which the {@link ILogicalOperator} shall be inserted.
	 * @param parameters The set of all used settings.
	 * @return The altered logical plan.
	 */
	public ILogicalOperator insertOperatorForDistribution(ILogicalOperator logicalPlan, int degreeOfParallelism, QueryBuildConfiguration parameters);
	
	/**
	 * Inserts an {@link ILogicalOperator}, which combines the results of several partitions, into a logical plan.
	 * @param logicalPlan The logical plan, into which the {@link ILogicalOperator} shall be inserted.
	 * @param parameters The set of all used settings.
	 * @return The altered logical plan.
	 */
	public ILogicalOperator insertOperatorForJunction(ILogicalOperator logicalPlan, QueryBuildConfiguration parameters);
	
	/**
	 * Returns the unique name of the fragmentation strategy.
	 */
	public String getName();

}