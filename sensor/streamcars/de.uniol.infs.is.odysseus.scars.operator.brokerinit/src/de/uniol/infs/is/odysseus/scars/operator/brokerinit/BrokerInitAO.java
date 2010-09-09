package de.uniol.infs.is.odysseus.scars.operator.brokerinit;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BrokerInitAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private int size = 1;
	
	public BrokerInitAO() {
		
	}
	
	public BrokerInitAO( BrokerInitAO other ) {
		setSize( other.getSize() );
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new BrokerInitAO(this);
	}

	public void setSize( int size ) {
		if( size < 1 ) throw new IllegalArgumentException("size of BrokerInit must be positive");
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
}
