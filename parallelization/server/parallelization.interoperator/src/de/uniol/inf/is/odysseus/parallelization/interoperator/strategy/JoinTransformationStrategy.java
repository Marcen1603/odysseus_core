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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.parallelization.helper.SDFAttributeHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult.State;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class JoinTransformationStrategy extends
		AbstractParallelTransformationStrategy<JoinAO> {

	private UnionAO union;
	private List<Pair<AbstractStaticFragmentAO, Integer>> fragmentsSinkInPorts;
	private List<AbstractStaticFragmentAO> fragments;
	private Map<Integer, List<SDFAttribute>> attributes;

	@Override
	public String getName() {
		return "JoinTransformationStrategy";
	}

	/**
	 * evaluates the compatibility of this strategy with a given operator. this
	 * strategy is compatible of the join predicate has the correct structure
	 * 
	 * @param operator
	 * @return
	 */
	@Override
	public int evaluateCompatibility(JoinAO operator) {
		if (operator.getPredicate() != null) {
			if (SDFAttributeHelper.getInstance().validateStructureOfPredicate(
					operator)) {
				// if the join operator has an join predicate, this strategy
				// is
				// compatible
				return 100;
			}
		}

		// if operator has no join predicate, this strategy
		// doesn't work
		return 0;
	}

	/**
	 * do the specific transformation based on the configuration. Instance need
	 * to be initialized via newInstance method
	 * 
	 * @return
	 */
	@Override
	public TransformationResult transform(JoinAO operator,
			ParallelOperatorConfiguration configurationForOperator) {
		super.operator = operator;
		super.configuration = configurationForOperator;
		super.transformationResult = new TransformationResult(State.SUCCESS);

		try {
			super.doValidation();
			prepareTransformation();
			createAndSubscribeFragments();
		} catch (ParallelizationStrategyException e) {
			transformationResult.setState(State.FAILED);
			return transformationResult;
		}

		createUnionOperator();

		// for every degree, insert buffer, clone join operator and connect
		// everything
		int bufferCounter = 0;
		for (int i = 0; i < configuration.getDegreeOfParallelization(); i++) {
			JoinAO newJoinOperator = cloneJoinOperator(i);
			bufferCounter = doSubscriptions(bufferCounter, i, newJoinOperator);
			doPostParallelizationIfNeeded(i, newJoinOperator);
		}
		// connect fragmented data stream with union
		doFinalConnection();
		return transformationResult;
	}

	/**
	 * connect fragmented data stream with union
	 */
	private void doFinalConnection() {
		// get the last operator that need to be parallelized. if no end id is
		// set, the given operator for transformation is selected
		ILogicalOperator lastOperatorForParallelization = null;
		if (configuration.getEndParallelizationId() != null
				&& !configuration.getEndParallelizationId().isEmpty()) {
			lastOperatorForParallelization = LogicalGraphHelper
					.findDownstreamOperatorWithId(
							configuration.getEndParallelizationId(), operator);
		} else {
			lastOperatorForParallelization = operator;
		}

		CopyOnWriteArrayList<LogicalSubscription> downstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		downstreamOperatorSubscriptions.addAll(lastOperatorForParallelization
				.getSubscriptions());

		// unsibscribe existing operator from all sources and include new
		// operator
		lastOperatorForParallelization.unsubscribeFromAllSources();
		lastOperatorForParallelization.unsubscribeFromAllSinks();

		for (LogicalSubscription downstreamOperatorSubscription : downstreamOperatorSubscriptions) {
			downstreamOperatorSubscription.getSource().subscribeToSource(union,
					downstreamOperatorSubscription.getSinkInPort(),
					downstreamOperatorSubscription.getSourceOutPort(),
					union.getOutputSchema());
		}
		
		for (AbstractStaticFragmentAO fragmentAO : transformationResult.getFragmentOperators()) {
			fragmentAO.initialize();
		}
	}

	/**
	 * transform plan until endoperator is reached
	 * 
	 * @param i
	 *            portNumber / iteration
	 * @param newAggregateOperator
	 */
	private void doPostParallelizationIfNeeded(int i, JoinAO newJoinOperator) {
		if (configuration.getEndParallelizationId() != null
				&& !configuration.getEndParallelizationId().isEmpty()) {
			// check if the way to endpoint is valid,
			checkIfWayToEndPointIsValid(operator, configuration);

			// if endoperator id is set, do post parallelization
			ILogicalOperator lastParallelizedOperator = doPostParallelization(
					operator, newJoinOperator,
					configuration.getEndParallelizationId(), i);
			union.subscribeToSource(lastParallelizedOperator, i, 0,
					lastParallelizedOperator.getOutputSchema());
		} else {
			union.subscribeToSource(newJoinOperator, i, 0,
					newJoinOperator.getOutputSchema());
		}
	}

	/**
	 * subscribes fragment to buffer and buffer to new join operator
	 * 
	 * @param bufferCounter
	 * @param i
	 * @param newJoinOperator
	 * @return
	 */
	private int doSubscriptions(int bufferCounter, int i, JoinAO newJoinOperator) {
		// for every fragementation operator do subscriptions
		for (Pair<AbstractStaticFragmentAO, Integer> pair : fragmentsSinkInPorts) {
			BufferAO buffer = createBufferOperator(bufferCounter);
			bufferCounter++;

			buffer.subscribeToSource(pair.getE1(), 0, i, pair.getE1()
					.getOutputSchema());

			newJoinOperator.subscribeToSource(buffer, pair.getE2(), 0,
					buffer.getOutputSchema());
		}
		return bufferCounter;
	}

	/**
	 * creates a new buffer operator
	 * 
	 * @param bufferCounter
	 * @return
	 */
	private BufferAO createBufferOperator(int bufferCounter) {
		BufferAO buffer = new BufferAO();
		buffer.setName("Buffer_" + bufferCounter);
		buffer.setThreaded(configuration.isUseThreadedBuffer());
		buffer.setMaxBufferSize(configuration.getBufferSize());
		
		return buffer;
	}

	/**
	 * clones an existing join operator
	 * 
	 * @param i
	 *            index for naming
	 * @return
	 */
	private JoinAO cloneJoinOperator(int i) {
		// clone join operator
		JoinAO newJoinOperator = operator.clone();
		newJoinOperator.setName(operator.getName() + "_" + i);
		newJoinOperator.setUniqueIdentifier(UUID.randomUUID().toString());
		return newJoinOperator;
	}

	/**
	 * creates a new union operator
	 */
	private void createUnionOperator() {
		union = new UnionAO();
		union.setName("Union");
		union.setUniqueIdentifier(UUID.randomUUID().toString());
		transformationResult.setUnionOperator(union);
	}

	/**
	 * creates the different fragment operators for both input streams of join
	 * operator
	 * 
	 * @throws ParallelizationStrategyException
	 */
	private void createAndSubscribeFragments()
			throws ParallelizationStrategyException {
		int numberOfFragments = 0;
		fragmentsSinkInPorts = new ArrayList<Pair<AbstractStaticFragmentAO, Integer>>();
		fragments = new ArrayList<AbstractStaticFragmentAO>();

		CopyOnWriteArrayList<LogicalSubscription> upstreamOperatorSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		upstreamOperatorSubscriptions.addAll(operator.getSubscribedToSource());

		// for each input port of the join
		for (Integer inputPort : attributes.keySet()) {
			// get all source subscriptions
			for (LogicalSubscription upstreamOperatorSubscription : upstreamOperatorSubscriptions) {
				if (upstreamOperatorSubscription.getSinkInPort() == inputPort) {
					List<SDFAttribute> attributesForSource = attributes
							.get(inputPort);
					// if join predicate attribute references on the source
					// from
					// this subscription, create fragment operator
					// Fragment operator
					AbstractStaticFragmentAO fragmentAO = null;
					try {
						fragmentAO = createFragmentAO(
								configuration.getFragementationType(),
								configuration.getDegreeOfParallelization(),
								numberOfFragments + "", attributesForSource,
								null, null);
					} catch (InstantiationException | IllegalAccessException e) {
						throw new ParallelizationStrategyException("");
					}
					if (fragmentAO == null) {
						throw new ParallelizationStrategyException("");
					}
					transformationResult.addFragmentOperator(fragmentAO);
					storeFragmentOperators(upstreamOperatorSubscription,
							fragmentAO);
					subscribeFragmentOperator(upstreamOperatorSubscription,
							fragmentAO);
					numberOfFragments++;
					break;
				}
			}
		}
	}

	/**
	 * subscribe fragment operator to existing upstream operator
	 * 
	 * @param upstreamOperatorSubscription
	 * @param fragmentAO
	 */
	private void subscribeFragmentOperator(
			LogicalSubscription upstreamOperatorSubscription,
			AbstractStaticFragmentAO fragmentAO) {
		// unsubscribe the join and subscribe the fragemnt operator
		operator.unsubscribeFromSource(upstreamOperatorSubscription);
		fragmentAO.subscribeToSource(upstreamOperatorSubscription.getSink(),
				0, upstreamOperatorSubscription.getSourceOutPort(),
				upstreamOperatorSubscription.getSink().getOutputSchema());
	}

	/**
	 * saves the created fragment operators for later usage
	 * 
	 * @param upstreamOperatorSubscription
	 * @param fragmentAO
	 */
	private void storeFragmentOperators(
			LogicalSubscription upstreamOperatorSubscription,
			AbstractStaticFragmentAO fragmentAO) {
		// save the fragement operator and the input port in a map
		Pair<AbstractStaticFragmentAO, Integer> pair = new Pair<AbstractStaticFragmentAO, Integer>();
		pair.setE1(fragmentAO);
		pair.setE2(upstreamOperatorSubscription.getSinkInPort());
		fragmentsSinkInPorts.add(pair);
		fragments.add(fragmentAO);
	}

	/**
	 * prepares transformation and validates values
	 * 
	 * @throws ParallelizationStrategyException
	 */
	private void prepareTransformation()
			throws ParallelizationStrategyException {
		transformationResult.setAllowsModificationAfterUnion(true);

		attributes = new HashMap<Integer, List<SDFAttribute>>();
		attributes = SDFAttributeHelper.getInstance()
				.getSDFAttributesFromEqualPredicates(attributes, operator);
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
	 * returns a new instance of this strategy
	 */
	@Override
	public IParallelTransformationStrategy<JoinAO> getNewInstance() {
		return new JoinTransformationStrategy();
	}
}
