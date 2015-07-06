package de.uniol.inf.is.odysseus.parallelization.benchmark.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.parallelization.benchmark.logicaloperator.ObserverBenchmarkAO;
import de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator.ObserverBenchmarkPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TObserverBenchmarkAO extends
		AbstractTransformationRule<ObserverBenchmarkAO> {

	@Override
	public void execute(ObserverBenchmarkAO operator,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new ObserverBenchmarkPO<IStreamObject<?>>(),
				config, true, true);
	}

	@Override
	public boolean isExecutable(ObserverBenchmarkAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "ObserverBenchmarkAO -> ObserverBenchmarkPO";
	}
}
