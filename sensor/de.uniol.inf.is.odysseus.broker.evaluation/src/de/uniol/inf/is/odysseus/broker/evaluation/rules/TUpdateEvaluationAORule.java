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
package de.uniol.inf.is.odysseus.broker.evaluation.rules;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.UpdateEvaluationAO;
import de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator.UpdateEvaluationPO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
@SuppressWarnings({"unchecked","rawtypes"})
public class TUpdateEvaluationAORule extends AbstractTransformationRule<UpdateEvaluationAO> {

	@Override
	public int getPriority() {		
		return 5;
	}

	@Override
	public void execute(UpdateEvaluationAO operator, TransformationConfiguration config) {			
		if(!exists(operator)){
			UpdateEvaluationPO<?> updatePO = new UpdateEvaluationPO<IMetaAttributeContainer<ITimeInterval>>(operator.getNumber());
			updatePO.setOutputSchema(operator.getOutputSchema());			
			Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator,updatePO);							
			for (ILogicalOperator o:toUpdate){			
				update(o);			
			}		
			insert(updatePO);
		}
		retract(operator);			
	}

	@Override
	public boolean isExecutable(UpdateEvaluationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	private boolean exists(UpdateEvaluationAO operator){		
		List<UpdateEvaluationPO> liste = super.getAllOfSameTyp(new UpdateEvaluationPO(0));
		for(UpdateEvaluationPO<?> po : liste){			
			if(po.getNumber() == operator.getNumber()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getName() {		
		return "UpdateEvaluationAO -> UpdateEvaluationPO";
	}

	
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
