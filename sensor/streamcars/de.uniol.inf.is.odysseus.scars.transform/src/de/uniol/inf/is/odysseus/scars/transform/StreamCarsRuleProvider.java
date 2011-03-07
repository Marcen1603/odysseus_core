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
package de.uniol.inf.is.odysseus.scars.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TBrokerInitAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TDistanceObjectSelectorAOAndreRule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TDistanceObjectSelectorAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TEvaluateAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterExpressionCovarianceAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterExpressionEstimateAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterExpressionGainAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.THypothesisExpressionEvaluationAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.THypothesisExpressionGatingAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.THypothesisGenarationAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.THypothesisSelectionAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TJDVEAccessMVPOAsListRule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TJDVESinkAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TMetadataInitLatencyProbabilityStreamCarsRule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TMetadataObjectRelationalCreationPORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TPredictionAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TPredictionAssignPORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TScarsXMLProfilerAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TTemporaryDataBouncerAORule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class StreamCarsRuleProvider implements ITransformRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		ArrayList<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new TBrokerInitAORule());
		rules.add(new TDistanceObjectSelectorAORule());
		rules.add(new TEvaluateAORule());
		rules.add(new THypothesisGenarationAORule());
		rules.add(new THypothesisSelectionAORule());
		rules.add(new TJDVESinkAORule());
		rules.add(new TJDVEAccessMVPOAsListRule());
		rules.add(new TMetadataInitLatencyProbabilityStreamCarsRule());
		rules.add(new TMetadataObjectRelationalCreationPORule());
		rules.add(new TPredictionAORule());
		rules.add(new TPredictionAssignPORule());
		rules.add(new TScarsXMLProfilerAORule());
		rules.add(new TTemporaryDataBouncerAORule());
		rules.add(new TDistanceObjectSelectorAOAndreRule());
		rules.add(new THypothesisExpressionEvaluationAORule());
		rules.add(new THypothesisExpressionGatingAORule());
		rules.add(new TFilterExpressionCovarianceAORule());
		rules.add(new TFilterExpressionEstimateAORule());
		rules.add(new TFilterExpressionGainAORule());
		return rules;
	}

}
