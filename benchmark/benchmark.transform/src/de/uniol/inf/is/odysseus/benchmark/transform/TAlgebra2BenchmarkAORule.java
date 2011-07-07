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
package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.benchmarker.impl.BenchmarkPO;
import de.uniol.inf.is.odysseus.benchmarker.impl.PriorityBenchmarkPO;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TAlgebra2BenchmarkAORule extends AbstractTransformationRule<ILogicalOperator> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(ILogicalOperator algebraOp, TransformationConfiguration trafo) {
		int processingTime = trafo.getOption("processingTime");
		double selectivity = trafo.getOption("selectivity");
		BenchmarkPO<?> po = createBenchmarkPO(processingTime, selectivity, trafo);
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(algebraOp, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(algebraOp);
		
	}

	@Override
	public boolean isExecutable(ILogicalOperator operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getDataType().equals("benchmark")){
			if(operator.isAllPhysicalInputSet()==true){
				if(!(operator instanceof AccessAO)){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AlgebraOp -> BenchmarkPO";
	}
	
	
	public BenchmarkPO createBenchmarkPO(int processingTime, double selectivity, TransformationConfiguration trafo) {
		if (trafo.getMetaTypes().contains(IPriority.class.getName())) {
			return new PriorityBenchmarkPO(processingTime, selectivity);
		} else {
			return new BenchmarkPO(processingTime, selectivity);
		}
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
