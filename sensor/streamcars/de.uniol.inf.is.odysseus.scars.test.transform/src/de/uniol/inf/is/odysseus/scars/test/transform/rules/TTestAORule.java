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
package de.uniol.inf.is.odysseus.scars.test.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.operator.test.ao.TestAO;
import de.uniol.inf.is.odysseus.scars.operator.test.po.TestPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TTestAORule extends AbstractTransformationRule<TestAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void execute(TestAO operator, TransformationConfiguration config) {
		TestPO<?> testPO = new TestPO<Object>();
		testPO.setOutputSchema(operator.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, testPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(operator);
	}

	@Override
	public boolean isExecutable(TestAO operator,
			TransformationConfiguration config) {
		if(config.getDataType().equals("relational") && 
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
		
		//DRL-Code
//		$testAO : TestAO( allPhysicalInputSet == true )
//		$trafo : TransformationConfiguration(dataType == "relational")
	}

	@Override
	public String getName() {
		return "TestAO -> TestPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
