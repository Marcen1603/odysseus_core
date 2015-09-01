package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCSystemTimeStampRule<T extends TimestampAO> extends AbstractRule<TimestampAO> {

	public AbstractCSystemTimeStampRule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(TimestampAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		
			
			return logicalOperator.isUsingSystemTime();

	
	}
}
