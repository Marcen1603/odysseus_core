package de.uniol.inf.is.odysseus.parallelization.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.parallelization.logicaloperator.ObserverCounterAO;
import de.uniol.inf.is.odysseus.parallelization.physicaloperator.ObserverCounterPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TObserverCounterAO extends
		AbstractTransformationRule<ObserverCounterAO> {

	@Override
	public void execute(ObserverCounterAO operator,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new ObserverCounterPO<IStreamObject<?>>(
				operator.getNumberOfElements()),
				config, true, true);
	}

	@Override
	public boolean isExecutable(ObserverCounterAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "ObserverCounterAO -> ObserverCounterPO";
	}
}
