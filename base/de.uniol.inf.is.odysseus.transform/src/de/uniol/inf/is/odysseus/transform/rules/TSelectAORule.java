package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSelectAORule extends AbstractTransformationRule<SelectAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(SelectAO selectAO, TransformationConfiguration transformConfig) {		
		SelectPO<?> selectPO = new SelectPO(selectAO.getPredicate());
		selectPO.setOutputSchema(selectAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(selectAO, selectPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(selectAO);
		
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SelectAO -> SelectPO";
	}

}
