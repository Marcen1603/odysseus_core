package de.uniol.inf.is.odysseus.wrapper.google.protobuf.base;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;
import de.uniol.inf.is.odysseus.wrapper.google.protobuf.base.rules.TProtobufAccessAORule;

public class RuleProvider implements IRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?, ?>> list = new LinkedList<IRule<?,?>>();
		list.add(new TProtobufAccessAORule());
		return list;
	}

}
