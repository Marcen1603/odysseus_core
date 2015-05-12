package de.uniol.inf.is.odysseus.condition.transform.rules;

import de.uniol.inf.is.odysseus.condition.logicaloperator.RarePatternAO;
import de.uniol.inf.is.odysseus.condition.physicaloperator.RarePatternPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class RarePatternPOTransformRule extends AbstractTransformationRule<RarePatternAO> {

	@Override
	public void execute(RarePatternAO operator, TransformationConfiguration config) throws RuleException {
		RarePatternPO<Tuple<ITimeInterval>, ITimeInterval> po = new RarePatternPO<Tuple<ITimeInterval>, ITimeInterval>(operator);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(RarePatternAO operator, TransformationConfiguration config) {
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
		return "RarePatterAO --> RarePatternPO";
	}

}
