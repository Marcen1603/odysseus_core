/*
 * Created on 07.12.2004
 *
 */
package mg.dynaquest.queryexecution.po.algebra;



/**
 * @author Marco Grawunder
 *
 */
public class IntersectionPO extends BinaryAlgebraPO {

    /**
     * @param intersectionPO
     */
    public IntersectionPO(IntersectionPO intersectionPO) {
        super(intersectionPO);
        setPOName(intersectionPO.getPOName());
    }

    public IntersectionPO(){
    	super();
    	setPOName("IntersectionPO");
    }
    
	public @Override
	SupportsCloneMe cloneMe() {
		return new IntersectionPO(this);
	}

    
    
}
