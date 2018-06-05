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

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

/**
 * Interface for inter operator parallelization strategies
 * 
 * @author ChrisToenjesDeye
 *
 */
public interface IParallelTransformationStrategy<T extends ILogicalOperator> {

	/**
	 * returns the name of this strategy
	 * 
	 * @return
	 */
	String getName();

	/**
	 * returns the operator type (e.g. aggregateAO) which is compatible to this
	 * strategy
	 * 
	 * @return
	 */
	Class<T> getOperatorType();

	/**
	 * returns a list of compatible fragmentation types (e.g. HashFragementAO)
	 * 
	 * @return
	 */
	List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes();

	/**
	 * returns the preferred fragementation type. this is needed if the user
	 * doesnt select a fragmentation
	 * 
	 * @return
	 */
	Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType();

	/**
	 * evaluates the compatibility of this strategy with a given operator. It
	 * also allows to define multiple strategies for a single operator type and
	 * select the best strategy
	 * 
	 * @param operator
	 * @return
	 */
	int evaluateCompatibility(T operator);

	/**
	 * do the specific transformation based on the configuration
	 * 
	 * @return
	 */
	TransformationResult transform(T operator, ParallelOperatorConfiguration configurationForOperator);

	/**
	 * creates a new instance of this strategy 
	 * 
	 * @return instance of strategy
	 */
	IParallelTransformationStrategy<T> getNewInstance();
}
