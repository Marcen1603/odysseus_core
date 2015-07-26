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

	private AggregateAO operator;
	private ParallelOperatorConfiguration configuration;
	private TransformationResult transformationResult;
	private AbstractStaticFragmentAO fragmentAO;
	private UnionAO union;

	@Override
	public IParallelTransformationStrategy<AggregateAO> getNewInstance(
			ILogicalOperator operator,
			ParallelOperatorConfiguration configurationForOperator) {
		GroupedAggregateTransformationStrategy instance = new GroupedAggregateTransformationStrategy();
		if (operator != null) {
			if (operator instanceof AggregateAO) {
				instance.operator = (AggregateAO) operator;
			} else {
				throw new IllegalArgumentException(
						"Operator type is invalid for strategy " + getName());
			}
		} else {
			throw new IllegalArgumentException(
					"Null value for operator is not allowed");
		}
		if (configurationForOperator != null) {
			instance.configuration = configurationForOperator;
		} else {
			throw new IllegalArgumentException(
					"Null value for configuration is not allowed");
		}

		return instance;
	}

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
	 * do the specific transformation based on the configuration. Instance need
	 * to be initialized via newInstance method
	 * 
	 * @return
	 */
	@Override
	public TransformationResult transform() {
		List<SDFAttribute> groupingAttributes = operator
				.getGroupingAttributes();

		// prepare and validate values
		prepareTransformation(groupingAttributes);

		// create fragment operator
		createFragmentOperator(groupingAttributes);

		if (transformationResult.getState() == State.FAILED) {
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
	 * @param i portnumber / iteration
	 * @param newAggregateOperator
	 */
	private void doPostParallelizationIfNeeded(int i,
			AggregateAO newAggregateOperator) {
		if (configuration.getEndParallelizationId() != null
				&& !configuration.getEndParallelizationId().isEmpty()) {
			// if a end operator id is set, do post parallelization
			List<AbstractStaticFragmentAO> fragments = new ArrayList<AbstractStaticFragmentAO>();
			fragments.add(fragmentAO);
			ILogicalOperator lastParallelizedOperator = doPostParallelization(
					operator, newAggregateOperator,
					configuration.getEndParallelizationId(), i, fragments,
					configuration);
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
	 */
	private void createFragmentOperator(List<SDFAttribute> groupingAttributes) {
		fragmentAO = null;
		try {
			fragmentAO = createFragmentAO(
					configuration.getFragementationType(),
					configuration.getDegreeOfParallelization(), "",
					groupingAttributes, null, null);
			transformationResult.addFragmentOperator(fragmentAO);
		} catch (InstantiationException | IllegalAccessException e) {
			transformationResult.setState(State.FAILED);
		}
		if (fragmentAO == null) {
			transformationResult.setState(State.FAILED);
		}
	}

	/**
	 * prepares transformation and validates values
	 * @param groupingAttributes
	 */
	private void prepareTransformation(List<SDFAttribute> groupingAttributes) {
		transformationResult = new TransformationResult(State.SUCCESS);
		
		// validates if operator and configuration are set
		if (configuration == null || operator == null){
			transformationResult.setState(State.FAILED);
			return;
		}
		
		// validate settings 
		if (!super.areSettingsValid(configuration)) {
			transformationResult.setState(State.FAILED);
			return;
		}

		// if there is no grouping
		if (groupingAttributes.isEmpty()) {
			transformationResult.setState(State.FAILED);
			return;
		}

		// check the way to endpoint if endoperator id is set
		checkIfWayToEndPointIsValid(operator, configuration, true);

		transformationResult.setAllowsModificationAfterUnion(true);

		// if the option for using parallel operators is set to true, the given
		// operator is modified to use multiple threads
		if (configuration.isUseParallelOperators()) {
			operator.setNumberOfThreads(configuration
					.getDegreeOfParallelization());
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
