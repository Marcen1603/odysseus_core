package de.uniol.inf.is.odysseus.scars.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TBrokerInitAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TDistanceObjectSelectorAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TEvaluateAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterCovarianceAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterEstimateAORule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class StreamCarsRuleProvider implements ITransformRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		ArrayList<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new TBrokerInitAORule());
		rules.add(new TDistanceObjectSelectorAORule());
		rules.add(new TEvaluateAORule());
		rules.add(new TFilterAORule());
		rules.add(new TFilterCovarianceAORule());
		rules.add(new TFilterEstimateAORule());
		
		return rules;
	}

}
