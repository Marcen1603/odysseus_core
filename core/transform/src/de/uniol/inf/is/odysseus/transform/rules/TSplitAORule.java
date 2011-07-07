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
package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.logicaloperator.SplitAO;
import de.uniol.inf.is.odysseus.physicaloperator.SplitPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TSplitAORule extends AbstractTransformationRule<SplitAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	
	@Override
	public void execute(SplitAO splitAO, TransformationConfiguration transformConfig) {		
		SplitPO<?> splitPO = new SplitPO(splitAO.getPredicates());
		splitPO.setOutputSchema(splitAO.getOutputSchema());
		replace(splitAO, splitPO, transformConfig);
		retract(splitAO);

	}

	@Override
	public boolean isExecutable(SplitAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SplitAO -> SplitPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return SplitAO.class;
	}

}
