package de.uniol.inf.is.odysseus.condition.transform.rules;

import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationSequenceLearnAO;
import de.uniol.inf.is.odysseus.condition.physicaloperator.DeviationSequenceLearnPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class DeviationCurveLearnAOTransformRule extends AbstractTransformationRule<DeviationSequenceLearnAO> {

	@Override
	public void execute(DeviationSequenceLearnAO operator, TransformationConfiguration config) throws RuleException {
		DeviationSequenceLearnPO<Tuple<ITimeInterval>, ITimeInterval> po = new DeviationSequenceLearnPO<Tuple<ITimeInterval>, ITimeInterval>(
				operator);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(DeviationSequenceLearnAO operator, TransformationConfiguration config) {
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
