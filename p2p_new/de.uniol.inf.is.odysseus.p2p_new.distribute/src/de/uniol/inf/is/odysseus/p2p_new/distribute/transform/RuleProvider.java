package de.uniol.inf.is.odysseus.p2p_new.distribute.transform;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

/**
 * The Provider of the {@link TReplicationMergeAORule}.
 * @author Michael Brand
 */
public class RuleProvider implements ITransformRuleProvider {

	/**
	 * Returns a new {@link TReplicationMergeAORule}.
	 */
	@Override
	public List<IRule<?, ?>> getRules() {
		
		List<IRule<?, ?>> rules = Lists.newArrayList();
		rules.add(new TReplicationMergeAORule());
		return rules;
		
	}

}