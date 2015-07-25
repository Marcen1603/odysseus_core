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
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult.State;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

/**
 * Inter operator parallelization strategy for AggregateAO operators, which
 * contains grouping. this strategy used a HashFragmentation on the input
 * stream, clones the specific aggregate operator and combines the datastream
 * with a union operator. the fragmentation is based on the grouping attributes
 * 
 * @author ChrisToenjesDeye
 *
 */
public class GroupedAggregateTransformationStrategy extends
		AbstractParallelTransformationStrategy<AggregateAO> {

	@Override
	public String getName() {
		return "GroupedAggregateTransformationStrategy";
	}

	/**
	 * evaluates the compatibility of this strategy with a given operator. this
	 * strategy is compatible if the aggregate contains a grouping
	 * 
	 * @param operator
	 * @return
	 */
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

	/**
	 * do the specific transformation based on the configuration
	 * 
	 * @param operator
	 * @param configurationForOperator
	 * @return
	 */
	@Override
	public TransformationResult transform(ILogicalOperator operator,
			ParallelOperatorConfiguration configurationForOperator) {
		// validate settings and way to end point
		if (!super.areSettingsValid(configurationForOperator)) {
			return new TransformationResult(State.FAILED);
		}

		// check the way to endpoint if endoperator id is set
		checkIfWayToEndPointIsValid(operator, configurationForOperator, true);

		// create the transformation result object
		TransformationResult transformationResult = new TransformationResult(
				State.SUCCESS);
		transformationResult.setAllowsModificationAfterUnion(true);

		// if the option for using parallel operators is set to true, the given
		// operator is modified to use multiple threads
		AggregateAO aggregateOperator = (AggregateAO) operator;
		if (configurationForOperator.isUseParallelOperators()) {
			aggregateOperator.setNumberOfThreads(configurationForOperator
					.getDegreeOfParallelization());
		}

		// create fragment operator
		AbstractStaticFragmentAO fragmentAO;
		List<SDFAttribute> groupingAttributes = aggregateOperator
				.getGroupingAttributes();
		try {
			fragmentAO = createFragmentAO(
					configurationForOperator.getFragementationType(),
					configurationForOperator.getDegreeOfParallelization(), "",
					groupingAttributes, null, null);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return new TransformationResult(State.FAILED);
		}

		if (fragmentAO == null) {
			return new TransformationResult(State.FAILED);
		}
		transformationResult.addFragmentOperator(fragmentAO);

		// if there is a grouping
		if (!groupingAttributes.isEmpty()) {
			// subscribe new operator
			CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
			upstreamOperatorSubscriptions.addAll(aggregateOperator
					.getSubscribedToSource());

			for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
				// unsubscribe from all sources
				aggregateOperator
						.unsubscribeFromSource(upstreamOperatorSubscription);
				// subscribe new fragement operator to these sources
				fragmentAO.subscribeToSource(upstreamOperatorSubscription
						.getTarget(), upstreamOperatorSubscription
						.getSinkInPort(), upstreamOperatorSubscription
						.getSourceOutPort(), upstreamOperatorSubscription
						.getTarget().getOutputSchema());
			}

			// create union operator
			UnionAO union = new UnionAO();
			union.setName("Union");
			union.setUniqueIdentifier(UUID.randomUUID().toString());
			transformationResult.setUnionOperator(union);

			// for every degree, clone existing operator and connect it to
			// fragement and union operator
			for (int i = 0; i < configurationForOperator
					.getDegreeOfParallelization(); i++) {

				// create buffer operator
				BufferAO buffer = new BufferAO();
				buffer.setName("Buffer_" + i);
				buffer.setThreaded(configurationForOperator
						.isUseThreadedBuffer());
				buffer.setMaxBufferSize(configurationForOperator
						.getBufferSize());
				buffer.setDrainAtClose(false);

				// clone existing aggregate operator
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

				if (configurationForOperator.getEndParallelizationId() != null
						&& !configurationForOperator.getEndParallelizationId()
								.isEmpty()) {
					// if a end operator id is set, do post parallelization
					List<AbstractStaticFragmentAO> fragments = new ArrayList<AbstractStaticFragmentAO>();
					fragments.add(fragmentAO);
					ILogicalOperator lastParallelizedOperator = doPostParallelization(
							aggregateOperator, newAggregateOperator,
							configurationForOperator.getEndParallelizationId(),
							i, fragments, configurationForOperator);
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
			if (configurationForOperator.getEndParallelizationId() != null
					&& !configurationForOperator.getEndParallelizationId()
							.isEmpty()) {
				lastOperatorForParallelization = LogicalGraphHelper
						.findDownstreamOperatorWithId(configurationForOperator
								.getEndParallelizationId(), aggregateOperator);
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

	/**
	 * returns a list of compatible fragmentation types (e.g. HashFragementAO)
	 * 
	 * @return
	 */
	@Override
	public List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes() {
		List<Class<? extends AbstractStaticFragmentAO>> allowedFragmentTypes = new ArrayList<Class<? extends AbstractStaticFragmentAO>>();
		allowedFragmentTypes.add(HashFragmentAO.class);
		return allowedFragmentTypes;
	}

	/**
	 * returns the preferred fragementation type. this is needed if the user
	 * doesnt select a fragmentation
	 * 
	 * @return
	 */
	@Override
	public Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType() {
		return HashFragmentAO.class;
	}

	/**
	 * abstract method to allow strategy specific post parallelization works or
	 * validations. this method is used in postParalleliaztion for each operator
	 * between start and end
	 * 
	 * @param parallelizedOperator
	 * @param currentExistingOperator
	 * @param currentClonedOperator
	 * @param iteration
	 * @param fragments
	 * @param settingsForOperator
	 */
	@Override
	protected void doStrategySpecificPostParallelization(
			ILogicalOperator parallelizedOperator,
			ILogicalOperator currentExistingOperator,
			ILogicalOperator currentClonedOperator, int iteration,
			List<AbstractStaticFragmentAO> fragments,
			ParallelOperatorConfiguration settingsForOperator) {
		if (currentExistingOperator instanceof AggregateAO) {
			SDFAttributeHelper.checkIfAttributesAreEqual(
					(AggregateAO) currentExistingOperator, iteration,
					fragments,
					settingsForOperator.isAssureSemanticCorrectness());
		}
	}

}
