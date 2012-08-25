package de.uniol.inf.is.odysseus.securitypunctuation.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.securitypunctuation.rules.TSecurityShieldAORule;
import de.uniol.inf.is.odysseus.securitypunctuation.rules.TSelectAORule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?,?>> rules = new ArrayList<IRule<?,?>>();
		rules.add(new TSelectAORule());
		rules.add(new TSecurityShieldAORule());
		return rules;
	}
}
