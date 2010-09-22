package de.uniol.inf.is.odysseus.relational.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();
		rules.add(new TAccessAORelationalInputRule());
		rules.add(new TAccessAOAtomicDataRule());
		rules.add(new TAccessAORelationalByteBufferRule());
		rules.add(new TFixedSetAccessAORule());
		rules.add(new TAggregateAORule());
		rules.add(new TMapAORule());
		rules.add(new TProjectAORule());	
		
		rules.add(new TFileAccessAORule());
		
		return rules;
	}

}
