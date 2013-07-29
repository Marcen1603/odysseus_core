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
	 * Inserts (incl. subscriptions) an {@link ILogicalOperator}, which distributes the stream objects of an given source name 
	 * to several partitions, into a collection of {@link ILogicalOperator}s.
	 * @param operators The collection of {@link ILogicalOperator}s, into which the {@link ILogicalOperator} shall be inserted.
	 * @param sourceName The name of the source which stream objects shall be distributed.
	 * @param degreeOfParallelism The degree of parallelism is also the number of fragments.
	 * @param parameters The set of all used settings.
	 * @return The altered collection of {@link ILogicalOperator}s.
	 */
	public Collection<ILogicalOperator> insertOperatorForDistribution(Collection<ILogicalOperator> operators, String sourceName, 
			int degreeOfParallelism,QueryBuildConfiguration parameters);
	
	/**
	 * Returns the class of the inserted {@link ILogicalOperator}s for data distribution.
	 */
	public Class<? extends ILogicalOperator> getOperatorForDistributionClass();
	
	/**
	 * Returns an {@link ILogicalOperator} for data junction.
	 */
	public ILogicalOperator createOperatorForJunction();
	
	/**
	 * Returns the class of the inserted {@link ILogicalOperator}s for data junction.
	 */
	public Class<? extends ILogicalOperator> getOperatorForJunctionClass();
	
	/**
	 * Returns the unique name of the fragmentation strategy.
	 */
	public String getName();

}