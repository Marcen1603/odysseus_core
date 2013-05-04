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
package de.uniol.inf.is.odysseus.hmm.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

/**
 * 
 * @author Dennis Geesen
 * Created at: 14.05.2012
 */
public class RuleProvider implements ITransformRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		ArrayList<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new TVectorquantizationAORule());	
		rules.add(new THmmAORule());
		rules.add(new TFeatureExtractionAORule());
		return rules;
	}

}
