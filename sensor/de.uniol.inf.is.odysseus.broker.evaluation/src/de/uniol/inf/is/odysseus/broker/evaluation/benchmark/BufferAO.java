package de.uniol.inf.is.odysseus.broker.evaluation.benchmark;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BufferAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 9204364375031967542L;	
	
	public BufferAO(BufferAO ao) {
		super(ao);		
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

}
