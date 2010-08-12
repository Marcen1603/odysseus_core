package de.uniol.inf.is.odysseus.rewrite.flow;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public enum RewriteRuleFlowGroup implements IRuleFlowGroup {
	DELETE,
	SPLIT,
	SWITCH,
	GROUP,
	CLEANUP		
}
