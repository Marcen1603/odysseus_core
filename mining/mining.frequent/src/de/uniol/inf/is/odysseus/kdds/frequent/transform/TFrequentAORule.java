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
package de.uniol.inf.is.odysseus.kdds.frequent.transform;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.kdds.frequent.physical.LossyCountingFrequentItem;
import de.uniol.inf.is.odysseus.kdds.frequent.physical.SimpleFrequentItemPO;
import de.uniol.inf.is.odysseus.kdds.frequent.physical.SpaceSavingFrequentItem;
import de.uniol.inf.is.odysseus.logicaloperator.kdds.frequent.FrequentItemAO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFrequentAORule extends AbstractTransformationRule<FrequentItemAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(FrequentItemAO operator, TransformationConfiguration config) {
		IPhysicalOperator po = null;
		switch (operator.getStrategy()) {
		case Simple:
			po = new SimpleFrequentItemPO<RelationalTuple<?>>(operator.getRestrictList(), operator.getSize());
			break;
		case LossyCounting:
			po = new LossyCountingFrequentItem<RelationalTuple<?>>(operator.getRestrictList(), operator.getSize());
			break;
		case SpaceSaving:
			po = new SpaceSavingFrequentItem<RelationalTuple<?>>(operator.getRestrictList(), operator.getSize());
			break;
		default:
			break;
		}
		if(po!=null){
			po.setOutputSchema(operator.getOutputSchema());
			replace(operator, po, config);
			retract(operator);
		}
		
	}

	@Override
	public boolean isExecutable(FrequentItemAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "FrequentItemAO -> *Strategy*FrequentItemPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
