/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.benchmarker.transform;

import java.util.Collection;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.benchmarker.impl.TestproducerPO;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.benchmark.TestProducerAO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTestProducerAORule extends AbstractTransformationRule<TestProducerAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(TestProducerAO algebraOp, TransformationConfiguration trafo) {
		TestproducerPO po = new TestproducerPO(algebraOp.getInvertedPriorityRatio());
		po.setOutputSchema(algebraOp.getOutputSchema());
		Iterator<Long> it = algebraOp.getFrequencies().iterator();
		for(Integer size : algebraOp.getElementCounts()) {
			po.addTestPart(size, it.next());
		}
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(algebraOp, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(algebraOp);
		
	}

	@Override
	public boolean isExecutable(TestProducerAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "TestProducerAO -> TestProducerPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

}
