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
package de.uniol.inf.is.odysseus.relational.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();
		rules.add(new TInitPredicateRule());
		rules.add(new TAccessAORelationalInputRule());
		rules.add(new TAccessAOAtomicDataRule());
		rules.add(new TAccessAORelationalByteBufferRule());
		rules.add(new TFixedSetAccessAORule());
		rules.add(new TAggregatePORule());
		rules.add(new TMapAORule());
		rules.add(new TProjectAORule());	
		rules.add(new TSocketSinkAORule());
		rules.add(new TFileAccessAORule());
		
		rules.add(new TUnnestAORule());
		return rules;
	}

}
