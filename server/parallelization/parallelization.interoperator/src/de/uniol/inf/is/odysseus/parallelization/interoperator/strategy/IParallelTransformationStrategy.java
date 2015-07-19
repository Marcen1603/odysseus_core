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

public interface IParallelTransformationStrategy<T extends ILogicalOperator> {
	
	String getName();
	
	Class<T> getOperatorType();
	
	List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes();
	
	Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType();
	
	int evaluateCompatibility(ILogicalOperator operator);
	
	TransformationResult transform(ILogicalOperator operator, ParallelOperatorConfiguration settingsForOperator);
}
