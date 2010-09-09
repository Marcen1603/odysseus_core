/*
 * Copyright 2008 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.ruleflow.core.factory;

import org.drools.process.core.timer.Timer;
import org.drools.ruleflow.core.RuleFlowNodeContainerFactory;
import org.drools.workflow.core.Node;
import org.drools.workflow.core.NodeContainer;
import org.drools.workflow.core.impl.DroolsConsequenceAction;
import org.drools.workflow.core.node.RuleSetNode;

/**
 *
 * @author salaboy
 */
public class RuleSetNodeFactory extends NodeFactory {

    public RuleSetNodeFactory(RuleFlowNodeContainerFactory nodeContainerFactory, NodeContainer nodeContainer, long id) {
        super(nodeContainerFactory, nodeContainer, id);
    }

    @Override
	protected Node createNode() {
        return new RuleSetNode();
    }
    
    protected RuleSetNode getRuleSetNode() {
    	return (RuleSetNode) getNode();
    }

    public RuleSetNodeFactory name(String name) {
        getNode().setName(name);
        return this;
    }

    public RuleSetNodeFactory ruleFlowGroup(String ruleFlowGroup) {
        getRuleSetNode().setRuleFlowGroup(ruleFlowGroup);
        return this;
    }
    
    public RuleSetNodeFactory timer(long delay, long period, String dialect, String action) {
    	Timer timer = new Timer();
    	timer.setDelay(delay);
    	timer.setPeriod(period);
    	getRuleSetNode().addTimer(timer, new DroolsConsequenceAction(dialect, action));
    	return this;
    }

}
