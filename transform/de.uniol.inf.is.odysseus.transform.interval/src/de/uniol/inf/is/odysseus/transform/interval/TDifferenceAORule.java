package de.uniol.inf.is.odysseus.transform.interval;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.AntiJoinTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.DifferenceAO;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDifferenceAORule extends AbstractTransformationRule<DifferenceAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(DifferenceAO differenceAO, TransformationConfiguration transformConfig) {
		AntiJoinTIPO po = new AntiJoinTIPO(differenceAO);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(differenceAO, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(differenceAO);
		
	}

	@Override
	public boolean isExecutable(DifferenceAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DifferenceAO -> AntiJoinTIPO";
	}

}
