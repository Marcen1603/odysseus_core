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
import de.uniol.inf.is.odysseus.scars.objecttracking.temporarydatabouncer.logicaloperator.TemporaryDataBouncerAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.temporarydatabouncer.physicaloperator.TemporaryDataBouncerPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TTemporaryDataBouncerAORule extends AbstractTransformationRule<TemporaryDataBouncerAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(TemporaryDataBouncerAO operator,
			TransformationConfiguration config) {
		System.out.println("CREATE TemporaryDataBouncerPO.");
		TemporaryDataBouncerPO bouncerPO = new TemporaryDataBouncerPO();
		bouncerPO.setObjListPath(operator.getObjListPath());
		bouncerPO.setThreshold(operator.getThreshold());
		bouncerPO.setOperator(operator.getOperator());
		bouncerPO.setOutputSchema(operator.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, bouncerPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}

		insert(bouncerPO);
		retract(operator);
		System.out.println("CREATE TemporaryDataBouncerPO finished.");
	}

	@Override
	public boolean isExecutable(TemporaryDataBouncerAO operator,
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
//		$bouncerAO : TemporaryDataBouncerAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "TemporaryDataBouncerAO -> TemporaryDataBouncerPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
