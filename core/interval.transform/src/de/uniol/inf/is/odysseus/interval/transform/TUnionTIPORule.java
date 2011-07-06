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
package de.uniol.inf.is.odysseus.interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.UnionPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUnionTIPORule extends AbstractTransformationRule<UnionAO> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(UnionAO unionAO, TransformationConfiguration transformConfig) {
		UnionPO<IMetaAttributeContainer<ITimeInterval>> unionPO = new UnionPO<IMetaAttributeContainer<ITimeInterval>>(new TITransferArea<IMetaAttributeContainer<ITimeInterval>,IMetaAttributeContainer<ITimeInterval>>(unionAO.getNumberOfInputs()));
		unionPO.setOutputSchema(unionAO.getOutputSchema());
		
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(unionAO, unionPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(unionPO);
		retract(unionAO);	
		
	}

	@Override
	public boolean isExecutable(UnionAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
			if(operator.isAllPhysicalInputSet()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "UnionAO -> UnionPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return UnionAO.class;
	}

}
