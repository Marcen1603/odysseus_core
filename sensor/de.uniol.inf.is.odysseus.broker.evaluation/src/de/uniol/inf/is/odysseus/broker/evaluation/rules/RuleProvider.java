package de.uniol.inf.is.odysseus.broker.evaluation.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();
		//loading default rules		
		rules.add(new TBenchmarkAORule());
		rules.add(new TBrokerAORule());
		rules.add(new TBrokerCycleDetectionRule());
		rules.add(new TBufferAORule());
		rules.add(new TUpdateEvaluationAORule());		
		return rules;
	}

}
