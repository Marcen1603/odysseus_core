package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class GroupedAggregateMultithreadedTransformationStrategy extends
		AbstractMultithreadedTransformationStrategy<AggregateAO> {

	@Override
	public String getName() {
		return "GroupedAggregateMultithreadedTransformationStrategy";
	}

	@Override
	public int evaluateCompatibility(ILogicalOperator operator) {
		if (operator instanceof AggregateAO) {
			AggregateAO aggregateOperator = (AggregateAO) operator;
			if (!aggregateOperator.getGroupingAttributes().isEmpty()) {
				// only if the given operator has a grouping, this strategy
				// works
				return 100;
			}
		}
		// if operator is no aggregation or has no grouping, this strategy is
		// incompatible
		return 0;
	}

	@Override
	public boolean transform(ILogicalOperator operator,
			MultithreadedOperatorSettings settingsForOperator) {
		// validate settings and way to end point
		if (!super.areSettingsValid(settingsForOperator)) {
			return false;
		}
		checkIfWayToEndPointIsValid(operator, settingsForOperator, true);

		
		AggregateAO aggregateOperator = (AggregateAO) operator;

		// create fragment operator
		AbstractFragmentAO fragmentAO;
		List<SDFAttribute> groupingAttributes = aggregateOperator
				.getGroupingAttributes();
		try {
			fragmentAO = createFragmentAO(
					settingsForOperator.getFragementationType(),
					settingsForOperator.getDegreeOfParallelization(), "",
					groupingAttributes, null, null);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}

		if (fragmentAO == null) {
			return false;
		}

		
		
		if (!groupingAttributes.isEmpty()) {
			// subscribe new operator
			CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
			upstreamOperatorSubscriptions.addAll(aggregateOperator
					.getSubscribedToSource());
			
			for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
				aggregateOperator
						.unsubscribeFromSource(upstreamOperatorSubscription);
				fragmentAO.subscribeToSource(upstreamOperatorSubscription
						.getTarget(), upstreamOperatorSubscription
						.getSinkInPort(), upstreamOperatorSubscription
						.getSourceOutPort(), upstreamOperatorSubscription
						.getTarget().getOutputSchema());
			}

			UnionAO union = new UnionAO();
			union.setName("Union");

			for (int i = 0; i < settingsForOperator
					.getDegreeOfParallelization(); i++) {
				BufferAO buffer = new BufferAO();
				buffer.setName("Buffer_" + i);
				buffer.setThreaded(true);
				buffer.setMaxBufferSize(10000000);
				buffer.setDrainAtClose(false);

				AggregateAO newAggregateOperator = aggregateOperator.clone();
				newAggregateOperator.setName(aggregateOperator.getName() + "_"
						+ i);
				newAggregateOperator.setUniqueIdentifier(aggregateOperator
						.getUniqueIdentifier() + "_" + i);
				newAggregateOperator.setDrainAtClose(true);

				buffer.subscribeToSource(fragmentAO, 0, i,
						fragmentAO.getOutputSchema());

				newAggregateOperator.subscribeToSource(buffer, 0, 0,
						buffer.getOutputSchema());

				if (settingsForOperator.getEndParallelizationId() != null
						&& !settingsForOperator.getEndParallelizationId()
								.isEmpty()) {
					List<AbstractFragmentAO> fragments = new ArrayList<AbstractFragmentAO>();
					fragments.add(fragmentAO);
					ILogicalOperator lastParallelizedOperator = doPostParallelization(
							aggregateOperator, newAggregateOperator,
							settingsForOperator.getEndParallelizationId(), i,
							true, fragments);
					union.subscribeToSource(lastParallelizedOperator, i, 0,
							lastParallelizedOperator.getOutputSchema());
				} else {
					union.subscribeToSource(newAggregateOperator, i, 0,
							newAggregateOperator.getOutputSchema());
				}
			}
			
			// get the last operator that need to be parallelized. if no end id is set, the given operator for transformation is selected
			ILogicalOperator lastOperatorForParallelization = null;
			if (settingsForOperator.getEndParallelizationId() != null
					&& !settingsForOperator.getEndParallelizationId()
							.isEmpty()) {
				lastOperatorForParallelization = findOperatorWithId(settingsForOperator.getEndParallelizationId(), aggregateOperator);
			} else {
				lastOperatorForParallelization = aggregateOperator;
			}

			// remove subscriptions to sink from this operator and connect the union 
			CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
			downstreamOperatorSubscriptions.addAll(lastOperatorForParallelization
					.getSubscriptions());
			
			for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
				lastOperatorForParallelization
						.unsubscribeSink(downstreamOperatorSubscription);
				
				downstreamOperatorSubscription.getTarget().subscribeToSource(
						union, downstreamOperatorSubscription.getSinkInPort(),
						downstreamOperatorSubscription.getSourceOutPort(),
						union.getOutputSchema());
			}
		}
		return false;
	}

	@Override
	public List<Class<? extends AbstractFragmentAO>> getAllowedFragmentationTypes() {
		List<Class<? extends AbstractFragmentAO>> allowedFragmentTypes = new ArrayList<Class<? extends AbstractFragmentAO>>();
		allowedFragmentTypes.add(HashFragmentAO.class);
		return allowedFragmentTypes;
	}

	@Override
	public Class<? extends AbstractFragmentAO> getPreferredFragmentationType() {
		return HashFragmentAO.class;
	}

	@Override
	protected void doStrategySpecificPostParallelization(
			ILogicalOperator parallelizedOperator,
			ILogicalOperator currentExistingOperator,
			ILogicalOperator currentClonedOperator, int iteration) {
		// no strategy specific modifications
	}

}
