package de.uniol.inf.is.odysseus.p2p_new.fragment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IFragmentPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.fragment.logicaloperator.ReplicationMergeAO;

/**
 * An abstract implementation for primary horizontal fragmentation strategies.
 * @author Michael Brand
 */
public abstract class AbstractPrimaryHorizontalDataFragmentation extends AbstractDataFragmentation {
	
	@Override
	public abstract String getName();
	
	@Override
	public IFragmentPlan fragment(IFragmentPlan fragmentPlan, int numFragments, int numReplicates, 
			QueryBuildConfiguration parameters, String sourceName) {
		
		// Preconditions
		Preconditions.checkArgument(numFragments > 1);
		Preconditions.checkArgument(numReplicates > 0);
		
		if(numReplicates > 1) {
			
			// Replication of each fragment
			
			// The fragment plans for each fragment before replication
			Collection<IFragmentPlan> subFragmentPlansBeforeReplication = 
					AbstractPrimaryHorizontalDataFragmentation.splitFragmentPlanPerFragment(fragmentPlan, numReplicates, numFragments);
			
			// The fragment plans for each fragment after replication
			Collection<IFragmentPlan> subFragmentPlansAfterReplication = Lists.newArrayList();
			for(IFragmentPlan subFragmentPlan : subFragmentPlansBeforeReplication)
				subFragmentPlansAfterReplication.add(new Replication().fragment(subFragmentPlan, 1, numReplicates, parameters, null));
			
			fragmentPlan = AbstractPrimaryHorizontalDataFragmentation.mergeFragmentPlans(subFragmentPlansAfterReplication);
			
		}
		
		return super.fragment(fragmentPlan, numFragments, numReplicates, parameters, sourceName);
		
	}
	
	/**
	 * Splits an {@link IFragmentPlan} into several smaller {@link IFragmentPlan}s. <br />
	 * NOTE: Neither splitting of fragmentation part nor splitting of reunion part
	 * @param fragmentPlan The {@link IFragmentPlan} to be splitted.
	 * @param numReplicates The number of replicates define the number of {@link ILogicalQuery}s per new {@link IFragmentPlan}.
	 * @param numFragments The number of fragments define the number of new {@link IFragmentPlan}s.
	 * @return <code>numFragments</code> new {@link IFragmentPlan}s with <code>numReplicates</code> {@link ILogicalQuery}s, 
	 * but without any part of fragmentation or reunion.
	 */
	private static Collection<IFragmentPlan> splitFragmentPlanPerFragment(IFragmentPlan fragmentPlan, int numReplicates, int numFragments) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkArgument(numReplicates > 1);
		Preconditions.checkArgument(numFragments > 1);
		
		// The return value
		List<IFragmentPlan> fragmentPlans = Lists.newArrayList();
		
		// The operators before fragmentation for each new fragment plan
		List<Map<ILogicalQuery, List<ILogicalOperator>>> operatorsBeforeFragmentation = Lists.newArrayList();
		for(int i = 0; i < numFragments; i++)
			operatorsBeforeFragmentation.add(new HashMap<ILogicalQuery, List<ILogicalOperator>>());
		
		// The operators after fragmentation for each new fragment plan.
		List<Map<ILogicalQuery, List<ILogicalOperator>>> operatorsAfterFragmentation = Lists.newArrayList();
		for(int i = 0; i < numFragments; i++)
			operatorsAfterFragmentation.add(new HashMap<ILogicalQuery, List<ILogicalOperator>>());
		
		// Counter for the queries and the new fragment plans
		int replicationNo = 0;
		int fragmentNo = 0;
		
		for(ILogicalQuery query : fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().keySet()) {
			
			operatorsBeforeFragmentation.get(fragmentNo).put(query, fragmentPlan.getOperatorsPerLogicalPlanBeforeFragmentation().get(query));
			operatorsAfterFragmentation.get(fragmentNo).put(query, fragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().get(query));
			
			if(replicationNo == numReplicates - 1) {
				
				replicationNo = 0;
				fragmentNo++;
				
			} else replicationNo++;
			
		}
		
		for(int planNo = 0; planNo < numFragments; planNo++) {
			
			IFragmentPlan plan = new StandardFragmentPlan(operatorsBeforeFragmentation.get(planNo));
			plan.getOperatorsPerLogicalPlanAfterFragmentation().clear();
			plan.getOperatorsPerLogicalPlanAfterFragmentation().putAll(operatorsAfterFragmentation.get(planNo));			
			fragmentPlans.add(plan);
			
		}
		
		return fragmentPlans;
		
	}
	
	/**
	 * Merges several {@link IFragmentPlan}s to a single {@link IFragmentPlan}.
	 * @param fragmentPlans A collection of {@link IFragmentPlan}s to be merged.
	 */
	private static IFragmentPlan mergeFragmentPlans(Collection<IFragmentPlan> fragmentPlans) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlans);
		
		// The return value
		IFragmentPlan fragmentPlan = null;
		
		for(IFragmentPlan plan : fragmentPlans) {
			
			if(fragmentPlan == null)
				fragmentPlan = new StandardFragmentPlan(plan.getOperatorsPerLogicalPlanBeforeFragmentation());
			else {
				
				for(ILogicalQuery query : plan.getOperatorsPerLogicalPlanAfterFragmentation().keySet()) {
					
					fragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().clear();
					fragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().put(query, 
							plan.getOperatorsPerLogicalPlanAfterFragmentation().get(query));
					
				}
				
			}
			
			fragmentPlan.getOperatorsOfFragmentationPart().addAll(plan.getOperatorsOfFragmentationPart());
			fragmentPlan.getOperatorsOfReunionPart().addAll(plan.getOperatorsOfReunionPart());
			
		}
		
		return fragmentPlan;
		
	}

	@Override
	protected IFragmentPlan insertOperatorForFragmentation(IFragmentPlan fragmentPlan, int numFragments, int numReplicates, 
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
				insertOperatorForFragmentation(enhancedFragmentPlan, numFragments, parameters, sourceName, operatorsForFragmentation);
		
		// Subscribe all other logical plans
		boolean firstQuery = true;
		int fragmentNo = 0;
		for(ILogicalQuery query : enhancedFragmentPlan.getOperatorsPerLogicalPlanAfterFragmentation().keySet()) {
			
			if(firstQuery) {
				
				firstQuery = false;
				fragmentNo++;
				continue;
				
			}
			
			if(fragmentNo == numReplicates)
				fragmentNo = 0;
			
			enhancedFragmentPlan = 
					subscribeOperatorForFragmentation(enhancedFragmentPlan, sourceName, query, fragmentNo, operatorsForFragmentation);

			fragmentNo++;
			
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
	 * @param numFragments The number of fragments.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @param operatorsForFragmentation A list of all created operators for fragmentation for the query.
	 * @return The new status of the fragmentation.
	 */
	protected IFragmentPlan insertOperatorForFragmentation(IFragmentPlan fragmentPlan, int numFragments, 
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
				
				// Look up, if there is another StreamAO for that source collected before.
				// Keep only one StreamAO.
				
				Optional<ILogicalOperator> streamAO = 
						AbstractPrimaryHorizontalDataFragmentation.searchStreamAO(operatorsForFragmentationPart, sourceName);
				ILogicalOperator operatorForFragmentation = null;
				
				if(!streamAO.isPresent()) {
				
					operatorsForFragmentationPart.add(operator);
				
					// The operator for fragmentation to be inserted.
					// All sinks of the StreamAO will be subscribed to that new operator.
					// For following WindowAOs other operators for fragmentation will be inserted additional.
					operatorForFragmentation = createOperatorForFragmentation(numFragments, parameters);
					
				}
				
				// True, if the StreamAO is only subscribed by WindowAOs
				boolean onlySubscribedByWindows = true;
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					if(subToSink.getTarget() instanceof AbstractWindowAO)
						continue;
					
					onlySubscribedByWindows = false;
					operator.unsubscribeSink(subToSink);
					
					if(!streamAO.isPresent()) {
						
						operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, 0, 
								subToSink.getSchema());
						
					} else streamAO.get().subscribeSink(subToSink.getTarget(), 0, 0, 
							subToSink.getSchema());
					
				}
				
				if(!onlySubscribedByWindows) {
					
					if(!streamAO.isPresent()) {
						
						operator.subscribeSink(operatorForFragmentation, 0, 0, operator.getOutputSchema());
						operatorsForFragmentation.add(operatorForFragmentation);
						
					} else operator.subscribeSink(streamAO.get(), 0, 0, operator.getOutputSchema());
					
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
				
				// Look up, if there is another WindowAO for that source collected before.
				// Keep only one WindowAO.
				
				Optional<ILogicalOperator> windowAO = 
						AbstractPrimaryHorizontalDataFragmentation.searchWindowAO(operatorsForFragmentationPart, sourceName);
				ILogicalOperator operatorForFragmentation = null;
				
				if(!windowAO.isPresent()) {
					
					operatorsForFragmentationPart.add(operator);
					
					// The operator for fragmentation to be subscribed.
					// All sinks of the WindowAO will be subscribed to that new operator.
					operatorForFragmentation = createOperatorForFragmentation(numFragments, parameters);
					operator.subscribeSink(operatorForFragmentation, 0, 0, operator.getOutputSchema());
					operatorsForFragmentation.add(operatorForFragmentation);
					
				} else operator.subscribeSink(windowAO.get(), 0, 0, operator.getOutputSchema());
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					operator.unsubscribeSink(subToSink);
					
					if(!windowAO.isPresent()) {
						
						operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, 0, 
								subToSink.getSchema());
						
					} else windowAO.get().subscribeSink(subToSink.getTarget(), 0, 0, 
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
	 * Searches a {@link WindowAO} for a given source within a given collection of {@link ILogicalOperator}s.
	 * @param operatorsForFragmentationPart A collection of {@link ILogicalOperator}s.
	 * @param sourceName The name of a source.
	 * @return The {@link WindowAO} for <code>sourceName</code>, if <code>operatorsForFragmentationPart</code> contains one.
	 */
	private static Optional<ILogicalOperator> searchWindowAO(List<ILogicalOperator> operatorsForFragmentationPart, String sourceName) {
		
		// The return value
		Optional<ILogicalOperator> windowAO = Optional.absent();
		
		for(ILogicalOperator operator : operatorsForFragmentationPart) {
			
			for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
				
				if(subToSource.getTarget() instanceof StreamAO && 
						((StreamAO) subToSource.getTarget()).getStreamname().getResourceName().
						equals(sourceName))
					return Optional.of(operator);
				
			}
			
		}
		
		return windowAO;
		
	}

	/**
	 * Searches a {@link StreamAO} for a given source within a given collection of {@link ILogicalOperator}s.
	 * @param operatorsForFragmentationPart A collection of {@link ILogicalOperator}s.
	 * @param sourceName The name of a source.
	 * @return The {@link StreamAO} for <code>sourceName</code>, if <code>operatorsForFragmentationPart</code> contains one.
	 */
	private static Optional<ILogicalOperator> searchStreamAO(List<ILogicalOperator> operatorsForFragmentationPart, String sourceName) {
		
		// The return value
		Optional<ILogicalOperator> streamAO = Optional.absent();
		
		for(ILogicalOperator operator : operatorsForFragmentationPart) {
			
			if(operator instanceof StreamAO && ((StreamAO) operator).getStreamname().getResourceName().equals(sourceName))
				return Optional.of(operator);
			
		}
		
		return streamAO;
		
	}

	/**
	 * Subscribes operators for fragmentation, if a source was set by {@link #setSourceName(String)} 
	 * and deletes the source access of the current logical plan represented by <code>operators</code>.
	 * @param fragmentPlan The current status of the fragmentation.
	 * @param sourceName The name of the source to be fragmented.
	 * @param query The query to process.
	 * @param fragmentNo The fragment number for <code>query</code>. Is also the output port of the operator for fragmentation to use.
	 * It is also the output port of the inserted operator for fragmentation.
	 * @param operatorsForFragmentation A list of all created operators for fragmentation for the query.
	 * @return The new status of the fragmentation.
	 */
	protected IFragmentPlan subscribeOperatorForFragmentation(IFragmentPlan fragmentPlan, 
			String sourceName, ILogicalQuery query, int fragmentNo,  
			List<ILogicalOperator> operatorsForFragmentation) {
		
		// Preconditions
		Preconditions.checkNotNull(fragmentPlan);
		Preconditions.checkNotNull(sourceName);
		Preconditions.checkNotNull(query);
		Preconditions.checkArgument(fragmentNo >= 0);
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
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, fragmentNo, 
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
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, fragmentNo, 
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
	protected ILogicalOperator createOperatorForDataReunion(int numReplicates) {
		
		Preconditions.checkArgument(numReplicates > 0);
		
		if(numReplicates == 1)
			return new UnionAO();
		else return new ReplicationMergeAO();
		
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