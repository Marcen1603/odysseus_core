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

import java.util.UUID;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult.State;

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
		AbstractGroupedOperatorTransformationStrategy<AggregateAO> {


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
		newAggregateOperator.setDrainAtClose(false);
		return newAggregateOperator;
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
	 * returns a new instance of this strategy
	 */
	@Override
	public IParallelTransformationStrategy<AggregateAO> getNewInstance() {
		return new GroupedAggregateTransformationStrategy();
	}
}
