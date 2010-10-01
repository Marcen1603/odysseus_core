package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.Collection;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.benchmarker.impl.BatchProducer;
import de.uniol.inf.is.odysseus.benchmarker.impl.BatchProducerAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBatchProducerAORule extends AbstractTransformationRule<BatchProducerAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(BatchProducerAO algebraOp, TransformationConfiguration trafo) {
		BatchProducer po = new BatchProducer(algebraOp.getInvertedPriorityRatio());
		Iterator<Long> it = algebraOp.getFrequencies().iterator();
		for(Integer size : algebraOp.getElementCounts()) {
			po.addBatch(size, it.next());
		}
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(algebraOp, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(algebraOp);
		
	}

	@Override
	public boolean isExecutable(BatchProducerAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "BatchProducerAO -> BatchProducer";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}
}
