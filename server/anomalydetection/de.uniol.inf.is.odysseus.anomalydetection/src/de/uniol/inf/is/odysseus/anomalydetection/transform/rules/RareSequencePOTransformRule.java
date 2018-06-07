package de.uniol.inf.is.odysseus.anomalydetection.transform.rules;

import de.uniol.inf.is.odysseus.anomalydetection.logicaloperator.RareSequenceAO;
import de.uniol.inf.is.odysseus.anomalydetection.physicaloperator.RareSequencePO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class RareSequencePOTransformRule extends AbstractTransformationRule<RareSequenceAO> {

	@Override
	public void execute(RareSequenceAO operator, TransformationConfiguration config) throws RuleException {
		RareSequencePO<Tuple<ITimeInterval>, ITimeInterval> po = new RareSequencePO<Tuple<ITimeInterval>, ITimeInterval>(operator);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(RareSequenceAO operator, TransformationConfiguration config) {
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
		return "RareSequenceO --> RareSequencePO";
	}

}
