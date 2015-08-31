package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.UnboundedWindowTIPO;

public abstract class AbstractCUnboundedWindowTIPORule extends AbstractRule {

	public AbstractCUnboundedWindowTIPORule(String name) {
		super(name);
	}

	@Override
	public Class<?> getConditionClass() {
		return UnboundedWindowTIPO.class;
	}

	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
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
