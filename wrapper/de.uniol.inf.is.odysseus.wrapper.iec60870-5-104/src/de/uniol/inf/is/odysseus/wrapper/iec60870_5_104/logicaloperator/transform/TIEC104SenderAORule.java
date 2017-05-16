package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.logicaloperator.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.logicaloperator.IEC104SenderAO;
import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.physicaloperator.IEC104SenderPO;

//TODO This operator exists only for test purposes.
public class TIEC104SenderAORule extends AbstractTransformationRule<IEC104SenderAO> {

	@Override
	public void execute(IEC104SenderAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new IEC104SenderPO(operator.getHost(), operator.getPort(), operator.getTimeout()),
				config, true, true);
	}

	@Override
	public boolean isExecutable(IEC104SenderAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
