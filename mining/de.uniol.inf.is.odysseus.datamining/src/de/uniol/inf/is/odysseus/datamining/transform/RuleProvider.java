package de.uniol.inf.is.odysseus.datamining.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.datamining.classification.transform.TClassifyAORule;
import de.uniol.inf.is.odysseus.datamining.classification.transform.THoeffdingTreeAORule;
import de.uniol.inf.is.odysseus.datamining.clustering.transform.TLeaderAORule;
import de.uniol.inf.is.odysseus.datamining.clustering.transform.TSimpleSinglePassKMeansAORule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new TLeaderAORule());
		rules.add(new THoeffdingTreeAORule());
		rules.add(new TClassifyAORule());
		rules.add(new TSimpleSinglePassKMeansAORule());
		return rules;
	}

}
