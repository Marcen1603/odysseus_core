/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.securitypunctuation.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.securitypunctuation.rules.join.TSAJoinAORule;
import de.uniol.inf.is.odysseus.securitypunctuation.rules.join.TSAJoinAOSetSARule;
import de.uniol.inf.is.odysseus.securitypunctuation.rules.join.TSAJoinPOAddMetadataMergeRule;
import de.uniol.inf.is.odysseus.securitypunctuation.rules.join.TSAJoinPOInsertDataMergeRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?,?>> rules = new ArrayList<IRule<?,?>>();
		rules.add(new TSecurityShieldAORule());
		rules.add(new TSASelectAORule());
		rules.add(new TSAJoinAORule());
		rules.add(new TSAJoinAOSetSARule<>());
		rules.add(new TSAJoinPOInsertDataMergeRule());
		rules.add(new TSAJoinPOAddMetadataMergeRule());
		rules.add(new TProjectAORule());
		rules.add(new TSAStreamGroupingWithAggregationTIPORule());
		return rules;
	}
}
