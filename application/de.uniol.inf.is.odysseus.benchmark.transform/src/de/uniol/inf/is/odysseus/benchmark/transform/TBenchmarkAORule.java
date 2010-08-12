package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.benchmarker.impl.BenchmarkAO;
import de.uniol.inf.is.odysseus.benchmarker.impl.BenchmarkPO;
import de.uniol.inf.is.odysseus.benchmarker.impl.PriorityBenchmarkPO;
import de.uniol.inf.is.odysseus.priority.IPriority;
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
		if (trafo.getMetaTypes().contains(IPriority.class.getName())) {
			return new PriorityBenchmarkPO(processingTime, selectivity);
		} else {
			return new BenchmarkPO(processingTime, selectivity);
		}
	}
}
