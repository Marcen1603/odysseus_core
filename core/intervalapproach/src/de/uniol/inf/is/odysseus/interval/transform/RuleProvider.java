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
package de.uniol.inf.is.odysseus.interval.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.interval.transform.join.TJoinAORule;
import de.uniol.inf.is.odysseus.interval.transform.join.TJoinTIPOAddMetadataMergeRule;
import de.uniol.inf.is.odysseus.interval.transform.join.TLeftJoinAORule;
import de.uniol.inf.is.odysseus.interval.transform.join.TLeftJoinTIPOAddMetadataMergeRule;
import de.uniol.inf.is.odysseus.interval.transform.window.TPredicateWindowTIPORule;
import de.uniol.inf.is.odysseus.interval.transform.window.TSlidingAdvanceTimeWindowTIPORule;
import de.uniol.inf.is.odysseus.interval.transform.window.TSlidingElementWindowTIPORule;
import de.uniol.inf.is.odysseus.interval.transform.window.TSlidingPeriodicWindowTIPORule;
import de.uniol.inf.is.odysseus.interval.transform.window.TSlidingTimeWindowTIPORule;
import de.uniol.inf.is.odysseus.interval.transform.window.TUnboundedWindowRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?,?>> rules = new ArrayList<IRule<?,?>>();
		rules.add(new TDifferenceAORule());
		rules.add(new TExistenceAORule());
		rules.add(new TStreamGroupingWithAggregationTIPORule());
		rules.add(new TSystemTimestampRule());
		rules.add(new TUnionTIPORule());
		rules.add(new TSelectAORule());
		rules.add(new TFilterAORule());

		rules.add(new TJoinAORule());
		rules.add(new TJoinTIPOAddMetadataMergeRule());		
		
		rules.add(new TPredicateWindowTIPORule());
		rules.add(new TSlidingAdvanceTimeWindowTIPORule());
		rules.add(new TSlidingElementWindowTIPORule());
		rules.add(new TSlidingPeriodicWindowTIPORule());
		rules.add(new TSlidingTimeWindowTIPORule());
		rules.add(new TUnboundedWindowRule());
		
		rules.add(new TTimeStampOrderValdiatorRule());
		
		rules.add(new TLeftJoinAORule());
		rules.add(new TLeftJoinTIPOAddMetadataMergeRule());
		
		rules.add(new TPunctuationAORule());
		rules.add(new TAssureHeartbeatAORule());
		
		rules.add(new TChangeCorrelateAORule());
		rules.add(new TChangeCorrelateAddMetadataMergeRule());
		
		rules.add(new TDuplicateEliminationAORule());

			
		return rules;
	}

}
