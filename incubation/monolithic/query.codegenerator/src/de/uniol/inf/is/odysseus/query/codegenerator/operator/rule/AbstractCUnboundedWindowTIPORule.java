package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCUnboundedWindowTIPORule<T extends AbstractWindowAO> extends AbstractRule<AbstractWindowAO> {

	public AbstractCUnboundedWindowTIPORule(String name) {
		super(name);
	}



	@Override
	public boolean isExecutable(AbstractWindowAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator instanceof AbstractWindowAO) {

			AbstractWindowAO operator = (AbstractWindowAO) logicalOperator;
			switch (operator.getWindowType()) {
			case UNBOUNDED:
				return true;
			default:
				return false;
			}

		}
		return false;
	}

}
