package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class BufferAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 9204364375031967542L;

	private String type;
	
	public BufferAO(BufferAO ao) {
		super(ao);
		this.type = ao.type;
	}

	public BufferAO() {
	}

	@Override
	public BufferAO clone() {
		return new BufferAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

}
