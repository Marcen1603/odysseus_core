package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
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
			int degreeOfParallelization) {
		AggregateAO aggregateOperator = (AggregateAO) operator;

		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(aggregateOperator
				.getSubscribedToSource());

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(aggregateOperator
				.getSubscriptions());

		// remove subscriptions
		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			ILogicalOperator target = upstreamOperatorSubscription.getTarget();
			target.unsubscribeSink(upstreamOperatorSubscription);
		}

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			ILogicalOperator target = downstreamOperatorSubscription
					.getTarget();
			target.unsubscribeFromSource(downstreamOperatorSubscription);
		}

		List<SDFAttribute> groupingAttributes = aggregateOperator
				.getGroupingAttributes();
		if (!groupingAttributes.isEmpty()) {
			// Fragment operator
			HashFragmentAO fragment = new HashFragmentAO();
			fragment.setAttributes(groupingAttributes);
			fragment.setNumberOfFragments(degreeOfParallelization);
			fragment.setName("Hash Fragment");

			// subscribe new operator
			for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
				aggregateOperator
						.unsubscribeFromSource(upstreamOperatorSubscription);
				fragment.subscribeToSource(upstreamOperatorSubscription
						.getTarget(), upstreamOperatorSubscription
						.getSinkInPort(), upstreamOperatorSubscription
						.getSourceOutPort(), upstreamOperatorSubscription
						.getTarget().getOutputSchema());
			}

			UnionAO union = new UnionAO();
			union.setName("Union");

			for (int i = 0; i < degreeOfParallelization; i++) {
				BufferAO buffer = new BufferAO();
				buffer.setName("Buffer_" + i);
				buffer.setThreaded(true);
				buffer.setMaxBufferSize(10000000);

				AggregateAO newAggregateOperator = aggregateOperator.clone();
				newAggregateOperator.setName(aggregateOperator.getName() + "_"
						+ i);
				newAggregateOperator.setUniqueIdentifier(aggregateOperator
						.getUniqueIdentifier() + "_" + i);

				buffer.subscribeToSource(fragment, 0, i,
						fragment.getOutputSchema());

				newAggregateOperator.subscribeToSource(buffer, 0, 0,
						buffer.getOutputSchema());

				union.subscribeToSource(newAggregateOperator, i, 0,
						newAggregateOperator.getOutputSchema());
			}

			for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
				aggregateOperator
						.unsubscribeSink(downstreamOperatorSubscription);
				downstreamOperatorSubscription.getTarget().subscribeToSource(
						union, downstreamOperatorSubscription.getSinkInPort(),
						downstreamOperatorSubscription.getSourceOutPort(),
						union.getOutputSchema());
			}
		}
		return false;
	}

}
