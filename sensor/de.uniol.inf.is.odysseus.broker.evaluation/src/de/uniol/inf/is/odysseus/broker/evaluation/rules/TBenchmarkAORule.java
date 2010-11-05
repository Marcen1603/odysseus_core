package de.uniol.inf.is.odysseus.broker.evaluation.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.broker.evaluation.benchmark.BenchmarkAO;
import de.uniol.inf.is.odysseus.broker.evaluation.benchmark.BenchmarkPO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBenchmarkAORule extends AbstractTransformationRule<BenchmarkAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(BenchmarkAO algebraOp, TransformationConfiguration trafo) {
		BenchmarkPO po = createBenchmarkPO(algebraOp.getProcessingTimeInns(), algebraOp.getSelectivity(), trafo);
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(algebraOp, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(algebraOp);
		
	}

	@Override
	public boolean isExecutable(BenchmarkAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "BenchmarkAO -> BenchmarkPO";
	}

	
	public BenchmarkPO createBenchmarkPO(int processingTime, double selectivity, TransformationConfiguration trafo) {
			return new BenchmarkPO(processingTime, selectivity);		
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
