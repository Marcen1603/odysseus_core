package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class UnaryLogicalOp extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;
	private static final int PORTNUMBER = 0;

	public UnaryLogicalOp(AbstractLogicalOperator po) {
		super(po);
		setNoOfInputs(1);
	}

	public UnaryLogicalOp() {
		super();
		setNoOfInputs(1);
	}

	public void setInputAO(ILogicalOperator po) {
		setInputAO(PORTNUMBER, po);
	}

	public ILogicalOperator getInputAO() {
		return getInputAO(PORTNUMBER);
	}

	public SDFAttributeList getInputSchema() {
		return getInputSchema(0);
	}

	public void setInputSchema(SDFAttributeList schema) {
		setInputSchema(0, schema);
	}

	public IPhysicalOperator getPhysInputPO() {
		return getPhysInputPO(0);
	}
	
}
