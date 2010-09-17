package de.uniol.inf.is.odysseus.cep.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		ArrayList<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new TCep());
		return rules;
	}

}
