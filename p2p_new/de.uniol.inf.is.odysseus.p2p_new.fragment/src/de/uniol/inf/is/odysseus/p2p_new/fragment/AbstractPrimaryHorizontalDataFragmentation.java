package de.uniol.inf.is.odysseus.p2p_new.fragment;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * An abstract implementation for primary horizontal fragmentation strategies.
 * @author Michael Brand
 */
public abstract class AbstractPrimaryHorizontalDataFragmentation extends AbstractDataFragmentation {
	
	@Override
	public abstract String getName();

	@Override
	protected List<List<ILogicalOperator>> insertOperatorForFragmentation(
			List<ILogicalOperator> logicalPlans, QueryBuildConfiguration parameters, 
			String sourceName, Collection<ILogicalOperator> operatorsChangedDueToFragmentation, 
			Collection<ILogicalOperator> operatorsDeleted) {
		
		// Preconditions
		Preconditions.checkNotNull(logicalPlans);
		Preconditions.checkArgument(logicalPlans.size() > 1);
		Preconditions.checkNotNull(parameters);
		Preconditions.checkNotNull(sourceName);
		Preconditions.checkNotNull(operatorsChangedDueToFragmentation);
		Preconditions.checkNotNull(operatorsChangedDueToFragmentation.isEmpty());
		Preconditions.checkNotNull(operatorsDeleted);
		
		// The return value
		List<List<ILogicalOperator>> operatorsPerLogicalPlan = Lists.newArrayList();
		
		// The operators for fragmentation
		List<ILogicalOperator> operatorsForFragmentation = Lists.newArrayList();
		
		// Insert the operator for fragmentation
		List<ILogicalOperator> operators = Lists.newArrayList();
		RestructHelper.collectOperators(logicalPlans.get(0), operators);
		RestructHelper.removeTopAOs(operators);
		insertOperatorForFragmentation(operators, operatorsForFragmentation, parameters, sourceName, 
				logicalPlans.size(), operatorsChangedDueToFragmentation);
		operatorsPerLogicalPlan.add(operators);
		
		// Subscribe all other logical plans
		for(int planIndex = 1; planIndex < logicalPlans.size(); planIndex++) {
			
			operators = Lists.newArrayList();
			RestructHelper.collectOperators(logicalPlans.get(planIndex), operators);
			RestructHelper.removeTopAOs(operators);
			subscribeOperatorForFragmentation(operators, operatorsForFragmentation, sourceName, 
					planIndex, operatorsDeleted);
			operatorsPerLogicalPlan.add(operators);
			
		}
		
		// Initialize the operators for fragmentation
		for(ILogicalOperator operatorForFragmentation : operatorsForFragmentation)
			operatorForFragmentation.initialize();
		
		return operatorsPerLogicalPlan;
		
	}
	
	/**
	 * Inserts operators for fragmentation, if a source was set by {@link #setSourceName(String)}.
	 * @param operators A list of all operators within a logical plan.
	 * @param operatorsForFragmentation A list of all created operators for fragmentation for the query.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @param numFragments The number of fragments.
	 * @param operatorsChangedDueToFragmentation A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are added or modified in any way due to 
	 * fragmentation. A modification means e.g. a related StreamAO, which copies were deleted.
	 */
	protected void insertOperatorForFragmentation(List<ILogicalOperator> operators, 
			List<ILogicalOperator> operatorsForFragmentation, 
			QueryBuildConfiguration parameters, String sourceName, int numFragments, 
			Collection<ILogicalOperator> operatorsChangedDueToFragmentation) {
		
		// Preconditions
		Preconditions.checkNotNull(operators);
		Preconditions.checkArgument(!operators.isEmpty());
		Preconditions.checkNotNull(operatorsForFragmentation);
		Preconditions.checkNotNull(parameters);
		Preconditions.checkArgument(numFragments > 1);
		Preconditions.checkNotNull(sourceName);
		Preconditions.checkNotNull(operatorsChangedDueToFragmentation);
		Preconditions.checkNotNull(operatorsChangedDueToFragmentation.isEmpty());
		
		for(ILogicalOperator operator : operators) {
			
			// Only StreamAOs or following WindowAOs will be processed
			
			if(operator instanceof StreamAO && 
					((StreamAO) operator).getStreamname().getResourceName().equals(sourceName)) {
				
				operatorsChangedDueToFragmentation.add(operator);
				
				// The operator for fragmentation to be inserted.
				// All sinks of the StreamAO will be subscribed to that new operator.
				// For following WindowAOs other operators for fragmentation will be inserted additional.
				ILogicalOperator operatorForFragmentation = 
						createOperatorForFragmentation(numFragments, parameters);
				
				// True, if the StreamAO is only subscribed by WindowAOs
				boolean onlySubscribedByWindows = true;
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					if(subToSink.getTarget() instanceof AbstractWindowAO)
						continue;
					
					onlySubscribedByWindows = false;
					operator.unsubscribeSink(subToSink);
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, 0, 
							subToSink.getSchema());
					
				}
				
				if(!onlySubscribedByWindows) {
					
					operator.subscribeSink(operatorForFragmentation, 0, 0, operator.getOutputSchema());
					operatorsForFragmentation.add(operatorForFragmentation);
					
				}
				
			} else if(operator instanceof AbstractWindowAO) {
				
				// True, if the WindowAO is for the source to be fragmented
				boolean windowOfSourceToBeFragmented = false;
				
				// Check, if the WindowAO is for the source to be fragmented
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					if(subToSource.getTarget() instanceof StreamAO && 
							((StreamAO) subToSource.getTarget()).getStreamname().getResourceName().
							equals(sourceName)) {
						
						windowOfSourceToBeFragmented = true;
						break;
						
					}
					
				}
				if(!windowOfSourceToBeFragmented)
					continue;
				
				operatorsChangedDueToFragmentation.add(operator);
				
				// The operator for fragmentation to be subscribed.
				// All sinks of the WindowAO will be subscribed to that new operator.
				ILogicalOperator operatorForFragmentation = 
						createOperatorForFragmentation(numFragments, parameters);
				operator.subscribeSink(operatorForFragmentation, 0, 0, operator.getOutputSchema());
				operatorsForFragmentation.add(operatorForFragmentation);
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					operator.unsubscribeSink(subToSink);
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, 0, 
							subToSink.getSchema());
					
				}
				
			}
			
		}
		
		operators.addAll(operatorsForFragmentation);
		operatorsChangedDueToFragmentation.addAll(operatorsForFragmentation);
		
	}

	/**
	 * Subscribes operators for fragmentation, if a source was set by {@link #setSourceName(String)} 
	 * and deletes the source access of the current logical plan represented by <code>operators</code>.
	 * @param operators A list of all operators within a logical plan.
	 * @param operatorsForFragmentation A list of all created operators for fragmentation for the 
	 * query.
	 * @param sourceName The name of the source to be fragmented.
	 * @param planIndex The index of the logical plan to process. <br />
	 * It is also the output port of the inserted operator for fragmentation.
	 * @param operatorsDeleted A mutable, empty collection, 
	 * which will be filled by this method. <br /> 
	 * This collection contains all operators which are deleted due to fragmentation or data reunion.
	 */
	protected void subscribeOperatorForFragmentation(List<ILogicalOperator> operators, 
			List<ILogicalOperator> operatorsForFragmentation, String sourceName, int planIndex, 
			Collection<ILogicalOperator> operatorsDeleted) {
		
		// Preconditions
		Preconditions.checkNotNull(operators);
		Preconditions.checkArgument(!operators.isEmpty());
		Preconditions.checkNotNull(operatorsForFragmentation);
		Preconditions.checkArgument(planIndex >= 0);
		Preconditions.checkNotNull(sourceName);
		Preconditions.checkNotNull(operatorsDeleted);
		
		// The iterator of the operators for fragmentation as the subscription goes the same way as 
		// the creation
		Iterator<ILogicalOperator> operatorsForFragmentationIter = operatorsForFragmentation.iterator();
		
		for(ILogicalOperator operator : operators) {
			
			// Only StreamAOs or following WindowAOs will be processed
			
			if(operator instanceof StreamAO && 
					((StreamAO) operator).getStreamname().getResourceName().equals(sourceName)) {
				
				// The operator for fragmentation to be subscribed.
				// All sinks of the StreamAO will be subscribed to that new operator.
				// For following WindowAOs other operators for fragmentation will be inserted 
				// additional.
				ILogicalOperator operatorForFragmentation = operatorsForFragmentationIter.next();
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					if(subToSink.getTarget() instanceof AbstractWindowAO)
						continue;
										
					operator.unsubscribeSink(subToSink);
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, planIndex, 
							subToSink.getSchema());
					
				}
				
				operatorsDeleted.add(operator);
				
			} else if(operator instanceof AbstractWindowAO) {
				
				// True, if the WindowAO is for the source to be fragmented
				boolean windowOfSourceToBeFragmented = false;
				
				// Check, if the WindowAO is for the source to be fragmented
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					if(subToSource.getTarget() instanceof StreamAO && 
							((StreamAO) subToSource.getTarget()).getStreamname().getResourceName().
							equals(sourceName)) {
						
						windowOfSourceToBeFragmented = true;
						break;
						
					}
					
				}
				if(!windowOfSourceToBeFragmented)
					continue;
				
				// The operator for fragmentation to be subscribed.
				// All sinks of the WindowAO will be subscribed to that new operator.
				ILogicalOperator operatorForFragmentation = operatorsForFragmentationIter.next();
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					operator.unsubscribeSink(subToSink);
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, planIndex, 
							subToSink.getSchema());
					
				}
				
				operatorsDeleted.add(operator);
				
			}
			
		}
		
		// Remove the replicated source access operators
		operators.removeAll(operatorsDeleted);
		
	}

	/**
	 * Creates a new operator for fragmentation.
	 * @param numFragments The number of fragments.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 */
	protected abstract ILogicalOperator createOperatorForFragmentation(int numFragments, 
			QueryBuildConfiguration parameters);

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
					ILogicalOperator operatorForDataReunion = createOperatorForDataReunion();
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
	
	/**
	 * Creates a new operator for data reunion.
	 */
	protected ILogicalOperator createOperatorForDataReunion() {
		
		return new UnionAO();
		
	}
	
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