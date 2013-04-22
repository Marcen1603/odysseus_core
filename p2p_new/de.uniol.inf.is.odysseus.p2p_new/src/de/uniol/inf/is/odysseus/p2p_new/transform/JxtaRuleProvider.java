package de.uniol.inf.is.odysseus.p2p_new.transform;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class JxtaRuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?, ?>> rules = Lists.newArrayList();
		rules.add(new TJxtaSenderRule());
		rules.add(new TJxtaReceiverRule());
		return rules;
	}

}
