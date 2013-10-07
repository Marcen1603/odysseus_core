package de.uniol.inf.is.odysseus.p2p_new.fragment;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * An abstract implementation for fragmentation strategies.
 * @author Michael Brand
 */
public abstract class AbstractDataFragmentation implements IDataFragmentation {
	
	@Override
	public abstract String getName();
	
	@Override
	public ILogicalOperator fragment(List<ILogicalOperator> logicalPlans, 
			QueryBuildConfiguration parameters, String sourceName, 
			Collection<ILogicalOperator> operatorsChangedDueToFragmentation, 
			Collection<ILogicalOperator> operatorsChangedDueToDataReunion, 
			Collection<ILogicalOperator> operatorsDeleted) {
		
		// Preconditions
		Preconditions.checkNotNull(logicalPlans);
		Preconditions.checkArgument(logicalPlans.size() > 1);
		Preconditions.checkNotNull(parameters);
		Preconditions.checkNotNull(operatorsChangedDueToFragmentation);
		Preconditions.checkNotNull(operatorsChangedDueToFragmentation.isEmpty());
		Preconditions.checkNotNull(operatorsChangedDueToDataReunion);
		Preconditions.checkNotNull(operatorsChangedDueToDataReunion.isEmpty());
		Preconditions.checkNotNull(operatorsDeleted);
		Preconditions.checkNotNull(operatorsDeleted.isEmpty());
		
		// All operators of each logical plan
		List<List<ILogicalOperator>> operatorsPerLogicalPlan = null;
		
		// Insert operator for fragmentation
		operatorsPerLogicalPlan = insertOperatorForFragmentation(logicalPlans, parameters, sourceName, 
				operatorsChangedDueToFragmentation, operatorsDeleted);
		
		// Insert operator for data reunion
		return insertOperatorForDataReunion(operatorsPerLogicalPlan, operatorsChangedDueToDataReunion, 
				operatorsDeleted);
		
		
	}
	
	/**
	 * Inserts operators for fragmentation, if a source was set by {@link #setSourceName(String)} and 
	 * deletes all replications of the source access. <br />
	 * So there is only one source access left. All fragments will be subscribed to that single source 
	 * access.
	 * @param logicalPlans A list of semantically equivalent logical plans, which shall be used as 
	 * partitions.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @param operatorsChangedDueToFragmentation A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are added or modified in any way due to 
	 * fragmentation. A modification means e.g. a related StreamAO, which copies were deleted.
	 * @param operatorsDeleted A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are deleted due to fragmentation or data reunion.
	 * @return All operators of each logical plan inclusive the operators for fragmentation.
	 */
	protected abstract List<List<ILogicalOperator>> insertOperatorForFragmentation(
			List<ILogicalOperator> logicalPlans, QueryBuildConfiguration parameters, 
			String sourceName, Collection<ILogicalOperator> operatorsChangedDueToFragmentation, 
			Collection<ILogicalOperator> operatorsDeleted);
	
	/**
	 * Inserts operators for data reunion. All sinks of all fragments will be subscribed by the data 
	 * reunion operators.
	 * @param logicalPlans A list of semantically equivalent logical plans, which shall be used as 
	 * partitions.
	 * @param operatorsChangedDueToDataReunion A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are added or modified in any way due to 
	 * data reunion. A modification means e.g. a related FileSinkAO, which has been moved within 
	 * the logical plan and which copies were deleted.
	 * @param operatorsDeleted A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are deleted due to fragmentation or data reunion.
	 * @return The merged logical plan.
	 */
	protected abstract ILogicalOperator insertOperatorForDataReunion(
			List<List<ILogicalOperator>> operatorsPerLogicalPlan, 
			Collection<ILogicalOperator> operatorsChangedDueToDataReunion, 
			Collection<ILogicalOperator> operatorsDeleted);
	
	/**
	 * Sorts a given mapping of operators and planes.
	 * @param operatorsToPlaneMap A mapping of operators and planes. Will not been muted.
	 * @return A sorted list of all keys given by the map. The first operator of the list has the lowest plane.
	 */
	@SuppressWarnings("null")
	protected static List<ILogicalOperator> sortOperatorsToPlaneMap(
			Map<ILogicalOperator, Integer> operatorsToPlaneMap) {
		
		Preconditions.checkNotNull(operatorsToPlaneMap);
		
		// The return value
		List<ILogicalOperator> sortedList = Lists.newArrayList();
		
		// A copy of the input map
		Map<ILogicalOperator, Integer> copyMap = Maps.newHashMap(operatorsToPlaneMap);
		
		// The minium plane found
		Pair<ILogicalOperator, Integer> minimumPlane = null;
		
		while(!copyMap.isEmpty()) {
		
			for(ILogicalOperator operator : copyMap.keySet()) {
				
				if(minimumPlane == null || copyMap.get(operator) < minimumPlane.getE2())
					minimumPlane = new Pair<ILogicalOperator, Integer>(
							operator, copyMap.get(operator));
				
			}
			
			sortedList.add(minimumPlane.getE1());
			copyMap.remove(minimumPlane.getE1());
			minimumPlane = null;
			
		};
		
		return sortedList;
		
	}

}