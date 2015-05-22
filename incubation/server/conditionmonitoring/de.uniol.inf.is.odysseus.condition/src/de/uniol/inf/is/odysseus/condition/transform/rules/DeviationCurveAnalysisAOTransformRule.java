package de.uniol.inf.is.odysseus.condition.transform.rules;

import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationCurveAnalysisAO;
import de.uniol.inf.is.odysseus.condition.physicaloperator.DeviationCurveAnalysisPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class DeviationCurveAnalysisAOTransformRule extends AbstractTransformationRule<DeviationCurveAnalysisAO> {

	@Override
	public void execute(DeviationCurveAnalysisAO operator, TransformationConfiguration config) throws RuleException {
		DeviationCurveAnalysisPO<Tuple<ITimeInterval>, ITimeInterval> po = new DeviationCurveAnalysisPO<Tuple<ITimeInterval>, ITimeInterval>(
				operator);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(DeviationCurveAnalysisAO operator, TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class && operator.getInputSchema(1).getType() == Tuple.class) {
			return operator.isAllPhysicalInputSet();
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
