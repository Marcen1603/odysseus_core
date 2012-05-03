package de.uniol.inf.is.odysseus.wrapper.protobuf;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?, ?>> list = new LinkedList<IRule<?,?>>();
		list.add(new TProtobufAccessAORule());
		return list;
	}

}
