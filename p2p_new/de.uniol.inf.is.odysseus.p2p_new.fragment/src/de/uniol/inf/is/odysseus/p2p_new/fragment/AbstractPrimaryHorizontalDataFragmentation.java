package de.uniol.inf.is.odysseus.p2p_new.fragment;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IFragmentPlan;
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
	protected IFragmentPlan insertOperatorForFragmentation(IFragmentPlan fragmentPlan, 
			QueryBuildConfiguration parameters, String sourceName) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkNotNull(parameters);
		Preconditions.checkNotNull(sourceName);
		
		// The return value
		IFragmentPlan enhancedFragmentPlan = fragmentPlan.clone();
		
		// The operators for fragmentation
		List<ILogicalOperator> operatorsForFragmentation = Lists.newArrayList();
		
		// Insert the operator for fragmentation
		enhancedFragmentPlan = 
				insertOperatorForFragmentation(enhancedFragmentPlan, parameters, sourceName, operatorsForFragmentation);
		
		// Subscribe all other logical plans
		int planIndex = 0;
		for(ILogicalQuery query : enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().keySet()) {
			
			if(planIndex == 0) {
				
				planIndex++;
				continue;
				
			}
			
			enhancedFragmentPlan = 
					subscribeOperatorForFragmentation(enhancedFragmentPlan, sourceName, query, planIndex, operatorsForFragmentation);
			planIndex++;
			
		}
		
		// Initialize the operators for fragmentation
		for(ILogicalOperator operatorForFragmentation : 
			enhancedFragmentPlan.getOperatorsOfFragmentationPart()) {
			
			operatorForFragmentation.initialize();
			
		}
		
		return enhancedFragmentPlan;
		
	}
	
	/**
	 * Inserts operators for fragmentation, if a source was set by {@link #setSourceName(String)}.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @param operatorsForFragmentation A list of all created operators for fragmentation for the query.
	 * @return The new status of the fragmentation.
	 */
	protected IFragmentPlan insertOperatorForFragmentation(IFragmentPlan fragmentPlan, 
			QueryBuildConfiguration parameters, String sourceName, 
			List<ILogicalOperator> operatorsForFragmentation) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkNotNull(parameters);
		Preconditions.checkNotNull(sourceName);
		Preconditions.checkNotNull(operatorsForFragmentation);
		
		// The return value
		IFragmentPlan enhancedFragmentPlan = fragmentPlan.clone();
		
		// The query to be used for insertion
		ILogicalQuery query = fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().keySet().iterator().next();
		
		// Operators for the fragmentation part
		List<ILogicalOperator> operatorsForFragmentationPart = Lists.newArrayList();
		
		for(ILogicalOperator operator : 
			fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().get(query)) {
			
			// Only StreamAOs or following WindowAOs will be processed
			
			if(operator instanceof StreamAO && 
					((StreamAO) operator).getStreamname().getResourceName().equals(sourceName)) {
				
				operatorsForFragmentationPart.add(operator);
				
				// The operator for fragmentation to be inserted.
				// All sinks of the StreamAO will be subscribed to that new operator.
				// For following WindowAOs other operators for fragmentation will be inserted additional.
				ILogicalOperator operatorForFragmentation = 
						createOperatorForFragmentation(enhancedFragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().size(), parameters);
				
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
				
				operatorsForFragmentationPart.add(operator);
				
				// The operator for fragmentation to be subscribed.
				// All sinks of the WindowAO will be subscribed to that new operator.
				ILogicalOperator operatorForFragmentation = 
						createOperatorForFragmentation(enhancedFragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().keySet().size(), parameters);
				operator.subscribeSink(operatorForFragmentation, 0, 0, operator.getOutputSchema());
				operatorsForFragmentation.add(operatorForFragmentation);
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					operator.unsubscribeSink(subToSink);
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, 0, 
							subToSink.getSchema());
					
				}
				
			}
			
		}
		
		enhancedFragmentPlan.getOperatorsOfFragmentationPart().addAll(operatorsForFragmentationPart);
		enhancedFragmentPlan.getOperatorsOfFragmentationPart().addAll(operatorsForFragmentation);
		enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query).removeAll(operatorsForFragmentationPart);
		
		return enhancedFragmentPlan;
		
	}

	/**
	 * Subscribes operators for fragmentation, if a source was set by {@link #setSourceName(String)} 
	 * and deletes the source access of the current logical plan represented by <code>operators</code>.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @param sourceName The name of the source to be fragmented.
	 * @param query The query to process.
	 * @param planIndex The index of the logical plan to process. <br />
	 * It is also the output port of the inserted operator for fragmentation.
	 * @param operatorsForFragmentation A list of all created operators for fragmentation for the query.
	 * @return The new status of the fragmentation.
	 */
	protected IFragmentPlan subscribeOperatorForFragmentation(IFragmentPlan fragmentPlan, 
			String sourceName, ILogicalQuery query, int planIndex, List<ILogicalOperator> operatorsForFragmentation) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkNotNull(sourceName);
		Preconditions.checkNotNull(query);
		Preconditions.checkArgument(planIndex >= 0);
		Preconditions.checkArgument(
				planIndex < fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().keySet().size());
		Preconditions.checkNotNull(operatorsForFragmentation);
		
		// The return value
		IFragmentPlan enhancedFragmentPlan = fragmentPlan.clone();
		
		// Operators for the fragmentation part
		List<ILogicalOperator> operatorsForFragmentationPart = Lists.newArrayList();
		
		// The iterator of the operators for fragmentation as the subscription goes the same way as 
		// the creation
		Iterator<ILogicalOperator> operatorsForFragmentationIter = operatorsForFragmentation.iterator();
		
		for(ILogicalOperator operator : 
			fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().get(query)) {
			
			// Only StreamAOs or following WindowAOs will be processed
			
			if(operator instanceof StreamAO && 
					((StreamAO) operator).getStreamname().getResourceName().equals(sourceName)) {
				
				operatorsForFragmentationPart.add(operator);
				
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
				
				operatorsForFragmentationPart.add(operator);
				
				// The operator for fragmentation to be subscribed.
				// All sinks of the WindowAO will be subscribed to that new operator.
				ILogicalOperator operatorForFragmentation = operatorsForFragmentationIter.next();
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					operator.unsubscribeSink(subToSink);
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, planIndex, 
							subToSink.getSchema());
					
				}
				
			}
			
		}
		
		enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query).removeAll(operatorsForFragmentationPart);
		
		return enhancedFragmentPlan;
		
	}

	/**
	 * Creates a new operator for fragmentation.
	 * @param numFragments The number of fragments.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 */
	protected abstract ILogicalOperator createOperatorForFragmentation(int numFragments, 
			QueryBuildConfiguration parameters);

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
		
		// TODO
		// Collect aggregation, if present
//		Optional<AggregateAO> aggregation = replaceAggregation(operators);
//		if(aggregation.isPresent())
//			operatorsForDataReunionPartToPlaneMap.put(aggregation.get(), operatorsToPlaneMap.get(aggregation.get()));
		
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
		
		// TODO
		// Handle aggregation
//		replaceAggregation(operators);
		
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