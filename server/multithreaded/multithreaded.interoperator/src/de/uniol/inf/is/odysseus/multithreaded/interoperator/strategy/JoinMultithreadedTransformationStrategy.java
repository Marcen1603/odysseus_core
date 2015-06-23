package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.multithreaded.helper.SDFAttributeHelper;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class JoinMultithreadedTransformationStrategy extends
		AbstractMultithreadedTransformationStrategy<JoinAO> {

	@Override
	public String getName() {
		return "JoinMultithreadedTransformationStrategy";
	}

	@Override
	public int evaluateCompatibility(ILogicalOperator operator) {
		if (operator instanceof JoinAO) {
			JoinAO joinOperator = (JoinAO) operator;
			if (joinOperator.getPredicate() != null) {
				if (SDFAttributeHelper.getInstance()
						.validateStructureOfPredicate(joinOperator)) {
					// if the join operator has an join predicate, this strategy
					// is
					// compatible
					return 100;
				}
			}
		}
		// if operator is not a join or has no join predicate, this strategy
		// doesn't work
		return 0;
	}

	@Override
	public boolean transform(ILogicalOperator operator,
			MultithreadedOperatorSettings settingsForOperator) {
		if (!super.areSettingsValid(settingsForOperator)) {
			return false;
		}
		checkIfWayToEndPointIsValid(operator, settingsForOperator, true);

		JoinAO joinOperator = (JoinAO) operator;

		Map<Integer, List<SDFAttribute>> attributes = new HashMap<Integer, List<SDFAttribute>>();
		attributes = SDFAttributeHelper.getInstance()
				.getSDFAttributesFromEqualPredicates(attributes, joinOperator);

		int numberOfFragments = 0;
		List<Pair<AbstractFragmentAO, Integer>> fragmentsSinkInPorts = new ArrayList<Pair<AbstractFragmentAO, Integer>>();
		List<AbstractFragmentAO> fragments = new ArrayList<AbstractFragmentAO>();

		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(joinOperator
				.getSubscribedToSource());

		for (Integer inputPort : attributes.keySet()) {
			for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
				if (upstreamOperatorSubscription.getSinkInPort() == inputPort) {
					List<SDFAttribute> attributesForSource = attributes
							.get(inputPort);
					// if join predicate attribute references on the source
					// from
					// this subscription, create fragment operator
					// Fragment operator
					AbstractFragmentAO fragmentAO;
					try {
						fragmentAO = createFragmentAO(
								settingsForOperator.getFragementationType(),
								settingsForOperator
										.getDegreeOfParallelization(),
								numberOfFragments + "", attributesForSource,
								null, null);
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
						return false;
					}

					if (fragmentAO == null) {
						return false;
					}

					Pair<AbstractFragmentAO, Integer> pair = new Pair<AbstractFragmentAO, Integer>();
					pair.setE1(fragmentAO);
					pair.setE2(upstreamOperatorSubscription.getSinkInPort());
					fragmentsSinkInPorts.add(pair);
					fragments.add(fragmentAO);

					joinOperator
							.unsubscribeFromSource(upstreamOperatorSubscription);
					fragmentAO.subscribeToSource(upstreamOperatorSubscription
							.getTarget(), 0, upstreamOperatorSubscription
							.getSourceOutPort(), upstreamOperatorSubscription
							.getTarget().getOutputSchema());

					numberOfFragments++;
					break;
				}
			}
		}

		UnionAO union = new UnionAO();
		union.setName("Union");

		int bufferCounter = 0;
		for (int i = 0; i < settingsForOperator.getDegreeOfParallelization(); i++) {
			JoinAO newJoinOperator = joinOperator.clone();
			newJoinOperator.setName(joinOperator.getName() + "_" + i);
			newJoinOperator.setUniqueIdentifier(joinOperator
					.getUniqueIdentifier() + "_" + i);

			for (Pair<AbstractFragmentAO, Integer> pair : fragmentsSinkInPorts) {
				BufferAO buffer = new BufferAO();
				buffer.setName("Buffer_" + bufferCounter);
				buffer.setThreaded(true);
				buffer.setMaxBufferSize(settingsForOperator.getBufferSize());
				buffer.setDrainAtClose(false);
				bufferCounter++;

				buffer.subscribeToSource(pair.getE1(), 0, i, pair.getE1()
						.getOutputSchema());

				newJoinOperator.subscribeToSource(buffer, pair.getE2(), 0,
						buffer.getOutputSchema());

			}

			if (settingsForOperator.getEndParallelizationId() != null
					&& !settingsForOperator.getEndParallelizationId().isEmpty()) {
				ILogicalOperator lastParallelizedOperator = doPostParallelization(
						joinOperator, newJoinOperator,
						settingsForOperator.getEndParallelizationId(), i,
						fragments);
				union.subscribeToSource(lastParallelizedOperator, i, 0,
						lastParallelizedOperator.getOutputSchema());
			} else {
				union.subscribeToSource(newJoinOperator, i, 0,
						newJoinOperator.getOutputSchema());
			}
		}

		// get the last operator that need to be parallelized. if no end id is
		// set, the given operator for transformation is selected
		ILogicalOperator lastOperatorForParallelization = null;
		if (settingsForOperator.getEndParallelizationId() != null
				&& !settingsForOperator.getEndParallelizationId().isEmpty()) {
			lastOperatorForParallelization = LogicalGraphHelper
					.findDownstreamOperatorWithId(
							settingsForOperator.getEndParallelizationId(),
							joinOperator);
		} else {
			lastOperatorForParallelization = joinOperator;
		}

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(lastOperatorForParallelization
				.getSubscriptions());

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			lastOperatorForParallelization
					.unsubscribeSink(downstreamOperatorSubscription);
			downstreamOperatorSubscription.getTarget().subscribeToSource(union,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					union.getOutputSchema());
		}

		return true;
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
			List<AbstractFragmentAO> fragments) {
		// check if grouping attributes of aggregation already exists in
		// hashFragment, if not throw exception
		if (currentExistingOperator instanceof AggregateAO) {
			AggregateAO aggregateOperator = (AggregateAO) currentExistingOperator;

			List<SDFAttribute> groupingAttributes = aggregateOperator
					.getGroupingAttributes();
			for (SDFAttribute sdfAttribute : groupingAttributes) {
				boolean attributeFound = false;
				for (AbstractFragmentAO fragment : fragments) {
					if (fragment instanceof HashFragmentAO) {
						HashFragmentAO hashFragment = (HashFragmentAO) fragment;
						if (hashFragment.getAttributes().contains(sdfAttribute)){
							attributeFound = true;
						}
					}
				}
				if (!attributeFound){
					throw new IllegalArgumentException("");
				}
			}
		}
	}
}
