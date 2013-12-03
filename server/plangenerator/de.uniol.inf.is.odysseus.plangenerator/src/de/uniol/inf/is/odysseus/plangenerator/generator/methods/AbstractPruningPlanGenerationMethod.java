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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectLogicalChildrenPlanVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;

/**
 * @author Merlin Wasmann
 *
 */
public abstract class AbstractPruningPlanGenerationMethod extends
		AbstractPlanGenerationMethod {
	
	// top k plans should survive
	protected int topKJoin;
	protected int topKFill;
	
	protected ICostModel<ILogicalOperator> costModel;

	/* (non-Javadoc)
	 * @see de.uniol.inf.odysseus.plangenerator.generator.methods.AbstractPlanGenerationMethod#generatePlans(de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator, de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration)
	 */
	@Override
	public abstract List<ILogicalOperator> generatePlans(ILogicalOperator plan,
			PlanGenerationConfiguration config, IOperatorOwner owner);
	
	/**
	 * Returns a collection of the top k plans. Costs are calculated with a CostModel
	 * 
	 * @param plans
	 * @param k The top k plans will survive.
	 * @return a collection of the top k plans.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<ILogicalOperator> prunePlans(Collection<ILogicalOperator> plans, int k) {
		List<ILogicalOperator> prunedPlans = new ArrayList<ILogicalOperator>();
		
		// mit Hilfe des CostModels, das noch umgestellt werden muss, werden hier die Kosten für alle Pläne geschätzt.
		// danach werden die besten k Pläne zurückgegeben.
		Map<ICost<ILogicalOperator>, ILogicalOperator> costMap = new HashMap<ICost<ILogicalOperator>, ILogicalOperator>();
		List<ICost<ILogicalOperator>> costs = new ArrayList<ICost<ILogicalOperator>>();
		for(ILogicalOperator plan : plans) {
			List<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			
			GenericGraphWalker walker = new GenericGraphWalker();
			CollectLogicalChildrenPlanVisitor<ILogicalOperator> visitor = new CollectLogicalChildrenPlanVisitor<ILogicalOperator>();
			walker.prefixWalk(plan, visitor);
			
			operators.addAll(visitor.getResult());
			ICost<ILogicalOperator> cost = this.costModel.estimateCost(operators, false);
			costs.add(cost);
			costMap.put(cost, plan);
		}

		Collections.sort(costs);
		
		for(int i = 0; i < k; i++) {
			if(i < costs.size()) {
				prunedPlans.add(costMap.get(costs.get(i)));
			}
		}
		
		return prunedPlans;
	}
}
