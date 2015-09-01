package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.paralleltrack.service;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.paralleltrack.transform.TLoadBalancingSynchronizerAORule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;

public class LoadBalancingRuleProvider implements IRuleProvider {
	@Override
    public List<IRule<?, ?>> getRules() {
        List<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();     
        rules.add(new TLoadBalancingSynchronizerAORule());
        return rules;
    }
}