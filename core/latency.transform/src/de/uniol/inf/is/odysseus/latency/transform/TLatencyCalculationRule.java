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
package de.uniol.inf.is.odysseus.latency.transform;

import de.uniol.inf.is.odysseus.latency.LatencyCalculationPipe;
import de.uniol.inf.is.odysseus.logicaloperator.latency.LatencyCalculationAO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLatencyCalculationRule extends
		AbstractTransformationRule<LatencyCalculationAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(LatencyCalculationAO operator,
			TransformationConfiguration config) {
		@SuppressWarnings("rawtypes")
		LatencyCalculationPipe pO = new LatencyCalculationPipe();
		pO.setOutputSchema(operator.getOutputSchema());
		replace(operator, pO, config);		
		retract(operator);		
	}

	@Override
	public boolean isExecutable(LatencyCalculationAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "LatencyCalculationAO -> LatencyCalculationPipe";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
