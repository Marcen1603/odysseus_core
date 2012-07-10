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
package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.operator.association.ao.HypothesisSelectionAO;
import de.uniol.inf.is.odysseus.scars.operator.association.po.HypothesisSelectionPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"rawtypes"})
public class THypothesisSelectionAORule extends AbstractTransformationRule<HypothesisSelectionAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(HypothesisSelectionAO operator,
			TransformationConfiguration config) {
		
		System.out.println("CREATE HypothesisSelectionPO");
		HypothesisSelectionPO selePO = new HypothesisSelectionPO();
		selePO.setOldObjListPath(operator.getOldObjListPath());
		selePO.setNewObjListPath(operator.getNewObjListPath());
		selePO.setOutputSchema(operator.getOutputSchema());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, selePO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(selePO);
		retract(operator);
		System.out.println("CREATE HypothesisSelectionPO finished.");
		
	}

	@Override
	public boolean isExecutable(HypothesisSelectionAO operator,
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
//		$seleAO : HypothesisSelectionAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "HypothesisSelectionAO -> HypothesisSelectionPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
