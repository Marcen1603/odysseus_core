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
package de.uniol.inf.is.odysseus.objecttracking.transform.rules.join;

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.ObjectTrackingJoinPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"rawtypes","unchecked"})
public class TObjectTrackingJoinAOSweepAreasRule extends AbstractTransformationRule<ObjectTrackingJoinPO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public void execute(ObjectTrackingJoinPO operator,
			TransformationConfiguration config) {
		System.out.println("INIT Join Areas");
		operator.initDefaultAreas();
		System.out.println("INIT Join Areas finished.");
	}

	@Override
	public boolean isExecutable(ObjectTrackingJoinPO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.getAreas() == null && operator.getRangePredicates() != null){
			return true;
		}
		
		return false;
		
//		// DRL-Code
//		$trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		// the join predicate must be set first, since this will be set in the SweepAreas now
//		joinPO : ObjectTrackingJoinPO(areas == null && rangePredicates != null)
	}

	@Override
	public String getName() {
		return "ObjectTrackingJoinPO set default SweepAreas";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.METAOBJECTS;
	}

}
