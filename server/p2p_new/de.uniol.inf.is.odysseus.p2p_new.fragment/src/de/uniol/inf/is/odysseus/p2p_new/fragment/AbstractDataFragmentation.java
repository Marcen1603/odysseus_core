package de.uniol.inf.is.odysseus.p2p_new.fragment;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.distribution.IFragmentPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * An abstract implementation for fragmentation strategies.
 * @author Michael Brand
 */
public abstract class AbstractDataFragmentation implements IDataFragmentation {
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = Logger.getLogger(AbstractDataFragmentation.class);
	
	@Override
	public abstract String getName();
	
	@Override
	public IFragmentPlan fragment(IFragmentPlan fragmentPlan, int numFragments, int numReplicates, 
			QueryBuildConfiguration parameters, String sourceName) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkArgument(numFragments > 0);
		Preconditions.checkArgument(numReplicates > 0);
		Preconditions.checkNotNull(parameters);
		
		// Insert operator for fragmentation
		fragmentPlan = insertOperatorForFragmentation(fragmentPlan, numFragments, numReplicates, parameters, sourceName);
		
		for(ILogicalQuery query : fragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().keySet()) {
		
			if(fragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query).isEmpty())
				throw new QueryParseException("Can not distribute a query without any operators left after fragmentation");
			
		}
		
		// Insert operator for data reunion
		fragmentPlan = insertOperatorForDataReunion(fragmentPlan, numReplicates);
		
		return fragmentPlan;
		
	}
	
	/**
	 * Creates a new fragment plan for the current fragmentation.
	 */
	protected static IFragmentPlan createFragmentPlan(Map<ILogicalQuery, ILogicalOperator> logicalPlans) {
		
		Preconditions.checkNotNull(logicalPlans);
		
		// All operators per logical plan
		Map<ILogicalQuery, List<ILogicalOperator>> operatorsPerLogicalPlan = Maps.newHashMap();
		
		for(ILogicalQuery query : logicalPlans.keySet()) {
			
			// All operators of the logical Plan
			List<ILogicalOperator> operators = Lists.newArrayList();
			RestructHelper.collectOperators(logicalPlans.get(query), operators);
			RestructHelper.removeTopAOs(operators);
			operatorsPerLogicalPlan.put(query, operators);
			
		}
		
		return new StandardFragmentPlan(operatorsPerLogicalPlan);
		
	}
	
	/**
	 * Inserts operators for fragmentation, if a source was set by {@link #setSourceName(String)} and 
	 * deletes all replications of the source access. <br />
	 * So there is only one source access left. All fragments will be subscribed to that single source 
	 * access.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @param numFragments The number of fragments.
	 * @param numReplicates The number of replicates for each fragment.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @return The new status of the fragmentation.
	 */
	protected abstract IFragmentPlan insertOperatorForFragmentation(IFragmentPlan fragmentPlan, int numFragments, int numReplicates, 
			QueryBuildConfiguration parameters, String sourceName);
	
	/**
	 * Inserts operators for data reunion. All sinks of all fragments will be subscribed by the data 
	 * reunion operators.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @param numReplicates The number of replicates of each fragment.
	 * @return The new status of the fragmentation.
	 */
	protected IFragmentPlan insertOperatorForDataReunion(IFragmentPlan fragmentPlan, int numReplicates) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkArgument(numReplicates > 0);
		
		// The return value
		IFragmentPlan enhancedFragmentPlan = fragmentPlan.clone();
		
		// The operators for data reunion
		List<ILogicalOperator> operatorsForDataReunion = Lists.newArrayList();
		
		// Insert the operator for data reunion
		enhancedFragmentPlan = 
				insertOperatorForDataReunion(enhancedFragmentPlan, numReplicates, operatorsForDataReunion);
		
		// Subscribe all other logical plans
		boolean firstQuery = true;
		for(ILogicalQuery query : enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().keySet()) {
			
			if(firstQuery) {
				
				firstQuery = false;
				continue;
				
			}
			
			enhancedFragmentPlan = subscribeOperatorForDataReUnion(enhancedFragmentPlan, operatorsForDataReunion, query);
			
		}
		
		// Initialize the operators for data reunion
		for(ILogicalOperator operatorForDataReunion : enhancedFragmentPlan.getOperatorsOfReunionPart())
			operatorForDataReunion.initialize();
		
		return enhancedFragmentPlan;
		
	}
	
	/**
	 * Inserts operators for data reunion.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @param numReplicates The number of replicates for each fragment.
	 * @param operatorsForDataReunion A list of all created operators for data reunion for the query.
	 * @return The new status of the fragmentation.
	 */
	protected IFragmentPlan insertOperatorForDataReunion(IFragmentPlan fragmentPlan, int numReplicates, 
			List<ILogicalOperator> operatorsForDataReunion) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkArgument(numReplicates > 0);
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
		
		// Collect aggregation, if present
		IPair<IFragmentPlan, Optional<AggregateAO>> aggregation = replaceAggregation(enhancedFragmentPlan, query);
		enhancedFragmentPlan = aggregation.getE1();
		if(aggregation.getE2().isPresent())
			operatorsForDataReunionPartToPlaneMap.put(aggregation.getE2().get(), operatorsToPlaneMap.get(aggregation.getE2().get()));
		
		boolean finished = false;
		do {
			
			finished = true;
			
			for(ILogicalOperator operator : fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().get(query)) {
				
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
	 * It is also the input port of the inserted operator for data reunion.
	 * @return The new status of the fragmentation.
	 */
	protected IFragmentPlan subscribeOperatorForDataReUnion(IFragmentPlan fragmentPlan, 
			List<ILogicalOperator> operatorsForDataReunion, ILogicalQuery query) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkNotNull(operatorsForDataReunion);
		Preconditions.checkNotNull(query);
		
		// The return value
		IFragmentPlan enhancedFragmentPlan = fragmentPlan.clone();
		
		// Operators for the reunion part
		List<ILogicalOperator> operatorsForReunionPart = Lists.newArrayList();
		
		// The iterator of the operators for data reunion as the subscription goes the same way as 
		// the creation
		Iterator<ILogicalOperator> operatorsForDataReunionIter = operatorsForDataReunion.iterator();
		
		// Handle aggregation
		enhancedFragmentPlan = replaceAggregation(enhancedFragmentPlan, query).getE1();
		
		boolean finished = false;
		do {
			
			finished = true;
			
			for(ILogicalOperator operator : fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().get(query)) {
			
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
					operator.subscribeSink(operatorForDataReunion, operatorForDataReunion.getNumberOfInputs(), 0, 
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
	protected abstract ILogicalOperator createOperatorForDataReunion();
	
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
	
	/**
	 * Replaces one aggregation within a logical query. <br />
	 * The aggregation will be replaced by a partial aggregation.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @param A logical query of the current fragment plan.
	 * @return A pair of the changed fragment plan and the origin, replaced aggregation without subscriptions, 
	 * if there was exact one aggregation within <code>operators</code>.
	 */
	protected IPair<IFragmentPlan, Optional<AggregateAO>> replaceAggregation(IFragmentPlan fragmentPlan, ILogicalQuery query) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkNotNull(query);
		
		// The return value
		IPair<IFragmentPlan, Optional<AggregateAO>> returnPlan = null;
		Optional<AggregateAO> aggregation = Optional.absent();
		IFragmentPlan enhancedFragmentPlan = fragmentPlan.clone();
		
		// A new partial aggregation
		Optional<AggregateAO> partialAggr = Optional.absent();
		
		for(ILogicalOperator operator : fragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query)) {
			
			if(!(operator instanceof AggregateAO))
				continue;
			else if(aggregation.isPresent()) {
				
				LOG.error("A query with more than one aggregation can not be fragmented.");
				returnPlan = new Pair<IFragmentPlan, Optional<AggregateAO>>(enhancedFragmentPlan, aggregation);
				return returnPlan;
				
			} else {
				
				// The origin aggregation
				AggregateAO origin = (AggregateAO) operator;
				AggregateAO pa = origin.clone();
				for(SDFSchema inSchema : pa.getAggregations().keySet()) {
					
					for(AggregateFunction function : pa.getAggregations().get(inSchema).keySet()) {
						
						SDFAttribute oldOutAttr = pa.getAggregations().get(inSchema).get(function);
						SDFAttribute newOutAttr = null;
						
						if(function.getName().toUpperCase().equals("AVG") || function.getName().toUpperCase().equals("COUNT"))
							newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), SDFDatatype.AVG_SUM_PARTIAL_AGGREGATE);
						else if(function.getName().toUpperCase().equals("COUNT"))
							newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), SDFDatatype.COUNT_PARTIAL_AGGREGATE);
						else if(oldOutAttr.getDatatype().isListValue())
							newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), SDFDatatype.LIST_PARTIAL_AGGREGATE);
						else newOutAttr = new SDFAttribute(oldOutAttr.getSourceName(), oldOutAttr.getAttributeName(), SDFDatatype.RELATIONAL_ELEMENT_PARTIAL_AGGREGATE);
						
						pa.getAggregations().get(inSchema).put(function, newOutAttr);
						
					}
					
				}
				
				// Subscribe the partial one
				for(LogicalSubscription subToSink : origin.getSubscriptions()) {
					
					pa.subscribeSink(subToSink.getTarget(), subToSink.getSinkInPort(), 
							subToSink.getSourceOutPort(), pa.getOutputSchema());
					origin.unsubscribeSink(subToSink);
					
				}
				for(LogicalSubscription subToSource : origin.getSubscribedToSource()) {
					
					pa.subscribeToSource(subToSource.getTarget(), subToSource.getSinkInPort(), 
							subToSource.getSourceOutPort(), subToSource.getSchema());
					origin.unsubscribeFromSource(subToSource);
					
				}
				
				aggregation = Optional.of(origin);
				partialAggr = Optional.of(pa);
				
			}
			
		}
		
		if(aggregation.isPresent()) {
			
			enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query).remove(aggregation.get());
			enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query).add(partialAggr.get());
			
		}
		
		return new Pair<IFragmentPlan, Optional<AggregateAO>>(enhancedFragmentPlan, aggregation);
		
	}

}