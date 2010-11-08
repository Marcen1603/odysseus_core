package de.uniol.inf.is.odysseus.rewrite.engine;

import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlow;
import de.uniol.inf.is.odysseus.ruleengine.system.AbstractWorkingEnvironment;

public class RewriteEnvironment extends AbstractWorkingEnvironment<RewriteConfiguration> {

	public RewriteEnvironment(RewriteConfiguration config, IRuleFlow ruleflow) {
		// Currently no user in rewrite needed
		super(config, ruleflow, null);		
	}

}
