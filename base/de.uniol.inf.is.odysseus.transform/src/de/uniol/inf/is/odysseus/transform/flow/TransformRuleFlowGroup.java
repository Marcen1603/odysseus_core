package de.uniol.inf.is.odysseus.transform.flow;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public enum TransformRuleFlowGroup implements IRuleFlowGroup{
	INIT,
	ACCESS,
	CREATE_METADATA,
	TRANSFORMATION,
	METAOBJECTS,
	CLEANUP
}
