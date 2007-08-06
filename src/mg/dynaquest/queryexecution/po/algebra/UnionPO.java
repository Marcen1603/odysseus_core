/*
 * Created on 07.12.2004
 *
 */
package mg.dynaquest.queryexecution.po.algebra;



/**
 * @author Marco Grawunder
 *
 */
public class UnionPO extends BinaryAlgebraPO{

    /**
     * @param unionPO
     */
    public UnionPO(UnionPO unionPO) {
        super(unionPO);
        setPOName("UnionPO");
    }
    
    public UnionPO() {
        super();
        setPOName("UnionPO");
    }

	public @Override
	SupportsCloneMe cloneMe() {
		return new UnionPO(this);
	}
    
    
 

}
