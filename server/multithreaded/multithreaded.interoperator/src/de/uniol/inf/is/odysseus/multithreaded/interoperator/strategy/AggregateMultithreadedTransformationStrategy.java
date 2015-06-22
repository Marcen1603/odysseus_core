package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RoundRobinFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ShuffleFragmentAO;

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
				List<AggregateItem> existingAggregationItems = aggregateOperator
						.getAggregationItems();
				for (AggregateItem aggregateItem : existingAggregationItems) {
					if (aggregateItem.inAttributes.size() > 1) {
						everyAggregationHasOnlyOneInputAttribut = false;
					}
				}

				if (everyAggregationHasOnlyOneInputAttribut) {
					// Only aggregations with one input attribute are allowed
					// for partial aggregates
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
			MultithreadedOperatorSettings settingsForOperator) {
		if (!super.areSettingsValid(settingsForOperator)) {
			return false;
		}
		checkIfWayToEndPointIsValid(operator, settingsForOperator, false);

		AggregateAO aggregateOperator = (AggregateAO) operator;

		// create fragment operator
		AbstractFragmentAO fragmentAO;
		try {
			fragmentAO = createFragmentAO(
					settingsForOperator.getFragementationType(),
					settingsForOperator.getDegreeOfParallelization(), "", null,
					null, null);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}

		if (fragmentAO == null) {
			return false;
		}

		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(aggregateOperator
				.getSubscribedToSource());

		// subscribe new operator
		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			aggregateOperator
					.unsubscribeFromSource(upstreamOperatorSubscription);
			fragmentAO.subscribeToSource(
					upstreamOperatorSubscription.getTarget(),
					upstreamOperatorSubscription.getSinkInPort(),
					upstreamOperatorSubscription.getSourceOutPort(),
					upstreamOperatorSubscription.getTarget().getOutputSchema());
		}

		// Renaming of input and output attributes for partial aggregates
		List<AggregateItem> existingAggregationItems = aggregateOperator
				.getAggregationItems();
		List<AggregateItem> renamedPAAggregationItems = new ArrayList<AggregateItem>();
		List<AggregateItem> renamedCombineAggregationItems = new ArrayList<AggregateItem>();

		for (AggregateItem aggregateItem : existingAggregationItems) {
			SDFAttribute attr = aggregateItem.outAttribute;

			// output attributes
			String newPAAttributeName = "pa_" + attr.getQualName();
			SDFAttribute outAttribute = new SDFAttribute(attr.getSourceName(),
					newPAAttributeName, SDFDatatype.PARTIAL_AGGREGATE, null,
					attr.getDtConstraints(), null);
			AggregateItem newOutItem = new AggregateItem(
					aggregateItem.aggregateFunction.toString(),
					aggregateItem.inAttributes, outAttribute);
			renamedPAAggregationItems.add(newOutItem);

			// input attributes
			List<SDFAttribute> inAttributes = new ArrayList<SDFAttribute>();
			SDFAttribute inAttribute = outAttribute.clone();
			inAttributes.add(inAttribute);

			AggregateItem newInItem = new AggregateItem(
					aggregateItem.aggregateFunction.toString(), inAttributes,
					aggregateItem.outAttribute);
			renamedCombineAggregationItems.add(newInItem);
		}

		// create union operator for merging fragmented datastreams
		UnionAO union = new UnionAO();
		union.setName("Union");

		// for each degree of parallelization
		for (int i = 0; i < settingsForOperator.getDegreeOfParallelization(); i++) {
			// create buffer
			BufferAO buffer = new BufferAO();
			buffer.setName("Buffer_" + i);
			buffer.setThreaded(true);
			buffer.setMaxBufferSize(settingsForOperator.getBufferSize());
			buffer.setDrainAtClose(true);

			// create new aggregate operator from existing operator
			AggregateAO newAggregateOperator = aggregateOperator.clone();
			newAggregateOperator.setName(aggregateOperator.getName() + "_pa_"
					+ i);
			newAggregateOperator.setUniqueIdentifier(aggregateOperator
					.getUniqueIdentifier() + "_pa_" + i);
			newAggregateOperator.setOutputPA(true); // enable partial aggregates
			newAggregateOperator.clearAggregations();
			newAggregateOperator.setAggregationItems(renamedPAAggregationItems); // use
																					// renamed
																					// output
																					// name
																					// of
																					// attributes
			newAggregateOperator.setDrainAtClose(true);

			// subscribe buffer to fragment
			buffer.subscribeToSource(fragmentAO, 0, i,
					fragmentAO.getOutputSchema());

			// subscribe new aggregate operator to buffer
			newAggregateOperator.subscribeToSource(buffer, 0, 0,
					buffer.getOutputSchema());

			if (settingsForOperator.getEndParallelizationId() != null
					&& !settingsForOperator.getEndParallelizationId().isEmpty()) {
				List<AbstractFragmentAO> fragments = new ArrayList<AbstractFragmentAO>();
				fragments.add(fragmentAO);
				ILogicalOperator lastParallelizedOperator = doPostParallelization(
						aggregateOperator, newAggregateOperator,
						settingsForOperator.getEndParallelizationId(), i,
						fragments);
				union.subscribeToSource(lastParallelizedOperator, i, 0,
						lastParallelizedOperator.getOutputSchema());
			} else {
				union.subscribeToSource(newAggregateOperator, i, 0,
						newAggregateOperator.getOutputSchema());
			}
		}

		// create aggregate operator for combining partial aggregates
		AggregateAO combinePAAggregateOperator = aggregateOperator.clone();
		combinePAAggregateOperator.setName(aggregateOperator.getName()
				+ "_combinePA");
		combinePAAggregateOperator.setUniqueIdentifier(aggregateOperator
				.getUniqueIdentifier() + "_combinePA");
		combinePAAggregateOperator.clearAggregations();
		combinePAAggregateOperator
				.setAggregationItems(renamedCombineAggregationItems);
		combinePAAggregateOperator.setDrainAtClose(true);

		// subscribe aggregate operator for combining partial aggregates to
		// union
		combinePAAggregateOperator.subscribeToSource(union, 0, 0,
				union.getOutputSchema());

		// get the last operator that need to be parallelized. if no end id is
		// set, the given operator for transformation is selected
		ILogicalOperator lastOperatorForParallelization = null;
		if (settingsForOperator.getEndParallelizationId() != null
				&& !settingsForOperator.getEndParallelizationId().isEmpty()) {
			lastOperatorForParallelization = LogicalGraphHelper.findDownstreamOperatorWithId(
					settingsForOperator.getEndParallelizationId(),
					aggregateOperator);
		} else {
			lastOperatorForParallelization = aggregateOperator;
		}

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(lastOperatorForParallelization
				.getSubscriptions());

		// remove old subscription and subscribe new aggregate operator to
		// existing downstream operators
		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			lastOperatorForParallelization
					.unsubscribeSink(downstreamOperatorSubscription);
			downstreamOperatorSubscription.getTarget().subscribeToSource(
					combinePAAggregateOperator,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					combinePAAggregateOperator.getOutputSchema());
		}

		return true;
	}

	@Override
	public List<Class<? extends AbstractFragmentAO>> getAllowedFragmentationTypes() {
		List<Class<? extends AbstractFragmentAO>> allowedFragmentTypes = new ArrayList<Class<? extends AbstractFragmentAO>>();
		allowedFragmentTypes.add(RoundRobinFragmentAO.class);
		allowedFragmentTypes.add(ShuffleFragmentAO.class);
		return allowedFragmentTypes;
	}

	@Override
	public Class<? extends AbstractFragmentAO> getPreferredFragmentationType() {
		return RoundRobinFragmentAO.class;
	}

	@Override
	protected void doStrategySpecificPostParallelization(
			ILogicalOperator parallelizedOperator,
			ILogicalOperator currentExistingOperator,
			ILogicalOperator currentClonedOperator, int iteration, List<AbstractFragmentAO> fragments) {
		if (currentClonedOperator instanceof MapAO) {
			// map removes partial aggregates, so we need to add these
			// attributes to the map
			MapAO mapOperator = (MapAO) currentClonedOperator;
			// the parallelized operator is always in the type of the strategy
			if (parallelizedOperator instanceof AggregateAO) {
				AggregateAO aggregateOperator = (AggregateAO) parallelizedOperator;
				List<AggregateItem> aggregationItems = aggregateOperator
						.getAggregationItems();
				List<NamedExpression> expressions = mapOperator
						.getExpressions();
				IAttributeResolver attributeResolver = new DirectAttributeResolver(
						aggregateOperator.getOutputSchema());
				for (AggregateItem aggregateItem : aggregationItems) {
					NamedExpression namedExpression = new NamedExpression("",
							new SDFExpression(null,
									aggregateItem.outAttribute.getURI(),
									attributeResolver, MEP.getInstance(),
									AggregateFunctionBuilderRegistry
											.getAggregatePattern()));
					expressions.add(namedExpression);
				}
				mapOperator.setExpressions(expressions);
			}
		}
	}

}
