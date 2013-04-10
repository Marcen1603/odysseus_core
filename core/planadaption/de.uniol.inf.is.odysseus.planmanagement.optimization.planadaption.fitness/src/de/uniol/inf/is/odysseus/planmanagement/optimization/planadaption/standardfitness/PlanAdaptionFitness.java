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

package de.uniol.inf.is.odysseus.planmanagement.optimization.planadaption.standardfitness;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionFitness;

/**
 * 
 * @author Merlin Wasmann
 * 
 */
public class PlanAdaptionFitness implements IPlanAdaptionFitness {

	private final Logger LOG = LoggerFactory.getLogger(PlanAdaptionFitness.class);
	
	private Map<String, ICostModel<?>> costModels = new HashMap<String, ICostModel<?>>();
	private ICostModel<ILogicalOperator> selectedCostModel;
	private Map<ILogicalOperator, ICost<ILogicalOperator>> operatorCosts = new HashMap<ILogicalOperator, ICost<ILogicalOperator>>();

	@Override
	public Pair<ILogicalOperator, ICost<ILogicalOperator>> pickFittestPlan(
			ILogicalQuery query) {
		if(query == null) {
			throw new IllegalArgumentException("Query is null.");
		}
		List<ILogicalOperator> plans = query.getAlternativeLogicalPlans();
		if(plans == null || plans.isEmpty()) {
			// no alternative plans, so the current plan is the fittest of the all :)
			return new Pair<ILogicalOperator, ICost<ILogicalOperator>>(query.getLogicalPlan(), null);
		}
		estimateAllPlanCosts(plans);
		
		List<ILogicalOperator> fitter = getFitterPlans(query.getLogicalPlan());
		if(fitter.isEmpty()) {
			return new Pair<ILogicalOperator, ICost<ILogicalOperator>>(query.getLogicalPlan(), null);
		}
		
		ILogicalOperator fittest = fitter.get(0);
		ICost<ILogicalOperator> costDifference = this.operatorCosts.get(query.getLogicalPlan()).substract(this.operatorCosts.get(fittest));
		return new Pair<ILogicalOperator, ICost<ILogicalOperator>>(fittest, costDifference);
	}

	private void estimateAllPlanCosts(List<ILogicalOperator> plans) {
		for(ILogicalOperator plan : plans) {
			LOG.debug("Estimating costs for plan: " + plan);
			List<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			operators.add(plan);
			ICost<ILogicalOperator> cost = this.selectedCostModel.estimateCost(operators, true);
			LOG.debug("Costs: " + cost.toString());
			this.operatorCosts.put(plan, cost);
		}
	}
	
	private List<ILogicalOperator> getFitterPlans(ILogicalOperator plan) {
		List<ILogicalOperator> fitter = new ArrayList<ILogicalOperator>();
		
		ValueComparator vc = new ValueComparator(this.operatorCosts);
		TreeMap<ILogicalOperator, ICost<ILogicalOperator>> sorted = new TreeMap<ILogicalOperator, ICost<ILogicalOperator>>(vc);
		sorted.putAll(this.operatorCosts);
		
		for(ILogicalOperator p : sorted.navigableKeySet()) {
			if(!p.equals(plan)) {
				fitter.add(p);
			} else {
				break;
			}
		}
		return fitter;
	}
	
	private class ValueComparator implements Comparator<ILogicalOperator> {
		Map<ILogicalOperator, ICost<ILogicalOperator>> base;
		public ValueComparator(Map<ILogicalOperator, ICost<ILogicalOperator>> base) {
			this.base = base;
		}
		
		public int compare(ILogicalOperator a, ILogicalOperator b) {
			return base.get(a).compareTo(base.get(b));
		}
	}
	
	public void bindCostModel(ICostModel<?> costModel) {
		this.costModels.put(costModel.getClass().getSimpleName(), costModel);		
		LOG.debug("CostModel: " + costModel.getClass().getSimpleName() + " bound");
		if (getSelectedCostModel() == null) {
			selectCostModel(costModel.getClass().getSimpleName());
		}
	}

	public void unbindCostModel(ICostModel<?> costModel) {
		this.costModels.remove(costModel.getClass().getSimpleName());
		LOG.debug("CostModel: " + costModel.getClass().getSimpleName() + " unbound");
	}

	String getSelectedCostModel() {
		if (this.selectedCostModel == null) {
			return null;
		}
		return this.selectedCostModel.getClass().getSimpleName();
	}

	@SuppressWarnings("unchecked")
	public void selectCostModel(String costModel) {
		if (!this.costModels.containsKey(costModel)) {
			throw new RuntimeException("CostModel " + costModel + " not found.");
		}
		this.selectedCostModel = (ICostModel<ILogicalOperator>) this.costModels
				.get(costModel);
	}

}
