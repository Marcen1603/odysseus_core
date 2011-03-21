package de.uniol.inf.is.odysseus.markov.operator.transform;

import de.uniol.inf.is.odysseus.markov.operator.logical.MarkovAO;
import de.uniol.inf.is.odysseus.markov.operator.physical.MarkovPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMarkovAORule extends AbstractTransformationRule<MarkovAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(MarkovAO operator, TransformationConfiguration config) {
		MarkovPO po = new MarkovPO(operator.getHiddenMarkovModel(), operator.getAlgorithm());
		replace(operator, po, config);
		retract(operator);
		
	}

	@Override
	public boolean isExecutable(MarkovAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "MarkovAO -> MarkovPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
