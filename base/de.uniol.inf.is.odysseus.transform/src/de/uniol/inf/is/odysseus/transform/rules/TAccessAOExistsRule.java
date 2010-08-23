package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessAOExistsRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration trafo) {
		System.out.println("Transform AccessAO: " + accessAO);	
		ISource<?> accessPO = WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI());
		System.out.println("Transform to existing AccessPO: trafo = " + trafo);
		System.out.println("Transform to existing AccessPO: trafoHelper = " + trafo.getTransformationHelper());
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(accessAO,accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(accessPO);
		retract(accessAO);
		
	}

	@Override
	public boolean isExecutable(AccessAO operator, TransformationConfiguration transformConfig) {
		return WrapperPlanFactory.getAccessPlan(operator.getSource().getURI(false)) != null;
	}

	@Override
	public String getName() {
		return "Transform to existing AccessPO";
	}

}
