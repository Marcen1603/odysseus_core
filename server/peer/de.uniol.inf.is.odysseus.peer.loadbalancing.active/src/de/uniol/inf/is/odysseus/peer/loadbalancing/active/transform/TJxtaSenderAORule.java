package de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.JxtaBundleSenderPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TJxtaSenderAORule extends AbstractTransformationRule<JxtaSenderAO>{
	
	@Override
	public int getPriority() {
		return 1;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(JxtaSenderAO operator, TransformationConfiguration config) throws RuleException
	{
		try {
			defaultExecute(operator, new JxtaBundleSenderPO(operator), config, true, true);
			
		}
		catch (DataTransmissionException e) {
			throw new RuleException("Could not create JxtaBundleSenderPO", e);
		}
	}
	
	@Override
	public boolean isExecutable(JxtaSenderAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "JxtaSenderAO --> JxtaBundleSenderPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
}
