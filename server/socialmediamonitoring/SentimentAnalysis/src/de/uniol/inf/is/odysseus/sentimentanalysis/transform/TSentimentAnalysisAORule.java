package de.uniol.inf.is.odysseus.sentimentanalysis.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sentimentanalysis.logicaloperator.SentimentAnalysisAO;
import de.uniol.inf.is.odysseus.sentimentanalysis.physicaloperator.SentimentAnalysisPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSentimentAnalysisAORule extends
		AbstractTransformationRule<SentimentAnalysisAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SentimentAnalysisAO sentimentAnalysisAO,
			TransformationConfiguration config) throws RuleException {
		
		
		defaultExecute(
				sentimentAnalysisAO,
				new SentimentAnalysisPO<ITimeInterval>(sentimentAnalysisAO.getClassifier(),
						/*sentimentDetectionAO.getDomain(),*/
						sentimentAnalysisAO.getAttributeTextToBeClassified(),
						sentimentAnalysisAO.getTotalInputports(),
						sentimentAnalysisAO.getAttributeTrainSetText(),
						sentimentAnalysisAO.getAttributeTrainSetTrueDecision(),
						sentimentAnalysisAO.getNominals(),
						sentimentAnalysisAO.getMaxTrainSize()),
						config, true, true);
	}

	@Override
	public boolean isExecutable(SentimentAnalysisAO operator,
			TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SentimentAnalysisAO -> SentimentAnalysisPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SentimentAnalysisAO> getConditionClass() {
		return SentimentAnalysisAO.class;
	}
}
