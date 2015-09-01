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
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
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

	private AbstractStaticFragmentAO fragmentAO;
	private UnionAO union;
	private List<SDFAttribute> groupingAttributes;

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
	public int evaluateCompatibility(AggregateAO operator) {
		if (!operator.getGroupingAttributes().isEmpty()) {
			// only if the given operator has a grouping, this strategy
			// works
			return 100;
		}

		// if operator has no grouping, this strategy is
		// incompatible
		return 0;
	}

	/**
	 * do the specific transformation based on the configuration. Instance need
	 * to be initialized via newInstance method
	 * 
	 * @return
	 */
	@Override
	public TransformationResult transform(AggregateAO operator,
			ParallelOperatorConfiguration configurationForOperator) {
		super.operator = operator;
		super.configuration = configurationForOperator;
		super.transformationResult = new TransformationResult(State.SUCCESS);

		try {
			super.doValidation();

			// prepare and validate values
			prepareTransformation();

			// create fragment operator
			createFragmentOperator();
		} catch (ParallelizationStrategyException pse) {
			transformationResult.setState(State.FAILED);
			return transformationResult;
		}

		// subscribe fragment operator
		subscribeFragementAO();

		// create union operator
		createUnionOperator();

		// for every degree, clone existing operator and connect it to
		// fragement and union operator
		for (int i = 0; i < configuration.getDegreeOfParallelization(); i++) {
			// create buffer operator
			BufferAO buffer = createBufferOperator(i);
			// clone existing aggregate operator
			AggregateAO newAggregateOperator = cloneAggregateOperator(i);
			buffer.subscribeToSource(fragmentAO, 0, i,
					fragmentAO.getOutputSchema());
			newAggregateOperator.subscribeToSource(buffer, 0, 0,
					buffer.getOutputSchema());
			doPostParallelizationIfNeeded(i, newAggregateOperator);
		}
		// connect fragmented data stream with union
		doFinalConnection();
		return transformationResult;
	}

	/**
	 * connect fragmented data stream with union
	 */
	private void doFinalConnection() {
		// get the last operator that need to be parallelized. if no end id
		// is set, the given operator for transformation is selected
		ILogicalOperator lastOperatorForParallelization = null;
		if (configuration.getEndParallelizationId() != null
				&& !configuration.getEndParallelizationId().isEmpty()) {
			lastOperatorForParallelization = LogicalGraphHelper
					.findDownstreamOperatorWithId(
							configuration.getEndParallelizationId(), operator);
		} else {
			lastOperatorForParallelization = operator;
		}

		// remove subscriptions to sink from this operator and connect the
		// union
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
	}

	/**
	 * transform plan until endoperator is reached
	 * 
	 * @param i
	 *            portnumber / iteration
	 * @param newAggregateOperator
	 */
	private void doPostParallelizationIfNeeded(int i,
			AggregateAO newAggregateOperator) {
		if (configuration.getEndParallelizationId() != null
				&& !configuration.getEndParallelizationId().isEmpty()) {

			// check the way to endpoint
			checkIfWayToEndPointIsValid(operator, configuration);

			// if a end operator id is set, do post parallelization
			List<AbstractStaticFragmentAO> fragments = new ArrayList<AbstractStaticFragmentAO>();
			fragments.add(fragmentAO);
			ILogicalOperator lastParallelizedOperator = doPostParallelization(
					operator, newAggregateOperator,
					configuration.getEndParallelizationId(), i);
			union.subscribeToSource(lastParallelizedOperator, i, 0,
					lastParallelizedOperator.getOutputSchema());
		} else {
			union.subscribeToSource(newAggregateOperator, i, 0,
					newAggregateOperator.getOutputSchema());
		}
	}

	/**
	 * clones the existing aggregate operator
	 * 
	 * @param i
	 *            index for naming
	 * @return
	 */
	private AggregateAO cloneAggregateOperator(int i) {
		AggregateAO newAggregateOperator = operator.clone();
		newAggregateOperator.setName(operator.getName() + "_" + i);
		newAggregateOperator.setUniqueIdentifier(UUID.randomUUID().toString());
		newAggregateOperator.setDrainAtClose(true);
		return newAggregateOperator;
	}

	/**
	 * creates a new Buffer operator
	 * 
	 * @param i
	 *            index for naming
	 * @return
	 */
	private BufferAO createBufferOperator(int i) {
		BufferAO buffer = new BufferAO();
		buffer.setName("Buffer_" + i);
		buffer.setThreaded(configuration.isUseThreadedBuffer());
		buffer.setMaxBufferSize(configuration.getBufferSize());
		buffer.setDrainAtClose(false);
		return buffer;
	}

	/**
	 * creates a new Union Operator
	 */
	private void createUnionOperator() {
		union = new UnionAO();
		union.setName("Union");
		union.setUniqueIdentifier(UUID.randomUUID().toString());
		transformationResult.setUnionOperator(union);
	}

	/**
	 * subscribes the created fragment operator to existing upstream operator
	 */
	private void subscribeFragementAO() {
		// subscribe new operator
		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(operator.getSubscribedToSource());

		for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
			// unsubscribe from all sources
			operator.unsubscribeFromSource(upstreamOperatorSubscription);
			// subscribe new fragement operator to these sources
			fragmentAO.subscribeToSource(
					upstreamOperatorSubscription.getTarget(),
					upstreamOperatorSubscription.getSinkInPort(),
					upstreamOperatorSubscription.getSourceOutPort(),
					upstreamOperatorSubscription.getTarget().getOutputSchema());
		}
	}

	/**
	 * creates a fragment operator dynamically based on type
	 * 
	 * @param groupingAttributes
	 * @throws ParallelizationStrategyException
	 */
	private void createFragmentOperator()
			throws ParallelizationStrategyException {
		fragmentAO = null;
		try {
			fragmentAO = createFragmentAO(
					configuration.getFragementationType(),
					configuration.getDegreeOfParallelization(), "",
					groupingAttributes, null, null);
			transformationResult.addFragmentOperator(fragmentAO);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParallelizationStrategyException(
					"Error creating fragment operator");
		}
		if (fragmentAO == null) {
			throw new ParallelizationStrategyException(
					"Error creating fragment operator");
		}
	}

	/**
	 * prepares transformation and validates values
	 * 
	 * @param groupingAttributes
	 * @throws ParallelizationStrategyException
	 */
	private void prepareTransformation()
			throws ParallelizationStrategyException {
		// if there is no grouping
		groupingAttributes = operator.getGroupingAttributes();
		if (groupingAttributes.isEmpty()) {
			throw new ParallelizationStrategyException(
					"Strategy needs gouping attributes");
		}

		transformationResult.setAllowsModificationAfterUnion(true);

		// if the option for using parallel operators is set to true, the given
		// operator is modified to use multiple threads
		if (configuration.isUseParallelOperators()) {
			operator.setNumberOfThreads(configuration
					.getDegreeOfParallelization());
			operator.setUseRoundRobinAllocation(true);
		}
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

	@Override
	public IParallelTransformationStrategy<AggregateAO> getNewInstance() {
		return new GroupedAggregateTransformationStrategy();
	}
}
