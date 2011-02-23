package de.uniol.inf.is.odysseus.interval.pql;

import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.intervalapproach.TimeStampOrderValidatorAO;

public class TimeStampOrderValidatorBuilder extends AbstractOperatorBuilder{

	private static final long serialVersionUID = 6046492659283366578L;
	
	public TimeStampOrderValidatorBuilder() {
		super(1,1);
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return new TimeStampOrderValidatorAO();
	}

}
