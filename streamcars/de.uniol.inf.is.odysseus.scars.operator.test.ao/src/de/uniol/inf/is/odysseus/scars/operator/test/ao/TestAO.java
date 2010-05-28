package de.uniol.inf.is.odysseus.scars.operator.test.ao;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TestAO extends UnaryLogicalOp{

	private static final long serialVersionUID = 51541288611675625L;

	public TestAO() {
		
	}
	
	public TestAO( TestAO copy ) {
		
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}
	
	@Override
	public AbstractLogicalOperator clone() throws CloneNotSupportedException {
		return new TestAO(this);
	}

}
