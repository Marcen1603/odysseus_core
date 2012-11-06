/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.planmanagement.optimization.plan.standardplanoptimizer;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * StandardPlanOptimizer is the standard plan optimizer used by odysseus. 
 * 
 * @author Wolf Bauer
 * 
 */
public class StandardPlanOptimizer implements IPlanOptimizer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan.IPlanOptimizer
	 * #optimizePlan
	 * (de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IPlanOptimizable,
	 * de
	 * .uniol.inf.is.odysseus.planmanagement.optimization.OptimizationConfiguration
	 * . OptimizationConfiguration, java.util.List)
	 */
	@Override
	public void optimizePlan(OptimizationConfiguration parameters, IExecutionPlan toOptimize, IDataDictionary dd)
			throws QueryOptimizationException {
	
		ArrayList<IIterableSource<?>> leafSources = new ArrayList<IIterableSource<?>>();

		// Get Roots, PartialPlans and IIterableSource for the execution plan.
		// Each query will be one PartialPlan. Duplicated operators will be
		// ignored.
		for (IPhysicalQuery query : toOptimize.getQueries()) {
			leafSources.addAll(query.getLeafSources());
		} // for (IQuery query : allQueries)

		// Update ExecutionPlan with the found informations.
		toOptimize.setLeafSources(leafSources);

	}

}
