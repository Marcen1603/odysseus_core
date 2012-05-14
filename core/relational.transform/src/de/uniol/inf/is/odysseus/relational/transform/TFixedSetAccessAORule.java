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
package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.relational.FixedSetAccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.FixedSetPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFixedSetAccessAORule extends AbstractTransformationRule<FixedSetAccessAO<?>> {

	@Override
	public int getPriority() {		
		return 5;
	}

	@Override
	public void execute(FixedSetAccessAO<?> accessAO, TransformationConfiguration transformConfig) {
		String accessPOName = accessAO.getSourcename();
		ISource<?> accessPO = new FixedSetPO<IMetaAttributeContainer<?>>(accessAO.getTuples());
		accessPO.setOutputSchema(accessAO.getOutputSchema());
		getDataDictionary().putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		retract(accessAO);
		insert(accessPO);
		
	}

	@Override
	public boolean isExecutable(FixedSetAccessAO<?> accessAO, TransformationConfiguration transformConfig) {
		return (getDataDictionary().getAccessPlan(accessAO.getSourcename()) == null);
	}

	@Override
	public String getName() {		
		return "FixedSetAccessAO -> FixedSetPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return FixedSetAccessAO.class;
	}

}
