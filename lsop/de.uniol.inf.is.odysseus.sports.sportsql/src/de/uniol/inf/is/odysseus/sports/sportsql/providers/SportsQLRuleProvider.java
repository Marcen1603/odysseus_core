package de.uniol.inf.is.odysseus.sports.sportsql.providers;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;
import de.uniol.inf.is.odysseus.sports.sportsql.transform.SportsHeatMapAORule;

public class SportsQLRuleProvider implements IRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
        List<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();      
        rules.add(new SportsHeatMapAORule());
        return rules;
	}

}
