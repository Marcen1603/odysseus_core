package de.uniol.inf.is.odysseus.ruleengine.ruleflow;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;


public interface IRuleFlow extends Iterable<IRuleFlowGroup> {
	public void addRuleFlowGroup(IRuleFlowGroup group);
	public void addRule(IRule<?,?> rule, IRuleFlowGroup group);
	public Iterator<IRule<?,?>> iteratorRules(IRuleFlowGroup group);
}
