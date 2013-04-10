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

package de.uniol.inf.is.odysseus.plangenerator.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.plangeneration.IPlanGenerator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.plangenerator.generator.methods.IPlanGenerationMethod;
import de.uniol.inf.is.odysseus.plangenerator.generator.methods.PlanGenerationMethodFactory;

/**
 * @author Merlin Wasmann
 *
 */
public class PlanGeneratorExecutor implements IPlanGenerator {

	private Map<String, ICostModel<?>> costModels = new HashMap<String, ICostModel<?>>();
	private ICostModel<ILogicalOperator> selectedCostModel;
	
	/**
	 * This method generates semantically equivalent logical plans from one plan.
	 * 
	 * @param plan 
	 * @param planGenerationConfig
	 * @return a list of logical plans. first entry is the best possible plan at this point.
	 */
	@Override
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan,
			PlanGenerationConfiguration planGenerationConfig, IOperatorOwner owner) {
				
		IPlanGenerationMethod method = PlanGenerationMethodFactory.createPlanGenerationMethod(planGenerationConfig, getSelectedCostModelInstance());
		
		return method.generatePlans(plan, planGenerationConfig, owner);
	}

	public void bindCostModel(ICostModel<?> costModel) {
		costModels.put(costModel.getClass().getSimpleName(), costModel);

		if (getSelectedCostModel() == null) {
			selectCostModel(costModel.getClass().getSimpleName());
		}
	}

	public void unbindCostModel(ICostModel<?> costModel) {
		costModels.remove(costModel.getClass().getSimpleName());
	}
	
	@SuppressWarnings("unchecked")
	void selectCostModel(String model) {
		if (!costModels.containsKey(model))
			throw new RuntimeException("CostModel " + model + " not found");

		this.selectedCostModel = (ICostModel<ILogicalOperator>) costModels.get(model);
	}
	
	String getSelectedCostModel() {
		if(this.selectedCostModel == null) {
			return null;
		}
		return this.selectedCostModel.getClass().getSimpleName();
	}
	
	ICostModel<ILogicalOperator> getSelectedCostModelInstance() {
		return this.selectedCostModel;
	}

}
