package de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

/**
 * An abstract implementation for primary horizontal fragmentation strategies.
 * @author Michael Brand
 */
public abstract class AbstractPrimaryHorizontalDataFragmentation implements IDataFragmentation {
	
	@Override
	public abstract String getName();
	
	@Override
	public ILogicalOperator fragment(List<ILogicalOperator> logicalPlans, QueryBuildConfiguration parameters, String sourceName) {
		
		// Preconditions
		Preconditions.checkNotNull(logicalPlans);
		Preconditions.checkArgument(logicalPlans.size() > 1);
		Preconditions.checkNotNull(parameters);
		
		// All operators of each logical plan
		List<List<ILogicalOperator>> operatorsPerLogicalPlan = null;
		
		// Insert operator for fragmentation
		operatorsPerLogicalPlan = insertOperatorForFragmentation(logicalPlans, parameters, sourceName);
		
		// Insert operator for data reunion
		return insertOperatorForDataReunion(operatorsPerLogicalPlan);
		
		
	}

	/**
	 * Inserts operators for fragmentation, if a source was set by {@link #setSourceName(String)} and deletes all replications of the source access. <br />
	 * So there is only one source access left. All fragments will be subscribed to that single source access.
	 * @param logicalPlans A list of semantically equivalent logical plans, which shall be used as partitions.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @return All operators of each logical plan inclusive the operators for fragmentation.
	 */
	protected List<List<ILogicalOperator>> insertOperatorForFragmentation(List<ILogicalOperator> logicalPlans, 
			QueryBuildConfiguration parameters, String sourceName) {
		
		// Preconditions
		Preconditions.checkNotNull(logicalPlans);
		Preconditions.checkArgument(logicalPlans.size() > 1);
		Preconditions.checkNotNull(parameters);
		Preconditions.checkNotNull(sourceName);
		
		// The return value
		List<List<ILogicalOperator>> operatorsPerLogicalPlan = Lists.newArrayList();
		
		// The operators for fragmentation
		List<ILogicalOperator> operatorsForFragmentation = Lists.newArrayList();
		
		// Insert the operator for fragmentation
		List<ILogicalOperator> operators = Lists.newArrayList();
		RestructHelper.collectOperators(logicalPlans.get(0), operators);
		RestructHelper.removeTopAOs(operators);
		insertOperatorForFragmentation(operators, operatorsForFragmentation, parameters, sourceName, logicalPlans.size());
		operatorsPerLogicalPlan.add(operators);
		
		// Subscribe all other logical plans
		for(int planIndex = 1; planIndex < logicalPlans.size(); planIndex++) {
			
			operators = Lists.newArrayList();
			RestructHelper.collectOperators(logicalPlans.get(planIndex), operators);
			RestructHelper.removeTopAOs(operators);
			subscribeOperatorForFragmentation(operators, operatorsForFragmentation, sourceName, planIndex);
			operatorsPerLogicalPlan.add(operators);
			
		}
		
		// Initialize the operators for fragmentation
		for(ILogicalOperator operatorForFragmentation : operatorsForFragmentation) {
			
			operatorForFragmentation.setDestinationName(FragmentationHelper.FRAGMENTATION_DESTINATION_NAME);
			operatorForFragmentation.initialize();
			
		}
		
		return operatorsPerLogicalPlan;
		
	}

	/**
	 * Subscribes operators for fragmentation, if a source was set by {@link #setSourceName(String)} and deletes the source access of the 
	 * current logical plan represented by <code>operators</code>.
	 * @param operators A list of all operators within a logical plan.
	 * @param operatorsForFragmentation A list of all created operators for fragmentation for the query.
	 * @param sourceName The name of the source to be fragmented.
	 * @param planIndex The index of the logical plan to process. <br />
	 * It is also the output port of the inserted operator for fragmentation.
	 */
	protected void subscribeOperatorForFragmentation(List<ILogicalOperator> operators, List<ILogicalOperator> operatorsForFragmentation, 
			String sourceName, int planIndex) {
		
		// Preconditions
		Preconditions.checkNotNull(operators);
		Preconditions.checkArgument(!operators.isEmpty());
		Preconditions.checkNotNull(operatorsForFragmentation);
		Preconditions.checkArgument(planIndex >= 0);
		Preconditions.checkNotNull(sourceName);
		
		// The iterator of the operators for fragmentation as the subscription goes the same way as the creation
		Iterator<ILogicalOperator> operatorsForFragmentationIter = operatorsForFragmentation.iterator();
		
		// The replicated source access operators, which will be removed
		List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			// Only StreamAOs or following WindowAOs will be processed
			
			if(operator instanceof StreamAO && ((StreamAO) operator).getStreamname().getResourceName().equals(sourceName)) {
				
				// The operator for fragmentation to be subscribed.
				// All sinks of the StreamAO will be subscribed to that new operator.
				// For following WindowAOs other operators for fragmentation will be inserted additional.
				ILogicalOperator operatorForFragmentation = operatorsForFragmentationIter.next();
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					if(subToSink.getTarget() instanceof WindowAO) {
						continue;
					}
										
					operator.unsubscribeSink(subToSink);
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, planIndex, subToSink.getSchema());
				}
				
				operatorsToRemove.add(operator);
				
			} else if(operator instanceof WindowAO) {
				
				// True, if the WindowAO is for the source to be fragmented
				boolean windowOfSourceToBeFragmented = false;
				
				// Check, if the WindowAO is for the source to be fragmented
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					if(subToSource.getTarget() instanceof StreamAO && 
							((StreamAO) subToSource.getTarget()).getStreamname().getResourceName().equals(sourceName)) {
						
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
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, planIndex, subToSink.getSchema());
					
				}
				
				operatorsToRemove.add(operator);
				
			}
			
		}
		
		// Remove the replicated source access operators, which will be removed
		operators.removeAll(operatorsToRemove);
		
	}

	/**
	 * Inserts operators for fragmentation, if a source was set by {@link #setSourceName(String)}.
	 * @param operators A list of all operators within a logical plan.
	 * @param operatorsForFragmentation A list of all created operators for fragmentation for the query.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 * @param sourceName The name of the source to be fragmented.
	 * @param numFragments The number of fragments.
	 */
	protected void insertOperatorForFragmentation(List<ILogicalOperator> operators, List<ILogicalOperator> operatorsForFragmentation, 
			QueryBuildConfiguration parameters, String sourceName, int numFragments) {
		
		// Preconditions
		Preconditions.checkNotNull(operators);
		Preconditions.checkArgument(!operators.isEmpty());
		Preconditions.checkNotNull(operatorsForFragmentation);
		Preconditions.checkNotNull(parameters);
		Preconditions.checkArgument(numFragments > 1);
		Preconditions.checkNotNull(sourceName);
		
		for(ILogicalOperator operator : operators) {
			
			// Only StreamAOs or following WindowAOs will be processed
			
			if(operator instanceof StreamAO && ((StreamAO) operator).getStreamname().getResourceName().equals(sourceName)) {
				
				operator.setDestinationName(FragmentationHelper.FRAGMENTATION_DESTINATION_NAME);
				
				// The operator for fragmentation to be inserted.
				// All sinks of the StreamAO will be subscribed to that new operator.
				// For following WindowAOs other operators for fragmentation will be inserted additional.
				ILogicalOperator operatorForFragmentation = createOperatorForFragmentation(numFragments, parameters);
				
				// True, if the StreamAO is only subscribed by WindowAOs
				boolean onlySubscribedByWindows = true;
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					if(subToSink.getTarget() instanceof WindowAO) {
						continue;
					}
					
					onlySubscribedByWindows = false;
					operator.unsubscribeSink(subToSink);
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, 0, subToSink.getSchema());
				}
				
				if(!onlySubscribedByWindows) {
					
					operator.subscribeSink(operatorForFragmentation, 0, 0, operator.getOutputSchema());
					operatorsForFragmentation.add(operatorForFragmentation);
					
				}
				
			} else if(operator instanceof WindowAO) {
				
				// True, if the WindowAO is for the source to be fragmented
				boolean windowOfSourceToBeFragmented = false;
				
				// Check, if the WindowAO is for the source to be fragmented
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					if(subToSource.getTarget() instanceof StreamAO && 
							((StreamAO) subToSource.getTarget()).getStreamname().getResourceName().equals(sourceName)) {
						
						windowOfSourceToBeFragmented = true;
						break;
						
					}
					
				}
				if(!windowOfSourceToBeFragmented)
					continue;
				
				operator.setDestinationName(FragmentationHelper.FRAGMENTATION_DESTINATION_NAME);
				
				// The operator for fragmentation to be subscribed.
				// All sinks of the WindowAO will be subscribed to that new operator.
				ILogicalOperator operatorForFragmentation = createOperatorForFragmentation(numFragments, parameters);
				operator.subscribeSink(operatorForFragmentation, 0, 0, operator.getOutputSchema());
				operatorsForFragmentation.add(operatorForFragmentation);
				
				for(LogicalSubscription subToSink : operator.getSubscriptions()) {
					
					operator.unsubscribeSink(subToSink);
					operatorForFragmentation.subscribeSink(subToSink.getTarget(), 0, 0, subToSink.getSchema());
					
				}
				
			}
			
		}
		
		operators.addAll(operatorsForFragmentation);
		
	}

	/**
	 * Creates a new operator for fragmentation.
	 * @param numFragments The number of fragments.
	 * @param parameters the {@link QueryBuildConfiguration}.
	 */
	protected abstract ILogicalOperator createOperatorForFragmentation(int numFragments, QueryBuildConfiguration parameters);

	/**
	 * Inserts operators for data reunion. All sinks of all fragments will be subscribed by the data reunion operators.
	 * @param logicalPlans A list of semantically equivalent logical plans, which shall be used as partitions.
	 * @return The merged logical plan.
	 */
	protected ILogicalOperator insertOperatorForDataReunion(List<List<ILogicalOperator>> operatorsPerLogicalPlan) {
		
		// Preconditions
		Preconditions.checkNotNull(operatorsPerLogicalPlan);
		Preconditions.checkArgument(operatorsPerLogicalPlan.size() > 1);
		
		// The operators for data reunion
		List<ILogicalOperator> operatorsForDataReunion = Lists.newArrayList();
		
		// Insert the operator for data reunion
		insertOperatorForDataReunion(operatorsPerLogicalPlan.get(0), operatorsForDataReunion);
		
		// Subscribe all other logical plans
		for(int planIndex = 1; planIndex < operatorsPerLogicalPlan.size(); planIndex++)
			subscribeOperatorForDataReUnion(operatorsPerLogicalPlan.get(planIndex), operatorsForDataReunion, planIndex);
		
		// Initialize the operators for data reunion
		for(ILogicalOperator operatorForDataReunion : operatorsForDataReunion) {
			
			operatorForDataReunion.setDestinationName(FragmentationHelper.REUNION_DESTINATION_NAME);
			operatorForDataReunion.initialize();
			
		}
		
		return operatorsForDataReunion.iterator().next();
		
	}
	
	/**
	 * Subscribes operators for data reunion.
	 * @param operators A list of all operators within a logical plan.
	 * @param operatorsForDataReunion A list of all created operators for data reunion for the query.
	 * @param planIndex The index of the logical plan to process. <br />
	 * It is also the input port of the inserted operator for data reunion.
	 */
	protected void subscribeOperatorForDataReUnion(List<ILogicalOperator> operators, List<ILogicalOperator> operatorsForDataReunion, 
			int planIndex) {
		
		// Preconditions
		Preconditions.checkNotNull(operators);
		Preconditions.checkArgument(!operators.isEmpty());
		Preconditions.checkNotNull(operatorsForDataReunion);
		Preconditions.checkArgument(planIndex >= 0);
		
		// A list of operators which shall be moved to the data reunion part
		List<ILogicalOperator> operatorsForDataReunionPart = Lists.newArrayList();
		
		// The iterator of the operators for data reunion as the subscription goes the same way as the creation
		Iterator<ILogicalOperator> operatorsForDataReunionIter = operatorsForDataReunion.iterator();
		
		for(ILogicalOperator operator : operators) {
			
			// Only sinks will be processed
			
			if(operator.getSubscriptions().isEmpty()) {
				
				if(Arrays.asList(FragmentationHelper.OPERATOR_CLASSES_DATAREUNION_PART).contains(operator.getClass())) {
					
					// Collect operators to be moved to the data reunion part
					operatorsForDataReunionPart.add(operator);
					
				}
				
				// The operator for data reunion to be subscribed
				ILogicalOperator operatorForDataReunion = operatorsForDataReunionIter.next();
				operator.subscribeSink(operatorForDataReunion, planIndex, 0, operator.getOutputSchema());
				
			}
			
		}
		
		// Remove the operators for the data reunion part
		for(ILogicalOperator operator : operatorsForDataReunionPart) {
			
			// The subscriptions from the operator to its sources
			Collection<LogicalSubscription> subsToSources = Lists.newArrayList();
			for(LogicalSubscription subToSource : operator.getSubscribedToSource())
				subsToSources.add(subToSource);
			
			// The only(the operator was a sink before calling this method) subscription from the operator to an operator for data reunion.
			LogicalSubscription subToSink = operator.getSubscriptions().iterator().next();
			
			// The operator for data reunion.
			ILogicalOperator operatorForDataReunion = subToSink.getTarget();
			
			operator.unsubscribeFromAllSources();
			operator.unsubscribeSink(subToSink);
			
			for(LogicalSubscription subToSource : subsToSources)
				operatorForDataReunion.subscribeToSource(subToSource.getTarget(), subToSink.getSinkInPort(), subToSource.getSourceOutPort(), 
						subToSink.getSchema());
			
		}
		
	}
	
	/**
	 * Inserts operators for data reunion.
	 * @param operators A list of all operators within a logical plan.
	 * @param operatorsForDataReunion A list of all created operators for data reunion for the query.
	 */
	protected void insertOperatorForDataReunion(List<ILogicalOperator> operators, List<ILogicalOperator> operatorsForDataReunion) {
		
		// Preconditions
		Preconditions.checkNotNull(operators);
		Preconditions.checkArgument(!operators.isEmpty());
		Preconditions.checkNotNull(operatorsForDataReunion);
		
		// A list of operators which shall be moved to the data reunion part
		List<ILogicalOperator> operatorsForDataReunionPart = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			// Only sinks will be processed
			
			if(operator.getSubscriptions().isEmpty()) {
				
				if(Arrays.asList(FragmentationHelper.OPERATOR_CLASSES_DATAREUNION_PART).contains(operator.getClass())) {
					
					// Collect operators to be moved to the data reunion part
					operatorsForDataReunionPart.add(operator);
					
				}
				
				// The operator for data reunion to be inserted
				ILogicalOperator operatorForDataReunion = createOperatorForDataReunion();
				operator.subscribeSink(operatorForDataReunion, 0, 0, operator.getOutputSchema());
				operatorsForDataReunion.add(operatorForDataReunion);
				
			}
			
		}
		
		// Insert the operators for the data reunion part
		for(ILogicalOperator operator : operatorsForDataReunionPart) {
			
			operator.setDestinationName(FragmentationHelper.REUNION_DESTINATION_NAME);
			
			// The subscriptions from the operator to its sources
			Collection<LogicalSubscription> subsToSources = Lists.newArrayList();
			for(LogicalSubscription subToSource : operator.getSubscribedToSource())
				subsToSources.add(subToSource);
			
			// The only(the operator was a sink before calling this method) subscription from the operator to an operator for data reunion.
			LogicalSubscription subToSink = operator.getSubscriptions().iterator().next();
			
			// The operator for data reunion.
			ILogicalOperator operatorForDataReunion = subToSink.getTarget();
			
			operator.unsubscribeFromAllSources();
			operator.unsubscribeSink(subToSink);
			
			for(LogicalSubscription subToSource : subsToSources)
				operatorForDataReunion.subscribeToSource(subToSource.getTarget(), subToSink.getSinkInPort(), subToSource.getSourceOutPort(), 
						subToSink.getSchema());
			
			operatorForDataReunion.subscribeSink(operator, 0, 0, subToSink.getSchema());
			
		}
		
	}
	
	/**
	 * Creates a new operator for data reunion.
	 */
	protected ILogicalOperator createOperatorForDataReunion() {
		
		return new UnionAO();
		
	}
	
}