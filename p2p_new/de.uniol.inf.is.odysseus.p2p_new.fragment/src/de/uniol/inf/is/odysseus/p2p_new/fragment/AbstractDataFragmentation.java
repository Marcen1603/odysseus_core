package de.uniol.inf.is.odysseus.p2p_new.fragment;

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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.distribution.IFragmentPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
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
	public IFragmentPlan fragment(Map<ILogicalQuery,ILogicalOperator> logicalPlans, 
			QueryBuildConfiguration parameters, String sourceName) {
		
		// Preconditions
		Preconditions.checkNotNull(logicalPlans);
		Preconditions.checkArgument(logicalPlans.size() > 1);
		Preconditions.checkNotNull(parameters);
		
		// The return value
		IFragmentPlan fragmentPlan = createFragmentPlan(logicalPlans);
		
		// Insert operator for fragmentation
		fragmentPlan = insertOperatorForFragmentation(fragmentPlan, parameters, sourceName);
		
		// Insert operator for data reunion
		fragmentPlan = insertOperatorForDataReunion(fragmentPlan);
		
		return fragmentPlan;
		
	}
	
	/**
	 * Creates a new fragment plan for the current fragmentation.
	 */
	protected IFragmentPlan createFragmentPlan(Map<ILogicalQuery, ILogicalOperator> logicalPlans) {
		
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
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @return The new status of the fragmentation.
	 */
	protected abstract IFragmentPlan insertOperatorForFragmentation(IFragmentPlan fragmentPlan, 
			QueryBuildConfiguration parameters, String sourceName);
	
	/**
	 * Inserts operators for data reunion. All sinks of all fragments will be subscribed by the data 
	 * reunion operators.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @return The new status of the fragmentation.
	 */
	protected abstract IFragmentPlan insertOperatorForDataReunion(IFragmentPlan fragmentPlan);
	
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
		
		// FIXME Not working yet
		
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
				
				// A new partial aggregation
				AggregateAO pa = new AggregateAO();
				pa.setOutputPA(true);
				
				// Change origin aggegration to be used as partial aggegration
				Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations = origin.getAggregations();
				for(SDFSchema attributes : aggregations.keySet()) {
					
					for(AggregateFunction function : aggregations.get(attributes).keySet()) {
						
						SDFAttribute outAttr = aggregations.get(attributes).get(function);
//						pa.addAggregation(attributes, function, new SDFAttribute(outAttr.getSourceName(), outAttr.getAttributeName(), 
//								SDFDatatype.RELATIONAL_ELEMENT_PARTIAL_AGGREGATE));
						pa.addAggregation(attributes, function, outAttr);
						
					}
					
				}
				for(SDFAttribute groupBy : origin.getGroupingAttributes())
					pa.addGroupingAttribute(groupBy);
				
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