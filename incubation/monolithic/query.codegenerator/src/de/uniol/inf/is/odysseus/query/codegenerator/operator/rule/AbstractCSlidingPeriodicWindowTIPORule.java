package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCSlidingPeriodicWindowTIPORule<T extends AbstractWindowAO> extends
		AbstractRule<AbstractWindowAO> {

	public AbstractCSlidingPeriodicWindowTIPORule(String name) {
		super(name);
	}



	@Override
	public boolean isExecutable(AbstractWindowAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator instanceof AbstractWindowAO) {
			AbstractWindowAO operator = (AbstractWindowAO) logicalOperator;
			switch (operator.getWindowType()) {
			case TIME:
				if (operator.getWindowSlide() == null
						&& operator.getWindowAdvance() == null
						|| operator.getWindowSlide() == null) {
					return false;
				} else {
					return true;
				}
			default:
				return false;

			}

		}
		return false;

	}

}
