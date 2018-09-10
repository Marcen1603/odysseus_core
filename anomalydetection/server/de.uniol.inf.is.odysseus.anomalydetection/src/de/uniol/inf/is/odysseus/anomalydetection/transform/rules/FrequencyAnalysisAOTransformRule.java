package de.uniol.inf.is.odysseus.anomalydetection.transform.rules;

import de.uniol.inf.is.odysseus.anomalydetection.logicaloperator.FrequencyAnalysisAO;
import de.uniol.inf.is.odysseus.anomalydetection.physicaloperator.FrequencyAnalysisPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class FrequencyAnalysisAOTransformRule extends AbstractTransformationRule<FrequencyAnalysisAO> {

	@Override
	public void execute(FrequencyAnalysisAO operator, TransformationConfiguration config) throws RuleException {
		FrequencyAnalysisPO<Tuple<ITimeInterval>, ITimeInterval> po = new FrequencyAnalysisPO<Tuple<ITimeInterval>, ITimeInterval>(
				operator);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public String getName() {
		return "FrequencyAnalysisAO --> FrequencyAnalysisPO";
	}

	@Override
	public boolean isExecutable(FrequencyAnalysisAO operator, TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class) {
			return operator.isAllPhysicalInputSet();
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
