package de.uniol.inf.is.odysseus.parallelization.benchmark.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.parallelization.benchmark.logicaloperator.BenchmarkAO;
import de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator.BenchmarkPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBenchmarkAO extends
		AbstractTransformationRule<BenchmarkAO> {

	@Override
	public void execute(BenchmarkAO operator,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new BenchmarkPO<IStreamObject<?>>(),
				config, true, true);
	}

	@Override
	public boolean isExecutable(BenchmarkAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "BenchmarkAO -> BenchmarkPO";
	}
}
