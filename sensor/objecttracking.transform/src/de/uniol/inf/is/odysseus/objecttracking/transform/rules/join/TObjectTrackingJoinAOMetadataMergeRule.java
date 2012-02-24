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

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.latency.LatencyMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.ObjectTrackingJoinPO;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.merge.PredictionKeyMetadataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.merge.ProbabilityMetadataMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TObjectTrackingJoinAOMetadataMergeRule extends AbstractTransformationRule<ObjectTrackingJoinPO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public void execute(ObjectTrackingJoinPO operator,
			TransformationConfiguration config) {
		System.out.println("MULTI_VALUE ObjectTrackingJoinPO MetadataMergeFunction.");
		((CombinedMergeFunction)operator.getMetadataMerge()).add(new TimeIntervalInlineMetadataMergeFunction());
		((CombinedMergeFunction)operator.getMetadataMerge()).add(new LatencyMergeFunction());
		
		// TODO Andre: Covariance zwischen zwei Str�men auslesen und dem Join schon bei der Transformation
		// von logisch nach physisch �bergeben
		((CombinedMergeFunction)operator.getMetadataMerge()).add(new ProbabilityMetadataMergeFunction(null));
		((CombinedMergeFunction)operator.getMetadataMerge()).add(new PredictionKeyMetadataMergeFunction());
		
		// no update or retract
		// see JoinAO.drl in relational plug-in for explanation
		System.out.println("MULTI_VALUE ObjectTrackingJoinPO MetadataMergeFunction finished."); 
	}

	@Override
	public boolean isExecutable(ObjectTrackingJoinPO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.getMetadataMerge() instanceof CombinedMergeFunction){
			return true;
		}

		return false;
		// DRL-Code
//		$trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		joinPO : ObjectTrackingJoinPO(metadataMerge != null)
//		eval(joinPO.getMetadataMerge() instanceof CombinedMergeFunction)
	}

	@Override
	public String getName() {
		return "ObjectTrackingJoinPO add MetadataMerge";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.METAOBJECTS;
	}

}
