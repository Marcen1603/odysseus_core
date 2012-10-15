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
package de.uniol.inf.is.odysseus.objecttracking.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.transform.rules.TMetadataInit_Latency_Probability_Rule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.TMetadataObjectRelationalCreationPORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.TObjectTrackingPredictionAssignAORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.TObjectTrackingPunctuationAORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.access.TAccessMVPORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAODataMergeRule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAOMetadataMergeRule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAOSweepAreasRule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAOTransferFunctionRule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.projection.TObjectTrackingPredictionProjectAORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.projection.TObjectTrackingProjectAORule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;


public class ObjectTrackingTransformationRuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		ArrayList<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();
		rules.add(new TAccessMVPORule());
//		rules.add(new TAccessAOSILABRule());
		rules.add(new TObjectTrackingJoinAORule());
		rules.add(new TObjectTrackingJoinAOSweepAreasRule());
		rules.add(new TObjectTrackingJoinAODataMergeRule());
		rules.add(new TObjectTrackingJoinAOMetadataMergeRule());
		rules.add(new TObjectTrackingJoinAOTransferFunctionRule());
		rules.add(new TMetadataInit_Latency_Probability_Rule());
		rules.add(new TObjectTrackingPredictionAssignAORule());
		rules.add(new TObjectTrackingProjectAORule());
		rules.add(new TObjectTrackingPredictionProjectAORule());
		rules.add(new TObjectTrackingPunctuationAORule());
		rules.add(new TMetadataObjectRelationalCreationPORule());
		return rules;
	}

}
