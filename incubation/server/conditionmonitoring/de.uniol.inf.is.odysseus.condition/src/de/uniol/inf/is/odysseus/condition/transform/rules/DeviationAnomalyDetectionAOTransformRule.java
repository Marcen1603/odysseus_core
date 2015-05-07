package de.uniol.inf.is.odysseus.condition.transform.rules;

import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.condition.physicaloperator.DeviationAnomalyDetectionPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class DeviationAnomalyDetectionAOTransformRule extends AbstractTransformationRule<DeviationAnomalyDetectionAO> {

	@Override
	public void execute(DeviationAnomalyDetectionAO operator, TransformationConfiguration config) throws RuleException {
		DeviationAnomalyDetectionPO<Tuple<?>> deviatoionDetection = new DeviationAnomalyDetectionPO<Tuple<?>>(operator);
		defaultExecute(operator, deviatoionDetection, config, true, true);
	}

	@Override
	public String getName() {
		return "DeviationAnomalyDetectionAO --> DeviationAnomalyDetectionPO";
	}

	@Override
	public boolean isExecutable(DeviationAnomalyDetectionAO operator, TransformationConfiguration config) {
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