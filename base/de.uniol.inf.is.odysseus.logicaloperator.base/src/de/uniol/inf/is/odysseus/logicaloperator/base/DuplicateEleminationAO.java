package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DuplicateEleminationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -4456846799512963785L;

	public DuplicateEleminationAO(DuplicateEleminationAO eleminationAO) {
		super(eleminationAO);
	}

	@Override
	public DuplicateEleminationAO clone() {
		return new DuplicateEleminationAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}
	
}
