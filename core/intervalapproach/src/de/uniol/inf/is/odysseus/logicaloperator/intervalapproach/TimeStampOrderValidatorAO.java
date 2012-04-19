package de.uniol.inf.is.odysseus.logicaloperator.intervalapproach;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="TimeStampOrderValidate")
public class TimeStampOrderValidatorAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9204585315520513917L;

	public TimeStampOrderValidatorAO(){}
	
	public TimeStampOrderValidatorAO(
			TimeStampOrderValidatorAO timeStampOrderValidatorAO) {
		super(timeStampOrderValidatorAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimeStampOrderValidatorAO(this);
	}

}
