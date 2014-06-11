package de.uniol.inf.is.odysseus.peer.loadbalancing.active.service;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform.LoadBalancingTestReceiverAORule;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform.LoadBalancingTestSenderAORule;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform.TJxtaBundleReceiverAORule;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform.TJxtaBundleSenderAORule;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform.TJxtaSenderAORule;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform.TLoadBalancingSynchronizerAORule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;

public class LoadBalancingRuleProvider implements IRuleProvider {
	@Override
    public List<IRule<?, ?>> getRules() {
        List<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();      
        rules.add(new TJxtaBundleReceiverAORule());
        rules.add(new TJxtaBundleSenderAORule());
        rules.add(new TLoadBalancingSynchronizerAORule());
        rules.add(new LoadBalancingTestSenderAORule());
        rules.add(new TJxtaSenderAORule());
        rules.add(new LoadBalancingTestReceiverAORule());
        return rules;
    }
}
