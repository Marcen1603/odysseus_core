package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * abstract rule for the TimestampAO (SystemTimeStamp)
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractCSystemTimeStampRule<T extends TimestampAO> extends AbstractCOperatorRule<TimestampAO> {

	public AbstractCSystemTimeStampRule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(TimestampAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

			return logicalOperator.isUsingSystemTime();
	}
}
