package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.parallelization.helper.SDFAttributeHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.ParallelOperatorSettings;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult.State;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class GroupedAggregateTransformationStrategy extends
		AbstractParallelTransformationStrategy<AggregateAO> {
	
	@Override
	public String getName() {
		return "GroupedAggregateTransformationStrategy";
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
	public TransformationResult transform(ILogicalOperator operator,
			ParallelOperatorSettings settingsForOperator) {
		// validate settings and way to end point
		if (!super.areSettingsValid(settingsForOperator)) {
			return new TransformationResult(State.FAILED);
		}
		checkIfWayToEndPointIsValid(operator, settingsForOperator, true);

		TransformationResult transformationResult = new TransformationResult(State.SUCCESS);
		transformationResult.setAllowsModificationAfterUnion(true);

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
			return new TransformationResult(State.FAILED);
		}

		if (fragmentAO == null) {
			return new TransformationResult(State.FAILED);
		}
		transformationResult.addFragmentOperator(fragmentAO);

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
			union.setUniqueIdentifier(UUID.randomUUID().toString());
			transformationResult.setUnionOperator(union);

			for (int i = 0; i < settingsForOperator
					.getDegreeOfParallelization(); i++) {
				BufferAO buffer = new BufferAO();
				buffer.setName("Buffer_" + i);
				buffer.setThreaded(settingsForOperator.isUseThreadedBuffer());
				buffer.setMaxBufferSize(settingsForOperator.getBufferSize());
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
							fragments, settingsForOperator);
					union.subscribeToSource(lastParallelizedOperator, i, 0,
							lastParallelizedOperator.getOutputSchema());
				} else {
					union.subscribeToSource(newAggregateOperator, i, 0,
							newAggregateOperator.getOutputSchema());
				}
			}

			// get the last operator that need to be parallelized. if no end id
			// is set, the given operator for transformation is selected
			ILogicalOperator lastOperatorForParallelization = null;
			if (settingsForOperator.getEndParallelizationId() != null
					&& !settingsForOperator.getEndParallelizationId().isEmpty()) {
				lastOperatorForParallelization = LogicalGraphHelper
						.findDownstreamOperatorWithId(
								settingsForOperator.getEndParallelizationId(),
								aggregateOperator);
			} else {
				lastOperatorForParallelization = aggregateOperator;
			}

			// remove subscriptions to sink from this operator and connect the
			// union
			CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
			downstreamOperatorSubscriptions
					.addAll(lastOperatorForParallelization.getSubscriptions());
			
			lastOperatorForParallelization.unsubscribeFromAllSources();
			lastOperatorForParallelization.unsubscribeFromAllSinks();
			
			for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {

				downstreamOperatorSubscription.getTarget().subscribeToSource(
						union, downstreamOperatorSubscription.getSinkInPort(),
						downstreamOperatorSubscription.getSourceOutPort(),
						union.getOutputSchema());
			}
		}
		return transformationResult;
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
			ILogicalOperator currentClonedOperator, int iteration,
			List<AbstractFragmentAO> fragments,
			ParallelOperatorSettings settingsForOperator) {
		if (currentExistingOperator instanceof AggregateAO) {
			SDFAttributeHelper.checkIfAttributesAreEqual((AggregateAO) currentExistingOperator, iteration,
					fragments, settingsForOperator.isAssureSemanticCorrectness());
		}
	}

}
