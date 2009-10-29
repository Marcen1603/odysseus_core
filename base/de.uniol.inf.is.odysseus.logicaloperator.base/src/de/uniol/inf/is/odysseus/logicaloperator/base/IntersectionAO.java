/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



/**
 * @author Marco Grawunder
 *
 */
public class IntersectionAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -3649143474548582941L;

    public IntersectionAO(IntersectionAO intersectionPO) {
        super(intersectionPO);
    }

    public IntersectionAO(){
    	super();
    }
    
	@Override
	public IntersectionAO clone() {
		return new IntersectionAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(LEFT);
	}
	
}
