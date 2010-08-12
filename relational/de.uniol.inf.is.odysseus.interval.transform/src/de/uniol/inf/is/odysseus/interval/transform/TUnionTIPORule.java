package de.uniol.inf.is.odysseus.interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnionAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.UnionPO;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUnionTIPORule extends AbstractTransformationRule<UnionAO> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(UnionAO unionAO, TransformationConfiguration transformConfig) {
		UnionPO unionPO = new UnionPO(new TITransferArea());
		unionPO.setOutputSchema(unionAO.getOutputSchema());
		
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(unionAO, unionPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(unionPO);
		retract(unionAO);	
		
	}

	@Override
	public boolean isExecutable(UnionAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
			if(operator.isAllPhysicalInputSet()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "UnionAO -> UnionPO";
	}

}
