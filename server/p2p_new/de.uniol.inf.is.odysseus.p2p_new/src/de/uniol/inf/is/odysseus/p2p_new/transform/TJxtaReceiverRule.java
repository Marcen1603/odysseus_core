package de.uniol.inf.is.odysseus.p2p_new.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TJxtaReceiverRule extends AbstractTransformationRule<JxtaReceiverAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(JxtaReceiverAO operator, TransformationConfiguration config) throws RuleException {
		try {
			defaultExecute(operator, new JxtaReceiverPO(operator), config, true, true);
		} catch (DataTransmissionException e) {
			throw new RuleException("Could not create JxtaReceiverPO", e);
		}
	}

	@Override
	public boolean isExecutable(JxtaReceiverAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "JxtaReceiverAO --> JxtaReceiverPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
