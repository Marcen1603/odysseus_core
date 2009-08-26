package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class BinaryLogicalOp extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1558576598140153762L;

	public BinaryLogicalOp(AbstractLogicalOperator po) {
		super(po);
	}

	public BinaryLogicalOp() {
		super();
		setNoOfInputs(2);
	}

	public void setLeftInput(AbstractLogicalOperator po) {
		setInputAO(0, po);
	}

	public ILogicalOperator getLeftInput() {
		return getInputAO(0);
	}

	public void setRightInput(AbstractLogicalOperator po) {
		setInputAO(1, po);
	}

	public ILogicalOperator getRightInput() {
		return getInputAO(1);
	}

	public SDFAttributeList getLeftInputSchema() {
		return getInputSchema(0);
	}

	public void setLeftInputSchema(SDFAttributeList schema) {
		setInputSchema(0, schema);
	}

	public SDFAttributeList getRightInputSchema() {
		return getInputSchema(1);
	}

	public void setRightInputSchema(SDFAttributeList schema) {
		setInputSchema(1, schema);
	}

	public IPhysicalOperator getLeftPhysInput() {
		return getPhysInputPO(0);
	}
	
	public IPhysicalOperator getRightPhysInput() {
		return getPhysInputPO(1);
	}

	
}
