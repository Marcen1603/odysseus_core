package de.uniol.inf.is.odysseus.anomalydetection.transform.rules;

import de.uniol.inf.is.odysseus.anomalydetection.logicaloperator.ValueAreaAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.anomalydetection.physicaloperator.ValueAreaAnomalyDetectionPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class ValueAreaAnomalyDetectionAOTransformRule extends AbstractTransformationRule<ValueAreaAnomalyDetectionAO> {

	@Override
	public void execute(ValueAreaAnomalyDetectionAO operator, TransformationConfiguration config) throws RuleException {
		ValueAreaAnomalyDetectionPO<Tuple<?>> valueAreaDetection = new ValueAreaAnomalyDetectionPO<Tuple<?>>(operator);
		defaultExecute(operator, valueAreaDetection, config, true, true);
	}

	@Override
	public boolean isExecutable(ValueAreaAnomalyDetectionAO operator, TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class) {
			return operator.isAllPhysicalInputSet();
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "ValueAreaAnomalyDetectionAO --> ValueAreaAnomalyDetectionPO";
	}
	
	@Override
	public Class<? super ValueAreaAnomalyDetectionAO> getConditionClass() {
		return ValueAreaAnomalyDetectionAO.class;
	}

}
