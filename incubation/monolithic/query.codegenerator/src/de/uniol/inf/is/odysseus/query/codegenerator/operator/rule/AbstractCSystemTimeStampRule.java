package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCSystemTimeStampRule extends AbstractRule {

	public AbstractCSystemTimeStampRule(String name) {
		super(name);
	}
	
	@Override
	public Class<?> getConditionClass() {
		return TimestampAO.class;
	}

	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator instanceof TimestampAO) {

			TimestampAO timestampAO= (TimestampAO)logicalOperator;
			
			 return timestampAO.isUsingSystemTime();
		
		}
		return false;
	}
}
