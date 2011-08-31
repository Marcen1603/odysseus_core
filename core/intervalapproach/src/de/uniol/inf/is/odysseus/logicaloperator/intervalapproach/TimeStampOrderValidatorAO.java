package de.uniol.inf.is.odysseus.logicaloperator.intervalapproach;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TimeStampOrderValidatorAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9204585315520513917L;

	public TimeStampOrderValidatorAO(){};
	
	public TimeStampOrderValidatorAO(
			TimeStampOrderValidatorAO timeStampOrderValidatorAO) {
		super(timeStampOrderValidatorAO);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimeStampOrderValidatorAO(this);
	}

}
