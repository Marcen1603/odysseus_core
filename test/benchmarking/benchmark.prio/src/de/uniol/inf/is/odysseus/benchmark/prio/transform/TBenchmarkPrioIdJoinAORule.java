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
package de.uniol.inf.is.odysseus.benchmark.prio.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.benchmark.prio.logicaloperator.PrioIdJoinAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority_interval.PriorityIdHashSweepArea;
import de.uniol.inf.is.odysseus.priority_interval.PriorityTITransferArea;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TBenchmarkPrioIdJoinAORule extends AbstractTransformationRule<PrioIdJoinAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(PrioIdJoinAO operator,
			TransformationConfiguration config) {
		JoinTIPO joinPO = new JoinTIPO();
		joinPO.setTransferFunction(new PriorityTITransferArea());
		joinPO.setMetadataMerge(new CombinedMergeFunction());
		joinPO.setOutputSchema(operator.getOutputSchema() == null?null:operator.getOutputSchema().clone()); 
	 	ITimeIntervalSweepArea[] areas = new ITimeIntervalSweepArea[2];
		areas[0] = new PriorityIdHashSweepArea(operator.getRightPos(), operator.getLeftPos());
		areas[1] = new PriorityIdHashSweepArea(operator.getLeftPos(), operator.getRightPos());
	 	joinPO.setAreas(areas);
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, joinPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(operator);
		insert(joinPO);
	}

	@Override
	public boolean isExecutable(PrioIdJoinAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPriority.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.server.intervalapproach.ITimeInterval" && metaTypes contains "de.uniol.inf.is.odysseus.priority.IPriority" )
//		algebraOp : PrioIdJoinAO(allPhysicalInputSet == true)
	}

	@Override
	public String getName() {
		return "PrioIdJoinAO -> JoinTIPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
