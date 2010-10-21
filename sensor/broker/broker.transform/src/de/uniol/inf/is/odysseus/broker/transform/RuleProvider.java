package de.uniol.inf.is.odysseus.broker.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?,?>> rules = new ArrayList<IRule<?,?>>();
		try{
			rules.add(new TBrokerAccessAORule());
			rules.add(new TBrokerAOExistsRule());
			rules.add(new TBrokerAORule());
			rules.add(new TBrokerCycleDetectionRule());
			rules.add(new TBrokerJoinTIPORule());
			rules.add(new TMetricAORule());
		}catch(Throwable t){
			t.printStackTrace();
		}
		return rules;
	}

}
