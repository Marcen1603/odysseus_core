package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.Collection;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.benchmarker.impl.TestProducerAO;
import de.uniol.inf.is.odysseus.benchmarker.impl.TestproducerPO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTestProducerAORule extends AbstractTransformationRule<TestProducerAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(TestProducerAO algebraOp, TransformationConfiguration trafo) {
		TestproducerPO po = new TestproducerPO(algebraOp.getInvertedPriorityRatio());
		po.setOutputSchema(algebraOp.getOutputSchema());
		Iterator<Long> it = algebraOp.getFrequencies().iterator();
		for(Integer size : algebraOp.getElementCounts()) {
			po.addTestPart(size, it.next());
		}
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(algebraOp, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(algebraOp);
		
	}

	@Override
	public boolean isExecutable(TestProducerAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "TestProducerAO -> TestProducerPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

}
