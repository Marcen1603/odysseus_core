package de.uniol.inf.is.odysseus.condition.transform.rules;

import de.uniol.inf.is.odysseus.condition.logicaloperator.CurveCounterAO;
import de.uniol.inf.is.odysseus.condition.physicaloperator.CurveCounterPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class CurveCounterAOTransformRule extends AbstractTransformationRule<CurveCounterAO> {

	@Override
	public void execute(CurveCounterAO operator, TransformationConfiguration config) throws RuleException {
		CurveCounterPO<Tuple<ITimeInterval>, ITimeInterval> po = new CurveCounterPO<Tuple<ITimeInterval>, ITimeInterval>(
				operator);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public String getName() {
		return "CurveCounterAO --> CurveCounterPO";
	}

	@Override
	public boolean isExecutable(CurveCounterAO operator, TransformationConfiguration config) {
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
