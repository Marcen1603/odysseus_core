package de.uniol.inf.is.odysseus.p2p_new.fragment;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
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
	protected List<List<ILogicalOperator>> insertOperatorForFragmentation(
			List<ILogicalOperator> logicalPlans, QueryBuildConfiguration parameters, String sourceName, 
			Collection<ILogicalOperator> operatorsChangedDueToFragmentation, 
			Collection<ILogicalOperator> operatorsDeleted) {
		
		// The return value
		List<List<ILogicalOperator>> operatorsPerLogicalPlan = Lists.newArrayList();
		
		for(ILogicalOperator logicalPlan : logicalPlans) {
		
			List<ILogicalOperator> operators = Lists.newArrayList();
			RestructHelper.collectOperators(logicalPlan, operators);
			RestructHelper.removeTopAOs(operators);
			operatorsPerLogicalPlan.add(operators);
			
		}
		
		return operatorsPerLogicalPlan;
		
		
	}

	@Override
	protected ILogicalOperator insertOperatorForDataReunion(
			List<List<ILogicalOperator>> operatorsPerLogicalPlan, 
			Collection<ILogicalOperator> operatorsChangedDueToDataReunion, 
			Collection<ILogicalOperator> operatorsDeleted) {
		
		// Preconditions
		Preconditions.checkNotNull(operatorsPerLogicalPlan);
		Preconditions.checkArgument(operatorsPerLogicalPlan.size() > 1);
		Preconditions.checkNotNull(operatorsChangedDueToDataReunion);
		Preconditions.checkNotNull(operatorsChangedDueToDataReunion.isEmpty());
		Preconditions.checkNotNull(operatorsDeleted);
		
		// The operators for data reunion
		List<ILogicalOperator> operatorsForDataReunion = Lists.newArrayList();
		
		// Insert the operator for data reunion
		insertOperatorForDataReunion(operatorsPerLogicalPlan.get(0), operatorsForDataReunion, 
				operatorsChangedDueToDataReunion);
		
		// Subscribe all other logical plans
		for(int planIndex = 1; planIndex < operatorsPerLogicalPlan.size(); planIndex++)
			subscribeOperatorForDataReUnion(operatorsPerLogicalPlan.get(planIndex), 
					operatorsForDataReunion, planIndex, operatorsDeleted);
		
		// Initialize the operators for data reunion
		for(ILogicalOperator operatorForDataReunion : operatorsForDataReunion)
			operatorForDataReunion.initialize();
		
		return operatorsForDataReunion.iterator().next();
		
	}
	
	/**
	 * Inserts operators for data reunion.
	 * @param operators A list of all operators within a logical plan.
	 * @param operatorsForDataReunion A list of all created operators for data reunion for the query.
	 * @param operatorsChangedDueToDataReunion A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are added or modified in any way due to 
	 * data reunion. A modification means e.g. a related FileSinkAO, which has been moved within 
	 * the logical plan and which copies were deleted.
	 */
	// FIXME Seems to be a big workaround. M.B.
	protected void insertOperatorForDataReunion(List<ILogicalOperator> operators, 
			List<ILogicalOperator> operatorsForDataReunion, 
			Collection<ILogicalOperator> operatorsChangedDueToDataReunion) {
		
		// Preconditions
		Preconditions.checkNotNull(operators);
		Preconditions.checkArgument(!operators.isEmpty());
		Preconditions.checkNotNull(operatorsForDataReunion);
		Preconditions.checkNotNull(operatorsChangedDueToDataReunion);
		Preconditions.checkNotNull(operatorsChangedDueToDataReunion.isEmpty());
		
		// A mapping of all operators to their plane within the logical plan
		Map<ILogicalOperator, Integer> operatorsToPlaneMap = 
				RestructHelper.assignOperatorPlanes(operators.iterator().next());
		
		// A mapping of all operators which shall be moved to the data reunion part 
		// to their plane within the logical plan
		Map<ILogicalOperator, Integer> operatorsForDataReunionPartToPlaneMap = Maps.newHashMap();
		
		boolean finished = false;
		do {
			
			finished = true;
			
			for(ILogicalOperator operator : operators) {
				
				if(operatorsForDataReunionPartToPlaneMap.containsKey(operator))
					continue;
				else if(Arrays.asList(FragmentationHelper.OPERATOR_CLASSES_DATAREUNION_PART).contains(
						operator.getClass())) {
					
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
						
					// Collect operators to be moved to the data reunion part
					operatorsForDataReunionPartToPlaneMap.put(
							operator, operatorsToPlaneMap.get(operator));
					
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
			operatorsChangedDueToDataReunion.add(operator);
			
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
		
		operatorsChangedDueToDataReunion.addAll(operatorsForDataReunion);
		
	}
	
	/**
	 * Subscribes operators for data reunion.
	 * @param operators A list of all operators within a logical plan.
	 * @param operatorsForDataReunion A list of all created operators for data reunion for the query.
	 * @param planIndex The index of the logical plan to process. <br />
	 * It is also the input port of the inserted operator for data reunion.
	 * @param operatorsDeleted A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are deleted due to fragmentation or data reunion.
	 */
	// FIXME Seems to be a big workaround. M.B.
	protected void subscribeOperatorForDataReUnion(List<ILogicalOperator> operators, 
			List<ILogicalOperator> operatorsForDataReunion, int planIndex, 
			Collection<ILogicalOperator> operatorsDeleted) {
		
		// Preconditions
		Preconditions.checkNotNull(operators);
		Preconditions.checkArgument(!operators.isEmpty());
		Preconditions.checkNotNull(operatorsForDataReunion);
		Preconditions.checkArgument(planIndex >= 0);
		Preconditions.checkNotNull(operatorsDeleted);
		
		// A list of operators which shall be moved to the data reunion part
		List<ILogicalOperator> operatorsForDataReunionPart = Lists.newArrayList();
		
		// The iterator of the operators for data reunion as the subscription goes the same way as 
		// the creation
		Iterator<ILogicalOperator> operatorsForDataReunionIter = operatorsForDataReunion.iterator();
		
		boolean finished = false;
		do {
			
			finished = true;
			
			for(ILogicalOperator operator : operators) {
			
				if(operatorsForDataReunionPart.contains(operator))
					continue;
				else if(Arrays.asList(FragmentationHelper.OPERATOR_CLASSES_DATAREUNION_PART).contains(
						operator.getClass())) {
					
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
						
					// Collect operators to be moved to the data reunion part
					operatorsForDataReunionPart.add(operator);
					
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
		
		// Remove the operators for the data reunion part
		operatorsDeleted.addAll(operatorsForDataReunionPart);
		
	}

}