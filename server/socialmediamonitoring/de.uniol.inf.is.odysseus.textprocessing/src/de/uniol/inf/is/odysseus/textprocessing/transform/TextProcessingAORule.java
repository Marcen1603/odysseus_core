package de.uniol.inf.is.odysseus.textprocessing.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.textprocessing.logicaloperator.TextProcessingAO;
import de.uniol.inf.is.odysseus.textprocessing.physicaloperator.TextProcessingPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TextProcessingAORule extends AbstractTransformationRule<TextProcessingAO> {

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(TextProcessingAO textProcessingAO, TransformationConfiguration config) throws RuleException {
		defaultExecute(
				textProcessingAO,
				new TextProcessingPO(	textProcessingAO.getOutputPort(),
										textProcessingAO.getInputText(),
										textProcessingAO.getNGramSize(),
										textProcessingAO.isDoNgram(),
										textProcessingAO.isDoStemming(),
										textProcessingAO.isDoRemoveStopwords()
									),
						config, true, true );
	}

	@Override
	public boolean isExecutable(TextProcessingAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "TextProcessingAO -> TextProcessingPO";
	}

}
