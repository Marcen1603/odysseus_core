/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;

/**
 * Interface for a plan generation method.
 * 
 * @author Merlin Wasmann
 *
 */
public interface IPlanGenerationMethod {

	/**
	 * Generate a list of logical plans based on the given plan and configuration.
	 * 
	 * @param plan Logical plan on which the list of plans is based.
	 * @param config Configuration for the different plan generation methods.
	 * @return a list of logical plans.
	 */
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan, PlanGenerationConfiguration config, IOperatorOwner owner);
}
