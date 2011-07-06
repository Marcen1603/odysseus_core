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
package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TJoinTIPOInsertDataMergeRule extends AbstractTransformationRule<JoinTIPO<ITimeInterval, RelationalTuple<ITimeInterval>>> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(JoinTIPO<ITimeInterval, RelationalTuple<ITimeInterval>> joinPO, TransformationConfiguration transformConfig) {
		joinPO.setDataMerge(new RelationalMergeFunction<ITimeInterval>(joinPO.getOutputSchema().size()));
		update(joinPO);		
	}

	@Override
	public boolean isExecutable(JoinTIPO<ITimeInterval, RelationalTuple<ITimeInterval>> operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getDataType().equals("relational")){
			if(operator.getDataMerge()==null){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return"Insert DataMergeFunction (Relational)";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return JoinTIPO.class;
	}

}
