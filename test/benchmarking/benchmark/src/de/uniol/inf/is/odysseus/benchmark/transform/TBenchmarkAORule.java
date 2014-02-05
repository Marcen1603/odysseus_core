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
package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.benchmark.logicaloperator.BenchmarkAO;
import de.uniol.inf.is.odysseus.benchmark.physical.BenchmarkPO;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"rawtypes"})
public class TBenchmarkAORule extends AbstractTransformationRule<BenchmarkAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(BenchmarkAO algebraOp, TransformationConfiguration trafo) throws RuleException {
		BenchmarkPO po = createBenchmarkPO(algebraOp.getProcessingTimeInns(), algebraOp.getSelectivity(), algebraOp.getMemoryUsage(), trafo);
		po.setOutputSchema(algebraOp.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(algebraOp, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(algebraOp);
		
	}

	@Override
	public boolean isExecutable(BenchmarkAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "BenchmarkAO -> BenchmarkPO";
	}

	
	public BenchmarkPO createBenchmarkPO(int processingTime, double selectivity, long memUsage, TransformationConfiguration trafo) {
        return new BenchmarkPO(processingTime, selectivity, memUsage);
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
