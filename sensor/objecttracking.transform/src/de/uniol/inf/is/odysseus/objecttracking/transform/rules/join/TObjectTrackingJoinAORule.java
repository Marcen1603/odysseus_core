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

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingJoinAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.ObjectTrackingJoinPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TObjectTrackingJoinAORule extends AbstractTransformationRule<ObjectTrackingJoinAO>{

	@Override
	public int getPriority() {
		return 15;
	}

	@Override
	public void execute(ObjectTrackingJoinAO operator,
			TransformationConfiguration config) {
		System.out.println("CREATE ObjectTrackingJoinPO");
		ObjectTrackingJoinPO joinPO = new ObjectTrackingJoinPO();
		
		//default initialization of join
		joinPO.setOutputSchema(operator.getOutputSchema());
		joinPO.setRangePredicates(operator.getRangePredicates());
		joinPO.setMetadataMerge(new CombinedMergeFunction());
		joinPO.setInputSchema(0, operator.getInputSchema(0));
		joinPO.setInputSchema(1, operator.getInputSchema(1));
		joinPO.setOutputSchema(operator.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, joinPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(joinPO);
		retract(operator);
		System.out.println("CREATE ObjectTrackingJoinPO finished.");
	}

	@Override
	public boolean isExecutable(ObjectTrackingJoinAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
		// DRL-Code
//		$trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		$joinAO : ObjectTrackingJoinAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "ObjectTrackingJoinAO -> ObjectTrackingJoinPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
