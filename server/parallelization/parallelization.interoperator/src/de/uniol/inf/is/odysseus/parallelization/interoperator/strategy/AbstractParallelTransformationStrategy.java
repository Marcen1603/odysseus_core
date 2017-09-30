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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RangeFragmentAO;

/**
 * abstract class for inter operator parallelization strategies 
 * 
 * @author ChrisToenjesDeye
 */
public abstract class AbstractParallelTransformationStrategy<T extends ILogicalOperator>
		implements IParallelTransformationStrategy<T> {

	protected T operator;
	protected ParallelOperatorConfiguration configuration;
	protected TransformationResult transformationResult;

	/**
	 * returns the selected operator type
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getOperatorType() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		Type type = parameterizedType.getActualTypeArguments()[0];
		if(type instanceof Class<?>){
			return (Class<T>) type;
		} else if(type instanceof TypeVariable<?>){
			Class<T> theClass = (Class<T>) ((TypeVariable<?>) type).getBounds()[0];
			return theClass;
		}
		return null;
	}

	/**
	 * creates are fragementAO dynamically based on the given type
	 * 
	 * @param fragmentClass
	 * @param degreeOfParallelization
	 * @param namePostfix
	 * @param hashAttributes
	 * @param rangeAttributeString
	 * @param rangeRanges
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected AbstractStaticFragmentAO createFragmentAO(
			Class<? extends AbstractStaticFragmentAO> fragmentClass,
			int degreeOfParallelization, String namePostfix,
			List<SDFAttribute> hashAttributes, String rangeAttributeString,
			List<String> rangeRanges) throws InstantiationException,
			IllegalAccessException {
		// create an abstract fragment operator
		AbstractStaticFragmentAO fragmentAO = fragmentClass.newInstance();
		fragmentAO.setNumberOfFragments(degreeOfParallelization);
		fragmentAO.setName(fragmentAO.getName() + namePostfix); // set postfix
		fragmentAO.setUniqueIdentifier(UUID.randomUUID().toString());
		fragmentAO.setHeartbeatrate(0);

		if (fragmentAO instanceof HashFragmentAO) {
			// do some hash specific configurations
			HashFragmentAO hashFragmentAO = (HashFragmentAO) fragmentAO;
			if (hashAttributes != null && !hashAttributes.isEmpty()) {
				hashFragmentAO.setAttributes(hashAttributes);
			} else {
				throw new IllegalArgumentException(
						"Attributes must not be null for creating HashFragment");
			}
		} else if (fragmentAO instanceof RangeFragmentAO) {
			// do some range specific configurations
			RangeFragmentAO rangeFragmentAO = (RangeFragmentAO) fragmentAO;
			if (rangeAttributeString != null && rangeRanges != null
					&& !rangeAttributeString.isEmpty()
					&& !rangeRanges.isEmpty()) {
				rangeFragmentAO.setRanges(rangeRanges);
				rangeFragmentAO.setAttribute(rangeAttributeString);
			}
		}

		return fragmentAO;
	}

	/**
	 * checks if the way from start to end operator is valid. if
	 * assureSemanticCorrectness is set to true an exeception is thrown if the
	 * way is not valid
	 * 
	 * @param operatorForTransformation
	 * @param settingsForOperator
	 * @param aggregatesWithGroupingAllowed
	 */
	protected void checkIfWayToEndPointIsValid(
			ILogicalOperator operatorForTransformation,
			ParallelOperatorConfiguration settingsForOperator) {

		// get end operator and value for assureSemanticCorrectness
		String endParallelizationId = settingsForOperator
				.getEndParallelizationId();
		boolean assureSemanticCorrectness = settingsForOperator
				.isAssureSemanticCorrectness();

		// check the way to endpoint
		LogicalGraphHelper.checkWayToEndPoint(operatorForTransformation,
				endParallelizationId, assureSemanticCorrectness);

	}

	/**
	 * do the post parallelization from the given operator to the operator with
	 * the end id. Iteration parameter is needed because otherwise errors are
	 * shown multiple times.
	 * 
	 * @param existingOperator
	 * @param newOperator
	 * @param endOperatorId
	 * @param iteration
	 * @param fragments
	 * @param settingsForOperator
	 * @return last cloned operator
	 */
	protected ILogicalOperator doPostParallelization(
			ILogicalOperator existingOperator, ILogicalOperator newOperator,
			String endOperatorId, int iteration) {

		ILogicalOperator lastClonedOperator = newOperator;
		ILogicalOperator currentExistingOperator = LogicalGraphHelper
				.getNextOperator(existingOperator);
		ILogicalOperator lastExistingOperator = existingOperator;

		while (currentExistingOperator != null) {

			ILogicalOperator currentClonedOperator = currentExistingOperator
					.clone();
			currentClonedOperator.setName(currentClonedOperator.getName() + "_"
					+ iteration);
			currentClonedOperator.setUniqueIdentifier(UUID.randomUUID()
					.toString());

			CopyOnWriteArrayList<LogicalSubscription> operatorSourceSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
			operatorSourceSubscriptions.addAll(currentExistingOperator
					.getSubscribedToSource());
			for (LogicalSubscription sourceSubscription : operatorSourceSubscriptions) {
				if (sourceSubscription.getSource().equals(lastExistingOperator)) {
					// if target of subscription is last existing operator, set
					// new cloned one
					currentClonedOperator.subscribeToSource(lastClonedOperator,
							sourceSubscription.getSinkInPort(),
							sourceSubscription.getSourceOutPort(),
							lastClonedOperator.getOutputSchema());
				} else {
					// else connect new copied operator to target
					int newSourceOutPort = LogicalGraphHelper
							.calculateNewSourceOutPort(sourceSubscription,
									iteration);

					currentClonedOperator.subscribeToSource(sourceSubscription
							.getSource(), sourceSubscription.getSinkInPort(),
							newSourceOutPort, sourceSubscription.getSource()
									.getOutputSchema());
				}
			}

			// if end operator is reached, break loop and return last cloned
			// operator
			lastClonedOperator = currentClonedOperator;
			if (currentExistingOperator.getUniqueIdentifier() != null) {
				if (currentExistingOperator.getUniqueIdentifier()
						.equalsIgnoreCase(endOperatorId)) {
					break;
				}
			}
			lastExistingOperator = currentExistingOperator;
			currentExistingOperator = LogicalGraphHelper
					.getNextOperator(currentExistingOperator);
		}
		return lastClonedOperator;
	}

	public void doValidation() throws ParallelizationStrategyException {
		// validates if operator and configuration are set
		if (configuration == null || operator == null) {
			throw new ParallelizationStrategyException("");
		}

		if (configuration.getDegreeOfParallelization() == 1) {
			throw new ParallelizationStrategyException("");
		}
	}
}
