package de.uniol.inf.is.odysseus.keyperformanceindicators.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyperformanceindicators.logicaloperator.ShareOfVoiceAO;
import de.uniol.inf.is.odysseus.keyperformanceindicators.physicaloperator.ShareOfVoicePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TShareOfVoice extends AbstractTransformationRule<ShareOfVoiceAO> {

	@Override
	public void execute(ShareOfVoiceAO operator, TransformationConfiguration config) throws RuleException {
		
		defaultExecute(
				operator,
				new ShareOfVoicePO<>(	operator.getOwnCompany(),
									operator.getAllCompanies(),
									operator.getIncomingText(),
									operator.getThresholdValue()),
						config, true, true );
	}

	@Override
	public boolean isExecutable(ShareOfVoiceAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "ShareOfVoiceAO -> ShareOfVoicePO";
	}

}
