package de.uniol.inf.is.odysseus.timeseries.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;

public class RuleProvider implements IRuleProvider {

	@SuppressWarnings("deprecation")
	@Override
	public List<IRule<?, ?>> getRules() {
		final List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new TRegularTimeSeriesAORule());
		rules.add(new TImputationAORule());
		rules.add(new TModelVarianceAORule());
		rules.add(new TForecastVarianceAORule());
		return rules;
	}
}
