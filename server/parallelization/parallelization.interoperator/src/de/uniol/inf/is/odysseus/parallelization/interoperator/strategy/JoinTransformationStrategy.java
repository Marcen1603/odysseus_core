/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.parallelization.helper.SDFAttributeHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult.State;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class JoinTransformationStrategy extends
		AbstractParallelTransformationStrategy<JoinAO> {

	@Override
	public String getName() {
		return "JoinTransformationStrategy";
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
	public TransformationResult transform(ILogicalOperator operator,
			ParallelOperatorConfiguration settingsForOperator) {
		if (!super.areSettingsValid(settingsForOperator)) {
			return new TransformationResult(State.FAILED);
		}
		checkIfWayToEndPointIsValid(operator, settingsForOperator, true);

		TransformationResult transformationResult = new TransformationResult(State.SUCCESS);
		transformationResult.setAllowsModificationAfterUnion(true);

		JoinAO joinOperator = (JoinAO) operator;

		Map<Integer, List<SDFAttribute>> attributes = new HashMap<Integer, List<SDFAttribute>>();
		attributes = SDFAttributeHelper.getInstance()
				.getSDFAttributesFromEqualPredicates(attributes, joinOperator);

		int numberOfFragments = 0;
		List<Pair<AbstractStaticFragmentAO, Integer>> fragmentsSinkInPorts = new ArrayList<Pair<AbstractStaticFragmentAO, Integer>>();
		List<AbstractStaticFragmentAO> fragments = new ArrayList<AbstractStaticFragmentAO>();

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
					AbstractStaticFragmentAO fragmentAO;
					try {
						fragmentAO = createFragmentAO(
								settingsForOperator.getFragementationType(),
								settingsForOperator
										.getDegreeOfParallelization(),
								numberOfFragments + "", attributesForSource,
								null, null);
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
						return new TransformationResult(State.FAILED);
					}
					if (fragmentAO == null) {
						return new TransformationResult(State.FAILED);
					}
					transformationResult.addFragmentOperator(fragmentAO);

					Pair<AbstractStaticFragmentAO, Integer> pair = new Pair<AbstractStaticFragmentAO, Integer>();
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
		union.setUniqueIdentifier(UUID.randomUUID().toString());
		transformationResult.setUnionOperator(union);

		int bufferCounter = 0;
		for (int i = 0; i < settingsForOperator.getDegreeOfParallelization(); i++) {
			JoinAO newJoinOperator = joinOperator.clone();
			newJoinOperator.setName(joinOperator.getName() + "_" + i);
			newJoinOperator.setUniqueIdentifier(joinOperator
					.getUniqueIdentifier() + "_" + i);

			for (Pair<AbstractStaticFragmentAO, Integer> pair : fragmentsSinkInPorts) {
				BufferAO buffer = new BufferAO();
				buffer.setName("Buffer_" + bufferCounter);
				buffer.setThreaded(settingsForOperator.isUseThreadedBuffer());
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
						fragments, settingsForOperator);
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

		lastOperatorForParallelization.unsubscribeFromAllSources();
		lastOperatorForParallelization.unsubscribeFromAllSinks();
		
		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			downstreamOperatorSubscription.getTarget().subscribeToSource(union,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					union.getOutputSchema());
		}

		return transformationResult;
	}

	@Override
	public List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes() {
		List<Class<? extends AbstractStaticFragmentAO>> allowedFragmentTypes = new ArrayList<Class<? extends AbstractStaticFragmentAO>>();
		allowedFragmentTypes.add(HashFragmentAO.class);
		return allowedFragmentTypes;
	}

	@Override
	public Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType() {
		return HashFragmentAO.class;
	}

	@Override
	protected void doStrategySpecificPostParallelization(
			ILogicalOperator parallelizedOperator,
			ILogicalOperator currentExistingOperator,
			ILogicalOperator currentClonedOperator, int iteration,
			List<AbstractStaticFragmentAO> fragments,
			ParallelOperatorConfiguration settingsForOperator) {
		if (currentExistingOperator instanceof AggregateAO) {
			SDFAttributeHelper.checkIfAttributesAreEqual((AggregateAO) currentExistingOperator, iteration,
					fragments, settingsForOperator.isAssureSemanticCorrectness());
		}
	}
}
