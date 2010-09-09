package de.uniol.inf.is.odysseus.rewrite.engine;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlow;
import de.uniol.inf.is.odysseus.ruleengine.system.AbstractWorkingEnvironment;

public class RewriteEnvironment extends AbstractWorkingEnvironment<RewriteConfiguration> {

	public RewriteEnvironment(RewriteConfiguration config, IRuleFlow ruleflow) {
		super(config, ruleflow);		
	}

}
