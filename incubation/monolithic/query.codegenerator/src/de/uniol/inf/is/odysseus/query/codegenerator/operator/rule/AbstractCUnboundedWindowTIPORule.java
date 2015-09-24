package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCUnboundedWindowTIPORule<T extends WindowAO> extends AbstractCOperatorRule<WindowAO> {

	public AbstractCUnboundedWindowTIPORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(WindowAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator.getInputSchema().hasMetatype(ITimeInterval.class)){
			switch (logicalOperator.getWindowType()) {
			case UNBOUNDED:
				return true;
			default:
				return false;
			}
		}
		return false;
	
	}

}
