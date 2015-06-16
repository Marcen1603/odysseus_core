package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RoundRobinFragmentAO;

public class AggregateMultithreadedTransformationStrategy extends
		AbstractMultithreadedTransformationStrategy<AggregateAO> {

	@Override
	public String getName() {
		return "AggregateMultithreadedTransformationStrategy";
	}

	@Override
	public int evaluateCompatibility(ILogicalOperator operator) {
		if (operator instanceof AggregateAO) {
			AggregateAO aggregateOperator = (AggregateAO) operator;
			if (aggregateOperator.getGroupingAttributes().isEmpty()) {
				// if aggregation has no grouping this strategy works good

				boolean everyAggregationHasOnlyOneInputAttribut = true;
				List<AggregateItem> existingAggregationItems = aggregateOperator.getAggregationItems();
				for (AggregateItem aggregateItem : existingAggregationItems) {
					if (aggregateItem.inAttributes.size() > 1){
						everyAggregationHasOnlyOneInputAttribut = false;
					}
				}
				
				if (everyAggregationHasOnlyOneInputAttribut){
					// Only aggregations with one input attribute are allowed for partial aggregates
					return 100;					
				}
			} else {
				// if the aggregation has an grouping, there is might be a
				// better strategy
				return 50;
			}
		}
		// if the operator is no aggregation, this strategy is incompatible
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

		// round robin fragmentation, because there is no aggregate grouping
		RoundRobinFragmentAO fragment = new RoundRobinFragmentAO();
		fragment.setNumberOfFragments(degreeOfParallelization);
		fragment.setName("Round Robin Fragment");

		// subscribe new operator
		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			aggregateOperator
					.unsubscribeFromSource(upstreamOperatorSubscription);
			fragment.subscribeToSource(
					upstreamOperatorSubscription.getTarget(),
					upstreamOperatorSubscription.getSinkInPort(),
					upstreamOperatorSubscription.getSourceOutPort(),
					upstreamOperatorSubscription.getTarget().getOutputSchema());
		}
		
		// Renaming of input and output attributes for partial aggregates
		List<AggregateItem> existingAggregationItems = aggregateOperator.getAggregationItems();
		List<AggregateItem> renamedPAAggregationItems = new ArrayList<AggregateItem>();
		List<AggregateItem> renamedCombineAggregationItems = new ArrayList<AggregateItem>();
		
		for (AggregateItem aggregateItem : existingAggregationItems) {
			SDFAttribute attr = aggregateItem.outAttribute;
						
			// output attributes
			String newPAAttributeName = "pa_"+attr.getAttributeName();
			SDFAttribute outAttribute = new SDFAttribute(attr.getSourceName(), newPAAttributeName, SDFDatatype.PARTIAL_AGGREGATE, null, attr.getDtConstraints(), null);
			AggregateItem newOutItem = new AggregateItem(aggregateItem.aggregateFunction.toString(), aggregateItem.inAttributes, outAttribute);
			renamedPAAggregationItems.add(newOutItem);
			
			// input attributes
			List<SDFAttribute> inAttributes = new ArrayList<SDFAttribute>();
			SDFAttribute inAttribute = outAttribute.clone(); 
			inAttributes.add(inAttribute);
			
			AggregateItem newInItem = new AggregateItem(aggregateItem.aggregateFunction.toString(), inAttributes, aggregateItem.outAttribute);
			renamedCombineAggregationItems.add(newInItem);
		}
		
		// create union operator for merging fragmented datastreams
				UnionAO union = new UnionAO();
				union.setName("Union");

		// for each degree of parallelization
		for (int i = 0; i < degreeOfParallelization; i++) {
			// create buffer
			BufferAO buffer = new BufferAO();
			buffer.setName("Buffer_" + i);
			buffer.setThreaded(true);
			buffer.setMaxBufferSize(10000000);
			buffer.setDrainAtClose(true);

			// create new aggregate operator from existing operator
			AggregateAO newAggregateOperator = aggregateOperator.clone();
			newAggregateOperator.setName(aggregateOperator.getName() + "_pa_"
					+ i);
			newAggregateOperator.setUniqueIdentifier(aggregateOperator
					.getUniqueIdentifier() + "_pa_" + i);
			newAggregateOperator.setOutputPA(true); // enable partial aggregates
			newAggregateOperator.clearAggregations();
			newAggregateOperator.setAggregationItems(renamedPAAggregationItems); // use renamed output name of attributes
			
			// subscribe buffer to fragment
			buffer.subscribeToSource(fragment, 0, i, fragment.getOutputSchema());

			// subscribe new aggregate operator to buffer
			newAggregateOperator.subscribeToSource(buffer, 0, 0,
					buffer.getOutputSchema());

			// subscribe union to new aggregate operator
			union.subscribeToSource(newAggregateOperator, i, 0,
					newAggregateOperator.getOutputSchema());
		}

		// create aggregate operator for combining partial aggregates
		AggregateAO combinePAAggregateOperator = aggregateOperator.clone();
		combinePAAggregateOperator.setName(aggregateOperator.getName()
				+ "_combinePA");
		combinePAAggregateOperator.setUniqueIdentifier(aggregateOperator
				.getUniqueIdentifier() + "_combinePA");

		combinePAAggregateOperator.clearAggregations();
		combinePAAggregateOperator.setAggregationItems(renamedCombineAggregationItems);
		
		// subscribe aggregate operator for combining partial aggregates to union
		combinePAAggregateOperator.subscribeToSource(union, 0, 0,
				union.getOutputSchema());

		// remove old subscription and subscribe new aggregate operator to existing downstream operators
		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			aggregateOperator.unsubscribeSink(downstreamOperatorSubscription);
			downstreamOperatorSubscription.getTarget().subscribeToSource(
					combinePAAggregateOperator,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					combinePAAggregateOperator.getOutputSchema());
		}

		return true;
	}

}
