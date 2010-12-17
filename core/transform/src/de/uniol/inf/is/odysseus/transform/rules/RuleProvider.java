package de.uniol.inf.is.odysseus.transform.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();
		//loading default rules		
		rules.add(new TAccessAOExistsRule());
		rules.add(new TFileSinkAORule());
		rules.add(new TCreateMetadataRule());
		rules.add(new TDeleteRenameAORule());
		rules.add(new TSelectAORule());
		rules.add(new TSplitAORule());
		rules.add(new TTransformViewRule());
		return rules;
	}

}
