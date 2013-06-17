package de.uniol.inf.is.odysseus.sentimentdetection.transform;


import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sentimentdetection.logicaloperator.SentimentDetectionAO;
import de.uniol.inf.is.odysseus.sentimentdetection.physicaloperator.SentimentDetectionPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TSentimentDetectionAORule extends
		AbstractTransformationRule<SentimentDetectionAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SentimentDetectionAO sentimentDetectionAO,
			TransformationConfiguration config) {
		defaultExecute(sentimentDetectionAO, new SentimentDetectionPO(sentimentDetectionAO.getOutputType()), config, true, true);
	}

	@Override
	public boolean isExecutable(SentimentDetectionAO operator,
			TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SentimentDetectionAO -> SentimentDetectionPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SentimentDetectionAO> getConditionClass() {
		return SentimentDetectionAO.class;
	}
}
