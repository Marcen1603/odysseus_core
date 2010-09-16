package de.uniol.inf.is.odysseus.interval.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.interval.transform.join.TJoinAORule;
import de.uniol.inf.is.odysseus.interval.transform.join.TJoinAOSetSARule;
import de.uniol.inf.is.odysseus.interval.transform.join.TJoinTIPOAddMetadataMergeRule;
import de.uniol.inf.is.odysseus.interval.transform.join.TJoinTIPOSetMetadataMerge;
import de.uniol.inf.is.odysseus.interval.transform.window.TSlidingAdvanceTimeWindowTIPORule;
import de.uniol.inf.is.odysseus.interval.transform.window.TSlidingElementWindowTIPORule;
import de.uniol.inf.is.odysseus.interval.transform.window.TSlidingPeriodicWindowTIPORule;
import de.uniol.inf.is.odysseus.interval.transform.window.TSlidingTimeWindowTIPORule;
import de.uniol.inf.is.odysseus.interval.transform.window.TUnboundedWindowRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?,?>> rules = new ArrayList<IRule<?,?>>();
		rules.add(new TDifferenceAORule());
		rules.add(new TExistenceAORule());
		rules.add(new TStreamGroupingWithAggregationTIPORule());
		rules.add(new TSystemTimestampRule());
		rules.add(new TUnionTIPORule());
		
		rules.add(new TJoinAORule());
		rules.add(new TJoinAOSetSARule());
		rules.add(new TJoinTIPOAddMetadataMergeRule());
		rules.add(new TJoinTIPOSetMetadataMerge());
		
		rules.add(new TSlidingAdvanceTimeWindowTIPORule());
		rules.add(new TSlidingElementWindowTIPORule());
		rules.add(new TSlidingPeriodicWindowTIPORule());
		rules.add(new TSlidingTimeWindowTIPORule());
		rules.add(new TUnboundedWindowRule());
		
		return rules;
	}

}
