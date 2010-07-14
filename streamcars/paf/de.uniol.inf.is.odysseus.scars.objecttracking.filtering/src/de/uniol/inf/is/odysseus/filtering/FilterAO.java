package de.uniol.inf.is.odysseus.filtering;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterAO<M extends IProbability>  extends BinaryLogicalOp{

	
	
	
	public FilterAO() {
	super();
	}
	
	public FilterAO(FilterAO<M> copy) {
	super(copy);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int hashCode() {
		return 0;
	
	}

	public boolean equals (Object obj) {
		return recalcOutputSchemata;}

}