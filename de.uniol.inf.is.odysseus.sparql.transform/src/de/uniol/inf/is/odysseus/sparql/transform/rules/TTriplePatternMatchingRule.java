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
package de.uniol.inf.is.odysseus.sparql.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sparql.logicaloperator.TriplePatternMatching;
import de.uniol.inf.is.odysseus.sparql.physicaloperator.TriplePatternMatchingPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTriplePatternMatchingRule<M extends IMetaAttribute> extends AbstractTransformationRule<TriplePatternMatching> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(TriplePatternMatching tpmAO, TransformationConfiguration transformConfig) {
		
		TriplePatternMatchingPO<M> tpmPO = new TriplePatternMatchingPO<M>(tpmAO);
		
		
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(tpmAO, tpmPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(tpmPO);
		retract(tpmAO);		
	}

	@Override
	public boolean isExecutable(TriplePatternMatching operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "TriplePatternMatching (AO) -> TriplePatternMatching (PO)";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
