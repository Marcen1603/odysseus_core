package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.relational.FixedSetAccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.relational.FixedSetPO;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFixedSetAccessAORule extends AbstractTransformationRule<FixedSetAccessAO<?>> {

	@Override
	public int getPriority() {		
		return 5;
	}

	@Override
	public void execute(FixedSetAccessAO<?> accessAO, TransformationConfiguration transformConfig) {
		String accessPOName = accessAO.getSource().getURI(false);
		ISource accessPO = new FixedSetPO(accessAO.getTuples());
		accessPO.setOutputSchema(accessAO.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		retract(accessAO);
		insert(accessPO);
		
	}

	@Override
	public boolean isExecutable(FixedSetAccessAO<?> accessAO, TransformationConfiguration transformConfig) {
		return (WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null);
	}

	@Override
	public String getName() {		
		return "FixedSetAccessAO -> FixedSetPO";
	}

}
