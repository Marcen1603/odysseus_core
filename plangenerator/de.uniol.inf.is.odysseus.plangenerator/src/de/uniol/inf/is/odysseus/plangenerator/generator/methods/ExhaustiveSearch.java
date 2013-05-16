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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectLogicalChildrenPlanVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.plangenerator.util.PlanGeneratorHelper;

/**
 * This plan generation method performes an exhaustive search over all possible
 * semantiv equivalent plans of the given plan.
 * 
 * @author Merlin Wasmann
 * 
 */
public class ExhaustiveSearch extends AbstractPruningPlanGenerationMethod {

	private static final Logger LOG = LoggerFactory
			.getLogger(ExhaustiveSearch.class);

	public ExhaustiveSearch() {}
	
	public ExhaustiveSearch(ICostModel<ILogicalOperator> costModel) {
		this.costModel = costModel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.odysseus.plangenerator.generator.methods.IPlanGenerationMethod
	 * #
	 * generatePlans(de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator
	 * , de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.
	 * configuration.PlanGenerationConfiguration)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan,
			PlanGenerationConfiguration config, IOperatorOwner owner) {
		List<ILogicalOperator> plans = new ArrayList<ILogicalOperator>();

		initialize(plan);

		// walk through the different join plans.
		this.nWayJoinList = new ArrayList<Collection<ILogicalOperator>>();
		for (int i = 2; i <= getMaxJoinCount(); i++) {
			LOG.debug("Plans for i = " + i);
			createJoinPlans(i, this.nWayJoinList);
		}

		// the last collection in this list contains the max-way join plans.
		Collection<ILogicalOperator> joinPlans = this.nWayJoinList
				.get(this.nWayJoinList.size() - 1);

		LOG.debug("Print resulting Join plans.");
		for (ILogicalOperator joinPlan : joinPlans) {
			ILogicalOperator filledPlan = fillPlanWithRemainingOperators(joinPlan);
			if (PlanGeneratorHelper.hasValidWindowPositions(filledPlan)) {
				plans.add(filledPlan);
			} else {
				LOG.error("Plan: " + filledPlan.getClass().getSimpleName() + " (" + filledPlan.hashCode() + ") was not valid");
			}
			// plans.add(joinPlan);
		}
		
		List<ICost<ILogicalOperator>> costs = new ArrayList<ICost<ILogicalOperator>>();
		Map<ICost<ILogicalOperator>, ILogicalOperator> costMap = new HashMap<ICost<ILogicalOperator>, ILogicalOperator>();
		for(ILogicalOperator p : plans) {
			List<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			
			GenericGraphWalker walker = new GenericGraphWalker();
			CollectLogicalChildrenPlanVisitor<ILogicalOperator> visitor = new CollectLogicalChildrenPlanVisitor<ILogicalOperator>();
			walker.prefixWalk(plan, visitor);
			
			operators.addAll(visitor.getResult());
			
			ICost<ILogicalOperator> cost = this.costModel.estimateCost(operators, true);
			
			costs.add(cost);
			costMap.put(cost, p);
			LOG.debug("Cost: {} for plan {}", cost, p);
		}
		
		List<ILogicalOperator> sorted = new ArrayList<ILogicalOperator>();
		Collections.sort(costs);
		LOG.debug("Costs.size: {}", costs.size());
		LOG.debug("CostMap.size: {}", costMap.size());
		for(int i = costs.size() - 1; i >= 0; i--) {
			LOG.debug("Added plan {}", costMap.get(costs.get(i)));
			sorted.add(costMap.get(costs.get(i)));
		}


		// set the owners
		for (ILogicalOperator p : sorted) {
			PlanGeneratorHelper.setNewOwnerForPlan(p, owner);
			PlanGeneratorHelper.printPlan("With owner", p);
			LOG.debug("Outputschema: {}", p.getOutputSchema());
		}
		
		return sorted;
		// if(!getPredicateHelper().allPredicatesSatisfied()) {
		// System.err.println("[ExhaustiveSearch] Not all predicates have been satisfied.");
		// }

		//return plans;
	}

}
