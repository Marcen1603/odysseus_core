package de.uniol.inf.is.odysseus.core.server.distribution;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;

/**
 * An interface for ADT-classes, which provides the result of a fragmentation.
 * @see IDataFragmentation
 * @author Michael Brand
 */
public interface IFragmentPlan {
	
	/**
	 * Returns a mapping of all origin operators to their query. <br />
	 * This is the status before fragmentation.
	 * @see IDataFragmentation#fragment(List, de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration, String)
	 */
	public Map<ILogicalQuery,List<ILogicalOperator>> getOperatorsPerLogicalPlanBeforeFragmentation();
	
	/**
	 * Returns a mapping of all operators to their query. <br />
	 * This is the status after fragmentation.
	 * @see IDataFragmentation#fragment(List, de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration, String)
	 */
	public Map<ILogicalQuery,List<ILogicalOperator>> getOperatorsPerLogicalPlanAfterFragmentation();
	
	/**
	 * Returns the logical plan of the fragments put together by several operators for data reunion.
	 */
	public ILogicalOperator getCompleteLogicalPlan();
	
	/**
	 * Returns a list of operators which are allocated to a part called fragmentation part. <br />
	 * This part and therefore the return value contains the {@link StreamAO}s for the sources fragmented 
	 * and the inserted operators for fragmentation. It may also contain {@link WindowAO}s, if they 
	 * were related to that source.
	 */
	public List<ILogicalOperator> getOperatorsOfFragmentationPart();
	
	/**
	 * Returns a list of operators which are allocated to a part called data reunion part. <br />
	 * This part and therefore the return value contains the inserted operators for data reunion. 
	 * It may also contain other operators, if they were moved or inserted after that operators.
	 */
	public List<ILogicalOperator> getOperatorsOfReunionPart();
	
	/**
	 * Returns a copy of the fragmentation plan.
	 */
	public IFragmentPlan clone();

}