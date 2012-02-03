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
package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.operator.objectselector.ao.DistanceObjectSelectorAO;
import de.uniol.inf.is.odysseus.scars.operator.objectselector.po.DistanceObjectSelectorPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDistanceObjectSelectorAORule extends AbstractTransformationRule<DistanceObjectSelectorAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(DistanceObjectSelectorAO operator,
			TransformationConfiguration config) {
		System.out.println("CREATE DistanceObjectSelectorPO.");
		DistanceObjectSelectorPO po = new DistanceObjectSelectorPO();
		
		po.setTrackedObjectList(operator.getTrackedObjectList());
		po.setTrackedObjectX(operator.getTrackedObjectX());
		po.setTrackedObjectY(operator.getTrackedObjectY());
		po.setDistanceThresholdXLeft(operator.getDistanceThresholdXLeft());
		po.setDistanceThresholdXRight(operator.getDistanceThresholdXRight());
		po.setDistanceThresholdYLeft(operator.getDistanceThresholdYLeft());
		po.setDistanceThresholdYRight(operator.getDistanceThresholdYRight());
		po.setOutputSchema(operator.getOutputSchema());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(po);
		retract(operator);
		System.out.println("CREATE DistanceObjectSelectorPO finished.");
	}

	@Override
	public boolean isExecutable(DistanceObjectSelectorAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}

		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		$ao : DistanceObjectSelectorAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "DistanceObjectSelectorAO -> DistanceObjectSelectorPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
