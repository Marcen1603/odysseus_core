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
package de.uniol.inf.is.odysseus.objectrelational.transform;

import java.util.Collection;
import java.util.Set;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking.ObjectTrackingUnnestAO;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.ObjectTrackingUnnestPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUnnestAORule extends AbstractTransformationRule<ObjectTrackingUnnestAO> {

	@Override
	public int getPriority() {		
		return 15;
	}

	@Override
	public void execute(ObjectTrackingUnnestAO unnestAO, TransformationConfiguration transformConfig) {		
		ObjectTrackingUnnestPO<?> unnestPO = new ObjectTrackingUnnestPO(unnestAO.getInputSchema(),unnestAO.getOutputSchema(),unnestAO.getNestAttribute());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(unnestAO, unnestPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}				
		retract(unnestAO);
	}

	@Override
	public boolean isExecutable(ObjectTrackingUnnestAO operator, TransformationConfiguration transformConfig) {
		if(operator.isAllPhysicalInputSet()){
			Set<String> metaTypes = transformConfig.getMetaTypes();
			if(metaTypes.contains(IProbability.class.getCanonicalName())){
				if(metaTypes.contains(ILatency.class.getCanonicalName())){
					if(metaTypes.contains(IPredictionFunctionKey.class.getCanonicalName())){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ObjectTrackingUnnestAO -> ObjectTrackingUnnestPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
