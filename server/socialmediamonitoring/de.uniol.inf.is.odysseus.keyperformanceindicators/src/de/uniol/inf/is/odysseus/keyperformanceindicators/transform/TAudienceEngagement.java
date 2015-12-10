package de.uniol.inf.is.odysseus.keyperformanceindicators.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.keyperformanceindicators.logicaloperator.AudienceEngagementAO;
import de.uniol.inf.is.odysseus.keyperformanceindicators.physicaloperator.AudienceEngagementPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAudienceEngagement extends AbstractTransformationRule<AudienceEngagementAO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(AudienceEngagementAO operator, TransformationConfiguration config) throws RuleException {
		ITimeIntervalSweepArea sa;
		try {
			sa = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuleException(e);
		}

		defaultExecute(
				operator,
				new AudienceEngagementPO<>(operator.getConcreteTopics(),
										 operator.getAllTopics(),
										 operator.getIncomingText(),
										 operator.getThresholdValue(),
										 operator.getCountOfAllTopics(), sa),
						config, true, true );
		
	}

	@Override
	public boolean isExecutable(AudienceEngagementAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "AudienceEngagementAO -> AudienceEngagementPO";
	}


}
