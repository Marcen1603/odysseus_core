package de.uniol.inf.is.odysseus.p2p_new.fragment;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IFragmentPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
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
	public String getName() {
		
		return Replication.NAME;
		
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	protected IFragmentPlan insertOperatorForFragmentation(IFragmentPlan fragmentPlan, 
			QueryBuildConfiguration parameters, String sourceName) {
		
		return fragmentPlan;
		
		
	}
	
	@Override
	protected IFragmentPlan insertOperatorForDataReunion(IFragmentPlan fragmentPlan) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		
		// The return value
		IFragmentPlan enhancedFragmentPlan = fragmentPlan.clone();
		
		// The operators for data reunion
		List<ILogicalOperator> operatorsForDataReunion = Lists.newArrayList();
		
		// Insert the operator for data reunion
		enhancedFragmentPlan = 
				insertOperatorForDataReunion(enhancedFragmentPlan, operatorsForDataReunion);
		
		// Subscribe all other logical plans
		int planIndex = 0;
		for(ILogicalQuery query : enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().keySet()) {
			
			if(planIndex == 0) {
				
				planIndex++;
				continue;
				
			}
			
			enhancedFragmentPlan = 
					subscribeOperatorForDataReUnion(enhancedFragmentPlan, operatorsForDataReunion, query, planIndex);
			planIndex++;
			
		}
		
		// Initialize the operators for data reunion
		for(ILogicalOperator operatorForDataReunion : enhancedFragmentPlan.getOperatorsOfReunionPart())
			operatorForDataReunion.initialize();
		
		return enhancedFragmentPlan;
		
	}
	
	/**
	 * Inserts operators for data reunion.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @param operatorsForDataReunion A list of all created operators for data reunion for the query.
	 * @return The new status of the fragmentation.
	 */
	protected IFragmentPlan insertOperatorForDataReunion(IFragmentPlan fragmentPlan, 
			List<ILogicalOperator> operatorsForDataReunion) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkNotNull(operatorsForDataReunion);
		
		// The return value
		IFragmentPlan enhancedFragmentPlan = fragmentPlan.clone();
		
		// The query to be used for insertion
		ILogicalQuery query = fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().keySet().iterator().next();
		
		// A mapping of all operators to their plane within the logical plan
		Map<ILogicalOperator, Integer> operatorsToPlaneMap = 
				RestructHelper.assignOperatorPlanes(fragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query).iterator().next());
		
		// A mapping of all operators which shall be moved to the data reunion part 
		// to their plane within the logical plan
		Map<ILogicalOperator, Integer> operatorsForDataReunionPartToPlaneMap = Maps.newHashMap();
		
		boolean finished = false;
		do {
			
			finished = true;
			
			for(ILogicalOperator operator : 
				fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().get(query)) {
				
				if(operatorsForDataReunionPartToPlaneMap.containsKey(operator))
					continue;
				else if(Arrays.asList(FragmentationHelper.OPERATOR_CLASSES_DATAREUNION_PART).contains(
						operator.getClass())) {
					
					operatorsForDataReunionPartToPlaneMap.put(operator, operatorsToPlaneMap.get(operator));
					
					// Delete subscriptions
					for(LogicalSubscription subToSink : operator.getSubscriptions()) {
							
						for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
							
							subToSink.getTarget().subscribeToSource(
									subToSource.getTarget(), subToSink.getSinkInPort(), 
									subToSource.getSourceOutPort(), subToSink.getSchema());
							
						}
						operator.unsubscribeSink(subToSink);
						
					}
					operator.unsubscribeFromAllSources();
					
					finished = false;
						
				} else if(operator.getSubscriptions().isEmpty()) {
					
					// The operator for data reunion to be inserted
					ILogicalOperator operatorForDataReunion = new ReplicationMergeAO();
					operator.subscribeSink(operatorForDataReunion, 0, 0, operator.getOutputSchema());
					operatorsForDataReunion.add(operatorForDataReunion);
					
					finished = false;
				
				}
				
			}		
			
		} while(!finished);
		
		if(!operatorsForDataReunionPartToPlaneMap.isEmpty()) {
			
			// A sorted list of all operators which shall be moved to the data reunion part
			List<ILogicalOperator> sortedOperatorsForDataReunionPart = 
					sortOperatorsToPlaneMap(operatorsForDataReunionPartToPlaneMap);
			
			// The iterator through the operators for data reunion
			Iterator<ILogicalOperator> operatorsForDataReunionIter = operatorsForDataReunion.iterator();
			
			// The lowest plane
			int lowestPlane = operatorsForDataReunionPartToPlaneMap.get(
					sortedOperatorsForDataReunionPart.iterator().next());
			
			// Insert the operators for the data reunion part
			for(int operatorNo = 0; operatorNo < sortedOperatorsForDataReunionPart.size(); operatorNo++) {
				
				// The current operator
				ILogicalOperator operator = sortedOperatorsForDataReunionPart.get(operatorNo);
				
				if(operatorsForDataReunionPartToPlaneMap.get(operator) == lowestPlane &&
						operatorsForDataReunionIter.hasNext()) {
					
					// Subscribe to operator for data reunion
					ILogicalOperator operatorForDataReunion = operatorsForDataReunionIter.next();
					operatorForDataReunion.subscribeSink(operator, 0, 0, 
							operatorForDataReunion.getOutputSchema());
					
				} else {
					
					// Subscribe to previous operator
					operator.subscribeToSource(sortedOperatorsForDataReunionPart.get(operatorNo - 1), 
							0, 0, sortedOperatorsForDataReunionPart.get(operatorNo - 1).getOutputSchema());
					
				}
				
			}
			
		}
		
		enhancedFragmentPlan.getOperatorsOfReunionPart().addAll(operatorsForDataReunionPartToPlaneMap.keySet());
		enhancedFragmentPlan.getOperatorsOfReunionPart().addAll(operatorsForDataReunion);
		enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query).removeAll(operatorsForDataReunionPartToPlaneMap.keySet());
		
		return enhancedFragmentPlan;
		
	}
	
	/**
	 * Subscribes operators for data reunion.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @param operatorsForDataReunion A list of all created operators for data reunion for the query.
	 * @param query The query to process.
	 * @param planIndex The index of the logical plan to process. <br />
	 * It is also the input port of the inserted operator for data reunion.
	 * @return The new status of the fragmentation.
	 */
	protected IFragmentPlan subscribeOperatorForDataReUnion(IFragmentPlan fragmentPlan, 
			List<ILogicalOperator> operatorsForDataReunion, ILogicalQuery query, int planIndex) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkNotNull(operatorsForDataReunion);
		Preconditions.checkNotNull(query);
		Preconditions.checkArgument(planIndex >= 0);
		Preconditions.checkArgument(
				planIndex < fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().keySet().size());
		
		// The return value
		IFragmentPlan enhancedFragmentPlan = fragmentPlan.clone();
		
		// Operators for the reunion part
		List<ILogicalOperator> operatorsForReunionPart = Lists.newArrayList();
		
		// The iterator of the operators for data reunion as the subscription goes the same way as 
		// the creation
		Iterator<ILogicalOperator> operatorsForDataReunionIter = operatorsForDataReunion.iterator();
		
		boolean finished = false;
		do {
			
			finished = true;
			
			for(ILogicalOperator operator : 
				fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().get(query)) {
			
				if(operatorsForReunionPart.contains(operator))
					continue;
				else if(Arrays.asList(FragmentationHelper.OPERATOR_CLASSES_DATAREUNION_PART).contains(
						operator.getClass())) {
					
					operatorsForReunionPart.add(operator);
					
					// Delete subscriptions
					for(LogicalSubscription subToSink : operator.getSubscriptions()) {
							
						for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
							
							subToSink.getTarget().subscribeToSource(
									subToSource.getTarget(), subToSink.getSinkInPort(), 
									subToSource.getSourceOutPort(), subToSink.getSchema());
							
						}
						operator.unsubscribeSink(subToSink);
						
					}
					operator.unsubscribeFromAllSources();
					
					finished = false;
						
				} else if(operator.getSubscriptions().isEmpty()) {
					
					// The operator for data reunion to be subscribed
					ILogicalOperator operatorForDataReunion = operatorsForDataReunionIter.next();
					operator.subscribeSink(operatorForDataReunion, planIndex, 0, 
							operator.getOutputSchema());
					
					finished = false;
				
				}
				
			}		
			
		} while(!finished);
		
		enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query).removeAll(operatorsForReunionPart);
		
		return enhancedFragmentPlan;
		
	}

}