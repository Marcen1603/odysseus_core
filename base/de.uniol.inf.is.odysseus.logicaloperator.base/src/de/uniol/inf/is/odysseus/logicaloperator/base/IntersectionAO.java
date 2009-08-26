/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;



/**
 * @author Marco Grawunder
 *
 */
public class IntersectionAO extends BinaryLogicalOp {

    /**
     * @param intersectionPO
     */
    public IntersectionAO(IntersectionAO intersectionPO) {
        super(intersectionPO);
        setPOName(intersectionPO.getPOName());
    }

    public IntersectionAO(){
    	super();
    	setPOName("IntersectionPO");
    }
    
	public @Override
	IntersectionAO clone() {
		return new IntersectionAO(this);
	}

}
