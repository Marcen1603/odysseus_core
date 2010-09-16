package de.uniol.inf.is.odysseus.ruleengine.rule;

import java.util.List;

public interface IRuleProvider {

	public List<IRule<?,?>> getRules();	
}
