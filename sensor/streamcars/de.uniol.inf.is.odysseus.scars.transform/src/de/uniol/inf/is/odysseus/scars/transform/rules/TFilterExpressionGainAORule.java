/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator.FilterExpressionGainAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterExpressionGainPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFilterExpressionGainAORule extends AbstractTransformationRule<FilterExpressionGainAO> {

	@Override
	public int getPriority() {
		return 20;
	}
	

	@Override
	public void execute(FilterExpressionGainAO operator, TransformationConfiguration config) {
		System.out.print("CREATE Filter Expression Gain PO...");
		
		FilterExpressionGainPO<?> filterGainPO = new FilterExpressionGainPO();
		filterGainPO.setOutputSchema(operator.getOutputSchema());
		filterGainPO.setPredictedObjectListPath(operator.getPredictedListPath());
		filterGainPO.setScannedObjectListPath(operator.getScannedListPath());
		filterGainPO.setExpressionString(operator.getExpressionString());
		filterGainPO.setRestrictedPredVariables(operator.getRestrictedPredVariables());
		filterGainPO.setRestrictedScanVariables(operator.getRestrictedScanVarialbes());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, filterGainPO);
		for (ILogicalOperator o:toUpdate) {
			update(o);
		}
		insert(filterGainPO);
		retract(operator);
	
		System.out.println("CREATE Filter Expression Gain PO finished.");
	}

	@Override
	public boolean isExecutable(FilterExpressionGainAO operator, TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "FilterExpressionGainAO -> FilterExpressionGainPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
