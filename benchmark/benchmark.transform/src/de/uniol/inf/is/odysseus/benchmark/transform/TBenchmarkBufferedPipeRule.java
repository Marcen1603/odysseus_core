package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.base.BufferAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBenchmarkBufferedPipeRule extends AbstractTransformationRule<BufferAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(BufferAO algebraOp, TransformationConfiguration trafo) {
		BufferedPipe po = new BufferedPipe();
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(algebraOp, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(algebraOp);
		
	}

	@Override
	public boolean isExecutable(BufferAO operator, TransformationConfiguration transformConfig) {
		if(operator.isAllPhysicalInputSet()){
			if(operator.getType().equals("Normal")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "BufferAO -> BufferedPipe";
	}

}
