package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCSlidingAdvanceTimeWindowTIPORule<T extends TimeWindowAO> extends
		AbstractRule<TimeWindowAO>{

	public AbstractCSlidingAdvanceTimeWindowTIPORule(String name) {
		super(name);
	}


	@Override
	public boolean isExecutable(TimeWindowAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

	

			AbstractWindowAO operator = (AbstractWindowAO) logicalOperator;
			switch (operator.getWindowType()) {
			case TIME:
				if (operator.getWindowSlide() == null
						&& operator.getWindowAdvance() != null) {
					return true;
				}
				return false;
			default:
				return false;
			}

	
	
	}

}
