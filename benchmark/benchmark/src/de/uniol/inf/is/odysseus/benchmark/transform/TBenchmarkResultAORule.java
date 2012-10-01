package de.uniol.inf.is.odysseus.benchmark.transform;

import de.uniol.inf.is.odysseus.benchmark.logicaloperator.BenchmarkResultAO;
import de.uniol.inf.is.odysseus.benchmark.physical.BenchmarkResultPO;
import de.uniol.inf.is.odysseus.benchmark.result.BenchmarkResultFactoryRegistry;
import de.uniol.inf.is.odysseus.benchmark.result.IBenchmarkResultFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBenchmarkResultAORule extends
		AbstractTransformationRule<BenchmarkResultAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(BenchmarkResultAO operator,
			TransformationConfiguration config) {
		IBenchmarkResultFactory<?> resultFactory = BenchmarkResultFactoryRegistry.getEntry(operator.getResultType());
		if (resultFactory == null){
			throw new TransformationException("ResultFactory "+operator.getResultType()+" not registered!");
		}
		long resultsToRead = operator.getMaxResults();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		BenchmarkResultPO po = new BenchmarkResultPO(resultFactory, resultsToRead);
		super.defaultExecute(operator, po, config, true, false);
	}

	@Override
	public boolean isExecutable(BenchmarkResultAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "BenchmarkResultAO -> BenchmarkResultPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super BenchmarkResultAO> getConditionClass() {
		return BenchmarkResultAO.class;
	}

}
