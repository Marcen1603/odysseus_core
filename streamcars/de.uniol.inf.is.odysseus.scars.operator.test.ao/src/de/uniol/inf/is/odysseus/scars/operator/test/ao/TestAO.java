package de.uniol.inf.is.odysseus.scars.operator.test.ao;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TestAO extends UnaryLogicalOp{

	private static final long serialVersionUID = 51541288611675625L;

	public TestAO(String name) {
		this.setName(name);
		String s = this.getName();
		int i =0;
	}
	
	public TestAO( TestAO copy ) {
		this.setName(copy.getName());
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TestAO(this);
	}

}
