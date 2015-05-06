package de.uniol.inf.is.odysseus.condition.transform.rules;

import de.uniol.inf.is.odysseus.condition.logicaloperator.LOFAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.condition.physicaloperator.LOFAnomalyDetectionPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class LOFAnomalyDetectionAOTransformRule extends AbstractTransformationRule<LOFAnomalyDetectionAO> {

	@Override
	public void execute(LOFAnomalyDetectionAO operator, TransformationConfiguration config) throws RuleException {
		LOFAnomalyDetectionPO<Tuple<ITimeInterval>, ITimeInterval> po = new LOFAnomalyDetectionPO<Tuple<ITimeInterval>, ITimeInterval>(
				operator);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public String getName() {
		return "LOFAnomalyDetectionAO --> LOFAnomalyDetectionPO";
	}

	@Override
	public boolean isExecutable(LOFAnomalyDetectionAO operator, TransformationConfiguration config) {
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
