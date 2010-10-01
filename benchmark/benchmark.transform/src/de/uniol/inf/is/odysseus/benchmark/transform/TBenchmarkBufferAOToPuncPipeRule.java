package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.BufferedPunctuationPipe;
import de.uniol.inf.is.odysseus.logicaloperator.base.BufferAO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBenchmarkBufferAOToPuncPipeRule extends AbstractTransformationRule<BufferAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public void execute(BufferAO operator, TransformationConfiguration config) {
		
		BufferedPunctuationPipe po = new BufferedPunctuationPipe();
		po.setOutputSchema(operator.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(operator);
		
	}

	@Override
	public boolean isExecutable(BufferAO operator,
			TransformationConfiguration config) {
		if(operator.isAllPhysicalInputSet() && operator.getType().equals("Punct")){
			return true;
		}
		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration( )
//		algebraOp : BufferAO(allPhysicalInputSet == true, type == "Punct")
	}

	@Override
	public String getName() {
		return "BufferAO -> BufferedPunctuationPipe";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
