package de.uniol.inf.is.odysseus.relational.rewrite;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.relational.rewrite.rules.RDeleteProjectionWithoutFunctionRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RDeleteSelectionWithoutPredicate;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RMergeSelectionJoinRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RMergeSelectionRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSplitSelectionRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchProjectionRenameRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchProjectionWindowRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchSelectionJoinRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchSelectionProjectionRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchSelectionRenameRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchSelectionWindowRule;
import de.uniol.inf.is.odysseus.rewrite.flow.IRewriteRuleProvider;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;

public class RuleProvider implements IRewriteRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		ArrayList<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new RDeleteProjectionWithoutFunctionRule());
		rules.add(new RDeleteSelectionWithoutPredicate());
		rules.add(new RMergeSelectionJoinRule());
		rules.add(new RMergeSelectionRule());
		rules.add(new RSplitSelectionRule());
		rules.add(new RSwitchProjectionRenameRule());
		rules.add(new RSwitchProjectionWindowRule());
		rules.add(new RSwitchSelectionJoinRule());
		rules.add(new RSwitchSelectionProjectionRule());
		rules.add(new RSwitchSelectionRenameRule());
		rules.add(new RSwitchSelectionWindowRule());
		return rules;
	}

}
