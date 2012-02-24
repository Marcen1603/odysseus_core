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

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.operator.filter.ao.FilterExpressionCovarianceUpdateAO;
import de.uniol.inf.is.odysseus.scars.operator.filter.po.FilterExpressionCovarianceUpdatePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"rawtypes","unchecked"})
public class TFilterExpressionCovarianceAORule extends AbstractTransformationRule<FilterExpressionCovarianceUpdateAO> {

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public void execute(FilterExpressionCovarianceUpdateAO operator, TransformationConfiguration config) {
		
		System.out.print("CREATE Filter Expression Covariance PO...");
		FilterExpressionCovarianceUpdatePO filterCovarianceUpdatePO = new FilterExpressionCovarianceUpdatePO();
		filterCovarianceUpdatePO.setOutputSchema(operator.getOutputSchema());
		
		//TODO variablen getten und setten
		filterCovarianceUpdatePO.setPredictedObjectListPath(operator.getPredictedListPath());
		filterCovarianceUpdatePO.setScannedObjectListPath(operator.getScannedListPath());
		filterCovarianceUpdatePO.setExpressionString(operator.getExpressionString());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, filterCovarianceUpdatePO);
		for (ILogicalOperator o:toUpdate) {
			update(o);
		}
		insert(filterCovarianceUpdatePO);
		retract(operator);
		System.out.println("CREATE Filter Expression Covariance PO finished.");
	}

	@Override
	public boolean isExecutable(FilterExpressionCovarianceUpdateAO operator,TransformationConfiguration config) {
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
		return "FilterExpressionCovarianceUpdateAO -> FilterExpressionCovarianceUpdatePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
