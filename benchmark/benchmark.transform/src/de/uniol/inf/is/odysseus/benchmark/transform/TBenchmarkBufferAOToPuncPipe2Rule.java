package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.BufferedPunctuationPipe2;
import de.uniol.inf.is.odysseus.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBenchmarkBufferAOToPuncPipe2Rule extends AbstractTransformationRule<BufferAO>{

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(BufferAO operator, TransformationConfiguration config) {
		BufferedPunctuationPipe2 po = new BufferedPunctuationPipe2();
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(operator);
	}

	@Override
	public boolean isExecutable(BufferAO operator,
			TransformationConfiguration config) {
		if(operator.isAllPhysicalInputSet() && operator.getType().equals("Punct2")){
			return true;
		}
		
		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration( )
//		algebraOp : BufferAO(allPhysicalInputSet == true, type == "Punct2")
	}

	@Override
	public String getName() {
		return "BufferAO -> BufferedPunctuationPipe2";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
